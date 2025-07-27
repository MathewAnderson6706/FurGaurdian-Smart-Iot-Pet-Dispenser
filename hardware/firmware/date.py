import sys
import qwiic_scmd
import pyrebase
import datetime
import time

# Initialize Motor
myMotor = qwiic_scmd.QwiicScmd()
myMotor.begin()

# -------------------------
# Firebase Configuration
# -------------------------
firebase_config = {
  "apiKey": "AHHHHHHHHHHHHH",
  "authDomain": "idk",
  "databaseURL": "guessing",
  "storageBucket": "five big booms"
}

firebase = pyrebase.initialize_app(firebase_config)
db = firebase.database()

R_MTR = 0  # Motor A (Coil A)
L_MTR = 1  # Motor B (Coil B)

db_date = None
last_execution_time = None  # Track when the motor last ran
COOLDOWN_SECONDS = 60      # Wait 60 seconds before allowing another run

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

# Start listening for database changes
my_stream = db.child("dateObject").stream(stream_handler)

# Keep the script running
while True:
    current_time = datetime.datetime.now().strftime("%H:%M")
    
    if db_date == current_time:
        # Check if cooldown has passed (or if it's the first run)
        if last_execution_time is None or (time.time() - last_execution_time) >= COOLDOWN_SECONDS:
            motor_sequence()
    
    time.sleep(1)  # Check every second