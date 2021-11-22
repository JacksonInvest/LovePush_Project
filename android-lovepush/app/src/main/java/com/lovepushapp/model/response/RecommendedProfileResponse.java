package com.lovepushapp.model.response;

import java.util.List;

public class RecommendedProfileResponse {

    /**
     * status : 200
     * recommendeusers : {"current_page":1,"data":[{"id":5,"name":"hh","username":"bh","email":"vv@mailinator.com","phone_code":"+1","phone":"1234567890","country":"IN","city":"Chandigarh","address":"Unnamed Road, Phase - I, Manimajra, Chandigarh, Haryana 160101, India","latitude":"30.7278325","longitude":"76.84623828125004","gender":1,"social_id":null,"login_type":1,"email_verified_at":null,"love_push_intention":"fyuf","relationship":"Committed","language":"hffh","size":"hxhx","eyecolor":"ddhhd","haircolor":"hfhfhf","education":"hddydh","job":"fyhffh","employer":"gdgdgd","hobbies":"dyydyd","sexual_orientation":1,"aboutme":"fhhdhd","status":1,"is_restrict_fb_friends":1,"partner_preference":3,"verification_code":null,"is_phone_verify":0,"is_profile_complete":1,"device_type":1,"device_token":null,"profile_image":null,"role":2,"subscription_type":1,"subscription_expire_date":null,"created_at":"2019-01-22 10:02:01","updated_at":"2019-01-22 12:11:48","matched_percentage":100}],"first_page_url":"http://14.141.175.109/lovepush/public/api/recommended-profile?page=1","from":1,"last_page":1,"last_page_url":"http://14.141.175.109/lovepush/public/api/recommended-profile?page=1","next_page_url":null,"path":"http://14.141.175.109/lovepush/public/api/recommended-profile","per_page":10,"prev_page_url":null,"to":1,"total":1}
     */

    private int status;
    private String message;
    private RecommendeusersBean recommendeusers;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public RecommendeusersBean getRecommendeusers() {
        return recommendeusers;
    }

    public void setRecommendeusers(RecommendeusersBean recommendeusers) {
        this.recommendeusers = recommendeusers;
    }

    public static class RecommendeusersBean {
        /**
         * current_page : 1
         * data : [{"id":5,"name":"hh","username":"bh","email":"vv@mailinator.com","phone_code":"+1","phone":"1234567890","country":"IN","city":"Chandigarh","address":"Unnamed Road, Phase - I, Manimajra, Chandigarh, Haryana 160101, India","latitude":"30.7278325","longitude":"76.84623828125004","gender":1,"social_id":null,"login_type":1,"email_verified_at":null,"love_push_intention":"fyuf","relationship":"Committed","language":"hffh","size":"hxhx","eyecolor":"ddhhd","haircolor":"hfhfhf","education":"hddydh","job":"fyhffh","employer":"gdgdgd","hobbies":"dyydyd","sexual_orientation":1,"aboutme":"fhhdhd","status":1,"is_restrict_fb_friends":1,"partner_preference":3,"verification_code":null,"is_phone_verify":0,"is_profile_complete":1,"device_type":1,"device_token":null,"profile_image":null,"role":2,"subscription_type":1,"subscription_expire_date":null,"created_at":"2019-01-22 10:02:01","updated_at":"2019-01-22 12:11:48","matched_percentage":100}]
         * first_page_url : http://14.141.175.109/lovepush/public/api/recommended-profile?page=1
         * from : 1
         * last_page : 1
         * last_page_url : http://14.141.175.109/lovepush/public/api/recommended-profile?page=1
         * next_page_url : null
         * path : http://14.141.175.109/lovepush/public/api/recommended-profile
         * per_page : 10
         * prev_page_url : null
         * to : 1
         * total : 1
         */

        private int current_page;
        private String first_page_url;
        private int from;
        private int last_page;
        private String last_page_url;
        private String next_page_url;
        private String path;
        private int per_page;
        private String prev_page_url;
        private int to;
        private int total;
        private List<DataBean> data;

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }

        public String getFirst_page_url() {
            return first_page_url;
        }

        public void setFirst_page_url(String first_page_url) {
            this.first_page_url = first_page_url;
        }

        public int getFrom() {
            return from;
        }

        public void setFrom(int from) {
            this.from = from;
        }

        public int getLast_page() {
            return last_page;
        }

        public void setLast_page(int last_page) {
            this.last_page = last_page;
        }

        public String getLast_page_url() {
            return last_page_url;
        }

        public void setLast_page_url(String last_page_url) {
            this.last_page_url = last_page_url;
        }

        public String getNext_page_url() {
            return next_page_url;
        }

