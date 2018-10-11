package breathe.inventerous.com.breathe;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

////////TASKS IN THIS ACTIVITY
///SIGN IN ACTIVITY


//gets the database reference for idChild
//checks the entered Email and password existing at firebase database
//goes to LocationDetection(in actual interface only) after Email and Password match which is location(function btnSignIn)
//also we can do google sign in and Fb sign in


public class MainActivity extends AppCompatActivity {

    private ImageView imgProfile;
    private EditText edtEmail;
    private EditText edtPassword;
    //private Button btnSignUp;
    private Button btnSignIn;
    private SignInButton btnGoogle;
    private LoginButton btnFb;

    private FirebaseAuth firebaseAuth;

    private static final int RC_SIGN_IN=1;//for google sign in
    private static final int FB_SIGN_IN=2;//for fb sign in
    private GoogleSignInClient mGoogleSignInClient;
//    FirebaseAuth.AuthStateListener authStateListener;
    CallbackManager mCallbackManager;

    private DatabaseReference idChild;
    private String idChildString;

    private static final String REF_To_ID_CHILD="RefToIDChild";
    private FirebaseDatabase firebaseDatabase;

    private String myEmail,myPassword;


//    @Override
//    protected void onStart() {
//        super.onStart();
//        firebaseAuth.addAuthStateListener(authStateListener);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Taking data from previous activity
        Intent fromIDactivity=new Intent();
        idChildString=fromIDactivity.getStringExtra(REF_To_ID_CHILD);
        firebaseDatabase=FirebaseDatabase.getInstance();
        idChild=firebaseDatabase.getReferenceFromUrl(idChildString);


        imgProfile=(ImageView) findViewById(R.id.imgProfile);
        edtEmail=(EditText) findViewById(R.id.edtEmail);
        edtPassword=(EditText) findViewById(R.id.edtPassword);
        btnSignIn=(Button) findViewById(R.id.btnSignIn);
        //btnSignUp=(Button) findViewById(R.id.btnSignUp);


        //TO BE DONE YET
        //create our own custom buttons
        btnGoogle=(SignInButton) findViewById(R.id.btnGoogle);
        btnFb=(LoginButton) findViewById(R.id.btnFb);

        firebaseAuth=FirebaseAuth.getInstance();


        GoogleSignInOptions gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);

//        authStateListener=new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                //if user already signed in then go to actual interface directly
//                //can also use authStateListener
//                if(firebaseAuth.getCurrentUser()!=null){
//                    Intent intentToMain=new Intent(MainActivity.this,Actual_Interface.class);
//                    startActivity(intentToMain);
//                    finish();
//                }
//                else{
//                    Toast.makeText(MainActivity.this, "no user yet", Toast.LENGTH_SHORT).show();
//                }
//            }
//        };



        //signing in with Email and password
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 myEmail= edtEmail.getText().toString();
                 myPassword=edtPassword.getText().toString();

                //checking the firebase if email and password match

                idChild.child("E-MAIL").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            if(snapshot.getValue()==myEmail){
                                //email matches
                                Toast.makeText(MainActivity.this, "email matches", Toast.LENGTH_SHORT).show();
                                //check the password which is child of Email
                                if(snapshot.child("PASSWORD").getValue()==myPassword){
                                    //yes we have verified so go to location
                                    Toast.makeText(MainActivity.this, "Password matches", Toast.LENGTH_SHORT).show();
                                    Intent toLocation=new Intent(MainActivity.this,Actual_Interface.class);

                                    //send the refernce further
                                    toLocation.putExtra(REF_To_ID_CHILD,idChildString);
                                    startActivity(toLocation);
                                    finish();

                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
//                idChild.child("E-MAIL").setValue(myEmail);
//                idChild.child("PASSWORD").setValue(myPassword);

                Toast.makeText(MainActivity.this, "Data sent!", Toast.LENGTH_SHORT).show();
                signInWithEmailAndPassword(myEmail,myPassword);

                //moving to new activity Actual_interface and passing refernce to idChild further
                Intent toMainPage=new Intent(MainActivity.this,Actual_Interface.class);
                toMainPage.putExtra(REF_To_ID_CHILD,idChildString);
                startActivity(toMainPage);
                finish();
            }
        });

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


        //....// using the login button//....//

        //but we can also create custom button and set on click listener to that
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        //LoginButton loginButton = findViewById(R.id.buttonFacebookLogin);

        //sets on click listener automatically
        btnFb.setReadPermissions("email", "public_profile");
        //we can similarly take phone number etc

        btnFb.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("TAG", "facebook:onSuccess:" + loginResult);
                Toast.makeText(MainActivity.this, "facebook:onSuccess:" + loginResult, Toast.LENGTH_SHORT).show();
                //handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("TAG", "facebook:onCancel");
                Toast.makeText(MainActivity.this, "facebook:onCancel", Toast.LENGTH_SHORT).show();
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("TAG", "facebook:onError", error);
                Toast.makeText(MainActivity.this, "facebook:onError", Toast.LENGTH_SHORT).show();
                // ...
            }
        });

        // ...// till here



    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(MainActivity.this, "Google sign in failed", Toast.LENGTH_SHORT).show();
                Log.w("TAG", "Google sign in failed", e);
                // ...
            }
        }
        // ?maybe the request code is not sent for result
        //so we can use only else instead of elseif
        //  ??check
        else if(requestCode==FB_SIGN_IN){
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
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



    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user){
        if(user!=null){
            Intent intentToMain=new Intent(MainActivity.this,Actual_Interface.class);
            startActivity(intentToMain);
            finish();
        }
        else {
            Toast.makeText(MainActivity.this, "Sorry! No user!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    } //main_activity.java

}
