package sqli.com.hulkchallenge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import sqli.com.hulkchallenge.R;
import sqli.com.hulkchallenge.model.Player;

public class AddPlayerActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 12;
    public static String PLAYER_INFORMATION = "PLAYER_INFORMATION";
    private Player playerInformation;
    private RadioButton female;
    private RadioButton male;
    private EditText twitter;
    private EditText company;
    private EditText email;
    private EditText lastName;
    private EditText firstName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        email = (EditText) findViewById(R.id.email);
        company = (EditText) findViewById(R.id.company);
        twitter = (EditText) findViewById(R.id.twitter);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);

        if(getIntent().getExtras() != null){
            playerInformation = (Player) getIntent().getExtras().getSerializable(PLAYER_INFORMATION);
        }

        if (playerInformation == null){
            playerInformation = new Player();
        }
        firstName.setText(playerInformation.getFirstName());
        lastName.setText(playerInformation.getLastName());
        email.setText(playerInformation.getEmail());
        company.setText(playerInformation.getCompany());
        twitter.setText(playerInformation.getTwitter());
        if (playerInformation.isMale()) {
            male.setChecked(true);
        } else {
            female.setChecked(true);
        }

        setupLaunchBtn();
        setupCancelBtn();
    }

    private void setupCancelBtn() {
        Button cancelBtn = (Button) findViewById(R.id.cancelBtn);;
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setupLaunchBtn() {
        Button launchBtn = (Button) findViewById(R.id.launchBtn);
        final Intent intent = new Intent(this, GameActivity.class);
        launchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerInformation.setCompany(company.getText().toString());
                playerInformation.setEmail(email.getText().toString());
                playerInformation.setLastName(lastName.getText().toString());
                playerInformation.setFirstName(firstName.getText().toString());
                playerInformation.setTwitter(twitter.getText().toString());
                playerInformation.setMale(male.isChecked());
                intent.putExtra(GameActivity.PLAYER_INFORMATION,playerInformation);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(REQUEST_CODE == requestCode){
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }
}
