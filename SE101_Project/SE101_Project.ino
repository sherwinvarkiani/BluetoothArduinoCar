// Define the pin numbers
#include "dht.h" //temperature module
#define leftForward 3
#define leftBackward 2
#define rightForward 5
#define rightBackward 4
#define TEMP_PIN A0

dht DHT; 

char dir = '\0';
int counter = 0;
int avgtemp = 0;
int avghumidity = 0;
int value = 50;

void setup() {
    // Set the pin modes of the above IO pins to OUTPUT
    pinMode(leftForward, OUTPUT);
    pinMode(leftBackward, OUTPUT);
    pinMode(rightForward, OUTPUT);
    pinMode(rightBackward, OUTPUT);
    pinMode(2, INPUT);
    Serial.begin(9600);
}



void loop() {

  //if (counter % 2000 == 0) { //we dont want to constantly send data so we will only send it every so often
   /* DHT.read11(TEMP_PIN);
    avgtemp = DHT.temperature;
    avghumidity = DHT.humidity;
    Serial.print("Humidity: ");
    Serial.print(avghumidity);
    Serial.print("%\n");
    Serial.print("Temperature: ");
    Serial.print(avgtemp);
    Serial.print("C\n");
  }
  delay(3000);
  counter++;*/

  //if (Serial.available()) {

    Serial.println("forward");

      analogWrite(leftForward, value);
    
      digitalWrite(leftBackward, LOW);
      analogWrite(rightForward, value);
      digitalWrite(rightBackward, LOW);
      Serial.println("running");
      value++;
      if (value > 255) {
        value = 255;
      }
    
    /*dir = Serial.read();
    Serial.println("pressed");
    if (dir == 'u') { //forward
      
      Serial.println("forward");

      analogWrite(leftForward, value);
    
      digitalWrite(leftBackward, LOW);
      analogWrite(rightForward, value);
      digitalWrite(rightBackward, LOW);
      Serial.println("running");
      value++;
      if (value > 255) {
        value = 255;
      }
      
    } else if (dir == 'd') { //backward
      Serial.println("backward");
      
      digitalWrite(leftForward, LOW);
      digitalWrite(leftBackward, HIGH);
      digitalWrite(rightForward, LOW);
      digitalWrite(rightBackward, HIGH);
      value = 50;
      

    } else if (dir == 'l') { //left
      Serial.println("left");
  
      value = 50;   
      analogWrite(leftForward, 60);
      digitalWrite(leftBackward, LOW);
      analogWrite(rightForward, 255);
      digitalWrite(rightBackward, LOW);
    
      
    } else if (dir == 'r') { //right
      Serial.println("right");

      value = 50;
      analogWrite(leftForward, 255);
      digitalWrite(leftBackward, LOW);
      analogWrite(rightForward, 60);
      digitalWrite(rightBackward, LOW);

      
    }
    
  }*/

    
}
