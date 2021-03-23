package capstone.project.budgetroomba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private TextView register, forgotPassword;
    private EditText editTextEmail, editTextPassword;
    private Button signIn;
    private ImageButton googleSignin;

    GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 0;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        register = findViewById(R.id.signuptab);
        register.setOnClickListener(this);

        signIn = (Button)findViewById(R.id.loginbtn);
        signIn.setOnClickListener(this);

        googleSignin = (ImageButton)findViewById(R.id.googleloginbtn);
        googleSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.googleloginbtn:
                        signIn();
                        break;
                    // ...
                }
            }
        });


        editTextEmail = (EditText)findViewById(R.id.loginEmail);
        editTextPassword = (EditText)findViewById(R.id.loginPassword);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        forgotPassword = findViewById(R.id.forgotpasswordbtn);
        forgotPassword.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Intent intent = new Intent(Login.this, LobbyScreen.class);
            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        Intent intent = new Intent(Login.this,LobbyScreen.class);
//        startActivity(intent);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signuptab:
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
                break;

            case R.id.loginbtn:
                userLogin();
                break;

            case R.id.forgotpasswordbtn:
                startActivity(new Intent(this,ForgotPassword.class));
                break;
        }
    }

    private void userLogin()
    {
        String email= editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty())
        {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editTextEmail.setError("Email entered is not valid");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty())
        {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length()<6)
        {
            editTextPassword.setError("Must have a minimum of 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified())
                    {
                        //redirect to user profile
                        //Create the main activity and redirect to it from this block of code
                        Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(Login.this, LobbyScreen.class);
                        startActivity(intent);
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(Login.this, "Verify your account by going to your email", Toast.LENGTH_LONG).show();
                    }

                }else{
                    editTextPassword.setError("Email maybe incorrect");
                    editTextEmail.setError("Password maybe incorrect");
                    Toast.makeText(Login.this, "Login Unsucessful", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}