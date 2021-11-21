package com.example.termproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountActivity extends AppCompatActivity {

    TextView textName;
    TextView textPassword;
    Spinner spinnerAccountType;
    Button submitButton;

    DatabaseReference databaseAccounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        textName = (TextView) findViewById(R.id.textName);
        textPassword = (TextView) findViewById(R.id.textPassword);

        spinnerAccountType = (Spinner) findViewById(R.id.spinnerAccountType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.accountTypeArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAccountType.setAdapter(adapter);

        submitButton = (Button) findViewById(R.id.addClassButton);

        databaseAccounts = FirebaseDatabase.getInstance().getReference("accounts");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAccount();
            }
        });
    }

    private void addAccount() {
        String name = textName.getText().toString().trim();
        String password = textPassword.getText().toString().trim();

        String id = databaseAccounts.push().getKey();

        String accountType = (String) spinnerAccountType.getSelectedItem();

        Account account = new Account(id, name, password, accountType);
        databaseAccounts.child(id).setValue(account);

        Toast.makeText(this, "Account added", Toast.LENGTH_LONG).show();

        switchBackToMainPage();
    }

    private void switchBackToMainPage() {
        Intent switchActivityIntent = new Intent(CreateAccountActivity.this, MainActivity.class);
        startActivity(switchActivityIntent);
    }

}