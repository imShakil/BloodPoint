package com.android.iunoob.bloodpoint.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.iunoob.bloodpoint.R;
import com.android.iunoob.bloodpoint.activities.Dashboard;
import com.android.iunoob.bloodpoint.viewmodels.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class PostActivity extends Fragment {

    ProgressDialog pd;

    EditText text1, text2, text3;
    Spinner spinner1, spinner2;
    Button btnpost;

    FirebaseDatabase fdb;
    DatabaseReference db_ref;

    Calendar cal;
    String uid;

    public PostActivity()
    {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_post, container, false);


        pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);

        getActivity().setTitle("Post Blood Request");

        text1 = view.findViewById(R.id.getMobile);
        text2 = view.findViewById(R.id.getLocation);
        text3 = view.findViewById(R.id.getMessage);

        spinner1 = view.findViewById(R.id.SpinnerBlood);
        spinner2 = view.findViewById(R.id.SpinnerDistrict);

        btnpost = view.findViewById(R.id.postbtn);

        cal = Calendar.getInstance();

        FirebaseUser cur_user = FirebaseAuth.getInstance().getCurrentUser();


        uid = cur_user.getUid();

        fdb = FirebaseDatabase.getInstance();
        db_ref = fdb.getReference("posts");

        try {
            btnpost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pd.show();
                    final Query findname = fdb.getReference("users").child(uid);

                    if(text1.getText().length() == 0)
                    {
                        pd.dismiss();
                        Toast.makeText(getActivity(), "Enter your contact number!",
                                Toast.LENGTH_LONG).show();

                        text1.requestFocusFromTouch();
                    }
                    else if(text2.getText().length() == 0)
                    {
                        pd.dismiss();
                        Toast.makeText(getContext(), "Enter your location!",
                                Toast.LENGTH_LONG).show();
                        text2.requestFocusFromTouch();
                    }
                    else {
                        findname.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {

                                    db_ref.child(uid).child("Name").setValue(dataSnapshot.getValue(UserData.class).getName());
                                    db_ref.child(uid).child("Contact").setValue(text1.getText().toString());
                                    db_ref.child(uid).child("Address").setValue(text2.getText().toString());
                                    db_ref.child(uid).child("District").setValue(spinner2.getSelectedItem().toString());
                                    db_ref.child(uid).child("BloodGroup").setValue(spinner1.getSelectedItem().toString());
                                    db_ref.child(uid).child("Time").setValue(cal.getTimeInMillis());
                                    db_ref.child(uid).child("Message").setValue(text3.getText().toString());
                                    Toast.makeText(getContext(), "Your post has been published to the request board.",
                                            Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getContext(), Dashboard.class));

                                } else {
                                    Toast.makeText(getContext(), "Database error occurs.",
                                            Toast.LENGTH_LONG).show();
                                }

                                pd.dismiss();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.d("User", databaseError.getMessage());

                            }
                        });
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return view;
    }

}
