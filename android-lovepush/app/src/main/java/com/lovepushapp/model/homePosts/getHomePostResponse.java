package com.lovepushapp.model.homePosts;

import java.util.List;

public class getHomePostResponse {

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
        private List<Datum> data = null;
        private String first_page_url;
        private Integer from;
        private Integer last_page;
        private String last_page_url;
        private String next_page_url;
        private String path;
        private Integer per_page;
        private Integer to;
        private Integer total;

        public Integer getCurrentPage() {
            return current_page;
        }

        public void setCurrentPage(Integer currentPage) {
            this.current_page = currentPage;
        }

        public List<Datum> getData() {
            return data;
        }

        public void setData(List<Datum> data) {
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

        public class Datum  {
            private Integer id;
            private Integer user_id;
            private String title;
            private String description;
            private String image;
            private Integer is_post_anonymously;
            private Integer status;
            private String created_at;
            private String updated_at;
            private Integer post_like_count;
            private Integer match_id;
            private MatchQbID matchQbID;
            private UserInfo user_info;
            private Integer isLiked;
            //private List<Object> post_like = null;
            String deletedBy;
            String blockedBy;




            private MatchInfo matchInfo;

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

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public Integer getIsPostAnonymously() {
                return is_post_anonymously;
            }

            public void setIsPostAnonymously(Integer isPostAnonymously) {
                this.is_post_anonymously = isPostAnonymously;
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

            public Integer getPostLikeCount() {
                return post_like_count;
            }

            public void setPostLikeCount(Integer postLikeCount) {
                this.post_like_count = postLikeCount;
            }
            public String getDeletedBy() {
                return deletedBy;
            }

            public void setDeletedBy(String deletedBy) {
                this.deletedBy = deletedBy;
            }

            public String getBlockedBy() {
                return blockedBy;
            }


            public void setBlockedBy(String blockedBy) {
                this.blockedBy = blockedBy;
            }
            public Integer getMatchId() {
                return match_id;
            }

            public void setMatchId(Integer matchId) {
                this.match_id = matchId;
            }

            public MatchQbID getMatchQbID() {
                return matchQbID;
            }
            public MatchInfo getMatchInfo() {
                return matchInfo;
            }

            public void setMatchInfo(MatchInfo matchInfo) {
                this.matchInfo = matchInfo;
            }
            public void setMatchQbID(MatchQbID matchQbID) {
                this.matchQbID = matchQbID;
            }

            public UserInfo getUserInfo() {
                return user_info;
            }

            public void setUserInfo(UserInfo userInfo) {
                this.user_info = userInfo;
            }

           /* public List<Object> getPostLike() {
                return post_like;
            }

            public void setPostLike(List<Object> postLike) {
                this.post_like = postLike;
            }*/

            public Integer getIsLiked() {
                return isLiked;
            }

            public void setIsLiked(Integer isLiked) {
                this.isLiked = isLiked;
            }

            public class UserInfo {

                private Integer id;
                private String email;
                private String name;
                private String username;
                private String profile_image;
                private String quickblox_id;
                String receive_notification;

                public Integer getId() {
                    return id;
                }

                public void setId(Integer id) {
                    this.id = id;
                }

                public String getEmail() {
                    return email;
                }

                public void setEmail(String email) {
                    this.email = email;
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

                public String getProfileImage() {
                    return profile_image;
                }

                public void setProfileImage(String profileImage) {
                    this.profile_image = profileImage;
                }

                public String getQuickbloxId() {
                    return quickblox_id;
                }

                public void setQuickbloxId(String quickbloxId) {
                    this.quickblox_id = quickbloxId;
                }
                public String getReceive_notification() {
                    return receive_notification;
                }

                public void setReceive_notification(String receive_notification) {
                    this.receive_notification = receive_notification;
                }
            }

            public class MatchQbID {

                private Integer id;
                private Integer match_id;
                private String qb_dialog_id;
                private Integer chat_status;
                private String created_at;
                private String updated_at;
                private MatchInfo match_info;

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

                public MatchInfo getMatchInfo() {
                    return match_info;
                }

                public void setMatchInfo(MatchInfo matchInfo) {
                    this.match_info = matchInfo;
                }


                public class MatchInfo {

                    private Integer id;
                    private String event_type;

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

                }

            }

            public class MatchInfo {
                private Integer id;
                private String event_type;
                private Integer user_id;
                private Integer like_by_me;
                private Integer status;
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



            }

        }

    }
}
