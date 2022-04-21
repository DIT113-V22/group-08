package com.quinstedt.graceapp

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.eclipse.paho.client.mqttv3.*


class ControlPad : AppCompatActivity() {

    // Connect to MQTT broker
    private val TAG = "GRace"
    private val EXTERNAL_MQTT_BROKER = "aerostun.dev"
    private val LOCALHOST = "10.0.2.2"
    private val MQTT_SERVER = "tcp://$LOCALHOST:1883"
    private var mMqttClient: MqttClient? = null
    private val QOS = 1
    private var isConnected = false

    // Subcription topics
    private val MAIN_TOPIC = "/smartcar/control"
    private val MANUAL = "$MAIN_TOPIC/manual"
    private val JOYSTICK = "$MAIN_TOPIC/joystick"
    private val CAMERA = "/smartcar/camera"
    private val ULTRASOUND ="/smartcar/ultrasound/front"

    // Controller messages to MQTT broker
    private val FORWARD = 1
    private val RIGHT = 3
    private val REVERSE = 5
    private val LEFT = 7
    private val STOP = 9

    // Camera view
    private val IMAGE_WIDTH = 320
    private val IMAGE_HEIGHT = 240
    private var mCameraView: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control_pad)
        mMqttClient = MqttClient(applicationContext, MQTT_SERVER, TAG)
        mCameraView = findViewById(R.id.controlPad_camera)
        connectToMqttBroker()
    }


    override fun onResume() {
        super.onResume()
        connectToMqttBroker()
    }

    override fun onPause() {
        super.onPause()
        mMqttClient!!.disconnect(object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken) {
                Log.i(TAG, "Disconnected from broker")
            }

            override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {}
        })
    }

    private fun connectToMqttBroker() {
        if (!isConnected) {
            mMqttClient!!.connect(TAG, "", object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    isConnected = true
                    val successfulConnection = "Connected to MQTT broker"
                    Log.i(TAG, successfulConnection)
                    Toast.makeText(applicationContext, successfulConnection, Toast.LENGTH_SHORT)
                        .show()

                    mMqttClient!!.subscribe(ULTRASOUND, QOS, null)
                    mMqttClient!!.subscribe(CAMERA, QOS, null)
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    val failedConnection = "Failed to connect to MQTT broker"
                    Log.e(TAG, failedConnection)
                    Toast.makeText(applicationContext, failedConnection, Toast.LENGTH_SHORT).show()
                }
            }, object : MqttCallback {
                override fun connectionLost(cause: Throwable) {
                    isConnected = false
                    val connectionLost = "Connection to MQTT broker lost"
                    Log.w(TAG, connectionLost)
                    Toast.makeText(applicationContext, connectionLost, Toast.LENGTH_SHORT).show()
                }

                @Throws(Exception::class)  // camera
                // image processing, + stuff, reaserach
                override fun messageArrived(topic: String, message: MqttMessage) {
                    if (topic == CAMERA) {
                        val bm =
                            Bitmap.createBitmap(IMAGE_WIDTH, IMAGE_HEIGHT, Bitmap.Config.ARGB_8888)
                        val payload = message.payload
                        val colors = IntArray(IMAGE_WIDTH * IMAGE_HEIGHT)
                        for (ci in colors.indices) {
                            val r = payload[3 * ci]
                            val g = payload[3 * ci + 1]
                            val b = payload[3 * ci + 2]
                            colors[ci] = Color.rgb(r.toInt(), g.toInt(), b.toInt())
                        }
                        bm.setPixels(colors, 0, IMAGE_WIDTH, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT)
                        mCameraView!!.setImageBitmap(bm)
                    } else {
                        Log.i(TAG, "[MQTT] Topic: $topic | Message: $message")
                    }
                }

                override fun deliveryComplete(token: IMqttDeliveryToken) {
                    Log.d(TAG, "Message delivered")
                }
            })
        }
    }

    private fun drive(direction: Int, actionDescription: String?) {
        if (!isConnected) {
            val notConnected = "Not connected (yet)"
            Log.e(TAG, notConnected)
            Toast.makeText(applicationContext, notConnected, Toast.LENGTH_SHORT).show()
            return
        }
        Log.i(TAG, actionDescription!!)
        mMqttClient!!.publish(MANUAL, direction.toString(), QOS, null)
        mMqttClient!!.publish(JOYSTICK, direction.toString(), QOS, null)
    }

    fun moveForward(view: View?) {
        drive(FORWARD, "Moving forward")
    }

    fun moveForwardLeft(view: View?) {
        drive(LEFT, "Moving to the left")
    }

    fun stop(view: View?) {
        drive(STOP, "Stopping")
    }

    fun moveForwardRight(view: View?) {
        drive(RIGHT, "Moving right")
    }

    fun moveBackward(view: View?) {
        drive(REVERSE, "Moving to the left")
    }
}