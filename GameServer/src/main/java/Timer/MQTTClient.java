/*package main.java;

import org.eclipse.paho.client.mqttv3.*;

import java.nio.charset.StandardCharsets;

public class MQTTClient {
    private static MqttClient client;
    private static final String TAG = "IslandRush";
    private static final String LOCALHOST = "127.0.0.1";
    private static final String MQTT_SERVER = "tcp://" + LOCALHOST + ":1883";
    private static final int QOS = 1;
    private boolean isConnected=false;

    private static final String server = "/IslandRush/server";

    public static void connect(){
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
    public static void publish(String topic, String message){
        try
        {
            MqttMessage m= new MqttMessage(message.getBytes(StandardCharsets.UTF_8));
            m.setQos(QOS);
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
    public void subscribe (String topic,int qos){
                      try {
                         client.subscribe(topic, qos, null);
                      } catch (MqttException e) {
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
    public  void connectToMqttBroker() {
        if(!isConnected) {
            client.connect(TAG,
                    "",
                    new IMqttActionListener(){
                        @Override
                        public void onSuccess(IMqttToken iMqttToken) {
                            isConnected = true;

                            final String successConnection = "Connected to MQTT broker";
                            System.out.println(TAG + successConnection);
                            try {
                                client.subscribe(server,QOS,null);
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(IMqttToken iMqttToken, Throwable throwable) {

                        }
                    },
                    new MqttCallback(){

                        @Override
                        public void connectionLost(Throwable throwable) {

                        }

                        @Override
                        public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

                        }

                        @Override
                        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                        }
                    });
        }
    }

    /**
     * @Override
     *             public void onSuccess (IMqttToken iMqttToken){
     *
     *             }
     *
     *             @Override
     *             public void onFailure (IMqttToken iMqttToken, Throwable throwable){
     *
     *             }
     *
     *             public void subscribe (String topic,int qos){
     *                 try {
     *                     client.subscribe(topic, qos, null);
     *                 } catch (MqttException e) {
     *                     e.printStackTrace();
     *                 }
     *             }
     *
     *
     *             @Override
     *             public void connectionLost (Throwable throwable){
     *                 System.out.println("Connection to the broker lost");
     *             }
     *
     *             @Override
     *             public void messageArrived (String topic, MqttMessage mqttMessage) throws Exception {
     *                 String message = new String(mqttMessage.getPayload());
     *                 System.out.println("[MQTT] Topic: " + topic + " | Message: " + message);
     *                 // add the if statement for the different topic
     *             }
     *
     *             @Override
     *             public void deliveryComplete (IMqttDeliveryToken iMqttDeliveryToken){
     *                 System.out.println("Message Delivered");
     *             }
     */