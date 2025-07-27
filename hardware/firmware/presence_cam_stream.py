#!/usr/bin/env python3
import time
import threading
import cv2
import smbus2
import qwiic_scmd
import socket
import datetime
import RPi.GPIO as GPIO
from flask import Flask, Response
from pyrebase import pyrebase
from pyngrok import ngrok

# -------------------------
# Firebase Configuration
# -------------------------
# Replace these values with your actual Firebase project configuration.
firebase_config = {
    "apiKey": "",
    "authDomain": "",
    "databaseURL": "",
    "storageBucket": ""
}

# Initialize Motor
myMotor = qwiic_scmd.QwiicScmd()
myMotor.begin()

# Initialize Firebase
firebase = pyrebase.initialize_app(firebase_config)
db = firebase.database()

# Initialize ngrok
public_url = ngrok.connect(5000, "http").public_url

R_MTR = 0  # Motor A (Coil A)
L_MTR = 1  # Motor B (Coil B)

db_date = None
last_execution_time = None  # Track when the motor last ran
COOLDOWN_SECONDS = 60      # Wait 60 seconds before allowing another run

############################
# IR Sensor Code (AK9753)
############################
LED_PIN = 17
GPIO.setmode(GPIO.BCM)
GPIO.setup(LED_PIN, GPIO.OUT)

is_human_present = False # Global flag
state_change = False # Global state change flag

class AK9753:
    AK975X_DEFAULT_ADDRESS = 0x64
    AK975X_ST1 = 0x05
    AK975X_IR1 = 0x06
    AK975X_IR2 = 0x08
    AK975X_IR3 = 0x0A
    AK975X_IR4 = 0x0C
    AK975X_ST2 = 0x10

    def __init__(self, i2c_bus=1, i2c_address=AK975X_DEFAULT_ADDRESS):
        self.bus = smbus2.SMBus(i2c_bus)
        self.address = i2c_address

    def write_register(self, register, value):
        self.bus.write_byte_data(self.address, register, value)

    def read_register(self, register, length=1):
        return self.bus.read_i2c_block_data(self.address, register, length)

    def soft_reset(self):
        # Soft-reset register
        self.write_register(0x1D, 0x01)
        time.sleep(0.1)

    def configure_sensor(self):
        # Put sensor in standby first
        self.write_register(0x1C, 0x00)
        time.sleep(0.1)
        # Set Continuous Mode 0 with cutoff frequency
        self.write_register(0x1C, 0x0C)

    def read_ir_data(self):
        ir1 = int.from_bytes(self.read_register(self.AK975X_IR1, 2), byteorder='little', signed=True)
        ir2 = int.from_bytes(self.read_register(self.AK975X_IR2, 2), byteorder='little', signed=True)
        ir3 = int.from_bytes(self.read_register(self.AK975X_IR3, 2), byteorder='little', signed=True)
        ir4 = int.from_bytes(self.read_register(self.AK975X_IR4, 2), byteorder='little', signed=True)
        # Clear DRDY
        self.read_register(self.AK975X_ST2)
        return ir1, ir2, ir3, ir4

    def check_data_ready(self):
        status = self.read_register(self.AK975X_ST1)[0]
        return (status & 0x01) != 0  # DRDY flag

def presence_monitor():
    """
    Background thread that continuously polls the IR sensor.
    If presence is detected, sets is_human_present = True,
    else False.
    """
    global is_human_present
    global state_change

    sensor = AK9753()
    sensor.soft_reset()
    sensor.configure_sensor()

    HUMAN_PRESENCE_THRESHOLD = 5000

    print("Starting IR presence monitor...")
    while True:
        if sensor.check_data_ready():
            ir1, ir2, ir3, ir4 = sensor.read_ir_data()
            max_ir = max(ir1, ir2, ir3, ir4)
            # Print debug info
            # print(f"IR Data: IR1={ir1}, IR2={ir2}, IR3={ir3}, IR4={ir4}, max={max_ir}")
            
            if max_ir > HUMAN_PRESENCE_THRESHOLD:
                GPIO.output(LED_PIN, GPIO.HIGH)
                is_human_present = True
            else:
                GPIO.output(LED_PIN, GPIO.LOW)
                is_human_present = False
            
            if state_change != is_human_present:
                update_firebase_stream_url()
                state_change = is_human_present
                

        time.sleep(0.2)

############################
# Flask + OpenCV Streaming
############################
app = Flask(__name__)

