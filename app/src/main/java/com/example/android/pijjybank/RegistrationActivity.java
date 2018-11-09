package com.example.android.pijjybank;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

public class RegistrationActivity extends AppCompatActivity {
    private EditText etUserName, etPassword, etRegConfirmPassword, etRegName;
    private Button registerBtn;
    private TextView gotoLogin;
    private Toolbar toolbar;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference db;
    static boolean calledAlready = false;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regsitration);

        initialize();
        //enabling offline capabilities
        if (!calledAlready) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }


        firebaseAuth = FirebaseAuth.getInstance();

        //set onClickListener for login button
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    progressDialog.dismiss();
                    progressDialog.setMessage("Registering User");
                    progressDialog.show();
                    //upload data to database
                    String user_email = etUserName.getText().toString().trim();
                    String user_password = etPassword.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) { //Add username to the database
                                final String id = firebaseAuth.getCurrentUser().getUid();
                                db = FirebaseDatabase.getInstance().getReference("Users/");
                                db.child(id).setValue(new User(name, 0));
                                Log.v("Jash", "User id :" + id);
                                /*
                                 * {
                                 *   users : {
                                 *       id : {
                                 *           name : etRegName
                                 *       }
                                 *   }
                                 * }
                                 */

                                progressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, "Registration Successful ", Toast.LENGTH_SHORT).show();
                                RegistrationActivity.this.finish();
                                startActivity(new Intent(RegistrationActivity.this, PayrollActivity.class));
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, "Registration FAILED ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                RegistrationActivity.this.finish();
            }
        });
    }

    private void initialize() {
        etUserName = (EditText) findViewById(R.id.etRegUsername);
        etPassword = (EditText) findViewById(R.id.etRegPassword);
        etRegName = (EditText) findViewById(R.id.etRegName);
        etRegConfirmPassword = (EditText) findViewById(R.id.etRegConfirmPassword);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        gotoLogin = (TextView) findViewById(R.id.tvToLogin);
        progressDialog = new ProgressDialog(this);
    }

    private boolean validate() {
        boolean result = false;
        progressDialog.setMessage("Verifying Details");
        progressDialog.show();

        String username = etUserName.getText().toString().trim();//for email
        String password = etPassword.getText().toString().trim();
        String cpassword = etRegConfirmPassword.getText().toString().trim();
        name = etRegName.getText().toString();

        if (username.isEmpty() || password.isEmpty() || cpassword.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Detials cannot be blank ", Toast.LENGTH_SHORT).show();
        } else if (!cpassword.equals(password)) {
            Toast.makeText(this, "Passwords do not match " + cpassword, Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }
        return result;
    }

}
