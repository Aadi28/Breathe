package breathe.inventerous.com.breathe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class AccDetails extends AppCompatActivity {
    private Button btnShop, btnForecast, btnSearchLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_details);

        btnForecast=(Button) findViewById(R.id.btnForecast);
        btnSearchLoc=(Button) findViewById(R.id.btnSearchLoc);
        btnShop=(Button) findViewById(R.id.btnShop);
    }
}
