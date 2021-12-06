package com.example.termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddClassActivity extends AppCompatActivity {

    Spinner spinnerClassType;
    Spinner spinnerDifficultyLevel;
    Spinner spinnerDay;
    Spinner spinnerTime;
    TextView textMaximumCapacity;
    DatabaseReference databaseAccounts;
    DatabaseReference databaseClassTypes;
    DatabaseReference databaseClasses;

    List<String> classTypeNames;
    List<ClassType> classTypeList;

    String[] classTypeNameArray;

    String instructorID;
    Account loggedInUser;

    String[] difficultyLevels = {"Beginner","Intermediate","Advanced"};
    String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday","Friday"};
    String[] timeSlots = {"9-10","10-11","11-12","1-2","2-3","3-4","4-5"};

    Button addClassButton;

    ListView listViewGymClasses;
    List<GymClass> gymClasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        databaseAccounts = FirebaseDatabase.getInstance().getReference("accounts");
        databaseClassTypes = FirebaseDatabase.getInstance().getReference("classTypes");
        databaseClasses = FirebaseDatabase.getInstance().getReference("gymClasses");

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            instructorID = extras.getString("instructorID");

            /* because i seem incapable of passing an
             * String accountID = extras.getString("account");
             * account object between activites, i get
             * to query the database all over again!
             * yay me! */

            databaseAccounts.orderByKey().equalTo(instructorID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                        /* the account variable overwrites every time,
                         * but that shouldn't matter since only one result
                         * should ever by in getChildren(). This is more just to
                         * remove the list container that the datasnapshot is in. */
                        loggedInUser = childSnapshot.getValue(Account.class);
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

        spinnerClassType = (Spinner) findViewById(R.id.spinnerClassType);

        spinnerDifficultyLevel = (Spinner) findViewById(R.id.spinnerDifficultyLevel);
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<String>(AddClassActivity.this, android.R.layout.simple_spinner_dropdown_item, difficultyLevels);
        spinnerDifficultyLevel.setAdapter(difficultyAdapter);

        spinnerDay = (Spinner) findViewById(R.id.spinnerDay);
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(AddClassActivity.this, android.R.layout.simple_spinner_dropdown_item, days);
        spinnerDay.setAdapter(dayAdapter);

        spinnerTime = (Spinner) findViewById(R.id.spinnerTime);
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(AddClassActivity.this, android.R.layout.simple_spinner_dropdown_item, timeSlots);
        spinnerTime.setAdapter(timeAdapter);

        textMaximumCapacity = (TextView) findViewById(R.id.textMaximumCapacity);

        classTypeList = new ArrayList<ClassType>();
        classTypeNames = new ArrayList<String>();

        addClassButton = (Button) findViewById(R.id.addClassButton);

        addClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGymClass();
            }
        });

        listViewGymClasses = (ListView) findViewById(R.id.listViewGymClasses);
        gymClasses = new ArrayList<>();



    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseClassTypes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classTypeNames.clear();
                for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ClassType classType = postSnapshot.getValue(ClassType.class);
                    classTypeList.add(classType);
                    classTypeNames.add(String.valueOf(classType.getName()));
                    /* This is super clunky but basically what's happening here is
                     * i can't figure out a way to store the classType id in the spinner,
                     * so i'm keeping a parallel list of the classType objects, so
                     * i can just match the spinner index to the classType object, and
                     * get the id that way. This has the added bonus of meaning that 
                     * i don't have to query the database with the classType name, since
                     * i already have the id. */
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddClassActivity.this, android.R.layout.simple_spinner_dropdown_item, classTypeNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerClassType.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                /* bro imagine actually throwing an exception
                 * instead of just using print statements */
                System.out.println("oopsie that didn't work");
            }
        });

        databaseClasses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gymClasses.clear();
                for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                    GymClass gymClass = postSnapshot.getValue(GymClass.class);
                    gymClasses.add(gymClass);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                /* bro imagine actually throwing an exception
                 * instead of just using print statements */
                System.out.println("oopsie that didn't work");
            }
        });
    }

    private void addGymClass() {
        String id = databaseClasses.push().getKey();
        String instructorName = loggedInUser.getName();
        ClassType classType = classTypeList.get(spinnerClassType.getSelectedItemPosition());
        String difficultyLevel = (String) spinnerDifficultyLevel.getSelectedItem();
        String day = (String) spinnerDay.getSelectedItem();
        String time = (String) spinnerTime.getSelectedItem();
        int maximumCapacity = (int) Integer.parseInt(textMaximumCapacity.getText().toString());

        Boolean conflict = false;
        String errorString = "";

        for(GymClass c : gymClasses) {
            if(c.getClassType().getName().equals(classType.getName())) {
                if (c.getDay().equals(day) && !c.getInstructorName().equals(instructorName)) {
                    conflict = true;
                    errorString = "Sorry, a " + classType.getName() + " class is already scheduled by " + c.getInstructorName() + " on " + day;
                }

                else if (c.getInstructorName().equals(instructorName) && c.getTime().equals(time) && c.getDay().equals((day))) {
                    conflict = true;
                    errorString = "You have already booked a " + classType.getName() + " class on " + day + " from " + time;
                }
            }
        }

        if(conflict) {
            Toast.makeText(this, errorString, Toast.LENGTH_LONG).show();
        }

        else {
            GymClass gymClass = new GymClass(id, instructorName, classType, difficultyLevel, day, time, maximumCapacity);
            databaseClasses.child(id).setValue(gymClass);
            switchToInstructorLanding();
        }
    }

    private void switchToInstructorLanding() {
        /* I wanted to pass the account object rather than
         * just the account ID, but android studio actually hated
         * that and i simply don't have the time to figure out why. */
        Intent switchActivityIntent = new Intent(AddClassActivity.this, InstructorLandingActivity.class).putExtra("account",loggedInUser.get_id());
        Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show();
        startActivity(switchActivityIntent);
    }
}