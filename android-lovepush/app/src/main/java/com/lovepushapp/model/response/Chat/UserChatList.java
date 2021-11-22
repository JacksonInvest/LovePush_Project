package com.lovepushapp.model.response.Chat;

import java.util.List;

public class UserChatList {
//     "status": true,
//  "message": "Match List.",
private String status;
    private String message;
    private List<Data> data;

    public Integer getRequests() {
        return requests;
    }

    public void setRequests(Integer requests) {
        this.requests = requests;
    }

    private Integer requests;


    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public class Data{






//                  "first_page_url": "http:\/\/14.141.175.109\/lovepush\/public\/api\/myMatches?page=1",
//      "from": 1,
//      "last_page": 1,
//      "last_page_url": "http:\/\/14.141.175.109\/lovepush\/public\/api\/myMatches?page=1",
//      "next_page_url": null,
//      "path": "http:\/\/14.141.175.109\/lovepush\/public\/api\/myMatches",
//      "per_page": 15,
//      "prev_page_url": null,
//      "to": 5,
//      "total": 5
//
//            String first_page_url;
//            String from;
//            String last_page;
//            String last_page_url;
//            String next_page_url;
//            String path;
//            String per_page;
//            String prev_page;
//            String to;
//            String total;


//            public String getFirst_page_url() {
//                return first_page_url;
//            }
//
//            public void setFirst_page_url(String first_page_url) {
//                this.first_page_url = first_page_url;
//            }
//
//            public String getFrom() {
//                return from;
//            }
//
//            public void setFrom(String from) {
//                this.from = from;
//            }
//
//            public String getLast_page() {
//                return last_page;
//            }
//
//            public void setLast_page(String last_page) {
//                this.last_page = last_page;
//            }
//
//            public String getLast_page_url() {
//                return last_page_url;
//            }
//
//            public void setLast_page_url(String last_page_url) {
//                this.last_page_url = last_page_url;
//            }
//
//            public String getNext_page_url() {
//                return next_page_url;
//            }
//
//            public void setNext_page_url(String next_page_url) {
//                this.next_page_url = next_page_url;
//            }
//
//            public String getPath() {
//                return path;
//            }
//
//            public void setPath(String path) {
//                this.path = path;
//            }
//
//            public String getPer_page() {
//                return per_page;
//            }
//
//            public void setPer_page(String per_page) {
//                this.per_page = per_page;
//            }
//
//            public String getPrev_page() {
//                return prev_page;
//            }
//
//            public void setPrev_page(String prev_page) {
//                this.prev_page = prev_page;
//            }
//
//            public String getTo() {
//                return to;
//            }
//
//            public void setTo(String to) {
//                this.to = to;
//            }
//
//            public String getTotal() {
//                return total;
//            }
//
//            public void setTotal(String total) {
//                this.total = total;
//            }
//
//            public String getCurrent_page() {
//                return current_page;
//            }
//
//            public void setCurrent_page(String current_page) {
//                this.current_page = current_page;
//            }




                //            "id": 5,
//        "event_type": "C",
//        "user_id": 2,
//        "like_by_me": 12,
//        "status": 1,
//        "created_at": "2019-06-19 13:51:22",
//        "updated_at": "2019-06-19 13:51:22",
                String id;
                String event_type;
                String user_id;
                String like_by_me;
                String status;
                String created_at;
                String updated_at;
                UserInfo1 user_info1;
                UserInfo2 user_info2;
                QbChatDialog qb_chat_dialog;
                Integer isDeleted,isBlocked;
                String deletedBy,blockedBy;



                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getEvent_type() {
                    return event_type;
                }

                public void setEvent_type(String event_type) {
                    this.event_type = event_type;
                }

                public String getUser_id() {
                    return user_id;
                }

                public void setUser_id(String user_id) {
                    this.user_id = user_id;
                }

                public String getLike_by_me() {
                    return like_by_me;
                }

                public void setLike_by_me(String like_by_me) {
                    this.like_by_me = like_by_me;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
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

                public Integer getIsDeleted() {
                    return isDeleted;
                }

                public void setIsDeleted(Integer isDeleted) {
                    this.isDeleted = isDeleted;
                }

                public Integer getIsBlocked() {
                    return isBlocked;
                }

                public void setIsBlocked(Integer isBlocked) {
                    this.isBlocked = isBlocked;
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
                public UserInfo1 getUser_info1() {
                    return user_info1;
                }

                public void setUser_info1(UserInfo1 user_info1) {
                    this.user_info1 = user_info1;
                }

                public UserInfo2 getUser_info2() {
                    return user_info2;
                }

                public void setUser_info2(UserInfo2 user_info2) {
                    this.user_info2 = user_info2;
                }

                public QbChatDialog getQbChatDialog() {
                    return qb_chat_dialog;
                }

                public void setQbChatDialog(QbChatDialog qb_chat_dialog) {
                    qb_chat_dialog = qb_chat_dialog;
                }

                public class UserInfo1 {
                    String id;
                    String name;
                    String email;
                    String profile_image;
                    String quickblox_id;
                    String receive_notification;
                    public String getReceive_notification() {
                        return receive_notification;
                    }

                    public void setReceive_notification(String receive_notification) {
                        this.receive_notification = receive_notification;
                    }

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getEmail() {
                        return email;
                    }

                    public void setEmail(String email) {
                        this.email = email;
                    }

                    public String getProfile_image() {
                        return profile_image;
                    }

                    public void setProfile_image(String profile_image) {
                        this.profile_image = profile_image;
                    }

                    public String getQuickblox_id() {
                        return quickblox_id;
                    }

                    public void setQuickblox_id(String quickblox_id) {
                        this.quickblox_id = quickblox_id;
                    }
                }

                public class UserInfo2 {
                    String id;
                    String name;
                    String email;
                    String profile_image;
                    String quickblox_id;
                    String receive_notification;

                    public String getReceive_notification() {
                        return receive_notification;
                    }

                    public void setReceive_notification(String receive_notification) {
                        this.receive_notification = receive_notification;
                    }

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getEmail() {
                        return email;
                    }

                    public void setEmail(String email) {
                        this.email = email;
                    }

                    public String getProfile_image() {
                        return profile_image;
                    }

                    public void setProfile_image(String profile_image) {
                        this.profile_image = profile_image;
                    }

                    public String getQuickblox_id() {
                        return quickblox_id;
                    }

                    public void setQuickblox_id(String quickblox_id) {
                        this.quickblox_id = quickblox_id;
                    }
                }

                public class QbChatDialog {
                     String id;
                     String match_id;
                     String qb_dialog_id;
                     String chat_status;
                     String created_at;
                     String updated_at;

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                    public String getMatch_id() {
                        return match_id;
                    }

                    public void setMatch_id(String match_id) {
                        this.match_id = match_id;
                    }

                    public String getQb_dialog_id() {
                        return qb_dialog_id;
                    }

                    public void setQb_dialog_id(String qb_dialog_id) {
                        this.qb_dialog_id = qb_dialog_id;
                    }

                    public String getChat_status() {
                        return chat_status;
                    }

                    public void setChat_status(String chat_status) {
                        this.chat_status = chat_status;
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
                }

        }


}
