package edu.cis.pset1_twitteranalysis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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
        ImageView birdA = (ImageView) findViewById(R.id.birdA);
        ImageView birdB = (ImageView) findViewById(R.id.birdB);
        Button start = (Button) findViewById(R.id.start);

        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                setContentView(R.layout.io);
            }
        });




        //TODO 1: Tweet something!
//        TwitterController myC = new TwitterController(this);
        try {
            TwitterController myC = new TwitterController(this);
            try{
                myC.findUserStats("cs_cis");
//                myC.postTweet("yes i am just bad "); //this will tweet to your account
            } catch (Exception e) {
                System.out.println("problem in finding handle");
                e.printStackTrace();
            }
            try {
                myC.getRecommendations();
//                System.out.println(ConsoleColors.GREEN + "Got Recommendation" + ConsoleColors.RESET);
            } catch (TwitterException e) {
                System.out.println(ConsoleColors.PURPLE + "BigProblems" + ConsoleColors.RESET);
                e.printStackTrace();

            }
        } catch (TwitterException e) {
            System.out.println(e.getMessage());
            System.out.println(ConsoleColors.PURPLE + "EvenBiggerProblems" + ConsoleColors.RESET);
            e.printStackTrace();
        }
    }
}