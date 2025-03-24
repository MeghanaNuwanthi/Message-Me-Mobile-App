package edu.cinec.sendsms;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword, editTextEmail, editTextSignUpPassword;
    private Button loginButton, cancelButton, buttonSignUp;
    private TextView signUpLink, loginLink;
    private View loginLayout, signUpLayout;
    private ListView userListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> userList;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        // Login Layout Components
        loginLayout = findViewById(R.id.loginLayout);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.loginbtn);
        cancelButton = findViewById(R.id.cancelbtn);
        signUpLink = findViewById(R.id.textViewSignUp);

        // SignUp Layout Components
        signUpLayout = findViewById(R.id.signUpLayout);
        editTextEmail = findViewById(R.id.editTextNewUsername);
        editTextSignUpPassword = findViewById(R.id.editTextNewPassword);
        buttonSignUp = findViewById(R.id.signupButton);
        loginLink = findViewById(R.id.textViewBackToLogin);

        // ListView Initialization
        userListView = findViewById(R.id.userListView);

        if (userListView == null) {
            Log.e("DEBUG", "ListView is NULL! Check your XML layout file.");
            return; // Prevent app from crashing
        }

        // Set up ListView
        userList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        userListView.setAdapter(adapter);
        loadUsers(); // Load users into the ListView

        // Initially show only login layout
        signUpLayout.setVisibility(View.GONE);

        // Login Button
        loginButton.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            } else if (dbHelper.checkUser(username, password)) {
                Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, NewActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        });

        // Cancel Button - Clear fields
        cancelButton.setOnClickListener(v -> {
            editTextUsername.setText("");
            editTextPassword.setText("");
            Toast.makeText(MainActivity.this, "Fields cleared", Toast.LENGTH_SHORT).show();
        });

        // Switch to SignUp Layout
        signUpLink.setOnClickListener(v -> {
            loginLayout.setVisibility(View.GONE);
            signUpLayout.setVisibility(View.VISIBLE);
        });

        // SignUp Button
        buttonSignUp.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextSignUpPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            } else if (dbHelper.insertUser(email, password)) {
                Toast.makeText(MainActivity.this, "User Registered Successfully!", Toast.LENGTH_SHORT).show();
                loadUsers();
                signUpLayout.setVisibility(View.GONE);
                loginLayout.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(MainActivity.this, "Username already exists!", Toast.LENGTH_SHORT).show();
            }
        });

        // Switch to Login Layout
        loginLink.setOnClickListener(v -> {
            signUpLayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
        });
    }

    // Load users into ListView
    private void loadUsers() {
        userList.clear();
        Cursor cursor = dbHelper.getAllUsers();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    userList.add(cursor.getString(1)); // Username column
                } while (cursor.moveToNext());
            }
            cursor.close();
        } else {
            Log.e("DEBUG", "Cursor is null! Check your database.");
        }
        adapter.notifyDataSetChanged();
    }
}
