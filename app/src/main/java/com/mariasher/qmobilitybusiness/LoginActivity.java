package com.mariasher.qmobilitybusiness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void loginButtonClicked(View view) {

    }

    public void RegisterBusinessActivity(View view) {
        Intent registerBusinessIntent = new Intent(this, RegisterBusinessActivity.class);
        startActivity(registerBusinessIntent);
        finish();
    }


}