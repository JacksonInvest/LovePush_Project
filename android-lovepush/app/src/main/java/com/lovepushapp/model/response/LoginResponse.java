package com.lovepushapp.model.response;

import java.util.List;

public class LoginResponse {


    /**
     * status : 200
     * data : {"id":41,"name":"vivek","locale":"en","email":"vivek@mailinator.com","username":"vivek123","phone_code":"+1","phone":"","country":null,"city":null,"address":null,"gender":1,"login_type":1,"social_id":null,"email_verified_at":null,"love_push_intention":null,"relationship":null,"language":null,"size":null,"eyecolor":null,"haircolor":null,"education":null,"job":null,"employer":null,"hobbies":null,"sexual_orientation":null,"aboutme":null,"is_restrict_fb_friends":1,"partner_preference":3,"verification_code":2120,"is_profile_complete":0,"profile_image":"http://14.141.175.109/lovepush/public/images/system/default_user.png","profile_images":[]}
     * message : Registration has been done successfully.
     */

    private int status;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;
    private DataBean data;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * id : 41
         * name : vivek
         * locale : en
         * email : vivek@mailinator.com
         * username : vivek123
         * phone_code : +1
         * phone :
         * country : null
         * city : null
         * address : null
         * gender : 1
         * login_type : 1
         * social_id : null
         * email_verified_at : null
         * love_push_intention : null
         * relationship : null
         * language : null
         * size : null
         * eyecolor : null
         * haircolor : null
         * education : null
         * job : null
         * employer : null
         * hobbies : null
         * sexual_orientation : null
         * aboutme : null
         * is_restrict_fb_friends : 1
         * partner_preference : 3
         * verification_code : 2120
         * is_profile_complete : 0
         * profile_image : http://14.141.175.109/lovepush/public/images/system/default_user.png
         * profile_images : []
         */

        private int id;
        private String name;
        private String locale;
        private String email;
        private String username;
        private String phone_code;
        private String phone;
        private String country;
        private String city;
        private String address;
        private int gender;
        private int login_type;
        private String social_id;
        private String email_verified_at;
        private String love_push_intention;
        private String relationship;
        private String language;
        private String size;
        private String eyecolor;
        private String haircolor;
        private String education;
        private String job;
        private String employer;
        private String hobbies;
        private String sexual_orientation;
        private String aboutme;
        private int is_restrict_fb_friends;
        private int partner_preference;
        private int verification_code;
        private int is_profile_complete;
        private String profile_image;
        private List<?> profile_images;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLocale() {
            return locale;
        }

        public void setLocale(String locale) {
            this.locale = locale;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPhone_code() {
            return phone_code;
        }

        public void setPhone_code(String phone_code) {
            this.phone_code = phone_code;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getLogin_type() {
            return login_type;
        }

        public void setLogin_type(int login_type) {
            this.login_type = login_type;
        }

        public String getSocial_id() {
            return social_id;
        }

        public void setSocial_id(String social_id) {
            this.social_id = social_id;
        }

        public String getEmail_verified_at() {
            return email_verified_at;
        }

        public void setEmail_verified_at(String email_verified_at) {
            this.email_verified_at = email_verified_at;
        }

        public String getLove_push_intention() {
            return love_push_intention;
        }

        public void setLove_push_intention(String love_push_intention) {
            this.love_push_intention = love_push_intention;
        }

        public String getRelationship() {
            return relationship;
        }

        public void setRelationship(String relationship) {
            this.relationship = relationship;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getEyecolor() {
            return eyecolor;
        }

        public void setEyecolor(String eyecolor) {
            this.eyecolor = eyecolor;
        }

        public String getHaircolor() {
            return haircolor;
        }

        public void setHaircolor(String haircolor) {
            this.haircolor = haircolor;
        }

        public String getEducation() {
            return education;
        }

        public void setEducation(String education) {
            this.education = education;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getEmployer() {
            return employer;
        }

        public void setEmployer(String employer) {
            this.employer = employer;
        }

        public String getHobbies() {
            return hobbies;
        }

        public void setHobbies(String hobbies) {
            this.hobbies = hobbies;
        }

        public String getSexual_orientation() {
            return sexual_orientation;
        }

        public void setSexual_orientation(String sexual_orientation) {
            this.sexual_orientation = sexual_orientation;
        }

        public String getAboutme() {
            return aboutme;
        }

        public void setAboutme(String aboutme) {
            this.aboutme = aboutme;
        }

        public int getIs_restrict_fb_friends() {
            return is_restrict_fb_friends;
        }

        public void setIs_restrict_fb_friends(int is_restrict_fb_friends) {
            this.is_restrict_fb_friends = is_restrict_fb_friends;
        }

        public int getPartner_preference() {
            return partner_preference;
        }

        public void setPartner_preference(int partner_preference) {
            this.partner_preference = partner_preference;
        }

        public int getVerification_code() {
            return verification_code;
        }

        public void setVerification_code(int verification_code) {
            this.verification_code = verification_code;
        }

        public int getIs_profile_complete() {
            return is_profile_complete;
        }

        public void setIs_profile_complete(int is_profile_complete) {
            this.is_profile_complete = is_profile_complete;
        }

        public String getProfile_image() {
            return profile_image;
        }

        public void setProfile_image(String profile_image) {
            this.profile_image = profile_image;
        }

        public List<?> getProfile_images() {
            return profile_images;
        }

        public void setProfile_images(List<?> profile_images) {
            this.profile_images = profile_images;
        }
    }
}
