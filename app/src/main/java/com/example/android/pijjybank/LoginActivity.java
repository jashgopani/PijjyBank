package com.example.android.pijjybank;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText etUserName, etPassword;
    private Button loginBtn;
    private TextView gotoRegister;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //initialize all the elements and store them in variables
        initialize();

        //get instance of firebase object
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        //check whether user is already logged in or not
        if (user != null) {
            finish();
            startActivity(new Intent(LoginActivity.this, PayrollActivity.class));
        }

        //set onClickListener for login button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUserName.getText().toString();
                String password = etPassword.getText().toString();

                if (!username.isEmpty() || !password.isEmpty())//if username and password are not blank
                    validate(username, password);
                else {//display alert message
                    AlertDialog.Builder errorMsg = new AlertDialog.Builder(LoginActivity.this);
                    errorMsg.setMessage("Credentials cannot be blank").setCancelable(false)
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                    AlertDialog alert = errorMsg.create();
                    alert.setTitle("Error");
                    alert.show();
                }
                //verification
            }
        });

        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
    }

    private void initialize() {
        etUserName = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        gotoRegister = (TextView) findViewById(R.id.tvToRegister);

    }

    private void validate(String username, String password) {
        progressDialog.setMessage("Verifying Details");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, PayrollActivity.class));
                } else {
                    progressDialog.dismiss();
                    Log.w("login", "signInWithCustomToken:failure", task.getException());
                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void onBackPressed() {
        finish();
        moveTaskToBack(true);
    }
}
