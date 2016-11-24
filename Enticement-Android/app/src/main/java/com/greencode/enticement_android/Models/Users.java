package com.greencode.enticement_android.Models;

import java.io.Serializable;

/**
 * Created by Hung Nguyen on 11/24/2016.
 */

public class Users {

    public static class User implements Serializable {
        String id;
        String name;
        String imageURL;

        public User(String id, String name, String imageURL) {
            this.id = id;
            this.name = name;
            this.imageURL = imageURL;
        }

        public String getImageURL() {
            return imageURL;
        }

        public void setImageURL(String imageURL) {
            this.imageURL = imageURL;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }
}
