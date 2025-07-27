# This program polls for a IR heat signature then lights up the LED when it passes the threshold. 

import smbus2
import time
import RPi.GPIO as GPIO

LED_PIN = 17

GPIO.setmode(GPIO.BCM)
GPIO.setup(LED_PIN, GPIO.OUT)

class AK9753:
    AK975X_DEFAULT_ADDRESS = 0x64
    AK975X_ST1 = 0x05
    AK975X_IR1 = 0x06
    AK975X_IR2 = 0x08
    AK975X_IR3 = 0x0A
    AK975X_IR4 = 0x0C
    AK975X_ST2 = 0x10
    AK975X_ECNTL1 = 0x1C
    AK975X_CNTL2 = 0x19
    AK975X_MODE_0 = 0b100  # Continuous mode

    def __init__(self, i2c_bus=1, i2c_address=AK975X_DEFAULT_ADDRESS):
        self.bus = smbus2.SMBus(i2c_bus)
        self.address = i2c_address

    def write_register(self, register, value):
        self.bus.write_byte_data(self.address, register, value)

    def read_register(self, register, length=1):
        return self.bus.read_i2c_block_data(self.address, register, length)

    def soft_reset(self):
        self.write_register(0x1D, 0x01)
        time.sleep(0.1)

    def configure_sensor(self):
        # Set to Standby Mode
        self.write_register(0x1C, 0x00)
        time.sleep(0.1)

        # Set Continuous Mode 0 and cutoff frequency (EFC = 100 -> 0.6 Hz)
        self.write_register(0x1C, 0x0C)

    def refresh_sensor(self):
        """Trigger sensor refresh by reading the dummy register."""
        self.read_register(self.AK975X_ST2)
        print("Sensor manually refreshed.")

    def read_ir_data(self):
        """Reads IR data from the sensor."""
        ir1 = int.from_bytes(self.read_register(self.AK975X_IR1, 2), byteorder='little', signed=True)
        ir2 = int.from_bytes(self.read_register(self.AK975X_IR2, 2), byteorder='little', signed=True)
        ir3 = int.from_bytes(self.read_register(self.AK975X_IR3, 2), byteorder='little', signed=True)
        ir4 = int.from_bytes(self.read_register(self.AK975X_IR4, 2), byteorder='little', signed=True)
        self.read_register(0x10)  # Clear DRDY
        return ir1, ir2, ir3, ir4

    def check_data_ready(self):
        """Checks if data is ready by polling the DRDY flag."""
        status = self.read_register(self.AK975X_ST1)[0]
        return status & 0x01  # DRDY flag

# Main execution
sensor = AK9753()
sensor.soft_reset()
sensor.configure_sensor()

# Human presence threshold (adjust based on environment)
HUMAN_PRESENCE_THRESHOLD = 200  # Example threshold for IR values

print("Polling for human presence...")
while True:
    if sensor.check_data_ready():
        ir1, ir2, ir3, ir4 = sensor.read_ir_data()
        print(f"IR Data: IR1={ir1}, IR2={ir2}, IR3={ir3}, IR4={ir4}")

        # Check if any IR sensor exceeds the threshold
        if max(ir1, ir2, ir3, ir4) > HUMAN_PRESENCE_THRESHOLD:
            print("Human is present!")
            GPIO.output(LED_PIN, GPIO.HIGH)
        else:
            print("No human detected.")
            GPIO.output(LED_PIN, GPIO.LOW)

        time.sleep(0.5)  # Delay to avoid excessive polling
    else:
        print("Data not ready. Retrying...")
        time.sleep(0.1)
