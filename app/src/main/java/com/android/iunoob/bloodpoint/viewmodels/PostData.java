package com.android.iunoob.bloodpoint.viewmodels;

import java.io.Serializable;

/***
 Project Name: BloodBank
 Project Date: 10/11/18
 Created by: imshakil
 Email: mhshakil_ice_iu@yahoo.com
 ***/

public class PostData implements Serializable {
    private String Address, District, Contact;
    private String Name, BloodGroup, Message;
    private long Time;


    public String getAddress() {
        return Address;
    }

    public String getDistrict() {
        return District;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public void setBloodGroup(String bloodGroup) {
        BloodGroup = bloodGroup;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void setTime(long time) {
        Time = time;
    }

    public String getContact() {
        return Contact;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
       this.Name = name;
    }

    public String getBloodGroup() {
        return BloodGroup;
    }

    public long getTime() {
        return Time;
    }
}
