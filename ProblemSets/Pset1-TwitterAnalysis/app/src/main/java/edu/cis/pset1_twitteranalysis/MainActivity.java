package edu.cis.pset1_twitteranalysis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;

import edu.cis.pset1_twitteranalysis.twitter.TwitterController;
import twitter4j.TwitterException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //This is not great, for extra credit you can fix this so that network calls happen on a different thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //TODO 1: Tweet something!
//        TwitterController myC = new TwitterController(this);
        try {
            TwitterController myC = new TwitterController(this);
            try {
                myC.getRecommendations();
            } catch (TwitterException e) {
                System.out.println(ConsoleColors.PURPLE + "BigProblems" + ConsoleColors.RESET);
                e.printStackTrace();

            }
        } catch (TwitterException e) {
            System.out.println(e.getMessage());
            System.out.println(ConsoleColors.PURPLE + "EvenBiggerProblems" + ConsoleColors.RESET);
            e.printStackTrace();
        }

//        myC.postTweet(this.getRecommendations); //this will tweet to your account
    }
}