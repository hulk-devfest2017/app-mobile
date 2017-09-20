package sqli.com.hulkchallenge.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import sqli.com.hulkchallenge.Constants;
import sqli.com.hulkchallenge.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button button = (Button) findViewById(R.id.submitSettings);
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.app_name),Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString(Constants.URI,((EditText)findViewById(R.id.mqttUriText)).getText().toString());
                editor.apply();
                finish();
            }
        });
    }
}
