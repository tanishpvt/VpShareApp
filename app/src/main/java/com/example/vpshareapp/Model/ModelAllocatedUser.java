package com.example.vpshareapp.Model;

public class ModelAllocatedUser {
String Address,Area,Barcode,CommanderId,CommanderName,Donated,Email,Name,Passsword,Phone,bagid,city,country;


    public ModelAllocatedUser(String address, String area, String barcode, String commanderId,
                              String commanderName, String donated, String email, String name,
                              String passsword, String phone, String bagid, String city, String country) {
        Address = address;
        Area = area;
        Barcode = barcode;
        CommanderId = commanderId;
        CommanderName = commanderName;
        Donated = donated;
        Email = email;
        Name = name;
        Passsword = passsword;
        Phone = phone;
        this.bagid = bagid;
        this.city = city;
        this.country = country;
    }

    public ModelAllocatedUser() {
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public String getCommanderId() {
        return CommanderId;
    }

    public void setCommanderId(String commanderId) {
        CommanderId = commanderId;
    }

    public String getCommanderName() {
        return CommanderName;
    }

    public void setCommanderName(String commanderName) {
        CommanderName = commanderName;
    }

    public String getDonated() {
        return Donated;
    }

    public void setDonated(String donated) {
        Donated = donated;
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

    public String getPasssword() {
        return Passsword;
    }

    public void setPasssword(String passsword) {
        Passsword = passsword;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getBagid() {
        return bagid;
    }

    public void setBagid(String bagid) {
        this.bagid = bagid;
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
}
