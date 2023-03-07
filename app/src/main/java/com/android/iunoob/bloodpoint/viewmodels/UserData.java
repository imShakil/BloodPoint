package com.android.iunoob.bloodpoint.viewmodels;

/***
 Project Name: BloodBank
 Project Date: 10/12/18
 Created by: imshakil
 Email: mhshakil_ice_iu@yahoo.com
 ***/

public class UserData {

    private String Name, Email, Contact, Address, BirthDate;
    private int Gender, BloodGroup, District, DonorInfo;
    private long TotalDonated, LastDonated;

    public UserData() {

    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getAddress() {
        return Address;
    }

    public int getDistrict() {
        return District;
    }

    public String getName() {
        return Name;
    }

    public int getBloodGroup() {
        return BloodGroup;
    }

    public String getEmail() {
        return Email;
    }

    public int getGender() {
        return Gender;
    }

    public void setName(String name) { this.Name = name; }

    public void setEmail(String email) {
        this.Email = email;
    }

    public long getTotalDonated() {
        return TotalDonated;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setGender(int gender) {
        Gender = gender;
    }

    public void setBloodGroup(int bloodGroup) {
        BloodGroup = bloodGroup;
    }

    public void setDistrict(int district) {
        District = district;
    }

    public void setTotalDonated(int totalDonated) {
        TotalDonated = totalDonated;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }

    public void setLastDonated(int lastDonated) {
        LastDonated = lastDonated;
    }

    public void setDonorInfo(int donorInfo) {
        DonorInfo = donorInfo;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public long getLastDonated() {
        return LastDonated;
    }

    public int getDonorInfo() {
        return DonorInfo;
    }

}
