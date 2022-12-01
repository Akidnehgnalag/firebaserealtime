package com.example.firebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    Button login,register;
    EditText _txEmail, _txPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.btLogin);
        login.setOnClickListener(this);
        register = findViewById(R.id.btReg);
        register.setOnClickListener(this);
        _txEmail = findViewById(R.id.txEmail);
        _txPassword = findViewById(R.id.txPass);


        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            if (currentUser.isEmailVerified()) {
                Intent home = new Intent(this, Home.class);
                home.putExtra("email", currentUser.getEmail());
                startActivity(home);
            }
        }



    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.btLogin)  // login
        {
            mAuth.signInWithEmailAndPassword(_txEmail.getText().toString(),_txPassword.getText().toString())
                    .addOnCompleteListener(this,
                            new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                 FirebaseUser user = mAuth.getCurrentUser();
                                if(user!=null)
                                {
                                    if (user.isEmailVerified()){
                                    Intent home = new Intent(MainActivity.this,Home.class);
                                    home.putExtra("email",_txEmail.getText().toString());
                                    startActivity(home);
                                    }

                                    else
                                        {
                                            Toast.makeText(MainActivity.this, "Not verified",Toast.LENGTH_LONG).show();

                                        }
                                }

                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "Authentication failed.",Toast.LENGTH_LONG).show();
                            }

                        }
                    }

            );

        }
        else if(v.getId()==R.id.btReg) // registrasi
        {
            mAuth.createUserWithEmailAndPassword(_txEmail.getText().toString(),_txPassword.getText().toString())
                    .addOnCompleteListener(this,
                            new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                if(user!=null)
                                {

                                    //Kasih nama, jika menggunakan auth google tidak perlu krena sdah ada nama di googlenya
                                    //nama bisa diberikan textfield sendiri agar user ketika registrasi masukan nama user tersebut
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName("User Test") //contoh user test namanya
                                            .build();

                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("gagal update profil", "User profile updated.");
                                                    }
                                                }
                                            });
                                    /////////////////////////////////////////

                                    if (user.isEmailVerified()){   //cek sudah di verifikasi di emailnya apa belum
                                    Intent home = new Intent(MainActivity.this,Home.class);
                                    home.putExtra("email",_txEmail.getText().toString());
                                    startActivity(home);
                                    }
                                    else {

                                        final String email = user.getEmail();

                                        user.sendEmailVerification().addOnCompleteListener(MainActivity.this, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(MainActivity.this,"Verification email sent to " + email,Toast.LENGTH_SHORT).show(); }
                                                else {
                                                    Log.e("Error di verifikasi", "sendEmailVerification", task.getException());
                                                    Toast.makeText(MainActivity.this,"Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                                }
                                                // [END_EXCLUDE]
                                            }
                                        });

                                    }
                                }
                            }

                            else
                            {
                                Toast.makeText(MainActivity.this, "Authentication failed.",Toast.LENGTH_LONG).show();
                            }

                        }
                    }

            );

        }

    }
}
