package edu.cis.pset1_twitteranalysis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.Thread;
import java.util.ArrayList;


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

        mainPage();


    }

    TwitterController myC;

    {
        try {
            myC = new TwitterController(this);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    public void postATweet() {
        Button bostTweet = (Button) findViewById(R.id.postTweet);
        bostTweet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText txtOutput = findViewById(R.id.txtOutput);
                String tweetInput = txtOutput.getText().toString();
                myC.postTweet(tweetInput);
            }
        });
        Button backButton = (Button) findViewById(R.id.Back);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_main);
            }
        });
    }

    public void mainPage() {
        Button teacherRec = (Button) findViewById(R.id.teacherRec);
        Button postTweet = (Button) findViewById(R.id.post);
        Button getTop = (Button) findViewById(R.id.getTop);

        teacherRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.io);
                getSomeTeachers();
            }
        });
        postTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.post_tweet);
                postATweet();
            }
        });
        getTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.get_top);
                getCommon();
            }
        });
    }

    public void getSomeTeachers() {

    }

    public void getCommon(){
        Button bostTweet = (Button) findViewById(R.id.postTweet);
        bostTweet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText txtOutput = findViewById(R.id.txtOutput);
                TextView topWord = findViewById(R.id.outputTxt);
                String tweetInput = txtOutput.getText().toString();
                String output = myC.findUserStats(tweetInput);
                topWord.setText(output);
            }
        });
        Button backButton = (Button) findViewById(R.id.Back);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_main);
            }
        });
    }

//        try {
//            TwitterController myC = new TwitterController(this);
//            try {
////                myC.findUserStats("cs_cis");
//                String result = myC.findUserStats("cs_cis");
//                System.out.println(result + "GOOGOGOGOGOGGOGOGAOGAGAGAGAGAGA");
//                myC.postTweet("yes i am just bad "); //this will tweet to your account
//            } catch (Exception e) {
//                System.out.println("problem in finding handle");
//                e.printStackTrace();
//            }
//            try {
//                ArrayList<String> Names = myC.getTeacherName(7);
//                ArrayList<String> Discription = myC.getTeacherDescription(7);
//                ArrayList<String> Photos = myC.getTeacherPhoto(7);
//                System.out.println(Names);
//                System.out.println(Discription);
//                System.out.println(Photos);
//                System.out.println("LIFE IS OVER ITS ALL DONE IT WORKS");
////                System.out.println(ConsoleColors.GREEN + "Got Recommendation" + ConsoleColors.RESET);
//            } catch (TwitterException e) {
//                System.out.println("Twitter Exception: BigProblems \n " +
//                        "someone has been using the apis keys too much before i got a chance ");
//                e.printStackTrace();
//
//            }
//        } catch (
//                TwitterException e) {
//            System.out.println(e.getMessage());
//            System.out.println(ConsoleColors.PURPLE + "EvenBiggerProblems" + ConsoleColors.RESET);
//            e.printStackTrace();
//        }


}
