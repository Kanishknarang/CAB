package kanishknarang.tk.cab;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverLoginRegisterActivity extends AppCompatActivity {

    private Button driverLoginButton;
    private Button driverRegisterButton;
    private TextView driverRegisterLink;
    private TextView driverStatus;

    private EditText emailDriver;
    private EditText passwordDriver;

    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private DatabaseReference driverDatabaseRef;
    private String onlineDriverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login_register);

        mAuth = FirebaseAuth.getInstance();



        driverLoginButton = (Button) findViewById(R.id.driver_login_btn);
        driverRegisterButton =(Button) findViewById(R.id.driver_register_btn);
        driverRegisterLink = (TextView) findViewById(R.id.driver_register_link);
        driverStatus = (TextView) findViewById(R.id.driver_status);
        loadingBar = new ProgressDialog(this);

        emailDriver = (EditText) findViewById(R.id.email_driver);
        passwordDriver = (EditText) findViewById(R.id.password_driver);

        driverRegisterButton.setVisibility(View.INVISIBLE);
        driverRegisterButton.setEnabled(false);

        driverRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driverLoginButton.setVisibility(View.INVISIBLE);
                driverRegisterLink.setVisibility(View.INVISIBLE);

                driverStatus.setText("driver register");
                driverRegisterButton.setVisibility(View.VISIBLE);
                driverRegisterButton.setEnabled(true);
            }
        });

        driverRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailDriver.getText().toString();
                String password = passwordDriver.getText().toString();
                
                registerDriver(email,password);
            }
        });

        driverLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailDriver.getText().toString();
                String password = passwordDriver.getText().toString();

                signInDriver(email,password);
            }
        });
    }

    private void signInDriver(String email, String password) {
        if(TextUtils.isEmpty(email)){
            Toast.makeText(DriverLoginRegisterActivity.this,"Please enter email...",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(DriverLoginRegisterActivity.this,"Please enter password...",Toast.LENGTH_SHORT).show();
        }
        else{

            loadingBar.setTitle("Driver login");
            loadingBar.setMessage("Please wait");
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                        Intent driverIntent = new Intent(DriverLoginRegisterActivity.this,DriversMapActivity.class);
                        startActivity(driverIntent);

                        Toast.makeText(DriverLoginRegisterActivity.this, "Driver Login Successfully...",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                    }else{
                        Toast.makeText(DriverLoginRegisterActivity.this, "Driver Login unsuccessfull.Please try again",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }

    }

    private void registerDriver(String email, String password) {

        if(TextUtils.isEmpty(email)){
            Toast.makeText(DriverLoginRegisterActivity.this,"Please enter email...",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(DriverLoginRegisterActivity.this,"Please enter password...",Toast.LENGTH_SHORT).show();
        }
        else{

            loadingBar.setTitle("Driver Registration");
            loadingBar.setMessage("Please wait");
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        onlineDriverId = mAuth.getCurrentUser().getUid();
                        driverDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(onlineDriverId);

                        driverDatabaseRef.setValue(true);
                        Intent driverIntent = new Intent(DriverLoginRegisterActivity.this,DriversMapActivity.class);
                        startActivity(driverIntent);

                        Toast.makeText(DriverLoginRegisterActivity.this, "Driver Registered Successfully...",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();



                    }else{
                        Toast.makeText(DriverLoginRegisterActivity.this, "Driver Registration unsuccessfull.Please try again",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }
}
