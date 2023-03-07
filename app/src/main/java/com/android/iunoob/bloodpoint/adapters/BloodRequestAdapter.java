package com.android.iunoob.bloodpoint.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.iunoob.bloodpoint.R;
import com.android.iunoob.bloodpoint.viewmodels.CustomMethod;
import com.android.iunoob.bloodpoint.viewmodels.PostData;

import java.util.List;

/***
 Project Name: BloodBank
 Project Date: 10/11/18
 Created by: imshakil
 Email: mhshakil_ice_iu@yahoo.com
 ***/

public class BloodRequestAdapter extends RecyclerView.Adapter<BloodRequestAdapter.PostHolder> {


    private List<PostData> postLists;

    class PostHolder extends RecyclerView.ViewHolder
    {
        TextView Name, bloodgroup, Address, contact, posted, Message;

        PostHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.userName);
            contact = itemView.findViewById(R.id.targetCN);
            bloodgroup = itemView.findViewById(R.id.targetBG);
            Address = itemView.findViewById(R.id.reqstLocation);
            posted = itemView.findViewById(R.id.posted);
            Message = itemView.findViewById(R.id.showMessage);
        }
    }

    public BloodRequestAdapter(List<PostData> postLists)
    {
        this.postLists = postLists;
    }

    @Override
    public PostHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View listitem = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.request_list_item, viewGroup, false);

        return new PostHolder(listitem);
    }

    @Override
    public void onBindViewHolder(PostHolder postHolder, int i) {

        PostData postData = postLists.get(i);
        postHolder.Name.setText(postData.getName());
        postHolder.Address.setText(postData.getAddress()+", "+ postData.getDistrict());
        postHolder.bloodgroup.setText(postData.getBloodGroup()+" Blood Donor.");
        postHolder.Message.setText(postData.getMessage());
        postHolder.posted.setText(CustomMethod.GetDateAndTime(postData.getTime(),"hh:mm a, dd/MM/yyyy"));
        postHolder.contact.setText(postData.getContact());

    }

    @Override
    public int getItemCount() {
        return postLists.size();
    }
}
