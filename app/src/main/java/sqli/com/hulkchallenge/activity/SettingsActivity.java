package sqli.com.hulkchallenge.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.PrintWriter;
import java.io.StringWriter;

import sqli.com.hulkchallenge.Constants;
import sqli.com.hulkchallenge.R;
import sqli.com.hulkchallenge.mqtt.MqttService;
import sqli.com.hulkchallenge.mqtt.FailureHandler;

import static sqli.com.hulkchallenge.Constants.CLIENT_ID;

public class SettingsActivity extends AppCompatActivity implements FailureHandler {

    private String publishTopic = "stop";
    private MqttService mqttService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button button = (Button) findViewById(R.id.submitSettings);
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        final EditText mqttUriText = (EditText) findViewById(R.id.mqttUriText);
        String mqttUri = this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE).getString(Constants.URI, getString(R.string.mqttUri));
        mqttUriText.setText(mqttUri);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString(Constants.URI, mqttUriText.getText().toString());
                editor.apply();
                finish();
            }
        });

        button = (Button) findViewById(R.id.testing);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test();
            }
        });
    }

    public void test() {
        TextView errorTextView = (TextView) findViewById(R.id.errorTextView);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText("test en cours");
        final EditText mqttUriText = (EditText) findViewById(R.id.mqttUriText);
        mqttService = new MqttService(CLIENT_ID, getApplicationContext(), mqttUriText.getText().toString(), this);
        mqttService.connectToMqtt(this);
        handleMqttOperation(mqttService);

    }

    private void displayFailure(Throwable exception) {
        TextView errorTextView = (TextView) findViewById(R.id.errorTextView);
        errorTextView.setVisibility(View.VISIBLE);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String sStackTrace = sw.toString();
        errorTextView.setText(exception.getMessage()+"\n"+sStackTrace);
    }

    private void handleMqttOperation(final MqttService mqttService) {
        mqttService.getMqttAndroidClient().setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                mqttService.publishMessage("test", publishTopic);

            }

            @Override
            public void connectionLost(Throwable cause) {
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                displaySuccess();
            }
        });
    }

    private void displaySuccess() {
        TextView errorTextView = (TextView) findViewById(R.id.errorTextView);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(R.string.testMqttOk);
    }

    @Override
    public void failure(Throwable exception) {
        Log.e("ERROR", "erreur mqtt", exception);
        displayFailure(exception);
    }
}
