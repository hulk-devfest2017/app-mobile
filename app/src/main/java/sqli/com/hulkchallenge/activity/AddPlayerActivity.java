package sqli.com.hulkchallenge.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import sqli.com.hulkchallenge.R;
import sqli.com.hulkchallenge.model.Player;

public class AddPlayerActivity extends AppCompatActivity {

    public static String PLAYER_INFORMATION = "PLAYER_INFORMATION";
    private Player playerInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

        EditText firstName = (EditText) findViewById(R.id.firstName);
        EditText lastName = (EditText) findViewById(R.id.lastName);
        EditText email = (EditText) findViewById(R.id.email);
        EditText company = (EditText) findViewById(R.id.company);

        if(getIntent().getExtras() != null){
            playerInformation = (Player) getIntent().getExtras().getSerializable(PLAYER_INFORMATION);

            if(playerInformation != null){
                firstName.setText(playerInformation.getFirstName());
                lastName.setText(playerInformation.getLastName());
                email.setText(playerInformation.getEmail());
                company.setText(playerInformation.getCompany());
            }

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
        intent.putExtra(GameActivity.PLAYER_INFORMATION,playerInformation);
        launchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }
}
