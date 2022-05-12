#include <MQTT.h>
#include <WiFi.h>
#include <Smartcar.h>

MQTTClient mqtt;
WiFiClient net;

const char ssid[] = "***";
const char pass[] = "****";
const auto oneSecond = 1000UL;

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
    return (distance > 0 && distance <= 100);
}

void loop() {
  if (mqtt.connected()) {
    mqtt.loop();
    const auto currentTime = millis();
    static auto previousTransmission = 0UL;
    if (currentTime - previousTransmission >= oneSecond) {
// if the IR sensor detects an object publish to mqtt 
      if (carDetected()) {
        mqtt.publish(MAIN_TOPIC, "Race Finish");
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
