package com.example.termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/* To the poor soul who has to review this code:
 * I am so sorry.
 * Welcome to spaghettiland. Please stay as long
 * as you like. */

public class MainActivity extends AppCompatActivity {

    TextView textName;
    TextView textPassword;
    Button loginButton;
    Button createAccountButton;

    DatabaseReference databaseAccounts;

    public Account userAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textName = (TextView) findViewById(R.id.textName);
        textPassword = (TextView) findViewById(R.id.textPassword);

        loginButton = (Button) findViewById(R.id.loginButton);
        createAccountButton = (Button) findViewById(R.id.createAccountButton);

        databaseAccounts = FirebaseDatabase.getInstance().getReference("accounts");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToCreateAccountActivity();
            }
        });

    }

    private void login() {
        String name = textName.getText().toString().trim();
        String password = textPassword.getText().toString().trim();

        databaseAccounts.orderByChild("name").equalTo(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    /* the account variable overwrites every time,
                     * but that shouldn't matter since only one result
                     * should ever by in getChildren(). This is more just to
                     * remove the list container that the datasnapshot is in. */
                    Account account = childSnapshot.getValue(Account.class);

                    if(account.getPassword().equals(password)) {
                        System.out.println("match!");
                        userAccount = account;

                        if(userAccount.getAccountType().equals("Administrator")) {
                            switchToAdminLanding();
                        }

                        else if (userAccount.getAccountType().equals("Teacher")) {
                            switchToInstructorLanding();
                        }

                        else {
                            switchToRegUsrLanding();
                        }
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                /* bro imagine actually throwing an exception
                 * instead of just using print statements */
                System.out.println("oopsie that didn't work");
            }
        });
    }

    private void switchToCreateAccountActivity() {
        Intent switchActivityIntent = new Intent(MainActivity.this, CreateAccountActivity.class);
        startActivity(switchActivityIntent);
    }

    private void switchToAdminLanding() {
        /* I wanted to pass the account object rather than
         * just the account ID, but android studio actually hated
         * that and i simply don't have the time to figure out why. */
        Intent switchActivityIntent = new Intent(MainActivity.this, AdminLandingActivity.class).putExtra("account",userAccount.get_id());
        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
        startActivity(switchActivityIntent);
    }

    private void switchToInstructorLanding() {
        /* I wanted to pass the account object rather than
         * just the account ID, but android studio actually hated
         * that and i simply don't have the time to figure out why. */
        Intent switchActivityIntent = new Intent(MainActivity.this, InstructorLandingActivity.class).putExtra("account",userAccount.get_id());
        Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show();
        startActivity(switchActivityIntent);
    }

    private void switchToRegUsrLanding() {
        /* I wanted to pass the account object rather than
         * just the account ID, but android studio actually hated
         * that and i simply don't have the time to figure out why. */
        Intent switchActivityIntent = new Intent(MainActivity.this, RegUsrLandingActivity.class).putExtra("account",userAccount.get_id());
        Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show();
        startActivity(switchActivityIntent);
    }

}
