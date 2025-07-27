/*
tx_pwm.c
2020-11-18
Public Domain
derived from tx_pulse.c

http://abyz.me.uk/lg/lgpio.html

gcc -Wall -o tx_pwm tx_pwm.c -llgpio

sudo ./tx_pwm
*/

#include <stdio.h>
#include <stdlib.h>

#include <lgpio.h>

// Pin 9 = GPIO 17
#define OUT 17
#define LOOPS 120

#define LFLAGS 0

//lgTxPwm(h, OUT, 1, 50, 0, LOOPS);
/*int lgTxPwm(int handle, int gpio, float pwmFrequency, float pwmDutyCycle, int pwmOffset, int pwmCycles)
This starts software timed PWM on an output GPIO.

      handle: >= 0 (as returned by lgGpiochipOpen)
        gpio: the GPIO to be pulsed
pwmFrequency: PWM frequency in Hz (0=off, 0.1-10000)
pwmDutyCycle: PWM duty cycle in % (0-100)
   pwmOffset: offset from nominal pulse start position
   pwmCycles: the number of pulses to be sent, 0 for infinite*/
   
   
//lgTxPulse(h, OUT, 20000, 5000, 0, LOOPS);
/* Example converted from tx_pulse.c
int lgTxPulse(int handle, int gpio, int pulseOn, int pulseOff, int pulseOffset, int pulseCycles)
This starts software timed pulses on an output GPIO.

     handle: >= 0 (as returned by lgGpiochipOpen)
       gpio: the GPIO to be written
    pulseOn: pulse high time in microseconds
   pulseOff: pulse low time in microseconds
pulseOffset: offset from nominal pulse start position
pulseCycles: the number of pulses to be sent, 0 for infinite*/



int main(int argc, char *argv[])
{
   int h;
   int i;
   double start, end;

   h = lgGpiochipOpen(0);

   if (h >= 0)
   {
      if (lgGpioClaimOutput(h, LFLAGS, OUT, 0) == LG_OKAY)
      {
         // lgTxPulse(h, OUT, 20000, 30000, 0, 0);

         lguSleep(2);

         lgTxPwm(h, OUT, 1, 50, 0, LOOPS);

         // lgTxPulse(h, OUT, 20000, 5000, 0, LOOPS);

         start = lguTime();

         while (lgTxBusy(h, OUT, LG_TX_PWM)) lguSleep(0.01);

         end = lguTime();

         printf("%d cycles at 40 Hz took %.1f seconds\n", LOOPS, end-start);
      }

      lgGpiochipClose(h);
   }
}

