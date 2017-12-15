package com.eventblock.eventblock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UserAreaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);


        final EditText etUserName = (EditText) findViewById(R.id.etUserName);
        final EditText etAge = (EditText) findViewById(R.id.etAge);

        final EditText etEmail = (EditText) findViewById(R.id.etEmail);

        final TextView welcomeMsg = (TextView) findViewById(R.id.tvWelcomeMsg);
        final Button bNext = (Button) findViewById(R.id.bNext);


        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String username = intent.getStringExtra("username");
        int age = intent.getIntExtra("age", -1);

        String email = intent.getStringExtra("email");

        String message = "Welcome " + name ;
        welcomeMsg.setText(message);
        etUserName.setText(username);
        etAge.setText(age + "");
        etEmail.setText(email);


        bNext.setOnClickListener(new View.OnClickListener() {

            final String username = etUserName.getText().toString();
            final String age = etAge.getText().toString();
            final String email = etEmail.getText().toString();


            @Override
            public void onClick(View v) {

                Intent nextIntent = new Intent(UserAreaActivity.this, BrowserActivity.class);

                //nextIntent.putExtra("username", username);
                //nextIntent.putExtra("age", age);

                nextIntent.putExtra("username", username);
                nextIntent.putExtra("age", age);
                nextIntent.putExtra("email", email);


                UserAreaActivity.this.startActivity(nextIntent);
            }
        });

    }
}
