package com.codepath.apps.simpletweeter.activities;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.apps.simpletweeter.R;
import com.codepath.apps.simpletweeter.RestApplication;
import com.codepath.apps.simpletweeter.RestClient;
import com.codepath.apps.simpletweeter.activities.PostTweetActivity;
import com.codepath.apps.simpletweeter.adapters.TweetsAdapter;
import com.codepath.apps.simpletweeter.helpers.EndlessScrollListener;
import com.codepath.apps.simpletweeter.models.Tweet;
import com.codepath.apps.simpletweeter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TimelineActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{
    private static final int POST_TWEET_REQUEST_CODE = 341;

    private User user;
    private List<Tweet> tweets;
    private TweetsAdapter tweetsAdapter;
    private ListView lvTweets;
    private RestClient client;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // add tweeter icon to actionbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.tweeter_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        client = RestApplication.getRestClient();
        getUserProfile();

        // check if we have any saved tweets before making a fetch
        tweets = Tweet.getAll();
        if (tweets.size() == 0) {
            populateTimeline(1);
        }

        tweetsAdapter = new TweetsAdapter(this, tweets);
        lvTweets = (ListView)findViewById(R.id.lvTweets);
        lvTweets.setAdapter(tweetsAdapter);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                populateTimeline(page);
            }
        });
        lvTweets.setOnItemClickListener(this);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateTimeline(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void populateTimeline(final int page) {
        client.getHomeTimeline(page, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                if (page == 0) {
                    tweetsAdapter.clear();
                }
                Log.d("DEBUG", "timeline: " + jsonArray.toString());
                // Load json array into model classes
                tweetsAdapter.addAll(Tweet.fromJSON(jsonArray));
                tweetsAdapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    private void getUserProfile() {
        client.getUserProfile(new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                Log.d("DEBUG", "user: " + jsonObject.toString());
                user = new User(jsonObject);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_tweet) {
            Intent i = new Intent(this, PostTweetActivity.class);
            i.putExtra("user", user);
            startActivityForResult(i, POST_TWEET_REQUEST_CODE);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == POST_TWEET_REQUEST_CODE) {
            Tweet tweet = data.getParcelableExtra("tweet");
            tweetsAdapter.insert(tweet, 0);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Tweet tweet = tweetsAdapter.getItem(position);

        Intent i = new Intent(this, TweetDetailActivity.class);
        i.putExtra("tweet", tweet);
        startActivity(i);
    }
}
