package com.example.firebaseauth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity implements View.OnClickListener {

    TextView result;
    Button logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logOut = findViewById(R.id.btSignOut);
        logOut.setOnClickListener(this);

        Intent dataIntent =getIntent();
        String data = dataIntent.getStringExtra("email");
        result = findViewById(R.id.txResult);
        result.setText(data);
        Toast.makeText(this, data,Toast.LENGTH_LONG).show();

    }

    @Override
    public void onClick(View v) {

        FirebaseAuth.getInstance().signOut();
        this.finish();

    }
}
