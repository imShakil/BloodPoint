package com.android.iunoob.bloodpoint.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.iunoob.bloodpoint.R;
import com.android.iunoob.bloodpoint.viewmodels.UserData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class BankInfo extends Fragment {


    DatabaseReference db_ref;
    int[] Totals = new int[2];
    int[] DonorsCat = new int[8];
    int[][] DonorsPM = new int[64][8];

    TextView ttusers, ttdonors, type1, type2, type3, type4, type5, type6, type7, type8;
    Spinner btnDist;

    ProgressDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Bank Information");
        View view = inflater.inflate(R.layout.fragment_bank_info, container, false);

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(true);

        pd.show();

        for(int i=0; i<2; i++) Totals[i] = 0;
        for(int i=0; i<8; i++) DonorsCat[i] = 0;
        for(int i=0; i<64; i++)
        {
            for(int j=0; j<8; j++)
            {
                DonorsPM[i][j] = 0;
            }
        }

        btnDist = view.findViewById(R.id.btngetDivison);
        ttusers = view.findViewById(R.id.TotalUsers);
        ttdonors = view.findViewById(R.id.TotalDonors);
        type1 = view.findViewById(R.id.type1);
        type2 = view.findViewById(R.id.type2);
        type3 = view.findViewById(R.id.type3);
        type4 = view.findViewById(R.id.type4);
        type5 = view.findViewById(R.id.type5);
        type6 = view.findViewById(R.id.type6);
        type7 = view.findViewById(R.id.type7);
        type8 = view.findViewById(R.id.type8);

        db_ref = FirebaseDatabase.getInstance().getReference("users");

        db_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot SingleItem:dataSnapshot.getChildren())
                {
                    Totals[0] += 1;
                    UserData userData = SingleItem.getValue(UserData.class);
                    if(userData.getDonorInfo()==1) {
                        Totals[1] += 1;

                        DonorsCat[userData.getBloodGroup()] += 1;
                        DonorsPM[userData.getDistrict()][userData.getBloodGroup()] += 1;

                    }
                }

                ttusers.setText(String.format("%d users", Totals[0]));
                ttdonors.setText(Totals[1]+" donors");
                type1.setText(String.format(" %d donors", DonorsCat[0]));
                type2.setText(String.format(" %d donors", DonorsCat[1]));
                type3.setText(String.format(" %d donors", DonorsCat[2]));
                type4.setText(String.format(" %d donors", DonorsCat[3]));
                type5.setText(String.format(" %d donors", DonorsCat[4]));
                type6.setText(String.format(" %d donors", DonorsCat[5]));
                type7.setText(String.format(" %d donors", DonorsCat[6]));
                type8.setText(String.format(" %d donors", DonorsCat[7]));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pd.dismiss();
            }
        }, 2000);

        view.findViewById(R.id.btnSearch).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                pd.show();

                final int DistNum = btnDist.getSelectedItemPosition();
                final String DistName = btnDist.getSelectedItem().toString();

                ttusers.setText(String.format("%d users", Totals[0]));
                ttdonors.setText(Totals[1]+" donors");
                type1.setText(String.format("%d out of (%d) in %s", DonorsPM[DistNum][0], DonorsCat[0], DistName));
                type2.setText(String.format("%d out of (%d) in %s", DonorsPM[DistNum][1], DonorsCat[1], DistName));
                type3.setText(String.format("%d out of (%d) in %s", DonorsPM[DistNum][2], DonorsCat[2], DistName));
                type4.setText(String.format("%d out of (%d) in %s", DonorsPM[DistNum][3], DonorsCat[3], DistName));
                type5.setText(String.format("%d out of (%d) in %s", DonorsPM[DistNum][4], DonorsCat[4], DistName));
                type6.setText(String.format("%d out of (%d) in %s", DonorsPM[DistNum][5], DonorsCat[5], DistName));
                type7.setText(String.format("%d out of (%d) in %s", DonorsPM[DistNum][6], DonorsCat[6], DistName));
                type8.setText(String.format("%d out of (%d) in %s", DonorsPM[DistNum][7], DonorsCat[7], DistName));


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                    }
                }, 2000);
            }

        });

        return view;
    }

}
