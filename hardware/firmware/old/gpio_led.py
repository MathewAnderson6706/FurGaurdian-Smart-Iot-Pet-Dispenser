#From https://projects.raspberrypi.org/en/projects/physical-computing/4
#Run: sudo python gpio_led.py
from gpiozero import LED
from time import sleep

led = LED(17)

while True:
    led.on()
    sleep(1)
    led.off()
    sleep(1)
