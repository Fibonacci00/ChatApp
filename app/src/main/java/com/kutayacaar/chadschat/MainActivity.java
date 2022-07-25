package com.kutayacaar.chadschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
private EditText edtUsername,edtPassword,edtEmail;
private TextView buttonSubmit;
private TextView logininfo;
private boolean isSigningUp = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtUsername = findViewById(R.id.editUsername);
        edtEmail = findViewById(R.id.editEmail);
        edtPassword= findViewById(R.id.editPassword);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        logininfo = findViewById(R.id.loginInfo);

if(FirebaseAuth.getInstance().getCurrentUser()!=null){
    startActivity(new Intent(MainActivity.this,FriendsActivity.class));
    finish();
}

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
if (edtEmail.getText().toString().isEmpty()|| edtPassword.getText().toString().isEmpty()){
        if (isSigningUp&&edtUsername.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.this,"Yanlış giriyon haberin olsun",Toast.LENGTH_LONG).show();
            return;
        }
}
                if (isSigningUp){
handleSignUp();
}else{
    handleLogin();
}
            }
        });


         logininfo.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (isSigningUp){
                     isSigningUp = false;
                     edtUsername.setVisibility(View.GONE);
                     buttonSubmit.setText("Log In");
                     logininfo.setText("Don't have an account? Sign up");
                 }else{
                     isSigningUp=true;
                     edtUsername.setVisibility(View.VISIBLE);
                     buttonSubmit.setText("Sign up");
                     logininfo.setText("Already have an account? Log In");
                 }
             }
         });
    }
    private void handleSignUp(){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
if (task.isSuccessful()){
    FirebaseDatabase.getInstance().getReference("user/"+FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new User(edtUsername.getText().toString(),edtEmail.getText().toString(),""));
    startActivity(new Intent(MainActivity.this,FriendsActivity.class));
    Toast.makeText(MainActivity.this,"Hoşgeldin Ağam",Toast.LENGTH_LONG).show();
}else{
    Toast.makeText(MainActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();

}
            }
        });

    }private void handleLogin(){
FirebaseAuth.getInstance().signInWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()){
            startActivity(new Intent(MainActivity.this,FriendsActivity.class));
            Toast.makeText(MainActivity.this,"Hoşgeldin Ağam",Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(MainActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();

        }
    }
});
    }
}