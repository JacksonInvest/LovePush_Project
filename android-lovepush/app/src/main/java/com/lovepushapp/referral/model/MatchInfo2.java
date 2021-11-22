package com.lovepushapp.referral.model;

public class MatchInfo2 {
                
        private Integer id;
        private String event_type;
        private Integer user_id;
        private Integer like_by_me;
        private Integer status;
        private String created_at;
        private String updated_at;
        private QbChatDialog qb_chat_dialog;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getEventType() {
            return event_type;
        }

        public void setEventType(String eventType) {
            this.event_type = eventType;
        }

        public Integer getUserId() {
            return user_id;
        }

        public void setUserId(Integer userId) {
            this.user_id = userId;
        }

        public Integer getLikeByMe() {
            return like_by_me;
        }

        public void setLikeByMe(Integer likeByMe) {
            this.like_by_me = likeByMe;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getCreatedAt() {
            return created_at;
        }

        public void setCreatedAt(String createdAt) {
            this.created_at = createdAt;
        }

        public String getUpdatedAt() {
            return updated_at;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updated_at = updatedAt;
        }

        public QbChatDialog getQbChatDialog() {
            return qb_chat_dialog;
        }

        public void setQbChatDialog(QbChatDialog qbChatDialog) {
            this.qb_chat_dialog = qbChatDialog;
        }

    }