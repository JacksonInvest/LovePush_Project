
package com.lovepushapp.model.searchUsers;

import java.util.List;

public class GetSearchUsersResponse {


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

        private Integer current_page;
        private List<searchDatum> data = null;
        private String first_page_url;
        private Integer from;
        private Integer last_page;
        private String last_page_url;
        private String next_page_url;
        private String path;
        private Integer per_page;
        private String prev_page_url;
        private Integer to;
        private Integer total;

        public Integer getCurrentPage() {
            return current_page;
        }

        public void setCurrentPage(Integer currentPage) {
            this.current_page = currentPage;
        }

        public List<searchDatum> getData() {
            return data;
        }

        public void setData(List<searchDatum> data) {
            this.data = data;
        }

        public String getFirstPageUrl() {
            return first_page_url;
        }

        public void setFirstPageUrl(String firstPageUrl) {
            this.first_page_url = firstPageUrl;
        }

        public Integer getFrom() {
            return from;
        }

        public void setFrom(Integer from) {
            this.from = from;
        }

        public Integer getLastPage() {
            return last_page;
        }

        public void setLastPage(Integer lastPage) {
            this.last_page = lastPage;
        }

        public String getLastPageUrl() {
            return last_page_url;
        }

        public void setLastPageUrl(String lastPageUrl) {
            this.last_page_url = lastPageUrl;
        }

        public String getNextPageUrl() {
            return next_page_url;
        }

        public void setNextPageUrl(String nextPageUrl) {
            this.next_page_url = nextPageUrl;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Integer getPerPage() {
            return per_page;
        }

        public void setPerPage(Integer perPage) {
            this.per_page = perPage;
        }

        public String getPrevPageUrl() {
            return prev_page_url;
        }

        public void setPrevPageUrl(String prevPageUrl) {
            this.prev_page_url = prevPageUrl;
        }

        public Integer getTo() {
            return to;
        }

        public void setTo(Integer to) {
            this.to = to;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public class searchDatum {

            private Integer id;
            private String name;
            private String username;
            private String email;
            private String profile_image;
            private Integer gender;
            private Integer age;
            private String quickblox_id;
            private Double distance;
            private List<ProfileImage> profile_images = null;
            private List<MatchInfo1> match_info1 = null;
            private List<MatchInfo2> match_info2 = null;

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

            public String getProfileImage() {
                return profile_image;
            }

            public void setProfileImage(String profileImage) {
                this.profile_image = profileImage;
            }

            public Integer getGender() {
                return gender;
            }

            public void setGender(Integer gender) {
                this.gender = gender;
            }

            public Integer getAge() {
                return age;
            }

            public void setAge(Integer age) {
                this.age = age;
            }

            public String getQuickbloxId() {
                return quickblox_id;
            }

            public void setQuickbloxId(String quickbloxId) {
                this.quickblox_id = quickbloxId;
            }

            public Double getDistance() {
                return distance;
            }

            public void setDistance(Double distance) {
                this.distance = distance;
            }

            public List<ProfileImage> getProfileImages() {
                return profile_images;
            }

            public void setProfileImages(List<ProfileImage> profileImages) {
                this.profile_images = profileImages;
            }

            public List<MatchInfo1> getMatchInfo1() {
                return match_info1;
            }

            public void setMatchInfo1(List<MatchInfo1> matchInfo1) {
                this.match_info1 = matchInfo1;
            }

            public List<MatchInfo2> getMatchInfo2() {
                return match_info2;
            }

            public void setMatchInfo2(List<MatchInfo2> matchInfo2) {
                this.match_info2 = matchInfo2;
            }

            public class ProfileImage {

                private Integer id;
                private Integer user_id;
                private String image;
                private String created_at;
                private String updated_at;

                public Integer getId() {
                    return id;
                }

                public void setId(Integer id) {
                    this.id = id;
                }

                public Integer getUserId() {
                    return user_id;
                }

                public void setUserId(Integer userId) {
                    this.user_id = userId;
                }

                public String getImage() {
                    return image;
                }

                public void setImage(String image) {
                    this.image = image;
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

            public class MatchInfo1 {

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

                public class QbChatDialog {


                    private Integer id;
                    private Integer match_id;
                    private String qb_dialog_id;
                    private Integer chat_status;
                    private String created_at;
                    private String updated_at;
                    public Integer getId() {
                        return id;
                    }
                    public void setId(Integer id) {
                        this.id = id;
                    }
                    public Integer getMatchId() {
                        return match_id;
                    }
                    public void setMatchId(Integer matchId) {
                        this.match_id = matchId;
                    }
                    public String getQbDialogId() {
                        return qb_dialog_id;
                    }
                    public void setQbDialogId(String qbDialogId) {
                        this.qb_dialog_id = qbDialogId;
                    }
                    public Integer getChatStatus() {
                        return chat_status;
                    }
                    public void setChatStatus(Integer chatStatus) {
                        this.chat_status = chatStatus;
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

                public class QbChatDialog {


                    private Integer id;
                    private Integer match_id;
                    private String qb_dialog_id;
                    private Integer chat_status;
                    private String created_at;
                    private String updated_at;
                    public Integer getId() {
                        return id;
                    }
                    public void setId(Integer id) {
                        this.id = id;
                    }
                    public Integer getMatchId() {
                        return match_id;
                    }
                    public void setMatchId(Integer matchId) {
                        this.match_id = matchId;
                    }
                    public String getQbDialogId() {
                        return qb_dialog_id;
                    }
                    public void setQbDialogId(String qbDialogId) {
                        this.qb_dialog_id = qbDialogId;
                    }
                    public Integer getChatStatus() {
                        return chat_status;
                    }
                    public void setChatStatus(Integer chatStatus) {
                        this.chat_status = chatStatus;
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

        }
    }
}
