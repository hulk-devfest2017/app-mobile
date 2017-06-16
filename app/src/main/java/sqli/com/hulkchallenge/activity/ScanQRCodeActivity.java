package sqli.com.hulkchallenge.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.Result;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.io.chain.ChainingTextStringParser;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import sqli.com.hulkchallenge.R;
import sqli.com.hulkchallenge.factory.PlayerFactory;
import sqli.com.hulkchallenge.model.Player;

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
        VCard vCard = null;
        try {
            String qrCodeText = rawResult.getText();
            vCard = Ezvcard.parse(qrCodeText).first();
        } catch (Exception e) {
            Toast.makeText(this, "Erreur lors de la récupération du qrcode", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(this, AddPlayerActivity.class);
        intent.putExtra(AddPlayerActivity.PLAYER_INFORMATION, PlayerFactory.getPlayer(vCard));
        startActivity(intent);
    }
}
