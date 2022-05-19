package main.java;
//Dependencies
import org.eclipse.paho.client.mqttv3.*;
import java.nio.charset.StandardCharsets;
import org.eclipse.paho.client.mqttv3.MqttCallback;

public class MQttClient {

    public boolean isConnected = false;
    private MqttClient mMqttClient;
    private static final int QOS = 1;
    private static final String TAG = "IslandRush"; //unique identifier
    private static final String LOCALHOST = "127.0.0.1";
    private static final String MQTT_SERVER = "tcp://" + LOCALHOST + ":1883";

    //Constructor to create a mqtt instance
    public MQttClient(String serverUrl,String clientId) {
        try{
            mMqttClient = new MqttClient(serverUrl,clientId);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    //Connection to the local broker
    public void connect(MqttCallback clientCallback) {
        mMqttClient.setCallback(clientCallback);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        try {
            mMqttClient.connect(options);
            System.out.println("Connected to Broker");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    //disconnects from local broker
    public void diConnect() {
        try
        {
            mMqttClient.disconnect();
            System.out.println("disconnected");
            mMqttClient.close();
        }
        catch (MqttException e){
            e.printStackTrace();
        }
    }
    // Sending Messages to the broker
    public void publish(String topic, String message) throws MqttException {
        MqttMessage m= new MqttMessage(message.getBytes(StandardCharsets.UTF_8));
        m.setQos(QOS);
        m.setRetained(true);
        mMqttClient.publish(topic,m);
        System.out.println("message");
    }



    //Receiving messages from the broker
    public void subscribe(String topic, IMqttMessageListener iMqttMessageListener) throws MqttException {
        mMqttClient.subscribe(topic, new IMqttMessageListener() {
            public void messageArrived (String topic,MqttMessage message) throws Exception {
                if (topic.equals("/IslandRush/Server/name")) {
                    String payload = new String(message.getPayload());
                    Time player = new Time(payload,1000);
                    System.out.println(payload);
                    System.out.println(player.toString());
                }
            }
        });
    }

    /*public void connectToMqttBroker()  {
        if (!isConnected) {
            mMqttClient.connect(TAG, "", new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    isConnected = true;
                    try {
                        mMqttClient.subscribe("/IslandRush/Server/name", QOS, null);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                }
            }, new MqttCallback() {

                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                }

                @Override
                public void connectionLost(Throwable cause) {
                    isConnected = false;
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    if (topic.equals("/IslandRush/Server/name")) {
                        System.out.println(message.getPayload().toString());
                    }
                }
            });
        }
    }**/


        //Unsubscribe from a topic
    public void unSubscribe(String topic){
        try {
            mMqttClient.unsubscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}

