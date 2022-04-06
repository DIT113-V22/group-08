#include <Smartcar.h>

const int fSpeed   = 70;  
const int bSpeed   = -70; 
const int lDegrees = -75; 
const int rDegrees = 75;  
const int TRIGGER_PIN = 6;
const int ECHO_PIN = 7;
const unsigned int MAX_DISTANCE =100;

ArduinoRuntime arduinoRuntime;
SR04 front(arduinoRuntime, TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);
BrushedMotor leftMotor(arduinoRuntime, smartcarlib::pins::v2::leftMotorPins);
BrushedMotor rightMotor(arduinoRuntime, smartcarlib::pins::v2::rightMotorPins);
DifferentialControl control(leftMotor, rightMotor);

SimpleCar car(control);

void setup()
{
    Serial.begin(9600);
}

void handleInput(){
    if(Serial.available()){
        char input = Serial.read(); 
                                    
        switch (input)
        {
        case 'l': 
            car.setSpeed(fSpeed);
            car.setAngle(lDegrees);
            break;
        case 'r': 
            car.setSpeed(fSpeed);
            car.setAngle(90);
            break;
        case 'f': 
            car.setSpeed(fSpeed);
            car.setAngle(0);
            break;
        case 'b': 
            car.setSpeed(bSpeed);
            car.setAngle(0);
            break;
        default: 
            car.setSpeed(0);
            car.setAngle(0);
        }
    }
}

void loop()
{
   
    Serial.println(front.getDistance());
    delay(100);
    
    if(front.getDistance() <= 100 && front.getDistance()>0){
          car.setSpeed(0);
       // the car go back   
          car.setSpeed(bSpeed);
          delay(1500);
       // change angle to the left   
           car.setAngle(90);
           delay(1000);
           car.setAngle(0);
      // code below to create a small accelaration     
           car.setSpeed(20);
           delay(1000);
           car.setSpeed(40);
           delay(1000);
           car.setSpeed(fSpeed);
           
     }
   
    
     
      

    
     handleInput();
}
