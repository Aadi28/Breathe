package breathe.inventerous.com.breathe;

import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/////////////TASKS IN THIS ACTIVITY

//asks internet connection
//asks id input
//if id matches in firebase then go to sign up
//else create new id field in existing database
//databasereference to IdChild is passed on further so that we have id in entire app


public class IDactivity extends AppCompatActivity {

    private EditText edtID;
    private Button btnSubmit,btnBuyNow,btnHeatmap;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;

    private String myId;
    private final String REF_To_ID_CHILD="RefToIDChild";

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idactivity);

        edtID=(EditText) findViewById(R.id.edtID);
        btnSubmit=(Button) findViewById(R.id.btnSubmit);
        btnHeatmap=(Button) findViewById(R.id.btnHeatmap);
        btnBuyNow=(Button) findViewById(R.id.btnBuy);

        //change the rules for writing into database
        //also check if could database should be used

        Intent askInternet=new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
        startActivity(askInternet);


        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReferenceFromUrl("https://breathe-c990e.firebaseio.com/");


        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //if user already signed in then go to actual interface directly
                //can also use authStateListener
                if(firebaseAuth.getCurrentUser()!=null){
                    //no need to sign in and location again
                    //so directly go to actual interface
                    Intent intentToMain=new Intent(IDactivity.this,Actual_Interface.class);
                    intentToMain.putExtra(REF_To_ID_CHILD,databaseReference.child(myId).toString());
                    startActivity(intentToMain);
                    finish();
                }
                else{
                    Toast.makeText(IDactivity.this, "no user yet", Toast.LENGTH_SHORT).show();
                }
            }
        };




        //setting child values
        //in database->id child
        //             ->location child
        //             ->email child
        //                  ->password child
        //             ->readings child according to date may be
        //                   ->time to time readings


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myId=edtID.getText().toString();

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(myId)){
                            //id is already present so go to sign in page
                            Intent toSignIn=new Intent(IDactivity.this,MainActivity.class);
                            //refrence passed to sign in which is mainActivity
                            toSignIn.putExtra(REF_To_ID_CHILD,dataSnapshot.child(myId).toString());
                            startActivity(toSignIn);
                            finish();

                            //now we have the reference to myID child in sign in from there we can see already saved location
                        }

                        else{
                            //we dont have child an d it has to be created

                            // DatabaseReference theParent=myRef.child(edtBoxerId.getText().toString());
                            DatabaseReference idChild=databaseReference.child(myId);
                            //we will pass on these reference for myId child to sign up
                            //after creating the other fields
                            //later we will add values to these fields
                            //idChild.child("Location").setValue("No location yet");

                            Intent toSignUp=new Intent(IDactivity.this,SignUp.class);
                            //sending reference to sign up which is signUp
                            toSignUp.putExtra(REF_To_ID_CHILD,idChild.toString());
                            startActivity(toSignUp);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        btnHeatmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to website
                //check
            }
        });

        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to activity for shop now
                Intent toShopNow=new Intent(IDactivity.this,ShopNow.class);
                startActivity(toShopNow);
            }
        });
    }
}
