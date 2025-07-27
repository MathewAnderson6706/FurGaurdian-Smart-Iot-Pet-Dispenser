# KiCad   
1. We will be designing a Raspberry Pi Interface board using [KiCad 8.0.3](https://downloads.kicad.org/kicad/windows/explore/stable/download/kicad-8.0.3-x86_64.exe).
2. Chapter 18 of the below textbook on pages 259-284, pages 268-293 of the pdf, has Project 2: Design a small Raspberry Pi HAT.
   Dalmaris, Peter. (2018). KiCad Like a Pro 2nd edition. United Kingdom: Elektor International Media B.V. Tech Explorations: EBOOK SOLO 978-1-64440-886-5. Note that newer versions of the textbook do not include the Design a small Raspberry Pi HAT project.   
3. Launch KiCad:   
![](media/1.png)
4. Start with default settings:   
![](media/2.png)
5. I have followed [Using a template](https://techexplorations.com/guides/kicad/3e/create-a-new-kicad-project-from-a-template/) and [Vlad's guide to PCB facilities in J233](PCBPrototypes.md) to a certain extent in order to create the below files to start from:
-   [Project File](/hardware/pcb/StudentName2023-11-08.kicad_pro)
-   [Schematic File](/hardware/pcb/StudentName2023-11-08.kicad_sch)
-   [PCB File](/hardware/pcb/StudentName2023-11-08.kicad_pcb)   
6. Download the above /hardware/pcb/StudentName2023-11-08.kicad_pro, pcb, and sch.
7. Use KiCad to open the _pro project file, then navigate to opening the PCB file.
8. Change the board in the PCB view to have your name and the current date.
9. If not routed already, route the BJT as a switch circuit (Section 4.5 Page 192 in your Electronic Devices by Floyd) traces on the PCB. This involves a 2N4124 NPN transistor with a 2.2 kOhm resistor between pin 11 -- GPIO17 and the base of the transistor, connect the emitter to the ground, and connect an LED with a series 220 Ohm resistor on the collector side to 3.3V. To do this:   
   - Click on the the end of say a resistor and press 'x' on the keyboard.
   - Then click on the other terminal that it should be connected to.
   - Consider ease of soldering.
10. Add a PinHeader_1x03_P2.54mm_Horizontal near the edge of the board to be able to connect a micro servo to pin 33 -- GPIO13, 3.3V (middle pin), and GND.
11. Add an extra Conn_01x08_Female that makes it possible to connect to the signals that we use for I2C (3V3,GND,SDA,SCL), the LED (GPIO17), a possible micro servo (GPIO13), and 2 extra connections.
12. Consider widening the traces where possible to reduce the likelihood of lifted pads/traces.   
13. Save "project as" what you changed the name and date to.   
14. Use File->fabrication outputs->Gerbers (.gbr)... to Plot the Gerbers   
15. also "Generate Drill Files"->PTH and NPTH in single file   
16. Save project
17. Use the Gerber Viewer to preview:
    1. F_Cu.gtl
    2. B_Cu.gbl
    3. F_Silkscreen.gto
    4. B_Silkscreen.gbo
    5. F_Mask.gts
    6. B_Mask.gbs
    7. Edge_Cuts.gm1
    8. .drl    
18. Upload your work in a zip file to your /hardware/pcb/ folder in the PrototypeZone organisation. The name of the zip should be in the format of your changed name and date (StudentName2023-09-24.zip) and it should contain the 3 KiCad files you worked on, the combined drill file, and the default Gerber plots as per the following list:   
    1. F.Cu   
    2. B.Cu   
    3. F.Silkscreen   
    4. B.Silkscreen   
    5. F.Mask   
    6. B.Mask   
    7. Edge.Cuts   
    8. The drill file   
    9. Your _pro file   
    10. Your _sch file   
    11. Your _pcb file
19. Once the professor knows that your zip file has been uploaded correctly they will send the class set to the lab next door to be made [in house](/hardware/pcb/PCBPrototypes.md).
20. When the completed PCB is available, proceed to [inspect it](/hardware/pcb/inspection.md).
21. Further iterations of the board can be made [in house](/hardware/pcb/PCBPrototypes.md) or by [OSHpark](https://oshpark.com/),  [MicroArt](https://microart.pcborders.com/), [Bittele](https://onlinequote.7pcb.com/), [JLC](https://jlcpcb.com/), [PCBway](https://www.pcbway.com/QuickOrderOnline.aspx), [Seeedstudio](https://www.seeedstudio.com/fusion_pcb.html), or other alternatives.   

