/*
tx_openclose.c
2024-03-28
GreenLight Innovations
Faraz Ahmed, Nathaniel Lozano, Ali Sunan Faizi, Steven Lambrinos

derived from tx_servo.c
2020-12-21
Public Domain

http://abyz.me.uk/lg/lgpio.html

Compile: gcc -Wall -o tx_openclose tx_openclose.c -llgpio

Execute: ./tx_openclose
*/

#include <stdio.h>
#include <stdlib.h>

#include <lgpio.h>

#define SERVO 13

#define LFLAGS 0

// Min 550
#define P_WIDTH_OPEN 750
// 2450 is the max
#define P_WIDTH_CLOSE 1600

int main(int argc, char *argv[])
{
   int h;

   h = lgGpiochipOpen(0);

   if (h >= 0)
   {
      if (lgGpioClaimOutput(h, LFLAGS, SERVO, 0) == LG_OKAY)
      {
 	     lgTxServo(h, SERVO, P_WIDTH_OPEN, 50, 0, 0); /* 1500 microseconds, 50 Hz */
         lguSleep(1);
 	     lgTxServo(h, SERVO, P_WIDTH_CLOSE, 50, 0, 0); /* 1500 microseconds, 50 Hz */
         lguSleep(1);
      }

      lgGpiochipClose(h);
   }
}

