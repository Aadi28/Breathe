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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

////////TASKS FOR THIS ACTIVITY
//asks details and send to firebase at idChild
//NAME for name
//E-MAIL for email
//PASSWORD for password
//PHONE for phone
//after sign up move to LocationDetection(in actual interface)


public class SignUp extends AppCompatActivity {
    private EditText edtName,edtEmail,edtPhNo,edtPwd,edtRePwd;
    private Button btnsignup2;
    private FirebaseAuth firebaseAuth;

    private DatabaseReference idChild;
    private String idChildString;

    private static final String REF_To_ID_CHILD="RefToIDChild";
    private FirebaseDatabase firebaseDatabase;

    private String myEmail,myPassword,myPhone,myRePassword,myName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Intent fromIDactivity=new Intent();
        idChildString=fromIDactivity.getStringExtra(REF_To_ID_CHILD);
        firebaseDatabase=FirebaseDatabase.getInstance();
        idChild=firebaseDatabase.getReferenceFromUrl(idChildString);


        edtName = (EditText) findViewById(R.id.edtName);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPhNo = (EditText) findViewById(R.id.edtPhNo);
        edtPwd = (EditText) findViewById(R.id.edtPwd);
        edtRePwd = (EditText) findViewById(R.id.edtRePwd);
        btnsignup2 = (Button) findViewById(R.id.btnsignup2);

        firebaseAuth=FirebaseAuth.getInstance();

        //takes data from edit text and send to database
        //signup function called here

        btnsignup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myEmail=edtEmail.getText().toString();
                myPassword=edtPwd.getText().toString();
                myName=edtName.getText().toString();
                myPhone=edtPhNo.getText().toString();
                myRePassword=edtRePwd.getText().toString();

                if(myPassword!=myRePassword){
                    Toast.makeText(SignUp.this, "Password entered does not match!Try again.", Toast.LENGTH_SHORT).show();
                }
                else {
                    idChild.child("NAME").setValue(myName);
                    DatabaseReference idEmail=idChild.child("E-MAIL");
                    idEmail.setValue(myEmail);
                    idEmail.child("PASSWORD").setValue(myPassword);
                    idChild.child("PHONE").setValue(myPhone);
                    signUpUserEmailAndPassword(myEmail,myPassword);

                    Intent intent = new Intent(SignUp.this, Actual_Interface.class);
                    startActivity(intent);
                    finish();
                }
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
                            //from here we have to go to location
                        }
                    }
                });
    }

}

   


