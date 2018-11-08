package com.example.android.pijjybank;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity{


    FirebaseAuth firebaseAuth;
    String tempPassword;
    private DrawerLayout mDrawerLayout;
    private TextView appbarTitle;
    String userEmail;
    TextView emailSideBar;
    NavigationView navigationView;
    View headerView;
    Button resetPassword;
    TextView usernameSideBar;
    DatabaseReference UserRef;
    String navHeaderName;
    String id;
    EditText editUsername,editEmail,editBudget;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //setting the toolbar
        Toolbar toolbar = findViewById(R.id.profile_appbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px);

        final TextView usernametv = (TextView)findViewById(R.id.usernametv);
        final TextView emailtv = (TextView)findViewById(R.id.emailtv);
        final TextView budgettv = (TextView)findViewById(R.id.budgettv);

        //Firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        id = firebaseAuth.getCurrentUser().getUid();
        appbarTitle = (TextView) findViewById(R.id.appbarTitle);
        mDrawerLayout = findViewById(R.id.navd);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                onOptionsItemSelected(item);
                item.setChecked(true);// set item as selected to persist highlight
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        //setting up the navigation view
        headerView = navigationView.getHeaderView(0);
        if (user != null) {
            // User is signed in
            userEmail = user.getEmail();
            emailSideBar = (TextView) headerView.findViewById(R.id.userEmail);
            usernameSideBar = (TextView) headerView.findViewById(R.id.SidebarHeader);
        }
        emailSideBar.setText(userEmail);

        //Retriving Current Username and updating user details
        UserRef = FirebaseDatabase.getInstance().getReference("Users");
        ValueEventListener nameListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User current = snapshot.getValue(User.class);
                    String key = snapshot.getKey();
                    navHeaderName = current.getName();
                    if (key.equals(id)) {
                        navHeaderName = current.getName();
                        usernameSideBar.setText(navHeaderName);
                        usernametv.setText(navHeaderName);
                        emailtv.setText(firebaseAuth.getCurrentUser().getEmail());
                        budgettv.setText(Integer.toString(current.getBudget()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        UserRef.addValueEventListener(nameListener);



        //reset password link action
        resetPassword = findViewById(R.id.resetPassword);
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ProfileActivity.this)
                        .setTitle("Confirm")
                        .setMessage("Are you sure you want to reset your password ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                firebaseAuth.sendPasswordResetEmail(firebaseAuth.getCurrentUser().getEmail())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(ProfileActivity.this, "Reset Link sent to "+firebaseAuth.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });


        //Edit profile options
        editUsername = (EditText)findViewById(R.id.editusernametv);
        editUsername.setVisibility(View.GONE);
        editUsername.setText(firebaseAuth.getCurrentUser().getEmail());

        editBudget = (EditText)findViewById(R.id.editbudgettv);
        editBudget.setVisibility(View.GONE);

        editEmail = (EditText)findViewById(R.id.editemailtv);
        editEmail.setVisibility(View.GONE);

        final ImageView editusernameicon = (ImageView)findViewById(R.id.editusernameicon);
        final ImageView editemailicon = (ImageView)findViewById(R.id.editemailicon);
        final ImageView editBudgeticon = (ImageView)findViewById(R.id.editbudgeticon);

        editusernameicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateName(usernametv,editUsername,editusernameicon);
            }
        });

        editemailicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmail(emailtv,editEmail,editemailicon);
            }
        });

        editBudgeticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBudget(budgettv,editBudget,editBudgeticon);
            }
        });


    }

    public void updateName(TextView t1,EditText t2,ImageView icon){
        if(t1.getVisibility() == View.VISIBLE){//enter into editing mode
            t1.setVisibility(View.GONE);
            icon.setImageResource(R.drawable.done_icon);
            t2.setVisibility(View.VISIBLE);//showing edittext
            t2.setText(t1.getText());
        }else{//updating the details
            String newName = t2.getText().toString().trim();
            DatabaseReference temp = FirebaseDatabase.getInstance().getReference("Users/"+id);
            temp.child("name").setValue(newName);
            t1.setVisibility(View.VISIBLE);
            icon.setImageResource(R.drawable.edit_icon);
            t2.setVisibility(View.GONE);
        }
    }

    public void updateEmail(final TextView t1, final EditText t2, final ImageView icon){
        if(t1.getVisibility() == View.VISIBLE){
            t1.setVisibility(View.GONE);
            icon.setImageResource(R.drawable.done_icon);
            t2.setVisibility(View.VISIBLE);
            t2.setHint("New Email Id");
        }else{
            t2.setText(firebaseAuth.getCurrentUser().getEmail());
            final String newEmail = t2.getText().toString().trim();
            final FirebaseUser temp = firebaseAuth.getCurrentUser();
            final String enteredpassword;

            //Input Dialog Box For getting password
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter Password");
            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    tempPassword = input.getText().toString();
                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseAuth.getCurrentUser().getEmail(),tempPassword);
                    firebaseAuth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            firebaseAuth.getCurrentUser().updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        t1.setVisibility(View.VISIBLE);
                                        icon.setImageResource(R.drawable.edit_icon);
                                        t2.setVisibility(View.GONE);
                                        t1.setText(firebaseAuth.getCurrentUser().getEmail());
                                        Toast.makeText(ProfileActivity.this, "Email Updated Sucessfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();

        }
    }

    public void updateBudget(TextView t1,EditText t2,ImageView icon){
        if(t1.getVisibility() == View.VISIBLE){//enter into editing mode
            t1.setVisibility(View.GONE);
            icon.setImageResource(R.drawable.done_icon);
            t2.setVisibility(View.VISIBLE);//showing edittext
            t2.setText(t1.getText());
        }else{//updating the details
            int newbudget = Integer.parseInt(t2.getText().toString().trim());
            DatabaseReference temp = FirebaseDatabase.getInstance().getReference("Users/"+id);
            temp.setValue(new User(navHeaderName,newbudget));
            t1.setVisibility(View.VISIBLE);
            icon.setImageResource(R.drawable.edit_icon);
            t2.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START, true);
                return true;

            case R.id.Logout:
                logout();
                return true;

            case R.id.Timeline:
                startActivity(new Intent(ProfileActivity.this,PayrollActivity.class));
                return true;

            case R.id.Dashboard:
                startActivity(new Intent(ProfileActivity.this,dashboard.class));
                return true;

            case R.id.Profile:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        firebaseAuth.signOut();//firebase method
        finish();
        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        Toast.makeText(this, "Logout Successful ", Toast.LENGTH_SHORT).show();
    }


}
