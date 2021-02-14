package capstone.project.budgetroomba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

public class LobbyScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private ImageButton additionBtn;
    private static final String TAG = "MyActivity";
    DatabaseReference reference, runState;
    Button on,off;
    TextView result;
    String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_screen);

        Toolbar toolbar = findViewById(R.id.toolbar);
        on = (Button)findViewById(R.id.startBtn);
        result = (TextView)findViewById(R.id.resultTv);
        reference = FirebaseDatabase.getInstance().getReference();
        runState = FirebaseDatabase.getInstance().getReference();

        runState.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                on.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("HC-SR04/0-STATE/RUN_STATE");
                        myRef.setValue(1);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.nav_Language:
                //Create an activity where they can choose between french and english
                Toast.makeText(this, "Still in testing", Toast.LENGTH_SHORT).show();
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


    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
}