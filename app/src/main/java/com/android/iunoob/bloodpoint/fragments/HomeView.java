package com.android.iunoob.bloodpoint.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.iunoob.bloodpoint.R;
import com.android.iunoob.bloodpoint.adapters.BloodRequestAdapter;
import com.android.iunoob.bloodpoint.viewmodels.PostData;
import com.android.iunoob.bloodpoint.viewmodels.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;


/***
 Project Name: BloodBank
 Project Date: 10/12/18
 Created by: imshakil
 Email: mhshakil_ice_iu@yahoo.com
 ***/

public class HomeView extends Fragment {

    private DatabaseReference donor_ref;
    FirebaseAuth mAuth;
    private BloodRequestAdapter restAdapter;
    private List<PostData> postLists;
    private ProgressDialog pd;
    boolean isClicked = false;
    Spinner bg, dist;

    public HomeView() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_view_fragment, container, false);
        RecyclerView recentPosts = view.findViewById(R.id.recyleposts);

        recentPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        donor_ref = FirebaseDatabase.getInstance().getReference("posts");
        postLists = new ArrayList<>();

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);

        mAuth = FirebaseAuth.getInstance();
        getActivity().setTitle("Blood Point");

        restAdapter = new BloodRequestAdapter(postLists);
        RecyclerView.LayoutManager pmLayout = new LinearLayoutManager(getContext());
        recentPosts.setLayoutManager(pmLayout);
        recentPosts.setItemAnimator(new DefaultItemAnimator());
        recentPosts.setAdapter(restAdapter);

        TextView phoneCall = view.findViewById(R.id.targetCN);

        bg = view.findViewById(R.id.btngetBloodGroup);
        dist = view.findViewById(R.id.btngetDivison);


        view.findViewById(R.id.btnSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFilterPost();
            }
        });

        return view;

    }

    private void AddPosts()
    {

        pd.show();
        postLists.clear();
        restAdapter.notifyDataSetChanged();

        DatabaseReference ref = donor_ref.getRef();
        ref.orderByChild("Time").limitToLast(50);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    for (DataSnapshot singlepost : dataSnapshot.getChildren()) {

                        postLists.add(singlepost.getValue(PostData.class));

                    }
                    restAdapter.notifyDataSetChanged();
                    if(restAdapter.getItemCount()<=0)
                    {
                        Toast.makeText(getActivity(), "Database is empty now!",
                                Toast.LENGTH_LONG).show();
                    }

                }
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("User", databaseError.getMessage());

            }
        });

    }

    private void AddFilterPost()
    {
        pd.show();
        postLists.clear();
        restAdapter.notifyDataSetChanged();
        DatabaseReference ref = donor_ref.getRef();
        ref.orderByChild("Time");

        final String bloodg = bg.getSelectedItem().toString();
        final String district = dist.getSelectedItem().toString();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    for(DataSnapshot SingleItem:dataSnapshot.getChildren())
                    {
                        PostData postData = SingleItem.getValue(PostData.class);

                        if(postData.getDistrict().equals(district) && postData.getBloodGroup().equals(bloodg))
                        {
                            postLists.add(postData);
                            restAdapter.notifyDataSetChanged();
                        }
                        if(restAdapter.getItemCount()>50) break;

                    }

                    pd.dismiss();

                    if(restAdapter.getItemCount()<=0) {
                        Toast.makeText(getContext(), "There is no "+ bloodg +" blood request in "+ district +" right now.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public void onStart() {
        AddPosts();
        super.onStart();
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
