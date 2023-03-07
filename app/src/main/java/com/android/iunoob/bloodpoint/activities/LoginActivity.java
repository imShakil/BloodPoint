package com.android.iunoob.bloodpoint.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.iunoob.bloodpoint.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

  private EditText inputemail, inputpassword;
  private FirebaseAuth mAuth;
  private ProgressDialog pd;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    pd = new ProgressDialog(this);
    pd.setMessage("Loading...");
    pd.setCancelable(true);
    pd.setCanceledOnTouchOutside(false);

    mAuth = FirebaseAuth.getInstance();

    if(mAuth.getCurrentUser() != null)
    {
      if(mAuth.getCurrentUser().isEmailVerified()) {
        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(intent);
        finish();
      } else mAuth.signOut();
    }


    inputemail = findViewById(R.id.input_username);
    inputpassword = findViewById(R.id.input_password);

    Button signin = findViewById(R.id.button_login);
    Button signup = findViewById(R.id.button_register);
    Button resetpass = findViewById(R.id.button_forgot_password);

    signin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        final String email = inputemail.getText().toString()+"";
        final String password = inputpassword.getText().toString()+"";

        try {
          if(password.length()>0 && email.length()>0) {
            pd.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                          Toast.makeText(getApplicationContext(),
                                  "Authentication Failed",
                                  Toast.LENGTH_LONG).show();
                          Log.v("error", task.getException().getMessage());
                        }
                        else {
                          if(mAuth.getCurrentUser().isEmailVerified()) {
                            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                            startActivity(intent);
                            finish();
                          } else {
                            mAuth.getCurrentUser().sendEmailVerification();
                            Toast.makeText(LoginActivity.this,"Verify email before log in.", Toast.LENGTH_LONG).show();
                          }

                        }
                        pd.dismiss();
                      }
                    });
          }
          else
          {
            Toast.makeText(getApplicationContext(), "Please fill all the field.", Toast.LENGTH_LONG).show();
          }

        } catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    });

    signup.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intent);
      }
    });

    resetpass.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), RestorePassword.class);
        startActivity(intent);
      }
    });


  }

}
