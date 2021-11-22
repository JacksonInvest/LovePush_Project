package com.lovepushapp.model.response;

import java.util.List;

public class NotificationResponse {


    Integer status;
    String message;

    public Integer getNoti_count() {
        return noti_count;
    }

    public void setNoti_count(Integer noti_count) {
        this.noti_count = noti_count;
    }

    Integer noti_count;
    List<Data> data;



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public class Data{
        Userinfo user_info;

//        "id": 35,
//      "event_type": "S_M_R",
//      "event_id": 1,
//      "user_id": 3,
//      "to_user_id": 10,
//      "created_at": "2019-06-19 12:53:25",
//      "updated_at": "2019-06-19 12:53:25",
String id;
        String event_type;
        String event_id;
        String to_user_id;
        String created_at;
        String updated_at;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        String status;


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

        public String getEvent_id() {
            return event_id;
        }

        public void setEvent_id(String event_id) {
            this.event_id = event_id;
        }

        public String getTo_user_id() {
            return to_user_id;
        }

        public void setTo_user_id(String to_user_id) {
            this.to_user_id = to_user_id;
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

        public class Userinfo{

            private Integer id;
            private String name;
            private String email;
            private String profile_image;
            private String quickblox_id;
            private String receive_notification;

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

            public String getQuickbloxId() {
                return quickblox_id;
            }

            public void setQuickbloxId(String quickbloxId) {
                this.quickblox_id = quickbloxId;
            }

            public String getReceiveNotification() {
                return receive_notification;
            }

            public void setReceiveNotification(String receiveNotification) {
                this.receive_notification = receiveNotification;
            }
        }
        EventInfo event_info;

        public Userinfo getUser_info() {
            return user_info;
        }

        public void setUser_info(Userinfo user_info) {
            this.user_info = user_info;
        }

        public EventInfo getEvent_info() {
            return event_info;
        }

        public void setEvent_info(EventInfo event_info) {
            this.event_info = event_info;
        }

        public NotificationMessage getNotification_message() {
            return notification_message;
        }

        public void setNotification_message(NotificationMessage notification_message) {
            this.notification_message = notification_message;
        }

        public class EventInfo{
            String id;
            String from_user_id;
            String to_user_id;
            String status;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getFrom_user_id() {
                return from_user_id;
            }

            public void setFrom_user_id(String from_user_id) {
                this.from_user_id = from_user_id;
            }

            public String getTo_user_id() {
                return to_user_id;
            }

            public void setTo_user_id(String to_user_id) {
                this.to_user_id = to_user_id;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
        NotificationMessage notification_message;
        public class NotificationMessage{
            String notification_id;
            String message;

            public String getNotification_id() {
                return notification_id;
            }

            public void setNotification_id(String notification_id) {
                this.notification_id = notification_id;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }
        }
    }
}