def generate_frames():
    cap = None
    while True:
        if is_human_present:
            # If camera isn't open, open it
            if cap is None or not cap.isOpened():
                cap = cv2.VideoCapture(0)
                if not cap.isOpened():
                    print("Error: Could not open camera.")
                    time.sleep(1)
                    continue

            ret, frame = cap.read()
            if not ret:
                time.sleep(0.1)
                continue

            ret, buffer = cv2.imencode('.jpg', frame)
            if not ret:
                time.sleep(0.1)
                continue

            frame_bytes = buffer.tobytes()
            yield (b'--frame\r\n'
                   b'Content-Type: image/jpeg\r\n\r\n' + frame_bytes + b'\r\n')
        else:
            # No human present: close camera if it's open
            if cap is not None:
                cap.release()
                cap = None
                print("Camera closed due to no presence.")
            break
            time.sleep(0.2)

@app.route('/video_feed')
def video_feed():
    return Response(generate_frames(),
                    mimetype='multipart/x-mixed-replace; boundary=frame')

# -------------------------
# Firebase Signaling Functions
# -------------------------

def update_firebase_stream_url():
    """
    Waits briefly, constructs the stream URL using the Pi's local IP,
    and updates Firebase with the URL under the "cameraStream" node.
    """
    global is_human_present
    global public_url
    # Wait a few seconds to allow the Flask server to start up properly.
    time.sleep(2)
    ngrok.set_auth_token("2umCIzxqWMlGyRPm3r69I8utDjo_2yFsWAco19NBKAJtXCGjM")
    
    
    stream_url = f"{public_url}/video_feed"

    
    # Update Firebase with the stream URL
    db.child("cameraStream").update({"url": stream_url})
    db.child("cameraStream").update({"stream_ready": is_human_present})
    print("Firebase updated with stream URL:", stream_url)

def motor_sequence():
    global last_execution_time
    print("Motor initialized.")
    time.sleep(0.25)

    # Zero Motor Speeds
    myMotor.set_drive(R_MTR, 0, 0)
    myMotor.set_drive(L_MTR, 0, 0)

    myMotor.enable()
    print("Motor enabled.")
    time.sleep(0.25)
    time.sleep(0.75)

    step_delay = 0.01  # Delay between steps
    steps_needed = 50  # Number of steps to make a 180-degree turn

    for step in range(steps_needed):
        myMotor.set_drive(L_MTR, 0, 254)
        time.sleep(step_delay)
        myMotor.set_drive(L_MTR, 0, 20)

        myMotor.set_drive(R_MTR, 0, 254)
        time.sleep(step_delay)
        myMotor.set_drive(R_MTR, 0, 20)

        myMotor.set_drive(L_MTR, 1, 254)
        time.sleep(step_delay)
        myMotor.set_drive(L_MTR, 1, 20)

        myMotor.set_drive(R_MTR, 1, 254)
        time.sleep(step_delay)
        myMotor.set_drive(R_MTR, 1, 20)

    # Stop the Motor
    myMotor.set_drive(R_MTR, 0, 0)
    myMotor.set_drive(L_MTR, 0, 0)
    myMotor.disable()

    # Update last execution time
    last_execution_time = time.time()

def stream_handler(message):
    global db_date
    data = message["data"]
    if data:
        print("Change Detected")
        testing = db.child("dateObject").get()
        dateData = testing.val()
        db_date = dateData.get("date", None)
        
def motor_service():
    while True:
        current_time = datetime.datetime.now().strftime("%H:%M")
    
        if db_date == current_time:
            print("inside first if")
            # Check if cooldown has passed (or if it's the first run)
            if last_execution_time is None or (time.time() - last_execution_time) >= COOLDOWN_SECONDS:
                print("inside second if")
                motor_sequence()
    
        time.sleep(1)  # Check every second


############################
# Main
############################
if __name__ == '__main__':
    # Start the IR presence monitor in a background thread
    thread = threading.Thread(target=presence_monitor, daemon=True)
    thread.start()
    
    threading.Thread(target=motor_service, daemon=True).start()

    # Start listening for database changes
    my_stream = db.child("dateObject").stream(stream_handler)

    
    #threading.Thread(target=update_firebase_stream_url, daemon=True).start();
    print("before  flask")
    # Run the Flask app on all interfaces at port 5000
    app.run(host='0.0.0.0', port=5000, threaded=True)
    print("after flask")

    

    
