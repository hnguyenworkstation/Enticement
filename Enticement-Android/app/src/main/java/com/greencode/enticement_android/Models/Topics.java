package com.greencode.enticement_android.Models;

import com.greencode.enticement_android.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hung Nguyen on 11/18/2016.
 */

public class Topics {
    public static final List<Topic> TOPICS = new ArrayList<Topic>();

    private static final int[] listIcon = new int[] {
            R.drawable.ic_active_send
    } ;

    private static final String[] listTitle = new String[] {
            "Traveling"
    };

    static {
        for (int i = 0; i < listIcon.length; i++ )
            TOPICS.add(new Topic(i, listIcon[i], listTitle[i]));
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class Topic {
        public final int id;
        public final int logo;
        public final String details;

        public Topic(int id, int logo, String details) {
            this.id = id;
            this.logo = logo;
            this.details = details;
        }

        @Override
        public String toString() {
            return details;
        }
    }
}
