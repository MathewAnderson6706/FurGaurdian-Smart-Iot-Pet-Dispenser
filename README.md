# Automatic Pet Food Dispensor using RasperryPi 4 
Title Page (1st odd page not numbered, X.0 sections begin on odd pages, otherwise double sided and numbered)  
## Declaration of Joint Authorship   
### Task Distribution
We, FurGuardian, confirm that this breakdown of authorship represents my contribution to the work submitted for assessment and my contribution is my own work and  is expressed in my own words. Any uses made within the Technology Report of the works of any other author, separate to the work group, in any form (ideas, equations, figures, texts, tables, programs), are properly acknowledged at the point of use. A list of the references used is included.
| Task                  | Mathew | Justin | Tevadi | Zane |
|-----------------------|--------|--------|--------|------|
| **3D Print / Container**   | 50%   | 10%   | 10%   | 30%  |
| **Mobile Programming**     | 30%   | 50%   | 10%   | 10%  |
| **Hardware Connectivity**  | 45%   | 25%   | 15%   | 15%  |
| **Hardware Programming**   | 40%   | 40%   | 10%   | 10%  |
| **Documentation**          | 25%   | 25%   | 25%   | 25%  |

[^1]
[^1]: Technology Report Guidelines. OACETT, Revised September 2022. Available at: https://www.oacett.org/getmedia/5ad707d7-f472-4b24-a7fe-f34e270b0c41/2022_TR_Guidelines_-_Updated_Version_-_Sept_2022.pdf
## Proposal/Project Specifications   
[Link to proposal](wk01proposal.md).   
## Executive Summary   
Our project focuses on enhancing pet care for owners who are away during the day. It features a **smart pet food dispenser** equipped with advanced functionalities:

- **Camera**:  
  Allow owners to interact with their pets remotely, ensuring constant connection and engagement.

- **Infrared Sensor**:  
  Detects pet presence to automate feeding and interaction processes.

- **Stepper Motor**:  
  Dispenses precise portions of food to maintain proper dietary control.

- **solenoid push intermittent**:  
  Enables treat dispensing as an added feature for rewarding and engaging pets.

## Table of Contents

