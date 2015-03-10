package com.codepath.apps.simpletweeter.models;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mng on 3/7/15.
 */
public class User extends Model implements Parcelable {
    public String name;
    public String screenName;
    public String profileImageUrl;

    // Make sure to always define this constructor with no arguments
    public User() {
        super();
    }

    // Add a constructor that creates an object from the JSON response
    public User(JSONObject object) {
        super();

        try {
            this.name = object.getString("name");
            this.screenName = object.getString("screen_name");
            this.profileImageUrl = object.getString("profile_image_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(screenName);
        dest.writeString(profileImageUrl);
    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel in) {
        name = in.readString();
        screenName = in.readString();
        profileImageUrl = in.readString();
    }
}
