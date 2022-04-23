//#include <vector>

#include <MQTT.h>
#include <WiFi.h>

#include <Smartcar.h>

//#ifdef __SMCE__
//#include <OV767X.h>
//#endif

#ifdef __SMCE__
WiFiClient net;
#endif

MQTTClient mqtt;

//camera
const auto oneSecond = 1000UL;
//std::vector<char> frameBuffer;

//directions
const int forwardSpeed = 80;
const int backSpeed = -50;
const int acceleration = 100;
const int forwardLeft = -80;
const int forwardRight = 80;
const int backRight = 80;
const int backLeft = -80;
const int forwardDiagonalRight = 20;
const int forwardDiagonalLeft = -20;
const int backDiagonalRight = 20;
const int backDiagonalLeft = -20;

ArduinoRuntime arduinoRuntime;

//Motors
BrushedMotor leftMotor(arduinoRuntime, smartcarlib::pins::v2::leftMotorPins);
BrushedMotor rightMotor(arduinoRuntime, smartcarlib::pins::v2::rightMotorPins);

//Steering
DifferentialControl control(leftMotor, rightMotor);

//Odometers
const auto pulsesPerMeter = 600;
DirectionlessOdometer leftOdometer{ arduinoRuntime, smartcarlib::pins::v2::leftOdometerPin,
  []() { leftOdometer.update(); }, pulsesPerMeter };
DirectionlessOdometer rightOdometer{ arduinoRuntime, smartcarlib::pins::v2::rightOdometerPin,
  []() { rightOdometer.update(); }, pulsesPerMeter };

DistanceCar car(arduinoRuntime, control, leftOdometer, rightOdometer);

void setup()
{
  // put your setup code here, to run once:
  Serial.begin(9600);

#ifdef __SMCE__
  // mqtt.begin(WiFi);
  mqtt.begin(net);
#endif

  if (mqtt.connect("arduino", "public", "public")) {
    mqtt.subscribe("/smartcar/control/#", 1);
    mqtt.onMessage([](String topic, String message) {
      if (topic == "/smartcar/control/handleInput") {
        handleInput(message);
        Serial.println("Command " + message + " received");
      }
      else  {
        Serial.println(topic + " " + message);
      }
    });
  }
}

void loop()
{
  //Serial.println("Distance: " + String(leftOdometer.getDistance()));
  //Serial.println("Speed: " + String(leftOdometer.getSpeed()));
  //delay(100);

  if (mqtt.connected()) {
    mqtt.loop();
    const auto currentTime = millis();
    static auto previousTransmission = 0UL;
    if (currentTime - previousTransmission >= oneSecond) {
      previousTransmission = currentTime;
      const auto distanceR = String(leftOdometer.getDistance());
      const auto distanceL = String(rightOdometer.getDistance());
      const auto speedR = String(rightOdometer.getSpeed());
      const auto speedL = String(leftOdometer.getSpeed());

      mqtt.publish("/smartcar/odometer/leftOdometer", "Left odometer distance: " + distanceL);
      mqtt.publish("/smartcar/odometer/rightOdometer", "Right odometer distance: " + distanceR);
      mqtt.publish("/smartcar/odometer/rightOdometer", "Right odometer speed: " + speedR);
      mqtt.publish("/smartcar/odometer/leftOdometer", "Left odometer speed: " + speedL);
    }
  }
}

void handleInput(String mqttMessage)
{
  //if (Serial.available()) {
  //char input = Serial.read();

  int input = mqttMessage.substring(0).toInt();

  switch (input)
  {
    case 4: //left
      car.setSpeed(forwardSpeed);
      car.setAngle(forwardLeft);
      break;
    case 6: // right
      car.setSpeed(forwardSpeed);
      car.setAngle(forwardRight);
      break;
    case 8: // ahead
      car.setSpeed(forwardSpeed);
      car.setAngle(0);
      break;
    case 2: // back
      car.setSpeed(backSpeed);
      car.setAngle(0);
      break;
    case 7: // diagonally to the left
      car.setSpeed(forwardSpeed);
      car.setAngle(forwardDiagonalLeft);
      break;
    case 9: // diagonally to the right
      car.setSpeed(forwardSpeed);
      car.setAngle(forwardDiagonalRight);
      break;
    case 1: // diagonally back to the left
      car.setSpeed(backSpeed);
      car.setAngle(backDiagonalLeft);
      break;
    case 3: // diagonally back to the right
      car.setSpeed(backSpeed);
      car.setAngle(forwardDiagonalRight);
      break;
    case 5:
      car.setSpeed(acceleration);
      break;

    default: // just stop
      car.setSpeed(0);
      car.setAngle(0);
  }
}
