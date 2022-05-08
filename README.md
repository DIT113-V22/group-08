![Arduino Build Status](https://github.com/DIT113-V22/group-08/actions/workflows/arduino-build.yml/badge.svg)
![Android CI status](https://github.com/DIT113-V22/group-08/actions/workflows/android-ci.yml/badge.svg)
# group-08

# What is IslandRush?
It is an Android App that grants the user to drive a car in the SMCE client using a virtual controller on a phone. IslandRush allows a user to compete versus oneself and other users. The best scores are going to be saved in a leaderboard where the scores are going to be measured by time and best average speed. 

# Why will you make it?
Gaming is mind-workout disguised as fun. [Studies](https://www.sciencealert.com/gamers-have-more-grey-matter-and-better-brain-connectivity-study-suggests) have shown that playing video games regularly may increase gray matter in our brains. Grey matter are associated with the central nervous system that enables humans to control movement, memory, and emotions. Past [research](https://www.independent.co.uk/games/video-games-children-learning-intelligence-social-skills-study-a6920961.html) involving children have shown that games may improve the childrens social and intellectual skills.

It is also important to point out that video games have only two outcomes -either you win or you keep trying, learning from previous mistakes. It could be possible to argue that games inspire indivuals to be more persistent.[(for more information)](https://www.edutopia.org/blog/neurologist-makes-case-video-game-model-learning-tool)

# How does it work?
The App will be created in Android studios which uses XML for the UI and Kotlin or Java for the code.
The car-behavior will be writted in C++ using Arduino and the smartCar API.
We will use the SMCE emulator to visualize the racing track.
Github will be used for document management and updating the code.

# System Overview
## MQTT Communication to control the Car
![image](https://user-images.githubusercontent.com/91395562/167301922-1f88ef16-09fb-4330-87c5-134b6ddffba8.png)
***
## MQTT Communication to view the Leaderboard
![image](https://user-images.githubusercontent.com/91395562/167301857-1396b062-6cbf-4637-824f-2c654bcfb0cb.png)

For more details and clarity,please visit our Wiki page on [System Overview](https://github.com/DIT113-V22/group-08/wiki/System-Overview).
