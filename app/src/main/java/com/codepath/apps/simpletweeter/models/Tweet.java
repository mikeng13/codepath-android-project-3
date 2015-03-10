package com.codepath.apps.simpletweeter.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mng on 3/6/15.
 */
@Table(name = "Tweets")
public class Tweet extends Model implements Parcelable {
    // Define database columns and associated fields
    @Column(name = "remoteId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String id;
    @Column(name = "userName")
    public String userName;
    @Column(name = "userHandle")
    public String userHandle;
    @Column(name = "userProfileImage")
    public String userProfileImage;
    @Column(name = "timeCreated")
    public String timeCreated;
    @Column(name = "text")
    public String text;
    @Column(name = "retweetCount")
    public int retweetCount;
    @Column(name = "favoriteCount")
    public int favoriteCount;

    // Make sure to always define this constructor with no arguments
    public Tweet() {
        super();
    }

    // Add a constructor that creates an object from the JSON response
    public Tweet(JSONObject object){
        super();

        try {
            this.id = object.getString("id_str");
            JSONObject userJSONObject = object.getJSONObject("user");
            this.userName = userJSONObject.getString("name");
            this.userHandle = "@" + userJSONObject.getString("screen_name");
            this.userProfileImage = userJSONObject.getString("profile_image_url");
            this.timeCreated = object.getString("created_at");
            this.text = object.getString("text");
            this.retweetCount = object.getInt("retweet_count");
            this.favoriteCount = object.getInt("favorite_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Tweet> fromJSON(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = new Tweet(tweetJson);
            tweet.save();
            tweets.add(tweet);
        }

        return tweets;
    }

    public static List<Tweet> getAll() {
        return new Select()
                .from(Tweet.class)
                .execute();
    }

    public static void deleteAll() {
        new Delete()
                .from(Tweet.class)
                .execute();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(userHandle);
        dest.writeString(userProfileImage);
        dest.writeString(timeCreated);
        dest.writeString(text);
        dest.writeInt(retweetCount);
        dest.writeInt(favoriteCount);
    }

    public static final Parcelable.Creator<Tweet> CREATOR
            = new Parcelable.Creator<Tweet>() {
        @Override
        public Tweet createFromParcel(Parcel in) {
            return new Tweet(in);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };

    private Tweet(Parcel in) {
        userName = in.readString();
        userHandle = in.readString();
        userProfileImage = in.readString();
        timeCreated = in.readString();
        text = in.readString();
        retweetCount = in.readInt();
        favoriteCount = in.readInt();
    }
}
