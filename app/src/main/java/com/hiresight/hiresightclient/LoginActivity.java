package com.hiresight.hiresightclient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    Button loginBtn;
    EditText idTxt, passTxt;
    TextView signUpTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        idTxt = (EditText) findViewById(R.id.idTxt);
        passTxt = (EditText) findViewById(R.id.passTxt);
        verifyUser();
        signUp();
    }

    public void verifyUser(){
        loginBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        auth.signInWithEmailAndPassword(idTxt.getText().toString(), passTxt.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            String userID = currentUser.getUid();
                                            DocumentReference documentReference = db.collection("Clients").document(userID);
                                            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            Toast.makeText(getApplicationContext(), "Logged in succesfully!", Toast.LENGTH_LONG).show();
                                                            Intent startIntent = new Intent(getApplicationContext(), NavigationActivity.class);
                                                            startActivity(startIntent);
                                                        } else {
                                                            Toast.makeText(getApplicationContext(), "Wrong email or password!", Toast.LENGTH_LONG).show();
                                                        }
                                                    } else {
                                                        Log.d("login", "Failed with: ", task.getException());
                                                    }
                                                }
                                            });
                                        }else Toast.makeText(getApplicationContext(), "Wrong email or password!", Toast.LENGTH_LONG).show();
                                    }
                                });

                    }
                }
        );
    }


    public void signUp(){
        signUpTxt = (TextView) findViewById(R.id.signUpTxt);
        signUpTxt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent startIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                        startActivity(startIntent);
                    }
                }
        );

    }
}
