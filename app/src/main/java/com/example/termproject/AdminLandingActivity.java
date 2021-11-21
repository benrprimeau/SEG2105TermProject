package com.example.termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AdminLandingActivity extends AppCompatActivity {

    DatabaseReference databaseAccounts;
    DatabaseReference databaseClassTypes;
    Account userAccount;

    TextView textViewWelcome;
    TextView textViewAccountType;

    TextView textViewClassTypeName;
    TextView textViewClassTypeDescription;

    Button buttonAddClassType;

    ListView listViewClassTypes;
    List<ClassType> classTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_landing);

        textViewWelcome = (TextView) findViewById(R.id.textViewWelcome);
        textViewAccountType = (TextView) findViewById(R.id.textViewAccountType);

        textViewClassTypeName = (TextView) findViewById(R.id.textClassTypeName);
        textViewClassTypeDescription = (TextView) findViewById(R.id.textClassTypeDescription);

        buttonAddClassType = (Button) findViewById(R.id.buttonAddClassType);

        listViewClassTypes = (ListView) findViewById(R.id.listViewClassTypes);

        databaseAccounts = FirebaseDatabase.getInstance().getReference("accounts");

        classTypes = new ArrayList<>();
        databaseClassTypes = FirebaseDatabase.getInstance().getReference("classTypes");

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String accountID = extras.getString("account");
            
            /* because i seem incapable of passing an
             * account object between activites, i get
             * to query the database all over again!
             * yay me! */
            databaseAccounts.orderByKey().equalTo(accountID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                        /* the account variable overwrites every time,
                         * but that shouldn't matter since only one result
                         * should ever by in getChildren(). This is more just to
                         * remove the list container that the datasnapshot is in. */
                        userAccount = childSnapshot.getValue(Account.class);
                        textViewWelcome.setText(String.valueOf("Welcome " + userAccount.getName()));
                        textViewAccountType.setText(String.valueOf(("Your account is of type: " + userAccount.getAccountType())));
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

        buttonAddClassType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addClassType();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseClassTypes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classTypes.clear();
                for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ClassType classType = postSnapshot.getValue(ClassType.class);
                    classTypes.add(classType);
                }

                ClassTypeList classTypesAdapter = new ClassTypeList(AdminLandingActivity.this, classTypes);
                listViewClassTypes.setAdapter(classTypesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                /* bro imagine actually throwing an exception
                 * instead of just using print statements */
                System.out.println("oopsie that didn't work");
            }
        });
    }

    private void addClassType() {
        String name = textViewClassTypeName.getText().toString().trim();
        String description = textViewClassTypeDescription.getText().toString().trim();

        String id = databaseClassTypes.push().getKey();

        ClassType classType = new ClassType(id, name, description);
        databaseClassTypes.child(id).setValue(classType);

        Toast.makeText(this, "Class type added", Toast.LENGTH_LONG).show();
    }
}