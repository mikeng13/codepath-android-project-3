package com.codepath.apps.simpletweeter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletweeter.R;
import com.codepath.apps.simpletweeter.helpers.ParseDateHelper;
import com.codepath.apps.simpletweeter.models.Tweet;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by mng on 3/6/15.
 */
public class TweetsAdapter extends ArrayAdapter<Tweet> {

    private static class ViewHolder {
        ImageView ivUserImage;
        TextView tvUserName;
        TextView tvUserHandler;
        TextView tvDescription;
        TextView tvTimestamp;
        TextView tvRetweetCount;
        TextView tvFavoriteCount;
    }

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        Tweet tweet = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ivUserImage = (ImageView)convertView.findViewById(R.id.ivUserImage);
            viewHolder.tvUserName = (TextView)convertView.findViewById(R.id.tvUserName);
            viewHolder.tvUserHandler = (TextView)convertView.findViewById(R.id.tvUserHandle);
            viewHolder.tvDescription = (TextView)convertView.findViewById(R.id.tvDescription);
            viewHolder.tvTimestamp = (TextView)convertView.findViewById(R.id.tvTimestamp);
            viewHolder.tvRetweetCount = (TextView)convertView.findViewById(R.id.tvRetweetCount);
            viewHolder.tvFavoriteCount = (TextView)convertView.findViewById(R.id.tvFavoriteCount);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.tvUserName.setText(tweet.userName);
        viewHolder.tvUserHandler.setText(tweet.userHandle);
        viewHolder.tvDescription.setText(tweet.text);
        viewHolder.tvTimestamp.setText(ParseDateHelper.getRelativeTimeAgo(tweet.timeCreated));
        viewHolder.tvRetweetCount.setText(String.valueOf(tweet.retweetCount));
        viewHolder.tvFavoriteCount.setText(String.valueOf(tweet.favoriteCount));
        Picasso.with(getContext())
                .load(tweet.userProfileImage)
                .into(viewHolder.ivUserImage);

        return convertView;
    }
}