[Declaration of Joint Authorship](#declaration-of-joint-authorship)   
[Proposal/Project Specifications](#proposalproject-specifications)   
[Executive Summary](#executive-summary)   
[Table of Contents](#table-of-contents)   
[List of Figures](#list-of-figures)   

[1.0 Introduction](#10-introduction)   
[1.1 Background](#11-background)   
[1.2 Project Requirements and Specifications](#12-project-requirements-and-specifications)   
[1.3 Project Schedule](#13-project-schedule)   

[2.0 Hardware Development Platform Report/Build Instructions](#20-hardware-development-platform-reportbuild-instructions)  
[2.1 Mathew Anderson-Saavedra/A](#21-student-onea)  
[2.2 Tevadi Brookes/B](#22-student-twob)  
[2.3 Zane Aransevia/C](#23-student-threec)  
[2.4 Justin Chipman/D](#24-student-fourd)  

[3.0 Mobile Application Report](#30-mobile-application-report)  
[3.1 Deliverable 1](https://github.com/Chipman8472/FurGuardian/blob/master/Docs/FurGuardian_Deliverable1_Group8.pdf)  
[3.2 Deliverable 2](https://github.com/Chipman8472/FurGuardian/blob/master/Docs/Deliverable2/FurGuardian_PetWellness_Group8_2.pdf)  
[3.3 Deliverable 3](https://github.com/Chipman8472/FurGuardian/blob/master/Docs/FurGuardian_Deliverable3_Group8.pdf)  
[3.4 Deliverable 4](https://github.com/Chipman8472/FurGuardian/blob/master/Docs/FurGuardian_PetWellness_Deliverable4_Group8.pdf)   
[3.5 Deliverable 5](https://github.com/Chipman8472/FurGuardian/blob/master/Docs/Deliverable%205/FurGuardian_Deliverable5_Group8.pdf)  

[4.0 Integration](#40-integration)  
[4.1 Enterprise Wireless Connectivity](#41-enterprise-wireless-connectivity)  
[4.2 Database Configuration](#42-database-configuration)  
[4.3 Network and Security Considerations](#43-network-and-security-considerations)  
[4.4 Unit Testing](#44-unit-testing)  
[4.5 Production Testing](#45-production-testing)  
[4.6 Sustainability Considerations](#46-sustainability-considerations)  
[4.7 Challenges/Problems](#47-challengesproblems)  
[4.8 Solutions](#48-solutions)  

[5.0 Results and Discussion](#50-results-and-discussion)

[6.0 Conclusions](#60-conclusions)

[7.0 Appendix](#70-appendix)  
[7.1 Firmware Code](#71-firmware-code)  
[7.2 Mobile Application Code](#72-mobile-application-code)  

[8.0 References](#80-references)  

## List of Figures   
[Figure 1: Gantt Chart](#figure-1-gantt-chart)  

## 1.0 Introduction   
This project was undertaken to complete Humber Polytechnics CENG-355 Captsone project. The problem we are trying to solve is pet owners being away from their animals and not being able to interact or tend to thier needs while away. This dispensor aims to allow users to both interact with and feed thier pets while away from home. 

In section 1 we will discuss similar products on the market, the project requirements and specifications and the project schedule. In section 2 we will discuss the hardware devevlopment of the project. Section 3 links several reports about the development of the mobile app. Section 4 outlines the integration of the project as a whole.
### 1.1 Background
There are a few players on the market already for pet food dispenosrs. Pet Cube offers good remote pet interaction to keep pets engaged but lacks health information for the animals. Whistle offers good health tracking but lacks remote interaction with the animals. Our product aism to fuze these 2 functions into a single product.
### 1.2 Project Requirements and Specifications
This project has the following requirements.

Hardware:
- Raspberrypi 4
- Infared Sensor
- Solenoid push intermittent
- Stepper motor and a controller
- Camera

Firmware:
- Python dependencies (time, threading, cv2, smbus2, socket, RPI.GPIO, flask, pyrebase, pyngrok)
- Compatable python version

### 1.3 Project Schedule       
###### Figure 1: Gantt Chart     
![Gantt Chart](GhanttChart.png) 

## 2.0 Hardware Development Platform Report/Build Instructions   
### 2.1 Mathew Anderson-Saavedra/A
[Hardware report](hardware/ceng317reportMathew.md)   
### 2.2 Tevadi Brookes/B   
[Hardware report](hardware/ceng317reportTB.md)   
### 2.3 Zane Aransevia/C   
[Hardware report](hardware/rc522.md)  
### 2.4 Justin Chipman/D   
[Hardware report](hardware/ceng317report.md)   
  
## 3.0 Mobile Application Report   
### 3.1 Deliverable 1
[Mobile Deliverable 1](/tree/main/docs/Deliverable%201)   
### 3.2 Deliverable 2      
[Mobile Deliverable 2](/tree/main/docs/Deliverable%202)   
### 3.3 Deliverable 3      
[Mobile Deliverable 3](/tree/main/docs/Deliverable%203)   
### 3.4 Deliverable 4      
[Mobile Deliverable 4](/tree/main/docs/Deliverable%204)   
### 3.4 Deliverable 5      
[Mobile Deliverable 5](/tree/main/docs/Deliverable%205)   

## 4.0 Integration   
This secion will outline the various parts of our project being integrated together.
### 4.1 Enterprise Wireless Connectivity   
### 4.2 Database Configuration   
Our database is a realtime database using Firebase. We use the database to store feeding schedules, pet documents, login credentials, and remote connection URLs. The feeding schedules is fetched and stored locally on the Raspberrypi only being updated when the database signals there is a change. This allows the dispensor to still function without wifi if there is an outage. The pet documents are stored via the app for so the user has a one stop location for all its pets important information. The login credentials are stored in the database so that individual user data does not overlap and it can support more than one user.
### 4.3 Network and Security Considerations   
In this version of the project we are using a tunneling method to tie the raspberrypi's local ip on port 5000 to a public URL so that it can be accesses over other networks. In a production version I would ideally shift to a secure sever so that the feed is less accessable to the public. 

The passwords of the users are not stored in plain text in case of breaches. In future possible encrypting the email would increase security. 
### 4.4 Unit Testing   

### 4.5 Production Testing   
### 4.6 Sustainability Considerations
The controllers of the solenoid and the stepper motor require thier own individual power supply so they are supplied by either a battery or a seperate power adapter. This is not convinient and requires more powers sources than neccessary. 

A suggestion for future improvement would be creating a seperate PCB that can power all components with one adapter, this would make the project more sustainable.
### 4.7 Challenges/Problems   
- A major issue we had was getting the remote connection to be available accress other networks. 
### 4.8 Solutions   
- We created a tunneling method to allow the connection to be more available.
## 5.0 Results and Discussion   

## 6.0 Conclusions   

## 7.0 Appendix
### 7.1 Firmware Code   
[Link to firmware](hardware/firmware).
### 7.2 Mobile Application Code   
[Link to GitHub repository for app]()

## 8.0 References   
[COM-15093 Datasheet](https://mm.digikey.com/Volume0/opasdata/d220001/medias/docus/306/COM-15093_Web.pdf)

[ROB-11015 Datasheet](https://cdn.sparkfun.com/datasheets/Robotics/ZHO-0420S-05A4.5%20SPECIFICATION.pdf)

[Qwiic Relay Python Library](https://github.com/sparkfun/qwiic_relay_py/tree/main)

[AK9753 Datasheet](https://cdn.sparkfun.com/assets/6/7/9/8/e/AK9753_DS.pdf)

[sumbus2 Library](https://github.com/kplindegaard/smbus2)

[Qwiic_Human_Presence_Sensor_Breakout_AK9753 Github](https://github.com/sparkfun/Qwiic_Human_Presence_Sensor_Breakout_AK9753)

[Qwicc Motor Driver Guide](https://learn.sparkfun.com/tutorials/hookup-guide-for-the-qwiic-motor-driver#troubleshooting)

[Qwicc Motor Driver Python Library](https://github.com/sparkfun/Qwiic_SCMD_Py)

[Stepper Motor Datasheet](http://www.sparkfun.com/datasheets/Robotics/SM-42BYG011-25.pdf)
