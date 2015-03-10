package com.codepath.apps.simpletweeter.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.simpletweeter.R;
import com.codepath.apps.simpletweeter.RestApplication;
import com.codepath.apps.simpletweeter.RestClient;
import com.codepath.apps.simpletweeter.models.Tweet;
import com.codepath.apps.simpletweeter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class PostTweetActivity extends ActionBarActivity implements TextWatcher{
    private final int MAX_CHARACTERS = 140;

    private User user;
    private TextView tvUserName;
    private TextView tvScreenName;
    private ImageView ivUserProfileImage;
    private EditText etComposeTweet;
    private MenuItem miCharactersLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_tweet);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user = getIntent().getParcelableExtra("user");
        tvUserName = (TextView)findViewById(R.id.tvUserName);
        tvUserName.setText(user.name);
        tvScreenName = (TextView)findViewById(R.id.tvScreeName);
        tvScreenName.setText("@" + user.screenName);
        ivUserProfileImage = (ImageView)findViewById(R.id.ivUserProfileImage);
        Picasso.with(this)
                .load(user.profileImageUrl)
                .into(ivUserProfileImage);

        etComposeTweet = (EditText)findViewById(R.id.etComposeTweet);
        etComposeTweet.addTextChangedListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_tweet, menu);
        miCharactersLeft = menu.findItem(R.id.action_characters_left);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_post_tweet) {
            EditText etComposeTweet = (EditText)findViewById(R.id.etComposeTweet);
            String tweetBody = etComposeTweet.getText().toString();
            if (tweetBody.length() == 0) {
                Toast.makeText(this, "Please include a message in your tweet!", Toast.LENGTH_SHORT);
            }
            RestClient client = RestApplication.getRestClient();
            client.postTweet(tweetBody, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // make tweet object and pass back to timeline to add to it
                    Tweet tweet = new Tweet(response);
                    tweet.save();

                    // Prepare data intent
                    Intent data = new Intent();
                    data.putExtra("tweet", tweet);
                    setResult(RESULT_OK, data);
                    finish();
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int characters = s.toString().length();
        int charactersLeft = MAX_CHARACTERS - characters;
        miCharactersLeft.setTitle(String.valueOf(charactersLeft));
    }
}
