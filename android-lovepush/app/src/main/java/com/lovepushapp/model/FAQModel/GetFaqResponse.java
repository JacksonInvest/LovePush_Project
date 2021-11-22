package com.lovepushapp.model.FAQModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetFaqResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("faq")
    @Expose
    private List<Faq> faq = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Faq> getFaq() {
        return faq;
    }

    public void setFaq(List<Faq> faq) {
        this.faq = faq;
    }

    public class Faq {

        @SerializedName("category_name")
        @Expose
        private String categoryName;
        @SerializedName("qsn_list")
        @Expose
        private List<QsnList> qsnList = null;

        boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }




        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public List<QsnList> getQsnList() {
            return qsnList;
        }

        public void setQsnList(List<QsnList> qsnList) {
            this.qsnList = qsnList;
        }

        public class QsnList {

            @SerializedName("id")
            @Expose
            private Integer id;
            @SerializedName("language_id")
            @Expose
            private Integer languageId;
            @SerializedName("ques_type")
            @Expose
            private String quesType;
            @SerializedName("question")
            @Expose
            private String question;
            @SerializedName("answer")
            @Expose
            private String answer;
            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("created_at")
            @Expose
            private String createdAt;
            @SerializedName("updated_at")
            @Expose
            private String updatedAt;

            public boolean isShowAns() {
                return isShowAns;
            }

            public void setShowAns(boolean showAns) {
                isShowAns = showAns;
            }

            boolean isShowAns;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public Integer getLanguageId() {
                return languageId;
            }

            public void setLanguageId(Integer languageId) {
                this.languageId = languageId;
            }

            public String getQuesType() {
                return quesType;
            }

            public void setQuesType(String quesType) {
                this.quesType = quesType;
            }

            public String getQuestion() {
                return question;
            }

            public void setQuestion(String question) {
                this.question = question;
            }

            public String getAnswer() {
                return answer;
            }

            public void setAnswer(String answer) {
                this.answer = answer;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
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

        }

    }

}