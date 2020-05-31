package com.example.vpshareapp.Model;

public class ModelCommander {
    String Address, Allocated_Bags, Email, Name, Password, Phone, city, country,isAuthorized, uid;

    public ModelCommander(String address, String allocated_Bags, String email, String name, String password, String phone, String city, String country, String isAuthorized, String uid) {
        Address = address;
        Allocated_Bags = allocated_Bags;
        Email = email;
        Name = name;
        Password = password;
        Phone = phone;
        this.city = city;
        this.country = country;
        this.isAuthorized = isAuthorized;
        this.uid = uid;
    }

    public String getIsAuthorized() {
        return isAuthorized;
    }

    public void setIsAuthorized(String isAuthorized) {
        this.isAuthorized = isAuthorized;
    }

    public ModelCommander() {
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getAllocated_Bags() {
        return Allocated_Bags;
    }

    public void setAllocated_Bags(String allocated_Bags) {
        Allocated_Bags = allocated_Bags;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
