package com.android.iunoob.bloodpoint.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.iunoob.bloodpoint.R;
import com.android.iunoob.bloodpoint.adapters.SearchDonorAdapter;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/***
 Project Name: BloodBank
 Project Date: 10/14/18
 Created by: imshakil
 Email: mhshakil_ice_iu@yahoo.com
 ***/

public class SearchDonorFragment extends Fragment {

    private View view;

    FirebaseAuth mAuth;
    FirebaseUser fuser;
    FirebaseDatabase fdb;
    DatabaseReference db_ref;

    Spinner bloodgroup, district;
    Button btnsearch;
    ProgressDialog pd;
    List<UserData> donorItem;
    private RecyclerView recyclerView;
    private SearchDonorAdapter sdadapter;

    public SearchDonorFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.search_donor_fragment, container, false);

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);

        mAuth = FirebaseAuth.getInstance();
        fuser = mAuth.getCurrentUser();
        fdb = FirebaseDatabase.getInstance();
        db_ref = fdb.getReference("users");

        bloodgroup = view.findViewById(R.id.btngetBloodGroup);
        district = view.findViewById(R.id.btngetDivison);
        btnsearch = view.findViewById(R.id.btnSearch);

        getActivity().setTitle("Find Blood Donor");

        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                donorItem = new ArrayList<>();
                donorItem.clear();
                sdadapter = new SearchDonorAdapter(donorItem);
                recyclerView = view.findViewById(R.id.showDonorList);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                RecyclerView.LayoutManager searchdonor = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(searchdonor);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
                recyclerView.setAdapter(sdadapter);
                Query qpath  = db_ref;
                qpath.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       if(dataSnapshot.exists())
                       {
                           for(DataSnapshot singleitem : dataSnapshot.getChildren())
                           {
                               UserData donorData = singleitem.getValue(UserData.class);
                               final Calendar cldr = Calendar.getInstance();
                               final long totday = CustomMethod.GetDateInInt(cldr.get(Calendar.YEAR), cldr.get(Calendar.MONTH)+1, cldr.get(Calendar.DAY_OF_MONTH)) - donorData.getLastDonated();

                               if(donorData.getDonorInfo()!=0 && totday>=120 && donorData.getDistrict()==district.getSelectedItemPosition() && donorData.getBloodGroup()==bloodgroup.getSelectedItemPosition())
                               {
                                   donorItem.add(donorData);
                                   sdadapter.notifyDataSetChanged();
                               }

                               if(sdadapter.getItemCount()>100) break;
                           }

                           pd.dismiss();

                           if(sdadapter.getItemCount()<=0) Toast.makeText(getActivity(), "Sorry! No "+bloodgroup.getSelectedItem()+" Donor available in "+district.getSelectedItem()+".",
                                   Toast.LENGTH_LONG).show();
                       }
                       else
                       {

                           pd.dismiss();
                           Toast.makeText(getActivity(), "Database is empty now!",
                                   Toast.LENGTH_LONG).show();

                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {
                       Log.d("User", databaseError.getMessage());

                   }
               });
            }
        });

        return view;
    }

}
