import time
import sys
import qwiic_scmd

myMotor = qwiic_scmd.QwiicScmd()

def runExample():
    print("Stepper Motor Test.")
    R_MTR = 0  # Motor A (Coil A)
    L_MTR = 1  # Motor B (Coil B)

    if not myMotor.connected:
        print("Motor Driver not connected. Check connections.", file=sys.stderr)
        return

    myMotor.begin()
    print("Motor initialized.")
    time.sleep(0.25)

    # Zero Motor Speeds
    myMotor.set_drive(R_MTR, 0, 0)
    myMotor.set_drive(L_MTR, 0, 0)

    myMotor.enable()
    print("Motor enabled.")
    time.sleep(0.25)
    time.sleep(0.75)


    step_delay = .01  # Delay between steps
    steps_needed = 50  # Number of steps to make a 180-degree turn (adjust as necessary)

    for step in range(steps_needed):
        myMotor.set_drive(R_MTR, 0, 254)
        time.sleep(step_delay)
        myMotor.set_drive(R_MTR, 0, 20)

        myMotor.set_drive(L_MTR, 0, 254)
        time.sleep(step_delay)
        myMotor.set_drive(L_MTR, 0, 20)

        myMotor.set_drive(R_MTR, 1, 254)
        time.sleep(step_delay)
        myMotor.set_drive(R_MTR, 1, 20)

        myMotor.set_drive(L_MTR, 1, 254)
        time.sleep(step_delay)
        myMotor.set_drive(L_MTR, 1, 20)



    # Define the correct step sequence for a bipolar stepper motor
#    step_sequence = [
#        (255, 0),  # Step 1: Coil A forward, Coil B off
#        (255, 255),  # Step 2: Coil A forward, Coil B forward
#        (0, 255),  # Step 3: Coil A off, Coil B forward
#        (-255, 255),  # Step 4: Coil A backward, Coil B forward
#        (-255, 0),  # Step 5: Coil A backward, Coil B off
#        (-255, -255),  # Step 6: Coil A backward, Coil B backward
#        (0, -255),  # Step 7: Coil A off, Coil B backward
#        (255, -255),  # Step 8: Coil A forward, Coil B backward
#    ]
#    step_sequence = [
#        (255, 0),  # Step 1: Coil A forward, Coil B off
#        (0, 255),  # Step 3: Coil A off, Coil B forward
#        (-255, 0),  # Step 5: Coil A backward, Coil B off
#        (0, -255),  # Step 7: Coil A off, Coil B backward
#    ]
    step_sequence = [
        (255, 0),  # Ch A +
        (0, 0),  # 
        (-255, 0),  # Also A +
        (0,0),  # 
    ]

#     # Execute the steps
#     for step in range(steps_needed):
#         for phase in step_sequence:
#             # Apply each phase to the motor coils
#             coil_a_speed = abs(phase[0])
#             coil_b_speed = abs(phase[1])
#             coil_a_dir = 0 if phase[0] >= 0 else 1
#             coil_b_dir = 0 if phase[1] >= 0 else 1
# 
#             myMotor.set_drive(R_MTR, coil_a_dir, coil_a_speed)
#             myMotor.set_drive(L_MTR, coil_b_dir, coil_b_speed)
# 
#             print(f"Step: {step}, Phase: {phase}, Coil A: ({coil_a_dir}, {coil_a_speed}), Coil B: ({coil_b_dir}, {coil_b_speed})")
#             time.sleep(step_delay)

    # Stop the motor after completing the rotation
    myMotor.set_drive(R_MTR, 0, 0)
    myMotor.set_drive(L_MTR, 0, 0)
    myMotor.disable()
    print("Stepper motor test complete.")

if __name__ == '__main__':
    try:
        runExample()
    except (KeyboardInterrupt, SystemExit) as exErr:
        print("Ending example.")
        myMotor.disable()
        sys.exit(0)
