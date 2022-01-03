package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

        private EditText userText, ageText, phoneText, genderText, handText;
        //private RadioGroup genderRadio, handRadio;
        private Button logInButton;

        DatabaseReference databaseReference;

        private SharedPreferences prefs;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            databaseReference = FirebaseDatabase.getInstance().getReference("users");

            userText = findViewById(R.id.user_text);
            ageText = findViewById(R.id.age_text);
            phoneText = findViewById(R.id.phone_text);
            genderText = findViewById(R.id.gender_text);
            //genderRadio.getSelection().getActionCommand();
            handText = findViewById(R.id.hand_text);
            //handRadio.getSelection().getActionCommand();
            logInButton = findViewById(R.id.login_button);

            //Intent intent = new Intent(this, MainActivity.class);

            logInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //FirebaseDatabase.getInstance().getReference().child("Bok").child("book").setValue("bokkk");

                    addUsers();

                }
            });

        }

        public void addUsers() {

            String userName = userText.getText().toString();
            String userAge = ageText.getText().toString();
            String userPhone = phoneText.getText().toString();
            String userGender = genderText.getText().toString();
            String userHand = handText.getText().toString();

            if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userAge) &&
                    !TextUtils.isEmpty(userPhone) && !TextUtils.isEmpty(userGender) && !TextUtils.isEmpty(userHand)) {

                String id = databaseReference.push().getKey();

                Users users = new Users(id, userName, userAge, userPhone, userGender, userHand);

                databaseReference.child(id).setValue(users);
                userText.setText("");
                ageText.setText("");
                phoneText.setText("");
                genderText.setText("");
                handText.setText("");

            } else {
                Toast.makeText(this, "Upišite sve tražene podatke!", Toast.LENGTH_LONG).show();
            }
        }

    }