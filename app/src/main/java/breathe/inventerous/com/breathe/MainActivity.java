package breathe.inventerous.com.breathe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView imgProfile;
    EditText edtEmail;
    EditText edtPassword;
    Button btnSignUp;
    Button btnSignIn;
    Button btnGoogle,btnFb,btnTwitter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgProfile=(ImageView) findViewById(R.id.imgProfile);
        edtEmail=(EditText) findViewById(R.id.edtEmail);
        edtPassword=(EditText) findViewById(R.id.edtPassword);
        btnSignIn=(Button) findViewById(R.id.btnSignIn);
        btnSignUp=(Button) findViewById(R.id.btnSignUp);
        btnGoogle=(Button) findViewById(R.id.btnGoogle);
        btnFb=(Button) findViewById(R.id.btnFb);
        btnTwitter=(Button) findViewById(R.id.btnTwitter);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(MainActivity.this,SignUp.class);
                startActivity(intent1);
                finish();
            }
        });
    }
}
