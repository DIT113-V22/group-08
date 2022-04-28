#include <vector>
#include <MQTT.h>
#include <WiFi.h>
#ifdef __SMCE__
#include <OV767X.h>
#endif

#include <Smartcar.h>

MQTTClient mqtt;
WiFiClient net;

const char ssid[] = "***";
const char pass[] = "****";
const auto oneSecond = 1000UL;

//Camera 
std::vector<char> frameBuffer;

#ifdef __SMCE__
const auto triggerPin = 6;
const auto echoPin = 7;
const auto mqttBrokerUrl = "127.0.0.1";
#else
const auto triggerPin = 33;
const auto echoPin = 32;
const auto mqttBrokerUrl = "192.168.0.40";
#endif
const auto maxDistance = 400;

// car movements
int carSpeedB;
const auto carSpeed = 50;
const auto degreeLeft = -50; 
const auto degreeRight = 50;
const auto diagonalFirst = 80;
const auto diagonalRight = 20;
const auto diagonalLeft = -20;
const auto diagonalDelay = 1000;

ArduinoRuntime arduinoRuntime;
// Motors
BrushedMotor leftMotor(arduinoRuntime, smartcarlib::pins::v2::leftMotorPins);
BrushedMotor rightMotor(arduinoRuntime, smartcarlib::pins::v2::rightMotorPins);
// Steering
DifferentialControl control(leftMotor, rightMotor);
// Ultrasonic sensor
SR04 front(arduinoRuntime, triggerPin, echoPin, maxDistance);
// Odometer
const auto pulsesPerMeter = 600;
DirectionlessOdometer leftOdometer{ arduinoRuntime, smartcarlib::pins::v2::leftOdometerPin,
  []() { leftOdometer.update(); }, pulsesPerMeter };
DirectionlessOdometer rightOdometer{ arduinoRuntime, smartcarlib::pins::v2::rightOdometerPin,
  []() { rightOdometer.update(); }, pulsesPerMeter };

const auto distanceR = String(leftOdometer.getDistance());
const auto distanceL = String(rightOdometer.getDistance());
const auto speedR = String(rightOdometer.getSpeed());
const auto speedL = String(leftOdometer.getSpeed());


SimpleCar car(control);


void setup() {
  Serial.begin(9600);
#ifdef __SMCE__
  Camera.begin(QVGA, RGB888, 15);
  frameBuffer.resize(Camera.width() * Camera.height() * Camera.bytesPerPixel());
#endif

  WiFi.begin(ssid, pass);
  mqtt.begin(mqttBrokerUrl, 1883, net);

  Serial.println("Connecting to WiFi...");
  auto wifiStatus = WiFi.status();
  while (wifiStatus != WL_CONNECTED && wifiStatus != WL_NO_SHIELD) {
    Serial.println(wifiStatus);
    Serial.print(".");
    delay(1000);
    wifiStatus = WiFi.status();
  }


  Serial.println("Connecting to MQTT broker");
  while (!mqtt.connect("arduino", "public", "public")) {
    Serial.print(".");
    delay(1000);
  }

  mqtt.subscribe("/islandRush/control/#", 1);
  mqtt.onMessage([](String topic, String message)
  {
    if(topic == "/islandRush/control/controller") {
          auto input = message.toInt();
            switch (input){
        case 1: // up 
            car.setSpeed(carSpeed);
            car.setAngle(0);
            break;
        case 2: // up-right  
            car.setSpeed(carSpeed);
            car.setAngle(diagonalFirst);
            delay(diagonalDelay);
            car.setAngle(diagonalRight);
            break;
        case 3: //right 
            car.setSpeed(carSpeed);
            car.setAngle(degreeRight);
            break;
        case 4: // down-right 
          car.setSpeed(carSpeed);
            car.setAngle(-diagonalFirst);
            delay(diagonalDelay);
            car.setAngle(diagonalRight);
            break;
        case 5: // down
            car.setSpeed(carSpeed);
            car.setAngle(0);
            break;
        case 6: //   downleft
            car.setSpeed(carSpeed);
            car.setAngle(-diagonalFirst);
            delay(diagonalDelay);
            car.setAngle(diagonalLeft);
            break;
        case 7: //left
            car.setSpeed(carSpeed);
            car.setAngle(degreeLeft);
            break;
        case 8: // up left 
            car.setSpeed(carSpeed);
            car.setAngle(diagonalFirst);
            delay(diagonalDelay);
            car.setAngle(diagonalLeft);
            break;
        default: 
            car.setSpeed(0);
            car.setAngle(0);
        }
    } else if(topic == "/islandRush/control/speed") {
       // This is going to be use for the introduction of the speed buttons 
        carSpeedB= message.toInt();
        car.setSpeed(message.toInt());
    }else {
      Serial.println(topic + " " + message);
    }
  });
}

void loop() {
  if (mqtt.connected()) {
    mqtt.loop();
    const auto currentTime = millis();
#ifdef __SMCE__
    static auto previousFrame = 0UL;
    if (currentTime - previousFrame >= 65) {
      previousFrame = currentTime;
      Camera.readFrame(frameBuffer.data());
      mqtt.publish("/IslandRush/camera", frameBuffer.data(), frameBuffer.size(),
                 false, 0);
    }
#endif
    static auto previousTransmission = 0UL;
    if (currentTime - previousTransmission >= oneSecond) {
      previousTransmission = currentTime;
      const auto distance = String(front.getDistance());
      mqtt.publish("/IslandRush/ultrasound/front", distance);
      mqtt.publish("/IslandRush/Odometer/LeftDistance", "Left odometer distance: " + distanceL);
      mqtt.publish("/IslandRush/odometer/RightDistance", "Right odometer distance: " + distanceR);
      mqtt.publish("/IslandRush/odometer/RightSpeed", "Right odometer speed: " + speedR);
      mqtt.publish("/IslandRush/odometer/LeftSpeed", "Left odometer speed: " + speedL);
    }
  }
#ifdef __SMCE__
  // Avoid over-using the CPU if we are running in the emulator
  delay(1);
#endif
}
