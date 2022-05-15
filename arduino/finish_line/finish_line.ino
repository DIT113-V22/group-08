#include <MQTT.h>
#include <WiFi.h>
#include <Smartcar.h>

MQTTClient mqtt;
WiFiClient net;

const char ssid[] = "***";
const char pass[] = "****";
const auto oneSecond = 1000UL;
// variable to stop publishing to mqtt after the car has been detected
bool detected = false;

#ifdef __SMCE__
const auto triggerPin = 6;
const auto echoPin = 7;
const auto mqttBrokerUrl = "127.0.0.1";

#else 
const auto triggerPin = 33;
const auto echoPin = 32;
const auto mqttBrokerUrl = "192.168.0.40";

#endif

//topics

const String MAIN_TOPIC ="/IslandRush/Mood/Race/Finish";

ArduinoRuntime arduinoRuntime;

// Infrared sensor
GP2Y0A21 back (arduinoRuntime,3);

void setup() {
  Serial.begin(9600);
  
// starts wifi connection to localhost
  WiFi.begin(ssid, pass);
  
// begin broker on localhost at port 1883
  mqtt.begin(mqttBrokerUrl, 1883, net);

// checks wifistatus 
  Serial.println("Connecting to WiFi...");
  auto wifiStatus = WiFi.status();
  while (wifiStatus != WL_CONNECTED && wifiStatus != WL_NO_SHIELD) {
    Serial.println(wifiStatus);
    Serial.print(".");
    delay(1000);
    wifiStatus = WiFi.status();
  }

//trying to connect arduino to finish line
  Serial.println("Connecting to MQTT broker");
  while (!mqtt.connect("arduino2", "public", "public")) {
    Serial.print(".");
    delay(1000);
  } 
  if (mqtt.connected()) {
    Serial.println("Connected to MQTT Broker");
    }
}

bool carDetected() {
    const auto distance = back.getDistance();
    return (distance > 0 && distance <= 200);
}

void loop() {
  if (mqtt.connected()) {
    mqtt.loop();
// initializing timer
    const auto currentTime = millis();
    static auto previousTransmission = 0UL;
    if ((currentTime - previousTransmission >= oneSecond) && (detected == false)) {
// if the IR sensor detects an object publish topic to mqtt 
      if (carDetected() && currentTime > 30000) {
        mqtt.publish(MAIN_TOPIC, "");
// car is detected so I stop checking the surroundings
        detected = true;
        Serial.println("Passing Finish Line !");
      }
      previousTransmission = currentTime;
    }
  }
     
#ifdef __SMCE__
  // Avoid over-using the CPU if we are running in the emulator
  delay(1);
#endif
}
