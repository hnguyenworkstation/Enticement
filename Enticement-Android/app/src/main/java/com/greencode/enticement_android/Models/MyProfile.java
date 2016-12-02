package com.greencode.enticement_android.Models;

/**
 * Created by Hung Nguyen on 12/1/2016.
 */

public class MyProfile {

    String id = null;
    String email;
    String name;
    String nickname;
    String birthday;
    String created_at;

    public MyProfile() {

    }

    public MyProfile(String id, String name, String nickname, String created_at, String birthday) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.created_at = created_at;
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
