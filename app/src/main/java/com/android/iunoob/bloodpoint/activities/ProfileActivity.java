package com.android.iunoob.bloodpoint.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.iunoob.bloodpoint.R;
import com.android.iunoob.bloodpoint.viewmodels.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ProfileActivity extends Fragment {

    private EditText fullName, address, contact;
    private FirebaseAuth mAuth;
    private Spinner gender, bloodgroup, district;
    private Button date;

    private DatabaseReference db_ref;
    private CheckBox isDonor;
    private int DonorData;
    int day, month, year;
    ProgressDialog pd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_profile ,container, false);

        getActivity().setTitle("Profile");

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        FirebaseDatabase db_User = FirebaseDatabase.getInstance();
        db_ref = db_User.getReference("users");
        mAuth = FirebaseAuth.getInstance();


        fullName = view.findViewById(R.id.input_fullName);
        gender = view.findViewById(R.id.gender);
        address = view.findViewById(R.id.inputAddress);
        district = view.findViewById(R.id.inputDivision);
        bloodgroup = view.findViewById(R.id.inputBloodGroup);
        contact = view.findViewById(R.id.inputMobile);
        isDonor = view.findViewById(R.id.checkbox);

        date = view.findViewById(R.id.btndate);
        Button btnSignup = view.findViewById(R.id.button_register);

        Query Profile = db_ref.child(mAuth.getCurrentUser().getUid());
        Profile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                UserData userData = dataSnapshot.getValue(UserData.class);

                if (userData != null) {

                    fullName.setText(userData.getName());
                    gender.setSelection(userData.getGender());
                    address.setText(userData.getAddress());
                    contact.setText(userData.getContact());
                    bloodgroup.setSelection(userData.getBloodGroup());
                    district.setSelection(userData.getDistrict());
                    date.setText(userData.getBirthDate());
                    DonorData = userData.getDonorInfo();
                    if(DonorData==1)
                    {
                        isDonor.setChecked(true);
                        isDonor.setText("Unmark this to leave from donor list");
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Your are not a donor. "+userData.getDonorInfo(),
                                Toast.LENGTH_LONG).show();
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("User", databaseError.getMessage());
            }

        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pd.dismiss();
            }
        }, 2000);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                day = cldr.get(Calendar.DAY_OF_MONTH);
                month = cldr.get(Calendar.MONTH);
                year = cldr.get(Calendar.YEAR);
                DatePickerDialog picker = new DatePickerDialog(getContext(),
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
                    }
                    else {

                        String id = mAuth.getCurrentUser().getUid();
                        db_ref.child(id).child("Name").setValue(Name);
                        db_ref.child(id).child("Gender").setValue(Gender);
                        db_ref.child(id).child("Contact").setValue(Contact);
                        db_ref.child(id).child("BloodGroup").setValue(BloodGroup);
                        db_ref.child(id).child("Address").setValue(Address);
                        db_ref.child(id).child("District").setValue(District);
                        db_ref.child(id).child("BirthDate").setValue(date.getText());

                        if(isDonor.isChecked()) db_ref.child(id).child("DonorInfo").setValue(1);
                        else db_ref.child(id).child("DonorInfo").setValue(0);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pd.dismiss();
                            }
                        }, 2000);

                        Toast.makeText(getContext(), "Your account has been updated!", Toast.LENGTH_LONG)
                                .show();
                        Intent intent = new Intent(getActivity(), Dashboard.class);
                        startActivity(intent);
                    }

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    private void ShowError(String error) {

        Toast.makeText(getActivity(), "Please, Enter a valid "+error, Toast.LENGTH_LONG).show();
    }

}
