package breathe.inventerous.com.breathe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class AccDetails extends AppCompatActivity {
    TextView txtName,txtEmailId,txtPhoneNo,txtLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_details);

        txtName=(TextView) findViewById(R.id.txtName);
        txtEmailId=(TextView) findViewById(R.id.txtEmailId);
        txtLocation=(TextView) findViewById(R.id.txtLocation);
        txtPhoneNo=(TextView) findViewById(R.id.txtPhoneNo);

    }
}
