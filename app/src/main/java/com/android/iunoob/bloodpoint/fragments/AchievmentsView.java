package com.android.iunoob.bloodpoint.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.iunoob.bloodpoint.R;
import com.android.iunoob.bloodpoint.activities.Dashboard;
import com.android.iunoob.bloodpoint.viewmodels.CustomMethod;
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
import java.util.TimeZone;

/***
 Project Name: BloodBank
 Project Date: 10/12/18
 Created by: imshakil
 Email: mhshakil_ice_iu@yahoo.com
 ***/

public class AchievmentsView extends Fragment {

    private long cur_day;
    private long cur_month;
    private long cur_year;
    private long totday;
    private Calendar calendar;
    private ProgressDialog pd;
    DatabaseReference user_ref;
    FirebaseAuth mAuth;

    private TextView nextDonate, donateInfo, TotalDonate, LastDonate;

    private View view;
    private Button yes;
    private LinearLayout yesno;
    private UserData userData;
    private String UID;

    public AchievmentsView() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_achievment_fragment, container, false);

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
        donateInfo = view.findViewById(R.id.donateInfo);
        LastDonate = view.findViewById(R.id.setLastDonate);
        TotalDonate = view.findViewById(R.id.settotalDonate);

        getActivity().setTitle("Achievements");
        user_ref = FirebaseDatabase.getInstance().getReference("users");
        FirebaseUser cur_user = mAuth.getInstance().getCurrentUser();
        UID = cur_user.getUid();
        Query userQ = user_ref.child(UID);

        try {
            pd.show();
            userQ.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        userData = dataSnapshot.getValue(UserData.class);

                        int DonorData = userData.getDonorInfo();
                        Toast.makeText(getActivity(), "Is Ok"+DonorData, Toast.LENGTH_LONG);
                        if (DonorData==1) {
                            totday = 0;
                            nextDonate = view.findViewById(R.id.nextDonate);
                            yesno = view.findViewById(R.id.yesnolayout);

                            calendar = Calendar.getInstance(TimeZone.getDefault());
                                cur_day = calendar.get(Calendar.DAY_OF_MONTH);
                                cur_month = calendar.get(Calendar.MONTH) + 1;
                                cur_year = calendar.get(Calendar.YEAR);
                                totday = CustomMethod.GetDateInInt(cur_year, cur_month, cur_day) - userData.getLastDonated();

                                try {
                                    if (totday >= 120) {
                                        donateInfo.setText("Have you donated today?");
                                        nextDonate.setVisibility(View.GONE);
                                        yesno.setVisibility(View.VISIBLE);
                                        yes = view.findViewById(R.id.btnYes);
                                        yes.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                user_ref.child(UID).child("LastDonated").setValue(CustomMethod.GetDateInInt(cur_year, cur_month, cur_day));
                                                user_ref.child(UID).child("TotalDonated").setValue(userData.getTotalDonated()+1);
                                                startActivity(new Intent(getActivity(), Dashboard.class));
                                            }
                                        });
                                    } else {
                                        totday = 120 - totday;
                                        donateInfo.setText("You can donate blood again after:");
                                        yesno.setVisibility(View.GONE);
                                        nextDonate.setVisibility(View.VISIBLE);
                                        nextDonate.setText((totday) + (" Day"+ CustomMethod.GetS(totday)));
                                        LastDonate.setText(CustomMethod.GetDateFormat(userData.getLastDonated()));
                                        TotalDonate.setText(userData.getTotalDonated()+" Time"+ CustomMethod.GetS(userData.getTotalDonated()));

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                pd.dismiss();

                        } else {
                            LinearLayout linearLayout = view.findViewById(R.id.donorAchiev);
                            linearLayout.setVisibility(View.GONE);
                            TextView tv = view.findViewById(R.id.ShowInof);
                            tv.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), "Update your profile to be a donor first."+userData.getDonorInfo(), Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return view;
    }

}
