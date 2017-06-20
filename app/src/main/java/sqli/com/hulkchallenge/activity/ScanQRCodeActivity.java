package sqli.com.hulkchallenge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.Result;

import it.auron.library.mecard.MeCard;
import it.auron.library.mecard.MeCardParser;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import sqli.com.hulkchallenge.R;
import sqli.com.hulkchallenge.factory.PlayerFactory;

public class ScanQRCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(R.layout.activity_scan_qrcode);
        LinearLayout view = (LinearLayout)findViewById(R.id.camera);
        view.addView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        MeCard meCard = null;
        try {
            String meCardString = rawResult.getText();
             meCard = MeCardParser.parse(meCardString);
        } catch (Exception e) {
            Toast.makeText(this, "Erreur lors de la récupération du qrcode", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(this, AddPlayerActivity.class);
        intent.putExtra(AddPlayerActivity.PLAYER_INFORMATION, PlayerFactory.getPlayer(meCard));
        startActivity(intent);
    }
}
