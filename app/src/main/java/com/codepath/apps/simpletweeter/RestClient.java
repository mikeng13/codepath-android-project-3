package com.codepath.apps.simpletweeter;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.apps.simpletweeter.enums.TweetType;
import com.codepath.apps.simpletweeter.models.Tweet;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class RestClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "g54iU9IPcQEquyZIxltwiQRBa";       // Change this
	public static final String REST_CONSUMER_SECRET = "scVFzknymb2S3AuemvSxPTQbWgSiffRwWWq5d1tU20QppINRyW"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://codepathtweets"; // Change this (here and in manifest)

	public RestClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
    public void getHomeTimeline(int page, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("page", String.valueOf(page));
        params.put("count", 25);
        getClient().get(apiUrl, params, handler);
    }

    public void getMentionsTimeline(int page, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("page", String.valueOf(page));
        params.put("count", 25);
        getClient().get(apiUrl, params, handler);
    }

    public void getUserTimeline(int page, String userId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("page", String.valueOf(page));
        params.put("count", 25);
        params.put("user_id", userId);
        getClient().get(apiUrl, params, handler);
    }

    public void postTweet(String body, String tweetId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", body);

        // include this for tweet replies
        if (tweetId != null) {
            params.put("in_reply_to_status_id", tweetId);
        }

        getClient().post(apiUrl, params, handler);
    }

    public void postRetweet(String tweetId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/retweet/" + tweetId + ".json");
        RequestParams params = new RequestParams();
        getClient().post(apiUrl,params, handler);
    }

    public void postFavoriteTweet(String tweetId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("favorites/create.json");
        RequestParams params = new RequestParams();
        params.put("id", tweetId);
        getClient().post(apiUrl, params, handler);
    }

    public void getUserProfile(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        RequestParams params = new RequestParams();
        params.put("skip_status", 1);
        getClient().get(apiUrl, params, handler);
    }

    public void getTimeline(int page, int type, String userId, AsyncHttpResponseHandler handler) {
        if (type == TweetType.HOME.ordinal()) {
            getHomeTimeline(page, handler);
        } else if (type == TweetType.MENTIONS.ordinal()) {
            getMentionsTimeline(page, handler);
        } else if (type == TweetType.USER.ordinal()) {
            getUserTimeline(page, userId, handler);
        }
    }

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */

}