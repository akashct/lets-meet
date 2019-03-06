package com.project.letsmeet;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {


    EditText fname;
    EditText lname;
    EditText email;
    EditText password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fname = (EditText)findViewById(R.id.sign_up_first_name);
        lname = (EditText)findViewById(R.id.sign_up_last_name);
        email = (EditText)findViewById(R.id.sign_up_email_input);
        password = (EditText)findViewById(R.id.sign_up_password_input);

        mAuth = FirebaseAuth.getInstance();

    }

    public void handleCreateuser(View view) {
        Log.d("SignUp", "createAccount:" + email.getText().toString());
        if (!validateForm()) {
            Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
        Log.d("SignUperror", "createAccount:" + email.getText().toString());
            return;
        }

        //showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("SignUpValidation", "createAccount:" + email.getText().toString());
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SignUp", "createUserWithEmail:s uccess");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e("SignUp", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        //hideProgressDialog();

                    }
                });


    }

    private void updateUI(FirebaseUser user) {
        //hideProgressDialog();
        if (user != null) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            email.setText("");
            password.setText("");
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email_temp = email.getText().toString();
        if (TextUtils.isEmpty(email_temp)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }

        String password_temp = password.getText().toString();
        if (TextUtils.isEmpty(password_temp)) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }

    public void handleSignuptoLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
