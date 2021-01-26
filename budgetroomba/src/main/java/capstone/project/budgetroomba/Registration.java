package capstone.project.budgetroomba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.FirebaseDatabase;
//
//import org.w3c.dom.Text;

public class Registration extends AppCompatActivity {

    private TextView registerUser;
    private FirebaseAuth mAuth;
    private EditText editTextEmail, editTextPassword, editrepeatPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Toggle between Login & Registration page
        TextView textView = findViewById(R.id.logintab);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration.this, Login.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        registerUser =(Button)findViewById(R.id.registeruserBtn);
        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        editTextEmail = (EditText)findViewById(R.id.Remail);
        editTextPassword = (EditText)findViewById(R.id.Rpassword);
        editrepeatPassword = (EditText)findViewById(R.id.passwordconfirm);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void registerUser()
    {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String repeatPassword = editrepeatPassword.getText().toString().trim();

        if(email.isEmpty())
        {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty())
        {
            editTextPassword.setError("Password is required");
            editTextEmail.requestFocus();
            return;
        }else if(password.length()<6)
        {
            editTextPassword.setError("Password must contain a minimum of 6 characters");
            editTextEmail.requestFocus();
            return;
        }

        if(repeatPassword.isEmpty())
        {
            editrepeatPassword.setError("Must confirm password");
            editTextEmail.requestFocus();
            return;
        }else if(!repeatPassword.equals(password))
        {
            editrepeatPassword.setError("Does not match");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editTextEmail.setError("Please provide valid email");
            editTextEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(email, password, repeatPassword);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Registration.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.VISIBLE);

                                    } else {
                                        Toast.makeText(Registration.this, "Failed To Register", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(Registration.this, "Failed To Register", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
