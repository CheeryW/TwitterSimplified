package com.codepath.apps.restclienttemplate;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private RecyclerView rvTweets;
    private List<Tweet> tweets;
    private TweetsAdaptor adaptor;

    private SwipeRefreshLayout swipeContainer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient(this);
        swipeContainer = findViewById(R.id.swipeContainer);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        // Find the recycler view
        rvTweets = findViewById(R.id.rvTweets);
        // Initialize a list of tweets and adapter from the data source
        tweets = new ArrayList<>();
        adaptor = new TweetsAdaptor(this, tweets);
        // RecyclerView set up: layout manager and adapter
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(adaptor);

        populateHomeTimeline();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("TwitterClient", "content is being refreshed");
                populateHomeTimeline();
            }
        });
    }

    private void populateHomeTimeline() {
        client.getTwitterTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Iterate through the list of tweets
                List<Tweet> tweetsToAdd = new ArrayList<>();
                for(int i = 0; i < response.length(); i++) {
                    try {
                        // Convert each JsonObject into a Tweet object
                        JSONObject jsonObject = response.getJSONObject(i);
                        Tweet tweet = Tweet.fromJson(jsonObject);
                        // Add the tweet into our data source
                        tweetsToAdd.add(tweet);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("TwitterClient", tweetsToAdd.toString());
                // Clear the existing data
                adaptor.clear();
                // Show the data we just received
                adaptor.addTweets(tweetsToAdd);
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("TwitterClient", responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("TwitterClient", errorResponse.toString());
            }
        });
    }
}
