package com.lovepushapp.model.local;

public class RegisterData {

    public RegisterData(String name, String username, String email, String phone, String gender, String password, String password_confirmation, String phone_code, String login_type, String device_type, String referalBy) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.password = password;
        this.password_confirmation = password_confirmation;
        this.phone_code = phone_code;
        this.login_type = login_type;
        this.device_type = device_type;
        this.referalBy = referalBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_confirmation() {
        return password_confirmation;
    }

    public void setPassword_confirmation(String password_confirmation) {
        this.password_confirmation = password_confirmation;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getReferalBy() {
        return referalBy;
    }

    public void setReferalBy(String referalBy) {
        this.referalBy = referalBy;
    }

    public String name,username,email,phone,gender,password,password_confirmation,phone_code,login_type,device_type,referalBy;

}
