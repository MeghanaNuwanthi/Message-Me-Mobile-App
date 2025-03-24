package edu.cinec.sendsms;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class NewActivity extends AppCompatActivity {

    private EditText MobileNumber, Message;
    private Button sendButton, clearButton, logoutButton;
    private static final int REQUEST_SMS_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        MobileNumber = findViewById(R.id.MobileNumField);
        Message = findViewById(R.id.MessageField);
        sendButton = findViewById(R.id.sendButton);
        clearButton = findViewById(R.id.clearbtn);
        logoutButton = findViewById(R.id.logoutbtn);

        // Send Button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNumber = MobileNumber.getText().toString();
                String message = Message.getText().toString();

                if (mobileNumber.isEmpty() || message.isEmpty()) {
                    Toast.makeText(NewActivity.this, "Please enter both mobile number and message", Toast.LENGTH_SHORT).show();
                } else {
                    if (ActivityCompat.checkSelfPermission(NewActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(NewActivity.this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS_PERMISSION);
                    } else {
                        sendSMS(mobileNumber, message);
                    }
                }
            }
        });

        // Clear Button
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobileNumber.setText("");
                Message.setText("");
                Toast.makeText(NewActivity.this, "Fields cleared", Toast.LENGTH_SHORT).show();
            }
        });

        // Logout Button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logout and return to the login activity
                Intent intent = new Intent(NewActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // send SMS
    private void sendSMS(String mobileNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(mobileNumber, null, message, null, null);
            Toast.makeText(NewActivity.this, "Message sent to " + mobileNumber, Toast.LENGTH_SHORT).show();

            // Clear the fields after sending the SMS
            MobileNumber.setText("");
            Message.setText("");
        } catch (Exception e) {
            Toast.makeText(NewActivity.this, "SMS failed to send, please try again.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    // Handle permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_SMS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String mobileNumber = MobileNumber.getText().toString();
                String message = Message.getText().toString();
                sendSMS(mobileNumber, message);
            } else {
                Toast.makeText(NewActivity.this, "Permission denied to send SMS", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
