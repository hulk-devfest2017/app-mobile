package sqli.com.hulkchallenge.mqtt;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by renaud on 16/06/17.
 */

public class MqttService {

    MqttAndroidClient mqttAndroidClient;


    public MqttService(String clientId, Context context, String serverUri, final Activity activity) {
        try {

            clientId = clientId + System.currentTimeMillis();
            mqttAndroidClient = new MqttAndroidClient(context, serverUri, clientId);

            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setAutomaticReconnect(true);
            mqttConnectOptions.setCleanSession(false);
            //addToHistory("Connecting to " + serverUri);
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(activity, "erreur lors de la connexion",Toast.LENGTH_LONG).show();
                }
            });


        } catch (MqttException ex){
            ex.printStackTrace();
        }
    }

    public void publishMessage(String payload, String topic){

        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(payload.getBytes());
            mqttAndroidClient.publish(topic, message);
        } catch (MqttException e) {
            System.err.println("Error Publishing: " + e.getMessage());
            e.printStackTrace();
        }
    }



    public void subscribeToTopic(String topic, IMqttActionListener listener){
        try {
            mqttAndroidClient.subscribe(topic, 0, null, listener);

        } catch (MqttException ex){
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }

    public MqttAndroidClient getMqttAndroidClient() {
        return mqttAndroidClient;
    }
}
