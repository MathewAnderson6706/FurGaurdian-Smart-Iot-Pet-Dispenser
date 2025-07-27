/*  C code to read whoami registers from say the
 *  Raspberry Pi Sense HAT add-on board (HTS221 sensor)
 *  Inspired by: https://github.com/davebm1/c-sense-hat/blob/main/humidity.c
 *  Referenced: https://www.includehelp.com/c/working-with-hexadecimal-values-in-c-programming-language.aspx
 *  Note that from: https://www.nxp.com/docs/en/user-guide/UM10204.pdf
 *  "The Device ID Read can be done, starting with the 12 manufacturer bits (first byte +
 *   four MSBs of the second byte), followed by the nine part identification bits (four LSBs
 *   of the second byte + five MSBs of the third byte), and then the three die revision bits
 *   (three LSBs of the third byte)."
 *  Though I don't see it documented "0xFFFFFF87" seems to represent "register not found"
 *  sudo raspi-config --> interfacing options --> enable i2c
 *  sudo apt install libi2c-dev
 *  Build with:  gcc -Wall -O2 troubleshooting.c -o troubleshooting -li2c
 */

#include <fcntl.h>
#include <i2c/smbus.h>
#include <linux/i2c-dev.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/ioctl.h>
#include <unistd.h>

#define DEV_PATH "/dev/i2c-1"

#define CTRL_REG1 0x20

int main(void) {
    int fd = 0;
    uint8_t status = 0;

    /* open i2c bus */
    if ((fd = open(DEV_PATH, O_RDWR)) < 0) {
        perror("Unable to open i2c bus");
        exit(1);
    }

    fprintf(stdout,"Use i2cdetect -y 1 to determine what addresses have peripherals, then enter an i2c address\n");
    fprintf(stdout,"Examples:\n" 
        "mlx90393\t\t0x0C\n"
        "xa1110\t\t0x10\n"
        "mprls\t\t0x18\n"
        "lsm9ds1_mg\t0x1C\n"
        "kx132\t\t0x1F\n"
        "kx134\t\t0x1F\n"
        "cap1203\t\t0x28\n"
        "vl53l1x\t\t0x29\tvl53l4cd\t0x29\n"
        "nau7802\t\t0x2A\n"
        "mmc5983\t\t0x30\n"
        "stts22h\t\t0x3C\n"
        "tcs3400\t\t0x39\n"
        "tmp117\t\t0x48\n"
        "max30120\t\t0x48\n"
        "as7263\t\t0x49\n"
        "ens160\t\t0x53\n"
        "lps25h\t\t0x5C\n"
        "drv8835\t\t0x5D\n"
        "hts221\t\t0x5F\n"
        "vcnl4040\t\t0x60\n"
        "i2cdB\t\t0x64\n"
        "icm20948\t0x69\n"
        "lsm9ds1_ag\t0x6A\n"
        "lsm6dso\t\t0x6B\n"
        "ism330\t\t0x6B\n"
        "bme280\t\t0x77\n"
        "enter a hexidecimal i2c address (e.g. 5F): ");
    int i2caddr;
    scanf("%x",&i2caddr);
    
    /* configure i2c slave */
    if (ioctl(fd, I2C_SLAVE, i2caddr) < 0) {
        perror("Unable to configure i2c slave device");
        close(fd);
        exit(1);
    }

    fprintf(stdout,"Read the datasheet to determine what register contains the device id, then enter it\n");
    fprintf(stdout,"Examples:\n"
        "mlx90393\t\t0x10\n"
        "xa1110\t\t0x0F\n"
        "mprls\t\t0x?\n"
        "lsm9ds1_mg\t0x0F\n"
        "kx132\t\t0x13\n"
        "kx134\t\t0x87\n"
        "cap1203\t\t0xFD\n"
        "vl53l1x\t\t0x110\tvl53l4cd\t0x110\n"
        "nau7802\t\t01F\n"
        "mmc5983\t\t0x0F\n"
        "stts22h\t\t0x01\n"
        "tcs3400\t\t0x92\n"
        "tmp117\t\t0x0F\n"
        "max30120\t\t0x00\n"
        "as7263\t\t0x00\n"
        "ens160\t\t0x00\n"
        "lps25h\t\t0x0F\n"
        "drv8835\t\t0x01\n"
        "drv8835\t\t0x0C\n"
        "hts221\t\t0x0F\n"
        "vcnl4040\t\t0x00\n"
        "i2cdB\t\t0x?\n"
        "icm20948\t0x00\n"
        "lsm9ds1_ag\t0x0F\n"
        "lsm6dso\t\t0x0F\n"
        "ism330\t\t0x2F\n"
        "bme280\t\t0xD0\n"
        "enter a hexidecimal register (e.g. 0F): ");
    int whoami;
    scanf("%x",&whoami);
    
    printf("\nThe device at %#X, in register %#X, identifies as %#X\n",i2caddr,whoami,i2c_smbus_read_byte_data(fd, whoami));
 
    //This should provide for the
    //mlx90393:    The device at 0x0C, in register 0x10, identifies as 0xD4
    //xa1110:      The device at 0x10, in register 0x0F, identifies as 0x2C
    //mprls:       The device at 0x18, in register 0x, identifies as 0x46
    //lsm9ds1_mg:  The device at 0x1C, in register 0x0F, identifies as 0x3D
    //kx132:       The device at 0x1F, in register 0x13, identifies as 0x3D
    //kx134:       The device at 0x1F, in register 0x87, identifies as 0x79
    //cap1203:     The device at 0x28, in register 0xFD, identifies as 0x6D
    //vl53lseries: The device at 0x29, in register 0x110,identifies as 0x01
    //nau7802:     The device at 0x2A, in register 0x1F, identifies as 0x00
    //mmc5983:     The device at 0x30, in register 0x2F, identifies as 0x30
    //stts22h:     The device at 0x3C, in register 0x01, identifies as 0xA0
    //tcs3400:     The device at 0x39, in register 0x92, identifies as 0x90
    //tmp117:      The device at 0x48, in register 0x0F, identifies as 0x117
    //max30120:    The device at 0x48, in register 0x00, identifies as 0x0D
    //as7263:      The device at 0x49, in register 0x00, identifies as 0x80
    //ens160:      The device at 0x53, in register 0x00, identifies as 0x60
    //lps25h:      The device at 0x5C, in register 0x0F, identifies as 0xBD
    //drv8835:     The device at 0x5D, in register 0x01, identifies as 0xA9
    //drv8835:     The device at 0x5D, in register 0x0C, identifies as 0x92
    //hts221:      The device at 0x5F, in register 0x0F, identifies as 0xBC
    //vcnl4040:    The device at 0x60, in register 0x00, identifies as 0x01
    //i2cdB:       The device at 0x64, in register 0x, identifies as 0x
    //icm20948:    The device at 0x69, in register 0x00, identifies as 0xEA
    //lsm9ds1_ag:  The device at 0x6A, in register 0x0F, identifies as 0x68
    //lsm6dso:     The device at 0x6B, in register 0x0F, identifies as 0x6C
    //ism330:      The device at 0x6B, in register 0x0F, identifies as 0x6B
    //bme280:      The device at 0x77, in register 0xD0, identifies as 0x60

    /* Power down the device if CTRL_REG1 is correct*/
    i2c_smbus_write_byte_data(fd, CTRL_REG1, 0x00);
    close(fd);

    return (0);
}
