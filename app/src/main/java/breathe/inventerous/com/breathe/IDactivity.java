package breathe.inventerous.com.breathe;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    private LocationManager locationManager;
    private LocationListener listener;
    private double longX;
    private double latY;
    private static final int REQ_CODE=10;

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


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myId=edtID.getText().toString();

                locationManager=(LocationManager) getSystemService(LOCATION_SERVICE);

                listener= new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        longX=location.getLongitude();
                        latY=location.getLatitude();
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        Intent toPermission=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(toPermission);
                    }

                };

                configure_button();


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

                            // DatabaseReference theParent=myRef.child(edtBoxerId.getText().toString());
                            DatabaseReference idChild=databaseReference.child(myId);
                            //we will pass on these reference for myId child to sign up
                            //after creating the other fields
                            //later we will add values to these fields
                            //idChild.child("Location").setValue("No location yet");
//                            idChild.child("Email");
//                            idChild.child("Password");
//                            idChild.child("Readings");

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

            }
        });

//        btnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                DatabaseReference idChild=databaseReference.child(myId);
//                //we will pass on these reference for further use to new intent
//                DatabaseReference locationChild=idChild.child("Location");
//                DatabaseReference emailChild=idChild.child("Email");
//                DatabaseReference passwordChild=idChild.child("Password");
//                DatabaseReference readingsChild=idChild.child("Readings");
//
//            }
//        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){

            case REQ_CODE:

                configure_button();

                break;

            default:

                break;

        }
    }

    private void configure_button(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}

                        ,REQ_CODE);

            }

            return;
        }
        else{
            locationManager.requestLocationUpdates("gps",5000,0,listener);
            //idChild.child("Location").setValue("No location yet");

            Toast.makeText(IDactivity.this, "X"+ longX, Toast.LENGTH_SHORT).show();
            Toast.makeText(IDactivity.this, "Y"+ latY, Toast.LENGTH_SHORT).show();
//            databaseReference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                   dataSnapshot.getChildren().;
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
        }
    }
}
