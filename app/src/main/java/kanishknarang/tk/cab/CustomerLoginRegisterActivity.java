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

public class CustomerLoginRegisterActivity extends AppCompatActivity {

    private Button customerLoginButton;
    private Button customerRegisterButton;
    private TextView customerRegisterLink;
    private TextView customerStatus;

    private EditText emailCustomer;
    private EditText passwordCustomer;

    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private DatabaseReference customerDatabaseRef;
    private String onlineCustomerId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login_register);

        mAuth = FirebaseAuth.getInstance();




        customerLoginButton = (Button) findViewById(R.id.customer_login_btn);
        customerRegisterButton =(Button) findViewById(R.id.customer_register_btn);
        customerRegisterLink = (TextView) findViewById(R.id.register_customer_link);
        customerStatus = (TextView) findViewById(R.id.customer_status);

        loadingBar = new ProgressDialog(this);

        emailCustomer = (EditText) findViewById(R.id.email_customer);
        passwordCustomer = (EditText) findViewById(R.id.password_customer);

        customerRegisterButton.setVisibility(View.INVISIBLE);
        customerRegisterButton.setEnabled(false);

        customerRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerLoginButton.setVisibility(View.INVISIBLE);
                customerRegisterLink.setVisibility(View.INVISIBLE);

                customerStatus.setText("Customer register");
                customerRegisterButton.setVisibility(View.VISIBLE);
                customerRegisterButton.setEnabled(true);
            }
        });

        customerRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailCustomer.getText().toString();
                String password = passwordCustomer.getText().toString();

                registerCustomer(email,password);
            }
        });

        customerLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailCustomer.getText().toString();
                String password = passwordCustomer.getText().toString();

                signInCustomer(email,password);
            }
        });
    }

    private void signInCustomer(String email, String password) {
        if(TextUtils.isEmpty(email)){
            Toast.makeText(CustomerLoginRegisterActivity.this,"Please enter email...",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(CustomerLoginRegisterActivity.this,"Please enter password...",Toast.LENGTH_SHORT).show();
        }
        else{

            loadingBar.setTitle("Customer login");
            loadingBar.setMessage("Please wait");
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){


                        Intent customerIntent = new Intent(CustomerLoginRegisterActivity.this,CustomersMapActivity.class);
                        startActivity(customerIntent);
                        Toast.makeText(CustomerLoginRegisterActivity.this, "Customer logged Successfully...",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }else{
                        Toast.makeText(CustomerLoginRegisterActivity.this, "Customer login unsuccessfull.Please try again",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }




    private void registerCustomer(String email, String password) {
        if(TextUtils.isEmpty(email)){


            Toast.makeText(CustomerLoginRegisterActivity.this,"Please enter email...",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(CustomerLoginRegisterActivity.this,"Please enter password...",Toast.LENGTH_SHORT).show();
        }
        else{

            loadingBar.setTitle("Customer Registration");
            loadingBar.setMessage("Please wait");
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                        onlineCustomerId = mAuth.getCurrentUser().getUid();
                        customerDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(onlineCustomerId);

                        customerDatabaseRef.setValue(true);

                        Intent driverIntent = new Intent(CustomerLoginRegisterActivity.this,CustomersMapActivity.class);
                        startActivity(driverIntent);
                        Toast.makeText(CustomerLoginRegisterActivity.this, "Customer Registered Successfully...",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                        Intent customerIntent = new Intent(CustomerLoginRegisterActivity.this,CustomersMapActivity.class);
                        startActivity(customerIntent);
                    }else{
                        Toast.makeText(CustomerLoginRegisterActivity.this, "Customer Registration unsuccessfull.Please try again",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }
}
