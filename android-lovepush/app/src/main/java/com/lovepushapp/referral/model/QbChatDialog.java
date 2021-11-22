package com.lovepushapp.referral.model;

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

    public Integer getMatch_id() {
        return match_id;
    }

    public void setMatch_id(Integer match_id) {
        this.match_id = match_id;
    }

    public String getQb_dialog_id() {
        return qb_dialog_id;
    }

    public void setQb_dialog_id(String qb_dialog_id) {
        this.qb_dialog_id = qb_dialog_id;
    }

    public Integer getChat_status() {
        return chat_status;
    }

    public void setChat_status(Integer chat_status) {
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