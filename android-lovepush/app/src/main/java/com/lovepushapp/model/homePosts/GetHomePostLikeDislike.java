package com.lovepushapp.model.homePosts;

public class GetHomePostLikeDislike {
    private Integer status;
    private Data data;
    private String message;

    public Integer getStatus() {
    return status;
    }

    public void setStatus(Integer status) {
    this.status = status;
    }

    public Data getData() {
    return data;
    }

    public void setData(Data data) {
    this.data = data;
    }

    public String getMessage() {
    return message;
    }

    public void setMessage(String message) {
    this.message = message;
    }

    public class Data {

        private Integer id;
        private Integer post_id;
        private Integer user_id;
        private Integer status;
        private String created_at;
        private String updated_at;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getPostId() {
            return post_id;
        }

        public void setPostId(Integer postId) {
            this.post_id = postId;
        }

        public Integer getUserId() {
            return user_id;
        }

        public void setUserId(Integer userId) {
            this.user_id = userId;
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

    }

}