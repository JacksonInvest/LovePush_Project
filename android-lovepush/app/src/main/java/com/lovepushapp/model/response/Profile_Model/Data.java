
package com.lovepushapp.model.response.Profile_Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
@SuppressWarnings("unused")
public class Data {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("locale")
    @Expose
    private String locale;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("phone_code")
    @Expose
    private String phoneCode;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("gender")
    @Expose
    private Integer gender;
    @SerializedName("login_type")
    @Expose
    private Integer loginType;
    @SerializedName("social_id")
    @Expose
    private String socialId;
    @SerializedName("email_verified_at")
    @Expose
    private String emailVerifiedAt;
    @SerializedName("love_push_intention")
    @Expose
    private String lovePushIntention;
    @SerializedName("relationship")
    @Expose
    private Integer relationship;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("eyecolor")
    @Expose
    private String eyecolor;
    @SerializedName("haircolor")
    @Expose
    private String haircolor;
    @SerializedName("education")
    @Expose
    private String education;
    @SerializedName("job")
    @Expose
    private String job;
    @SerializedName("employer")
    @Expose
    private String employer;
    @SerializedName("religion")
    @Expose
    private String religion;
    @SerializedName("hobbies")
    @Expose
    private String hobbies;
    @SerializedName("sexual_orientation")
    @Expose
    private Integer sexualOrientation;
    @SerializedName("aboutme")
    @Expose
    private String aboutme;
    @SerializedName("is_restrict_fb_friends")
    @Expose
    private Integer isRestrictFbFriends;
    @SerializedName("partner_preference")
    @Expose
    private Integer partnerPreference;
    @SerializedName("verification_code")
    @Expose
    private String verificationCode;
    @SerializedName("is_profile_complete")
    @Expose
    private Integer isProfileComplete;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("physique")
    @Expose
    private Integer physique;
    @SerializedName("quickblox_id")
    @Expose
    private String quickbloxId;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("myReferalCode")
    @Expose
    private String myReferalCode;
    @SerializedName("profile_images")
    @Expose
    private List<ProfileImage> profileImages = null;
    @SerializedName("personality_test")
    @Expose
    private PersonalityTest personalityTest;
    @SerializedName("isDeleted")
    @Expose
    private Integer isDeleted;
    @SerializedName("deletedBy")
    @Expose
    private Integer deletedBy;

    @SerializedName("isBlocked")
    @Expose
    private Integer isBlocked;

    @SerializedName("plan_details")
    @Expose
    private PlanDetail plan_details;

    public PlanDetail getPlan_details() {
        return plan_details;
    }

    public void setPlan_details(PlanDetail plan_details) {
        this.plan_details = plan_details;
    }

    @SerializedName("blockedBy")
    @Expose
    private Integer blockedBy;

    @SerializedName("receive_notification")
    @Expose
    private String receive_notification;


    public Integer getIsDeleted() {
        return isDeleted;
    }

    public String getReceive_notification() {
        return receive_notification;
    }

    public void setReceive_notification(String receive_notification) {
        this.receive_notification = receive_notification;
    }


    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(Integer deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer isBlocked) {
        this.isBlocked = isBlocked;
    }

    public Integer getBlockedBy() {
        return blockedBy;
    }

    public void setBlockedBy(Integer blockedBy) {
        this.blockedBy = blockedBy;
    }

    @SerializedName("match_info1")
    @Expose
    private List<MatchInfo1> matchInfo1 = null;
    @SerializedName("match_info2")
    @Expose
    private List<MatchInfo2> matchInfo2 = null;

    public List<MatchInfo1> getMatchInfo1() {
        return matchInfo1;
    }

    public void setMatchInfo1(List<MatchInfo1> matchInfo1) {
        this.matchInfo1 = matchInfo1;
    }

    public List<MatchInfo2> getMatchInfo2() {
        return matchInfo2;
    }

