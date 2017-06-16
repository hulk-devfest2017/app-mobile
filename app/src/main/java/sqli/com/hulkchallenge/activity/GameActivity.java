package sqli.com.hulkchallenge.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import sqli.com.hulkchallenge.R;
import sqli.com.hulkchallenge.model.Player;
import sqli.com.hulkchallenge.mqtt.MqttService;

public class GameActivity extends AppCompatActivity {

    public static final String PLAYER_INFORMATION = "PLAYER_INFORMATION";

    String clientId = "ExampleAndroidClient";
    final String subscriptionTopic = "score";
    final String publishTopic = "start";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.getSerializable(PLAYER_INFORMATION) != null){
            final Player player = (Player) extras.getSerializable(PLAYER_INFORMATION);
            String serverUri =
                    this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE).getString(getString(R.string.mqttUri),"");;
            final MqttService mqttService = new MqttService(clientId, getApplicationContext(), serverUri, getParent());
            mqttService.getMqttAndroidClient().setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {

                    if (reconnect) {
                        // Because Clean Session is true, we need to re-subscribe
                        mqttService.subscribeToTopic(subscriptionTopic, null);
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            if (player != null) {
                                jsonObject.put("player", player.toJson());
                            }
                            mqttService.publishMessage(jsonObject.toString(),publishTopic);
                        } catch (JSONException e) {
                            System.err.print("erreur dans la serialisation");
                        }
                    }
                }

                @Override
                public void connectionLost(Throwable cause) {
                    //TODO
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    //TODO add new activity
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });








        }
    }


}
