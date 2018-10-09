package breathe.inventerous.com.breathe;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUp extends AppCompatActivity {
    private EditText edtName,edtEmail,edtPhNo,edtPwd,edtRePwd;
    private Button btnsignup2;
    private FirebaseAuth firebaseAuth;

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

        firebaseAuth=FirebaseAuth.getInstance();


        btnsignup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignUp.this,Actual_Interface.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void signUpUserEmailAndPassword(String UserEmail,String UserPassword){
        firebaseAuth.createUserWithEmailAndPassword(UserEmail, UserPassword)
                .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(SignUp.this, "There was a problem, Try Again!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(SignUp.this, "Successfully Signed Up!", Toast.LENGTH_SHORT).show();
                            //from here we have to go to actual Interface
                        }
                    }
                });
    }

}

   