    public void setMatchInfo2(List<MatchInfo2> matchInfo2) {
        this.matchInfo2 = matchInfo2;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
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

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getLoginType() {
        return loginType;
    }

    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public String getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(String emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public String getLovePushIntention() {
        return lovePushIntention;
    }

    public void setLovePushIntention(String lovePushIntention) {
        this.lovePushIntention = lovePushIntention;
    }

    public Integer getRelationship() {
        return relationship;
    }

    public void setRelationship(Integer relationship) {
        this.relationship = relationship;
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

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public Integer getSexualOrientation() {
        return sexualOrientation;
    }

    public void setSexualOrientation(Integer sexualOrientation) {
        this.sexualOrientation = sexualOrientation;
    }

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }

    public Integer getIsRestrictFbFriends() {
        return isRestrictFbFriends;
    }

    public void setIsRestrictFbFriends(Integer isRestrictFbFriends) {
        this.isRestrictFbFriends = isRestrictFbFriends;
    }

    public Integer getPartnerPreference() {
        return partnerPreference;
    }

    public void setPartnerPreference(Integer partnerPreference) {
        this.partnerPreference = partnerPreference;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Integer getIsProfileComplete() {
        return isProfileComplete;
    }

    public void setIsProfileComplete(Integer isProfileComplete) {
        this.isProfileComplete = isProfileComplete;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getPhysique() {
        return physique;
    }

    public void setPhysique(Integer physique) {
        this.physique = physique;
    }

    public String getQuickbloxId() {
        return quickbloxId;
    }

    public void setQuickbloxId(String quickbloxId) {
        this.quickbloxId = quickbloxId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMyReferalCode() {
        return myReferalCode;
    }

    public void setMyReferalCode(String myReferalCode) {
        this.myReferalCode = myReferalCode;
    }

    public List<ProfileImage> getProfileImages() {
        return profileImages;
    }

    public void setProfileImages(List<ProfileImage> profileImages) {
        this.profileImages = profileImages;
    }

    public PersonalityTest getPersonalityTest() {
        return personalityTest;
    }

    public void setPersonalityTest(PersonalityTest personalityTest) {
        this.personalityTest = personalityTest;
    }

    public class MatchInfo1 {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("event_type")
        @Expose
        private String eventType;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("like_by_me")
        @Expose
        private Integer likeByMe;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("qb_chat_dialog")
        @Expose
        private QbChatDialog qbChatDialog;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getEventType() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getLikeByMe() {
            return likeByMe;
        }

        public void setLikeByMe(Integer likeByMe) {
            this.likeByMe = likeByMe;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public QbChatDialog getQbChatDialog() {
            return qbChatDialog;
        }

        public void setQbChatDialog(QbChatDialog qbChatDialog) {
            this.qbChatDialog = qbChatDialog;
        }
        public class QbChatDialog {

            @SerializedName("id")
            @Expose
            private Integer id;
            @SerializedName("match_id")
            @Expose
            private Integer matchId;
            @SerializedName("qb_dialog_id")
            @Expose
            private String qbDialogId;
            @SerializedName("chat_status")
            @Expose
            private Integer chatStatus;
            @SerializedName("created_at")
            @Expose
            private String createdAt;
            @SerializedName("updated_at")
            @Expose
            private String updatedAt;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public Integer getMatchId() {
                return matchId;
            }

            public void setMatchId(Integer matchId) {
                this.matchId = matchId;
            }

            public String getQbDialogId() {
                return qbDialogId;
            }

            public void setQbDialogId(String qbDialogId) {
                this.qbDialogId = qbDialogId;
            }

            public Integer getChatStatus() {
                return chatStatus;
            }

            public void setChatStatus(Integer chatStatus) {
                this.chatStatus = chatStatus;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

        }

    }

    public class MatchInfo2 {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("event_type")
        @Expose
        private String eventType;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("like_by_me")
        @Expose
        private Integer likeByMe;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("qb_chat_dialog")
        @Expose
        private QbChatDialog qbChatDialog;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getEventType() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getLikeByMe() {
            return likeByMe;
        }

        public void setLikeByMe(Integer likeByMe) {
            this.likeByMe = likeByMe;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public QbChatDialog getQbChatDialog() {
            return qbChatDialog;
        }

        public void setQbChatDialog(QbChatDialog qbChatDialog) {
            this.qbChatDialog = qbChatDialog;
        }
        public class QbChatDialog {

            @SerializedName("id")
            @Expose
            private Integer id;
            @SerializedName("match_id")
            @Expose
            private Integer matchId;
            @SerializedName("qb_dialog_id")
            @Expose
            private String qbDialogId;
            @SerializedName("chat_status")
            @Expose
            private Integer chatStatus;
            @SerializedName("created_at")
            @Expose
            private String createdAt;
            @SerializedName("updated_at")
            @Expose
            private String updatedAt;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public Integer getMatchId() {
                return matchId;
            }

            public void setMatchId(Integer matchId) {
                this.matchId = matchId;
            }

            public String getQbDialogId() {
                return qbDialogId;
            }

            public void setQbDialogId(String qbDialogId) {
                this.qbDialogId = qbDialogId;
            }

            public Integer getChatStatus() {
                return chatStatus;
            }

            public void setChatStatus(Integer chatStatus) {
                this.chatStatus = chatStatus;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

        }

    }
}
