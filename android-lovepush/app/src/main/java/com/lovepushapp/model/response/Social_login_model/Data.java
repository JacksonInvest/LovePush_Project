
package com.lovepushapp.model.response.Social_login_model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lovepushapp.model.response.Profile_Model.PersonalityTest;
import com.lovepushapp.model.response.Profile_Model.ProfileImage;

@SuppressWarnings("unused")
public class Data {

    @SerializedName("aboutme")
    private String mAboutme;
    @SerializedName("address")
    private String mAddress;
    @SerializedName("age")
    private Long mAge;
    @SerializedName("city")
    private String mCity;
    @SerializedName("country")
    private String mCountry;
    @SerializedName("education")
    private String mEducation;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("email_verified_at")
    private String mEmailVerifiedAt;
    @SerializedName("employer")
    private String mEmployer;
    @SerializedName("eyecolor")
    private String mEyecolor;
    @SerializedName("gender")
    private Long mGender;
    @SerializedName("haircolor")
    private String mHaircolor;
    @SerializedName("hobbies")
    private String mHobbies;
    @SerializedName("id")
    private Long mId;
    @SerializedName("is_profile_complete")
    private Integer mIsProfileComplete;
    @SerializedName("is_restrict_fb_friends")
    private Long mIsRestrictFbFriends;
    @SerializedName("job")
    private String mJob;
    @SerializedName("locale")
    private String mLocale;
    @SerializedName("login_type")
    private Long mLoginType;
    @SerializedName("love_push_intention")
    private String mLovePushIntention;
    @SerializedName("name")
    private String mName;
    @SerializedName("partner_preference")
    private Long mPartnerPreference;
    @SerializedName("phone")
    private String mPhone;
    @SerializedName("phone_code")
    private String mPhoneCode;
    @SerializedName("physique")
    private Long mPhysique;
    @SerializedName("profile_image")
    private String mProfileImage;
    @SerializedName("profile_images")
    private List<ProfileImage> mProfileImages;
    @SerializedName("relationship")
    private Long mRelationship;
    @SerializedName("religion")
    private String mReligion;
    @SerializedName("sexual_orientation")
    private Long mSexualOrientation;
    @SerializedName("size")
    private String mSize;
    @SerializedName("social_id")
    private String mSocialId;
    @SerializedName("username")
    private String mUsername;
    @SerializedName("verification_code")
    private String mVerificationCode;
    @SerializedName("myReferalCode")
    @Expose
    private String myReferalCode;

    @SerializedName("personality_test")
    @Expose
    private PersonalityTest personalityTest;

    public String getQuickblox_id() {
        return quickblox_id;
    }

    public void setQuickblox_id(String quickblox_id) {
        this.quickblox_id = quickblox_id;
    }

    @SerializedName("quickblox_id")
    @Expose
    private String quickblox_id;


    public String getAboutme() {
        return mAboutme;
    }

    public void setAboutme(String aboutme) {
        mAboutme = aboutme;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public Long getAge() {
        return mAge;
    }

    public void setAge(Long age) {
        mAge = age;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public String getEducation() {
        return mEducation;
    }

    public void setEducation(String education) {
        mEducation = education;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getEmailVerifiedAt() {
        return mEmailVerifiedAt;
    }

    public void setEmailVerifiedAt(String emailVerifiedAt) {
        mEmailVerifiedAt = emailVerifiedAt;
    }

    public String getEmployer() {
        return mEmployer;
    }

    public void setEmployer(String employer) {
        mEmployer = employer;
    }

    public String getEyecolor() {
        return mEyecolor;
    }

    public void setEyecolor(String eyecolor) {
        mEyecolor = eyecolor;
    }

    public Long getGender() {
        return mGender;
    }

    public void setGender(Long gender) {
        mGender = gender;
    }

    public String getHaircolor() {
        return mHaircolor;
    }

    public void setHaircolor(String haircolor) {
        mHaircolor = haircolor;
    }

    public String getHobbies() {
        return mHobbies;
    }

    public void setHobbies(String hobbies) {
        mHobbies = hobbies;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public Integer getIsProfileComplete() {
        return mIsProfileComplete;
    }

    public void setIsProfileComplete(Integer isProfileComplete) {
        mIsProfileComplete = isProfileComplete;
    }

    public Long getIsRestrictFbFriends() {
        return mIsRestrictFbFriends;
    }

    public void setIsRestrictFbFriends(Long isRestrictFbFriends) {
        mIsRestrictFbFriends = isRestrictFbFriends;
    }

    public String getJob() {
        return mJob;
    }

    public void setJob(String job) {
        mJob = job;
    }

    public String getLocale() {
        return mLocale;
    }

    public void setLocale(String locale) {
        mLocale = locale;
    }

    public Long getLoginType() {
        return mLoginType;
    }

    public void setLoginType(Long loginType) {
        mLoginType = loginType;
    }

    public String getLovePushIntention() {
        return mLovePushIntention;
    }

    public void setLovePushIntention(String lovePushIntention) {
        mLovePushIntention = lovePushIntention;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Long getPartnerPreference() {
        return mPartnerPreference;
    }

    public void setPartnerPreference(Long partnerPreference) {
        mPartnerPreference = partnerPreference;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getPhoneCode() {
        return mPhoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        mPhoneCode = phoneCode;
    }

    public Long getPhysique() {
        return mPhysique;
    }

    public void setPhysique(Long physique) {
        mPhysique = physique;
    }

    public String getProfileImage() {
        return mProfileImage;
    }

    public void setProfileImage(String profileImage) {
        mProfileImage = profileImage;
    }

    public List<ProfileImage> getProfileImages() {
        return mProfileImages;
    }

    public void setProfileImages(List<ProfileImage> profileImages) {
        mProfileImages = profileImages;
    }

    public Long getRelationship() {
        return mRelationship;
    }

    public void setRelationship(Long relationship) {
        mRelationship = relationship;
    }

    public String getReligion() {
        return mReligion;
    }

    public void setReligion(String religion) {
        mReligion = religion;
    }

    public Long getSexualOrientation() {
        return mSexualOrientation;
    }

    public void setSexualOrientation(Long sexualOrientation) {
        mSexualOrientation = sexualOrientation;
    }

    public String getSize() {
        return mSize;
    }

    public void setSize(String size) {
        mSize = size;
    }

    public String getSocialId() {
        return mSocialId;
    }

    public void setSocialId(String socialId) {
        mSocialId = socialId;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getVerificationCode() {
        return mVerificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        mVerificationCode = verificationCode;
    }

    public String getMyReferalCode() {
        return myReferalCode;
    }

    public void setMyReferalCode(String myReferalCode) {
        this.myReferalCode = myReferalCode;
    }



    public PersonalityTest getPersonalityTest() {
        return personalityTest;
    }

    public void setPersonalityTest(PersonalityTest personalityTest) {
        this.personalityTest = personalityTest;
    }
}
