package breathe.inventerous.com.breathe;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class IDactivity extends AppCompatActivity {

    private EditText edtID;
    private Button btnSubmit,btnBuyNow,btnHeatmap;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private String myId;
    private final String REF_To_ID_CHILD="RefToIDChild";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idactivity);

        edtID=(EditText) findViewById(R.id.edtID);
        btnSubmit=(Button) findViewById(R.id.btnSubmit);
        btnHeatmap=(Button) findViewById(R.id.btnHeatmap);
        btnBuyNow=(Button) findViewById(R.id.btnBuy);

        //change the rules for writing into database
        //also check if cloud database should be used

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReferenceFromUrl("https://breathe-c990e.firebaseio.com/");

        //setting child values
        //in database->id child
        //             ->location child
        //             ->email child
        //             ->password child
        //             ->readings child according to date may be
        //                   ->time to time readings

        myId=edtID.getText().toString();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(myId)){
                    //id is already present so go to sign in page
                    Intent toSignIn=new Intent(IDactivity.this,MainActivity.class);
                    toSignIn.putExtra(REF_To_ID_CHILD,dataSnapshot.child(myId).toString());
                    startActivity(toSignIn);
                    finish();

                    //now we have the reference to myID child in sign in from there we can see already saved location
                }

                else{
                    //we dont have child an d it has to be created
                    DatabaseReference idChild=databaseReference.child(myId);
                    //we will pass on these reference for myId child to sign up
                    //after creating the other fields
                    //later we will add values to these fields
                    idChild.child("Location");
                    idChild.child("Email");
                    idChild.child("Password");
                    idChild.child("Readings");

                    Intent toSignUp=new Intent(IDactivity.this,SignUp.class);
                    toSignUp.putExtra(REF_To_ID_CHILD,idChild.toString());
                    startActivity(toSignUp);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference idChild=databaseReference.child(myId);
                //we will pass on these reference for further use to new intent
                DatabaseReference locationChild=idChild.child("Location");
                DatabaseReference emailChild=idChild.child("Email");
                DatabaseReference passwordChild=idChild.child("Password");
                DatabaseReference readingsChild=idChild.child("Readings");

            }
        });
    }
}
