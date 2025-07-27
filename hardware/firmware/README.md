# Firmware directory
- **presence_cam_stream.py** is our working firmware that polls our human presence detector and initiates a stream via local ip. The code then updates the firebase with the address for the stream and keeps it open until the presence is gone.
    - **Current Issues**: The stream leaves the last image open when presence is left. The IR readings are heavily influences by its surroundings. Need to figure out a way to dynamically create a threshold. As of now it is manually changed to enviroment.
- **date.py** is our food scheduling code that retrieves schedule information from the DB and dispenses food according to the provided schedule. It maintains a constant connection with the DB to monitor for changes to the schedule.
    - **Current Issues**: A temperary fix of a boolean value is put into the database to prevent the motor from dispensing for the whole minute. 
