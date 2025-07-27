# Pre-soldering Inspection
1. Visual Inspection:   
Inspect the PCB for any visible shorts or copper imperfections from the production process.
Check for any damage to the traces, or PCB.
2. Power and Ground:   
Ensure that the power and ground connections are correctly established for all components.
Verify that the power supply voltage matches the requirements of the components.
3. Continuity Test:   
Use a multimeter's continuity mode to verify the above and that there are no open circuits/breaks, or shorts in the
traces.
4. Consult Documentation:   
Refer to datasheets and technical documentation for components to ensure correct usage and
connections.
5. Component Orientation:   
Double-check that all components, including the LED and transistor are
correctly oriented.
6. Consider the [Safety Data Sheet](https://aimsolder.com/sites/default/files/alloy_sac305_ws482-ghs_america-english.pdf)   
7. Proceed to reduce the amount of oxide on the PCB by using a [3M Scotch-Brite cleaning pad](https://en.wikipedia.org/wiki/Scotch-Brite).   
8. If all is well, proceed to begin with surface mount components followed by through hole components.
# Space constraints
1. The LED should be on its side as per the KiCad PCB design file.   
2. Please note that for the transistor you only have about 1 cm of space.   
   ![media/transistorspace.jpg](media/transistorspace.jpg)
# Post-soldering Inspection Process adapted from BioBytes
1. Visual Inspection:
Inspect the soldered connections, components, and PCB for any visible solder bridges, cold
solder joints, or misplaced components.
Check for any damage to the components, traces, or PCB.
2. Power and Ground:
Ensure that the power and ground connections are correctly established for all components.
Verify that the power supply voltage matches the requirements of the components.
3. Consult Documentation:
Refer to datasheets and technical documentation for components to ensure correct usage and
connections.
4. Component Orientation:
Double-check that all components, including the LED, sensors/effectors, and resistors, are
correctly oriented and soldered in the right positions.
5. Soldering Quality:
Reheat any suspicious solder joints to ensure proper bonding and conductivity.
Remove excess solder or solder bridges with a soldering iron and desoldering wick.
6. Continuity Test:
Use a multimeter's continuity mode to verify that there are no open circuits or breaks in the
traces.
Test the continuity of connections between the components and their corresponding pins on the
PCB.
7. Signal Integrity:
Check if the GPIO pins on the Raspberry Pi are correctly connected to the LED and sensor/effector.
Use a multimeter or oscilloscope to verify that the signals are reaching the correct pins.
8. I2C Communication:
Verify the I2C communication between the Raspberry Pi and the sensor/effector.
Check the sender's address and the Raspberry Pi's I2C settings in the software code.
9. Code Inspection: Scrutinize code for errors and logic flaws.
10. LED Behavior:
If the LED is not blinking as expected, review the software code controlling the LED.
Confirm that the GPIO pin settings and blinking pattern are correctly programmed.
11. Sensor Data:
If the LED behaviour is influenced by the sensor or effector's use, ensure that the sensor is
correctly connected and functioning.
Check if the sensor is providing accurate acceleration data to the Raspberry Pi.
12. Component Replacement:
If a component is suspected to be faulty, consider replacing it with a new one and retest the
circuit.
Ensure that the replacement component is of the same type and specifications.
13. Isolation Testing:
Isolate sections of the circuit to identify specific problem areas.
Test individual components or sections of the PCB to pinpoint the source of the issue.
14. Seek External Help:
If the troubleshooting steps prove challenging, seek advice from experienced individuals, online
communities, or forums.
15. Record Changes and Repeat Testing:
Document any changes made during troubleshooting, including component replacements,
adjustments to code, or modifications to connections. After making changes, repeat testing to
verify whether the issue has been resolved.
