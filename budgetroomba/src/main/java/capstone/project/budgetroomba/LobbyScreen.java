package capstone.project.budgetroomba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.MessageFormat;
import java.util.Locale;

public class LobbyScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private ImageButton additionBtn;
    private static final String TAG = "MyActivity";
    DatabaseReference reference, runState, rootDatabaseReferenceDelete;
    GoogleSignInClient mGoogleSignInClient;
    Button on,off, changeLanguage, signOut;
    TextView result, state;
    String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_screen);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
//            String personGivenName = acct.getGivenName();
//            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            //Glide.with(this).load(String.valueOf(personPhoto).into(imageView);
        }

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle(getResources().getString(R.string.app_title));

        Toolbar toolbar = findViewById(R.id.toolbar);
        on = (Button)findViewById(R.id.startBtn);
        off = (Button)findViewById(R.id.stopBtn);

        rootDatabaseReferenceDelete = FirebaseDatabase.getInstance().getReference().child("HC-SR04");
        result = (TextView)findViewById(R.id.resultTv);
        //state = (TextView)findViewById(R.id.resultIntroTv);
        reference = FirebaseDatabase.getInstance().getReference();
        runState = FirebaseDatabase.getInstance().getReference();

        runState.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                on.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final TextView textViewToChange = (TextView) findViewById(R.id.resultIntroTv);
                        textViewToChange.setText("DEVICE STATE: ON");
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("/HC-SR04/0-STATE/RUN_STATE");
                        myRef.setValue(1);
                    }
                });

                off.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final TextView textViewToChange = (TextView) findViewById(R.id.resultIntroTv);
                        textViewToChange.setText("DEVICE STATE: OFF");
//                        FirebaseDatabase database = FirebaseDatabase.getInstance();
//                        DatabaseReference myRef = database.getReference("/HC-SR04/0-STATE/RUN_STATE");
//                        myRef.setValue(0);

                        rootDatabaseReferenceDelete.child("0-STATE").child("RUN_STATE").setValue(null);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot != null)
                {
                    status = snapshot.child("HC-SR04/1-Real-Time/DISTANCE").getValue().toString();
                    result.setText(status);
                }else{
                    Log.d(TAG,"Data Snapshot is null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG,"Database Error");
            }
        });

        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(LobbyScreen.this, "Signed out successfully",  Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.nav_Language:
                //Create an activity where they can choose between french and english
                showChangeLanugageDialog();
                break;

            case R.id.nav_HelpCenter:
                //Create an activity where they can choose between french and english
                Toast.makeText(this, "Still in testing", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_About:
                //Create an activity where they can choose between french and english
                Toast.makeText(this, "Still in testing", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_ContactUs:
                //Create an activity where they can choose between french and english
                Toast.makeText(this, "Still in testing", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_Password:
                //Finished
                Intent intent = new Intent(this,ForgotPassword.class);
                startActivity(intent);
                break;

            case R.id.nav_Logout:
                //Creating logout button
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Logout Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LobbyScreen.this, Login.class));
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showChangeLanugageDialog() {
        //Array of languages to display in alert dialog
        final String[] listItems = {"English", "French", "Hindi"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(LobbyScreen.this);
        mBuilder.setTitle("Choose Languages");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0)
                {
                    //French
                    setLocale("en");
                    recreate();
                }
                else if(i == 1)
                {
                    //French
                    setLocale("fr");
                    recreate();
                }
                else if(i == 2)
                {
                    //Hinidi
                    setLocale("hi");
                    recreate();
                }

                //Dismiss alert Dialog when language is selected
                dialogInterface.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        //show alert dialog
        mDialog.show();
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        //save data to shared preference
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Languages", language);
        editor.apply();
    }

    //Load language saved in shared prefereneces
    public void loadLocale()
    {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Languages", "");
        setLocale(language);
    }


    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
}