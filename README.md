![Arduino Build Status](https://github.com/DIT113-V22/group-08/actions/workflows/arduino-build.yml/badge.svg)
![Android CI Status](https://github.com/DIT113-V22/group-08/actions/workflows/android-ci.yml/badge.svg)
[![Build Island.zip Artifact](https://github.com/DIT113-V22/group-08/actions/workflows/Godot%20CI.yml/badge.svg)](https://github.com/DIT113-V22/group-08/actions/workflows/Godot%20CI.yml)
# group-08
#     IslandRush
![banner](https://user-images.githubusercontent.com/36161357/170886247-985108f7-9103-4294-9711-9f383b0ed6b8.png)
***
# Project Description
IslandRush is an Android app that grants the user to drive a car in the SMCE client using a virtual controller on a phone.The SMCE environment has been modded to represent an Island with race-tracks.It is a racing game that allows a user to compete against oneself and other users.
The game supports features to accomodate the feeling of a real-life racing experience such as multi-directional Joystick and Speed Controllers.The best scores are measured by the least time taken to reach the finish line and saved in a leaderboard.
Additionally,the app also offers its user to explore the Island and play in our customized explorer mode.The game can be played in both portrait and landscape mode.
***
# Project Purpose
Games are a kind of mind-workout exercises disguised as fun.[Scientific studies](https://www.sciencealert.com/gamers-have-more-grey-matter-and-better-brain-connectivity-study-suggests) have shown that playing video games regularly may increase gray matter in our brains. Grey matter is a region associated with the central nervous system that enables humans to control movement, memory, and emotions. Moreover, a [past research](https://www.independent.co.uk/games/video-games-children-learning-intelligence-social-skills-study-a6920961.html) involving children have shown that games may improve the childrens social and intellectual skills.

It is also important to point out that video games have only two outcomes -either you win or you keep trying, learning from previous mistakes. It could be possible to argue that games inspire individuals to be more persistent[(for more information)](https://www.edutopia.org/blog/neurologist-makes-case-video-game-model-learning-tool).

IslandRush aims to let users grow to become more social,intellectual and persistent and also have fun at the same time.
***
# How does it work ?
The Android app will be developed in Android Studio which uses XML for the UI and Kotlin or Java for the back-end code. The car-behavior will be programmed in C++ using Arduino and the SmartCar API. We will use GODOT to mod the environment in SMCE emulator to visualize the race and explorer mode of the game. A database will be set-up to store player data using the help of SQLite and Room Library. Github will be used to plan, manage documentations and update code during the development process. We also plan to have some UI testing using Expresso and ensure quality in our software product.
***
# System Architecture
### MQTT Communication Between Components

![image](https://user-images.githubusercontent.com/91395562/170117168-38bb8330-cdb9-4f63-9207-d5e69586391b.png)
![image](https://user-images.githubusercontent.com/91395562/170123349-4aa88ede-7eca-400d-8c26-636bdeda88f8.png)

For more details and clarity,visit our Wiki page on [System Architecture](https://github.com/DIT113-V22/group-08/wiki/System-Architecture).

### SmartCar Specifications
* ESP32 Board
* OV767X Camera (currently unused)
* Brushed Motors
* SENSORS
  - Ultra-Sonic
  - Directionless Odometer
***
# Setup Guide
### Software Requirements
* [Android Studio Bumblebee (2021.1.1)](https://developer.android.com/studio/archive)
  - Required Gradle Plugin Version :  3.2-7.1 [(for more info)](https://developer.android.com/studio/releases/gradle-plugin)
* [Mosquitto Broker](https://mosquitto.org/download/).
* [SMCE-gd v1.3.4](https://github.com/ItJustWorksTM/smce-gd/releases/tag/v1.3.4) 
* Virtual Device in Android Studio.
  - Recommended Emulator for App: Nexus 6 API 30
  - Visit our Wiki Page on [Virtual Device](https://github.com/DIT113-V22/group-08/wiki/Virtual-Device) for Setup.

For installation and further guidance,visit our Wiki Page on [Setup Guides](https://github.com/DIT113-V22/group-08/wiki/Setup-Guides) and choose your Operating System.

***
# Getting Started
### Installation

* Step 1: Navigate to our Repository using https://github.com/DIT113-V22/group-08
* Step 2: Navigate to [Releases](https://github.com/DIT113-V22/group-08/releases) and download IslandRush as a zip file and unzip it. 
* Step 3: While you are on [Releases](https://github.com/DIT113-V22/group-08/releases) page, you can also download mods for our Race and Explore mode.

### Opening IslandRush in Android Studio:
* Step 1: Open Android Studio
* Step 2: Navigate to "Open Project" and select the IslandRush (unzipped) folder.
* Step 3: Once the project is opened,click on "Sync Project with Gradle Files"
* Step 4: Create a virtual device to run our app. Visit our Wiki on [Virtual Device](https://github.com/DIT113-V22/group-08/wiki/Virtual-Device) for guidance.
* Step 5: Once you are done setting up your virtual device,click on the green arrow-head button on the top-right corner to run the app.

Note: Remember to keep Auto-Rotate "turned on" in the virtual device to play the game in both Portrait and Landscape mode.

### Open SMCE GODOT Environment
* Step 1: Assuming the SMCE app is installed, double click on it to start the app. 
* Step 2: Follow our [Modding Instructions](https://github.com/DIT113-V22/group-08/wiki/Modding-Instructions) wiki to change the default environment
* Step 3: Click on the "+" sign on the top-left corner of the app to add the SmartCar and Finish Line Sketch from the unzipped project.

### Get Mosquitto Broker running
* Open Command-line terminal
* Change Directory to "C:\Program Files\mosquitto"
* Type "mosquitto -v" to start the broker

### Typical Setup with broker running on background.
![image](https://user-images.githubusercontent.com/91395562/170592178-e08fefa4-7b16-4a57-b60b-a99bc02a5b40.png)

***
# Demo Video
Check out our [Demo Video](https://youtu.be/bcX4QcTOgmg) to discover and get a taste of what IslandRush has to offer!

Feel free to check out our Wiki Page on [User Manual](https://github.com/DIT113-V22/group-08/wiki/User-Manual) and [IslandRush Sitemap](https://github.com/DIT113-V22/group-08/wiki/IslandRush-Sitemap) for further guidance.
***
### License
MIT © group-08

The source code for the repository is licensed under the MIT license, refer to LICENSE file in the repository.
***






