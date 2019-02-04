package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetsAdaptor extends RecyclerView.Adapter<TweetsAdaptor.ViewHolder> {
    private Context context;
    private List<Tweet> tweets;

    // Pass in context and list of tweets
    public TweetsAdaptor(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    // for each row (tweet), inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    // bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Tweet tweet = tweets.get(i);
        viewHolder.tvScreenName.setText(tweet.getUser().getScreenName());
        viewHolder.tvBody.setText(tweet.getBody());
        Log.d("Profile_image", "inside bind " + tweet.getUser().getProfileImageUrl());
        Glide.with(context).load(tweet.getUser().getProfileImageUrl()).into(viewHolder.ivProfileImage);
        Log.d("Profile_image", "Should've displayed image " + tweet.getUser().getProfileImageUrl());

    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addTweets(List<Tweet> tweetList) {
        tweets.addAll(tweetList);
        notifyDataSetChanged();
    }

    // Define the Viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivProfileImage;
        public TextView tvScreenName;
        public TextView tvBody;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvBody = itemView.findViewById(R.id.tvBody);

        }
    }
}
