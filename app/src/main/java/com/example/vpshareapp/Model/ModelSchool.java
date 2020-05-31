package com.example.vpshareapp.Model;

public class ModelSchool {
    String Address,Email,Name,Password,Phone,area,city,requiredStuff,uid;

    public ModelSchool(String address, String email, String name, String password, String phone, String area, String city, String requiredStuff, String uid) {
        Address = address;
        Email = email;
        Name = name;
        Password = password;
        Phone = phone;
        this.area = area;
        this.city = city;
        this.requiredStuff = requiredStuff;
        this.uid = uid;
    }

    public ModelSchool() {
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRequiredStuff() {
        return requiredStuff;
    }

    public void setRequiredStuff(String requiredStuff) {
        this.requiredStuff = requiredStuff;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
