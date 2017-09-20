package sqli.com.hulkchallenge.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import sqli.com.hulkchallenge.Constants;
import sqli.com.hulkchallenge.R;
import sqli.com.hulkchallenge.model.Player;
import sqli.com.hulkchallenge.model.State;
import sqli.com.hulkchallenge.mqtt.MqttService;

public class GameActivity extends AppCompatActivity {

    public static final String PLAYER_INFORMATION = "PLAYER_INFORMATION";
    private static final String IS_MESSAGE_SENT = "messageSent";

    String clientId = "ExampleAndroidClient";
    final String subscriptionTopic = "score";
    final String publishTopic = "start";
    boolean isMessageSent;
    private MqttService mqttService;
    private Player player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setActiveView(State.SENDING);
        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.getSerializable(PLAYER_INFORMATION) != null){
            player = (Player) extras.getSerializable(PLAYER_INFORMATION);
            String serverUri =
                    this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE).getString(Constants.URI,getString(R.string.mqttUri));
            mqttService = new MqttService(clientId, getApplicationContext(), serverUri, this);
        }
    }

    private void handleMqttOperation(final Player player, final MqttService mqttService, MqttAndroidClient mqttAndroidClient) {
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                if (reconnect) {
                    // Because Clean Session is true, we need to re-subscribe
                    mqttService.subscribeToTopic(subscriptionTopic, null);
                } else {
                    if (!isMessageSent) {
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
            }

            @Override
            public void connectionLost(Throwable cause) {
                //TODO
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                if(subscriptionTopic.equals(topic)){
                    setActiveView(State.SUCCESS);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                isMessageSent = true;
                setActiveView(State.WAITING_RESULT);
            }
        });
    }

    @NonNull
    private MqttAndroidClient connectToMqtt(MqttService mqttService) {
        final MqttAndroidClient mqttAndroidClient = mqttService.getMqttAndroidClient();
        try {
            mqttAndroidClient.connect(mqttService.getMqttConnectOptions(), null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = getDisconnectedBufferOptions();
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    displayFailure(exception);
                }
            });
        } catch (MqttException exception) {
            displayFailure(exception);
        }
        return mqttAndroidClient;
    }

    public void setActiveView(State state){
        View successView = findViewById(R.id.success);
        View errorView = findViewById(R.id.error);
        View waitingView = findViewById(R.id.waiting_result);
        View sendingView = findViewById(R.id.sending);
        successView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.INVISIBLE);
        waitingView.setVisibility(View.INVISIBLE);
        sendingView.setVisibility(View.INVISIBLE);
        switch (state) {
            case ERROR:
                errorView.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                successView.setVisibility(View.VISIBLE);
                break;
            case WAITING_RESULT:
                waitingView.setVisibility(View.VISIBLE);
                break;
            case SENDING:
                sendingView.setVisibility(View.VISIBLE);
                break;
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(IS_MESSAGE_SENT, isMessageSent);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            isMessageSent = savedInstanceState.getBoolean(IS_MESSAGE_SENT);
        }
    }

    @NonNull
    private DisconnectedBufferOptions getDisconnectedBufferOptions() {
        DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
        disconnectedBufferOptions.setBufferEnabled(true);
        disconnectedBufferOptions.setBufferSize(100);
        disconnectedBufferOptions.setPersistBuffer(false);
        disconnectedBufferOptions.setDeleteOldestMessages(false);
        return disconnectedBufferOptions;
    }

    private void displayFailure(Throwable exception){
        setActiveView(State.ERROR);//exception.printStackTrace();
        TextView txtError = (TextView) findViewById(R.id.txt_error);
        txtError.setText(exception.getMessage());
        Toast.makeText(this, "erreur lors de la connexion",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        try {
            MqttAndroidClient mqttAndroidClient = mqttService.getMqttAndroidClient();
            if (mqttAndroidClient.isConnected()) {
                mqttAndroidClient.disconnect();
            }
        } catch (MqttException e) {
            Log.e("monTag","erreur lors de la déconnexion",e);
        }
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        final MqttAndroidClient mqttAndroidClient = connectToMqtt(mqttService);
        handleMqttOperation(player, mqttService, mqttAndroidClient);
    }

    @Override
    protected void onDestroy() {
        mqttService.getMqttAndroidClient().close();
        super.onDestroy();
    }
}
