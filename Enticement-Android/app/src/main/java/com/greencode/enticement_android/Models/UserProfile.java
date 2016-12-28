package com.greencode.enticement_android.Models;

import java.io.Serializable;

/**
 * Created by Hung Nguyen on 12/27/2016.
 */

public class UserProfile implements Serializable {
    String id = null;
    String email;
    String name;
    String nickname;
    String birthday;
    String created_at;
    String profile_url;

    public UserProfile() {

    }

    public UserProfile(String id, String name, String imageURL) {
        this.id = id;
        this.name = name;
        this.profile_url = imageURL;
    }

    public String getImageURL() {
        return profile_url;
    }

    public void setImageURL(String imageURL) {
        this.profile_url = imageURL;
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

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

}
