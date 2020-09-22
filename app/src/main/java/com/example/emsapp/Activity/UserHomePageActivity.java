package com.example.emsapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.emsapp.Adapter.EmployeeAdapter;
import com.example.emsapp.Model.Employee;
import com.example.emsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserHomePageActivity extends AppCompatActivity {

    private CardView profileInfo;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private TextView userOriginalName;
    private String userName;
    private DatabaseReference employeeReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page);

        inItView();
        firebaseAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        getAuthUserData();

    }

    private void getAuthUserData() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        employeeReference = FirebaseDatabase.getInstance().getReference("Employee");


        employeeReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    Employee employeeInfo = userSnapshot.getValue(Employee.class);

                    if(employeeInfo.getUserPgId().equals(userName)){
                        userOriginalName.setText("Hello! "+ employeeInfo.getUserName());

                        GoForDetalis(employeeInfo);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void GoForDetalis(final Employee employeeInfo) {
        profileInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserHomePageActivity.this, UserDetailsActivity.class);
                intent.putExtra("userInfo", employeeInfo);
                startActivity(intent);
            }
        });
    }

    private void inItView() {
        profileInfo = findViewById(R.id.profileInfoLayout);
        userOriginalName = findViewById(R.id.userName);
    }
}