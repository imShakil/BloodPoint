package com.android.iunoob.bloodpoint.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.iunoob.bloodpoint.R;
import com.android.iunoob.bloodpoint.viewmodels.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class SignupActivity extends AppCompatActivity {

    private EditText inputemail, inputpassword, retypePassword, fullName, address, contact;
    private FirebaseAuth mAuth;
    private Spinner gender, bloodgroup, district;
    private Button date;

    private DatabaseReference db_ref;
    private CheckBox isDonor;
    int day, month, year;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);

        FirebaseDatabase db_User = FirebaseDatabase.getInstance();
        db_ref = db_User.getReference("users");
        mAuth = FirebaseAuth.getInstance();

        inputemail = findViewById(R.id.input_userEmail);
        inputpassword = findViewById(R.id.input_password);
        retypePassword = findViewById(R.id.input_password_confirm);
        fullName = findViewById(R.id.input_fullName);
        gender = findViewById(R.id.gender);
        address = findViewById(R.id.inputAddress);
        district = findViewById(R.id.inputDivision);
        bloodgroup = findViewById(R.id.inputBloodGroup);
        contact = findViewById(R.id.inputMobile);
        isDonor = findViewById(R.id.checkbox);

        date = findViewById(R.id.btndate);
        Button btnSignup = findViewById(R.id.button_register);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                day = cldr.get(Calendar.DAY_OF_MONTH);
                month = cldr.get(Calendar.MONTH);
                year = cldr.get(Calendar.YEAR);
                DatePickerDialog picker = new DatePickerDialog(SignupActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                pd.show();

                final String email = inputemail.getText().toString();
                final String password = inputpassword.getText().toString();
                final String ConfirmPassword = retypePassword.getText().toString();
                final String Name = fullName.getText().toString();
                final int Gender = gender.getSelectedItemPosition();
                final String Contact = contact.getText().toString();
                final int BloodGroup = bloodgroup.getSelectedItemPosition();
                final String Address = address.getText().toString();
                final int District = district.getSelectedItemPosition();

                try {

                    if (Name.length() <= 2) {
                        ShowError("Name");
                        fullName.requestFocusFromTouch();
                    } else if (Contact.length() < 11) {
                        ShowError("Contact Number");
                        contact.requestFocusFromTouch();
                    } else if (Address.length() <= 2) {
                        ShowError("Address");
                        address.requestFocusFromTouch();
                    } else if (email.length() == 0) {
                            ShowError("Email ID");
                                inputemail.requestFocusFromTouch();
                    } else if (password.length() <= 5) {
                        ShowError("Password (At least 6 character long)");
                        inputpassword.requestFocusFromTouch();
                    } else if (password.compareTo(ConfirmPassword) != 0) {
                        Toast.makeText(SignupActivity.this, "Password did not match!", Toast.LENGTH_LONG)
                                .show();
                        retypePassword.requestFocusFromTouch();
                    } else {

                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(SignupActivity.this, "Registration failed! try agian.", Toast.LENGTH_LONG)
                                                    .show();
                                            Log.v("error", task.getException().getMessage());
                                        } else {
                                            String id = mAuth.getCurrentUser().getUid();
                                            db_ref.child(id).child("Name").setValue(Name);
                                            db_ref.child(id).child("Email").setValue(email);
                                            db_ref.child(id).child("Gender").setValue(Gender);
                                            db_ref.child(id).child("Contact").setValue(Contact);
                                            db_ref.child(id).child("BloodGroup").setValue(BloodGroup);
                                            db_ref.child(id).child("Address").setValue(Address);
                                            db_ref.child(id).child("District").setValue(District);
                                            db_ref.child(id).child("LastDonated").setValue(0);
                                            db_ref.child(id).child("TotalDonated").setValue(0);
                                            db_ref.child(id).child("BirthDate").setValue(date.getText());
                                            if (isDonor.isChecked()) {
                                                db_ref.child(id).child("DonorInfo").setValue(1);
                                            } else db_ref.child(id).child("DonorInfo").setValue(0);

                                            Toast.makeText(getApplicationContext(), "Welcome, your account has been created!", Toast.LENGTH_LONG)
                                                    .show();
                                            mAuth.signOut();
                                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                });
                    }
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
            }


        });
        pd.dismiss();
    }


    private void ShowError(String error) {

        Toast.makeText(SignupActivity.this, "Please, Enter a valid "+error,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}