        public void setNext_page_url(String next_page_url) {
            this.next_page_url = next_page_url;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getPer_page() {
            return per_page;
        }

        public void setPer_page(int per_page) {
            this.per_page = per_page;
        }

        public String getPrev_page_url() {
            return prev_page_url;
        }

        public void setPrev_page_url(String prev_page_url) {
            this.prev_page_url = prev_page_url;
        }

        public int getTo() {
            return to;
        }

        public void setTo(int to) {
            this.to = to;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * id : 5
             * name : hh
             * username : bh
             * email : vv@mailinator.com
             * phone_code : +1
             * phone : 1234567890
             * country : IN
             * city : Chandigarh
             * address : Unnamed Road, Phase - I, Manimajra, Chandigarh, Haryana 160101, India
             * latitude : 30.7278325
             * longitude : 76.84623828125004
             * gender : 1
             * social_id : null
             * login_type : 1
             * email_verified_at : null
             * love_push_intention : fyuf
             * relationship : Committed
             * language : hffh
             * size : hxhx
             * eyecolor : ddhhd
             * haircolor : hfhfhf
             * education : hddydh
             * job : fyhffh
             * employer : gdgdgd
             * hobbies : dyydyd
             * sexual_orientation : 1
             * aboutme : fhhdhd
             * status : 1
             * is_restrict_fb_friends : 1
             * partner_preference : 3
             * verification_code : null
             * is_phone_verify : 0
             * is_profile_complete : 1
             * device_type : 1
             * device_token : null
             * profile_image : null
             * role : 2
             * subscription_type : 1
             * subscription_expire_date : null
             * created_at : 2019-01-22 10:02:01
             * updated_at : 2019-01-22 12:11:48
             * matched_percentage : 100
             */

            private int id;
            private String name;
            private String username;
            private String email;
            private String phone_code;
            private String phone;
            private String country;
            private String city;
            private String address;
            private String latitude;
            private String longitude;
            private int gender;
            private String social_id;
            private int login_type;
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
            private int sexual_orientation;
            private String aboutme;
            private int status;
            private int is_restrict_fb_friends;
            private int partner_preference;
            private String verification_code;
            private int is_phone_verify;
            private int is_profile_complete;
            private int device_type;
            private String device_token;
            private String profile_image;
            private int role;
            private int subscription_type;
            private String subscription_expire_date;
            private String created_at;
            private String updated_at;
            private int matched_percentage;

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

            public String getLatitude() {
                return latitude;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }

            public String getLongitude() {
                return longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public String getSocial_id() {
                return social_id;
            }

            public void setSocial_id(String social_id) {
                this.social_id = social_id;
            }

            public int getLogin_type() {
                return login_type;
            }

            public void setLogin_type(int login_type) {
                this.login_type = login_type;
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

            public int getSexual_orientation() {
                return sexual_orientation;
            }

            public void setSexual_orientation(int sexual_orientation) {
                this.sexual_orientation = sexual_orientation;
            }

            public String getAboutme() {
                return aboutme;
            }

            public void setAboutme(String aboutme) {
                this.aboutme = aboutme;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
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

            public String getVerification_code() {
                return verification_code;
            }

            public void setVerification_code(String verification_code) {
                this.verification_code = verification_code;
            }

            public int getIs_phone_verify() {
                return is_phone_verify;
            }

            public void setIs_phone_verify(int is_phone_verify) {
                this.is_phone_verify = is_phone_verify;
            }

            public int getIs_profile_complete() {
                return is_profile_complete;
            }

            public void setIs_profile_complete(int is_profile_complete) {
                this.is_profile_complete = is_profile_complete;
            }

            public int getDevice_type() {
                return device_type;
            }

            public void setDevice_type(int device_type) {
                this.device_type = device_type;
            }

            public String getDevice_token() {
                return device_token;
            }

            public void setDevice_token(String device_token) {
                this.device_token = device_token;
            }

            public String getProfile_image() {
                return profile_image;
            }

            public void setProfile_image(String profile_image) {
                this.profile_image = profile_image;
            }

            public int getRole() {
                return role;
            }

            public void setRole(int role) {
                this.role = role;
            }

            public int getSubscription_type() {
                return subscription_type;
            }

            public void setSubscription_type(int subscription_type) {
                this.subscription_type = subscription_type;
            }

            public String getSubscription_expire_date() {
                return subscription_expire_date;
            }

            public void setSubscription_expire_date(String subscription_expire_date) {
                this.subscription_expire_date = subscription_expire_date;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }

            public int getMatched_percentage() {
                return matched_percentage;
            }

            public void setMatched_percentage(int matched_percentage) {
                this.matched_percentage = matched_percentage;
            }
        }
    }
}
