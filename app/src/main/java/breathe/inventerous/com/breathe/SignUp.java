package breathe.inventerous.com.breathe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SignUp extends AppCompatActivity {
    private EditText edtName,edtEmail,edtPhNo,edtPwd,edtRePwd;
    private Button btnsignup2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtName = (EditText) findViewById(R.id.edtName);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPhNo = (EditText) findViewById(R.id.edtPhNo);
        edtPwd = (EditText) findViewById(R.id.edtPwd);
        edtRePwd = (EditText) findViewById(R.id.edtRePwd);
        btnsignup2 = (Button) findViewById(R.id.btnsignup2);
   
    }   
    
    }

