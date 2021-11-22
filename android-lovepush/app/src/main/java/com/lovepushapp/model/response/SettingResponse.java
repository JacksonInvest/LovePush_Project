package com.lovepushapp.model.response;

public class SettingResponse {
   /* {
        "status": true,
            "message": "Success",
            "settings": {
        "hide_from_search": "0",
                "receive_like_request": "0",
                "receive_chat_request": "0",
                "receive_notification": "0"
    }
    }*/
   String status;
   String message;

   Settings settings;

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

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public  class Settings{
       Integer hide_from_search;
       Integer receive_like_request;
       Integer receive_chat_request;
       Integer receive_notification;

       public Integer getHide_from_search() {
           return hide_from_search;
       }

       public void setHide_from_search(Integer hide_from_search) {
           this.hide_from_search = hide_from_search;
       }

       public Integer getReceive_like_request() {
           return receive_like_request;
       }

       public void setReceive_like_request(Integer receive_like_request) {
           this.receive_like_request = receive_like_request;
       }

       public Integer getReceive_chat_request() {
           return receive_chat_request;
       }

       public void setReceive_chat_request(Integer receive_chat_request) {
           this.receive_chat_request = receive_chat_request;
       }

       public Integer getReceive_notification() {
           return receive_notification;
       }

       public void setReceive_notification(Integer receive_notification) {
           this.receive_notification = receive_notification;
       }
   }
}
