package com.android.iunoob.bloodpoint.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.iunoob.bloodpoint.R;
import com.android.iunoob.bloodpoint.viewmodels.CustomMethod;
import com.android.iunoob.bloodpoint.viewmodels.UserData;

import java.util.List;

/***
 Project Name: BloodBank
 Project Date: 10/11/18
 Created by: imshakil
 Email: mhshakil_ice_iu@yahoo.com
 ***/

public class SearchDonorAdapter extends RecyclerView.Adapter<SearchDonorAdapter.PostHolder> {


    private List<UserData> postLists;

    class PostHolder extends RecyclerView.ViewHolder
    {
        TextView Name, Address, contact, posted, totaldonate;

        PostHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.donorName);
            contact = itemView.findViewById(R.id.donorContact);
            totaldonate = itemView.findViewById(R.id.totaldonate);
            Address = itemView.findViewById(R.id.donorAddress);
            posted = itemView.findViewById(R.id.lastdonate);

        }
    }

    public SearchDonorAdapter(List<UserData> postLists)
    {
        this.postLists = postLists;
    }

    @Override
    public PostHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View listitem = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_donor_item, viewGroup, false);

        return new PostHolder(listitem);
    }

    @Override
    public void onBindViewHolder(PostHolder postHolder, int i) {

        UserData donorData = postLists.get(i);
        postHolder.Name.setText("Name: "+donorData.getName());
        postHolder.contact.setText(donorData.getContact());
        postHolder.Address.setText("Address: "+donorData.getAddress());
        postHolder.totaldonate.setText("Total Donation: "+donorData.getTotalDonated()+" time"+CustomMethod.GetS(donorData.getTotalDonated()));
        postHolder.posted.setText("Last Donation: "+ CustomMethod.GetDateFormat(donorData.getLastDonated()));

    }

    @Override
    public int getItemCount() {
        return postLists.size();
    }
}
