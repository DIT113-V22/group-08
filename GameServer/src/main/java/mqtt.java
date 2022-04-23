package com.company;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttConnect;

import java.nio.charset.StandardCharsets;

public class mqtt {
    private MqttClient client;
    private static final String TAG = "SmartcarMqttController";
    private static final String LOCALHOST = "127.0.0.1";
    private static final String MQTT_SERVER = "tcp://" + LOCALHOST + ":1883";

    public void connect(){
        try
            {
                MqttConnectOptions options= new MqttConnectOptions();
                options.setCleanSession(true);
                client= new MqttClient(MQTT_SERVER,TAG);
                client.connect(options);
                System.out.println("Connected to" +MQTT_SERVER);
            }
        catch (MqttException e){
            System.out.println("reason " + e.getReasonCode());
            System.out.println("msg " + e.getMessage());
            System.out.println("loc " + e.getLocalizedMessage());
            System.out.println("cause " + e.getCause());
            System.out.println("excep " + e);
            e.printStackTrace();
        }
    }
    public void publish(String topic, String message){
        try
        {
            MqttMessage m= new MqttMessage(message.getBytes(StandardCharsets.UTF_8));
            m.setQos(2);
            client.publish(topic,m);
            System.out.println("message");
        }
        catch (MqttException e){
            System.out.println("reason " + e.getReasonCode());
            System.out.println("msg " + e.getMessage());
            System.out.println("loc " + e.getLocalizedMessage());
            System.out.println("cause " + e.getCause());
            System.out.println("excep " + e);
            e.printStackTrace();
        }
    }

    public void diConnect() {
        try
        {
            client.disconnect();
            System.out.println("disconnected");
            client.close();
        }
        catch (MqttException e){
            System.out.println("reason " + e.getReasonCode());
            System.out.println("msg " + e.getMessage());
            System.out.println("loc " + e.getLocalizedMessage());
            System.out.println("cause " + e.getCause());
            System.out.println("excep " + e);
            e.printStackTrace();
        }
    }

}
