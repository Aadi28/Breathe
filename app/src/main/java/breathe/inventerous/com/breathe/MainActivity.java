package breathe.inventerous.com.breathe;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {

    private ImageView imgProfile;
    private EditText edtEmail;
    private EditText edtPassword;
    private Button btnSignUp;
    private Button btnSignIn;
    private Button btnGoogle,btnFb,btnTwitter;
    private FirebaseAuth firebaseAuth;


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

        firebaseAuth=FirebaseAuth.getInstance();

        //if user already signed in then go to actual interface directly
        if(firebaseAuth.getCurrentUser()!=null){
            Intent intentToMain=new Intent(MainActivity.this,Actual_Interface.class);
            startActivity(intentToMain);
            finish();
        }

        //going to sign up page but will sign up in sign up activity
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(MainActivity.this,SignUp.class);
                startActivity(intent1);
                finish();
            }
        });

        //signing in with Email and password
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myEmail= edtEmail.getText().toString();
                String myPassword=edtPassword.getText().toString();
                signInWithEmailAndPassword(myEmail,myPassword);
            }
        });
    }

    //signing in function
    private void signInWithEmailAndPassword(String userEmail,String userPassword){

        firebaseAuth.signInWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(!task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "There was an error", Toast.LENGTH_SHORT).show();
                }
                else{
                    //after signing in will go to actual interface
                    Intent intentToMain=new Intent(MainActivity.this,Actual_Interface.class);
                    startActivity(intentToMain);
                    finish();
                }
            }
        });
    }

}
