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

//Camera stream
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

const auto oneSecond = 1000UL;

//topics
const String MAIN_TOPIC = "/IslandRush";
const char CAMERA[] = "/IslandRush/camera";
//OLD Sensor topics
const String ULTRASOUND = MAIN_TOPIC +  "/ultrasound/front";
const String ODOMETER_LEFT_DISTANCE = MAIN_TOPIC + "/Odometer/LeftDistance";
const String ODOMETER_RIGHT_DISTANCE = MAIN_TOPIC + "/Odometer/RightDistance";
const String ODOMETER_LEFT_SPEED = MAIN_TOPIC + "/Odometer/LeftSpeed";
const String ODOMETER_RIGHT_SPEED = MAIN_TOPIC + "/Odometer/RightSpeed";
// new topics to test
const String ODOMETOR_DISTANCE  = MAIN_TOPIC + "/Odometer/Distance";
const String ODOMETOR_SPEED  = MAIN_TOPIC + "/Odometer/Speed";
//Controller topics;
const String JOYSTICK = MAIN_TOPIC + "/Control/Joystick/Direction";
const String CONTROLPAD = MAIN_TOPIC + "/Control/ControlPad/Direction";
const String SPEED = MAIN_TOPIC + "/Control/Speed";


ArduinoRuntime arduinoRuntime;
// Motors
BrushedMotor leftMotor(arduinoRuntime, smartcarlib::pins::v2::leftMotorPins);
BrushedMotor rightMotor(arduinoRuntime, smartcarlib::pins::v2::rightMotorPins);
// Steering
DifferentialControl control(leftMotor, rightMotor);
// Odometer
const auto pulsesPerMeter = 600;
DirectionlessOdometer leftOdometer{ arduinoRuntime, smartcarlib::pins::v2::leftOdometerPin,
  []() { leftOdometer.update(); }, pulsesPerMeter };
DirectionlessOdometer rightOdometer{ arduinoRuntime, smartcarlib::pins::v2::rightOdometerPin,
  []() { rightOdometer.update(); }, pulsesPerMeter };

DistanceCar car(arduinoRuntime, control, leftOdometer, rightOdometer);

// Car movements
int currentSpeed;
auto maxAngle = 40;

const auto degreeLeft = -40; 
const auto degreeRight = 40;

void setup() {
  Serial.begin(9600);
  
#ifdef __SMCE__
    Camera.begin(QVGA, RGB888, 15);
  frameBuffer.resize(Camera.width() * Camera.height() * Camera.bytesPerPixel());
#endif
// Stars Wifi connection to localhost using port 1883
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

  mqtt.subscribe(MAIN_TOPIC + "/Control/#", 1);
  mqtt.onMessage([](String topic, String message)
  {
    if(topic == JOYSTICK){
        car.setAngle(round(message.toFloat() * maxAngle));    
    }else if (topic == SPEED){
      // the speed is set between 0-100, increasing or decrising by 10 everytime
         currentSpeed = message.toInt();
        car.setSpeed(currentSpeed);
    }else if(topic == CONTROLPAD){
          auto input = message.toInt();
        switch (input){
        case 1: // up 
            car.setSpeed(currentSpeed);
            car.setAngle(0);
            break;
        case 2: //right 
            car.setSpeed(currentSpeed);
            car.setAngle(degreeRight);
            break;
        case 3: // down
            car.setSpeed(currentSpeed);
            car.setAngle(0);
            break;
        case 4: //left
            car.setSpeed(currentSpeed);
            car.setAngle(degreeLeft);
            break;
        default: 
            car.setSpeed(0);
            car.setAngle(0);
        }             
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
     mqtt.publish(CAMERA, frameBuffer.data(), frameBuffer.size(),
                   false, 0);
    }
#endif
    static auto previousTransmission = 0UL;
    if (currentTime - previousTransmission >= oneSecond) {
      previousTransmission = currentTime;
  // average speed of the car via both Odometers ( meters per second)
     mqtt.publish(ODOMETOR_SPEED, String(car.getSpeed()));
  // average distance from both Odometers (in centimeters)   
     mqtt.publish(ODOMETOR_DISTANCE, String(car.getDistance()));
    }
  }
#ifdef __SMCE__
  // Avoid over-using the CPU if we are running in the emulator
  delay(1);
#endif
}
