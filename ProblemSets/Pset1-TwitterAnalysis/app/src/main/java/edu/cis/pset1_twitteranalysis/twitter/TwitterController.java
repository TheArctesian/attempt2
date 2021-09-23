package edu.cis.pset1_twitteranalysis.twitter;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import edu.cis.pset1_twitteranalysis.ConsoleColors;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.InputStream;
import java.util.*;

public class TwitterController {
    private Twitter twitter;
    private ArrayList<Status> statuses;
    private ArrayList<String> tokens;
    private HashMap<String, Integer> wordCounts;
    ArrayList<String> commonWords;
    private String popularWord;
    private int frequencyMax;
    Context context;

    public TwitterController(Context currContext) throws TwitterException {
        context = currContext;

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("L2BKElNco18WhHak3NkCM05t4")
                .setOAuthConsumerSecret("DXL2KqEegX03uJbQNPxecGFqPiKUhw5vCpnzukiL3OvxFaJIiF")
                .setOAuthAccessToken("1042321525901152256-Po8en0crJfjW74hUWUgKt34n5AAAlP")
                .setOAuthAccessTokenSecret("74bEpnfXM8O3Or3kajQERMeTjJVoYJFGXamUa5qDopE7y");
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
        statuses = new ArrayList<Status>();
        tokens = new ArrayList<String>();
        wordCounts = new HashMap<String, Integer>();
        commonWords = new ArrayList<String>();
        getCommonWords();
    }

    /********** PART 1 *********/
    //can be used to get common words from the commonWords txt file
    public void getCommonWords() {

        try {
            AssetManager am = context.getAssets();
            //this file can be found in src/main/assets
            InputStream myFile = am.open("commonWords.txt");
            Scanner sc = new Scanner(myFile);
            while (sc.hasNextLine()) {
                commonWords.add(sc.nextLine());
            }

        } catch (Exception err) {
            Log.d(ConsoleColors.PURPLE + "COMMON_WORDS", err.toString() + ConsoleColors.RESET);
        }
    }

    public String postTweet(String message) {
        String statusTextToReturn = "";
        try {
            Status status = twitter.updateStatus(message);
            statusTextToReturn = status.getText();
        } catch (TwitterException e) {
            System.out.println(e.getErrorMessage());
        }
        return statusTextToReturn;
    }

    // Example query with paging and file output.
    private void fetchTweets(String handle) {
        //Create a twitter paging object that will start at page 1 and hole 200 entries per page.
        Paging page = new Paging(1, 200);
        //Use a for loop to set the pages and get the necessary tweets.
        for (int i = 1; i <= 10; i++) {
            page.setPage(i);
            /* Ask for the tweets from twitter and add them all to the statuses ArrayList.
            Because we set the page to receive 200 tweets per page, this should return
            200 tweets every request. */
            try {
                statuses.addAll(twitter.getUserTimeline(handle, page));
            } catch (Exception err) {
                Log.d("fetchTweets", "could not get user timeline");
            }
        }
        //Write to the file a header message. Useful for debugging.
        int numberOfTweetsFound = statuses.size();
        System.out.println("Number of Tweets Found: " + numberOfTweetsFound);
        //Use enhanced for loop to print all the tweets found.
        int count = 1;
//        for (Status tweet : statuses) {
//            System.out.println(count + ". " + tweet.getText());
//            count++;
//        }
    }

    /********** PART 2 *********/
    /*
     * TODO 2: this method splits a whole status into different words. Each word
     * is considered a token. Store each token in the "tokens" arrayList
     * provided. Loop through the "statuses" ArrayList.
     */
    private void splitIntoWords() {
        for (Status tweet : statuses) { //Loop through statuses
//            System.out.println(tweet.getText() + tweet.getText().getClass().getName());
            String word = tweet.getText();//New array to get the text and split it by " "
            String[] words = word.split(" ");
            for (int i = 0; i < words.length; i++) //loop through word
                tokens.add(words[i]);
            ; //add word to token
        }
        //System.out.println(tokens + ConsoleColors.CYAN + "a lof of words hear" + ConsoleColors.RESET);
    }

    /*
     * TODO 3: return a word after removing any punctuation and turn to lowercase from it.
     * If the word is "Edwin!!", this method should return "edwin".
     * We'll need this method later on.
     * If the word is a common word, return null
     */
    @SuppressWarnings("unchecked")
    private String cleanOneWord(String word) {
        String clean = word.toString().trim().replaceAll("[^a-zA-z]", "").toLowerCase(); //remove extra spaces replace everything non A-Z char and set it to lower case
        for (String commonWord : commonWords) { //loop through common words
            if (clean.equals(commonWord)) //if the clean words is in common Words
            {
                return null;
            }
        }
        return clean;
    }

    /*
     * TODO 4: loop through each word, get a clean version of each word
     * and save the list with only clean words.
     */
    @SuppressWarnings("unchecked")
    private void createListOfCleanWords() {
        ArrayList<String> cleanTokens = new ArrayList<String>(); //init new clean arraylist
        for (String token : tokens) { //loop through and add cleaned token to clean tokens
            cleanTokens.add(this.cleanOneWord(token));
        }
//        tokens = new ArrayList<String[]>(cleanTokens);
    }

    /*
     * TODO 5: count each clean word using. Use the frequentWords Hashmap.
     */
    @SuppressWarnings("unchecked")
    private void countAllWords() {
        for (String token : tokens) {
            if (wordCounts.containsKey(token)) {
                int count = wordCounts.get(token) + 1;
                wordCounts.put(token, count);
            } else {
                wordCounts.put(token, 1);
            }
        }
    }

    //TODO 6: return the most frequent word's string in any appropriate format
    @SuppressWarnings("unchecked")
    public String getTopWord() {
        int maxCount = 0;

        String topWord = "";
        for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
            int count = entry.getValue();
            String word = entry.getKey();
            if (count >= maxCount) {
                topWord = word.toString();
            }
        }
        return topWord;
    }

    //TODO 7: return the most frequent word's count as an integer.
    @SuppressWarnings("unchecked")
    public int getTopWordCount() {
        int maxCount = 0;
        for (int count : wordCounts.values()) {
            if (count >= maxCount) {
                maxCount = count;
            }
        }
        return maxCount;
    }

    public String findUserStats(String handle) {
        /*
         * TODO 8: you put it all together here. Call the functions you
         * finished in TODO's 2-7. They have to be in the correct order for the
         * program to work.
         * Remember to use .clear() method on collections so that
         * consecutive requests don't count words from previous requests.
         */
        this.fetchTweets(handle);
        this.splitIntoWords();
        this.createListOfCleanWords();
        this.countAllWords();
        String mostFeqWord = this.getTopWord();
        int wordCount = this.getTopWordCount();
        this.tokens.clear();
        this.statuses.clear();
        this.wordCounts.clear();
        System.out.println(handle + "s most common word is: " + mostFeqWord.toString() + " it came up " + wordCount + " times");
        return mostFeqWord + wordCount;
    }

    /*********** PART 3 **********/
    //TODO 9: Create your own method that recommends possible teaching candidates.
    // Example: A method that returns 100 tweets from keyword(s).
    public List<Status> searchKeywords(String keywords) {
        //Use the Query object from Twitter
        Query query = new Query(keywords);
        query.setCount(100);
        query.setSince("2015-12-1");
        //create an ArrayList to store results, which will be of type Status
        List<Status> searchResults = new ArrayList<>();
        try {
            //we try to get the results from twitter
            QueryResult result = twitter.search(query);
            searchResults = result.getTweets();
        } catch (TwitterException e) {
            //if an error happens, like the connection is interrupted,
            //we print the error and return an empty ArrayList
            e.printStackTrace();
        }
        return searchResults;
    }

    public List<User> searchSchools() throws TwitterException {
        String[] Schools = {
                "CISHK",
                "DwightSeoul",
                "SJIIES",
                "BISP",
                "standrewsbkk",
                "ISHCMC"
        };
        List<User> schoolUsers = new ArrayList<User>();
        for (String school : Schools) {
            List<User> users = twitter.searchUsers(school, 1);
            for (int m = 0; m > Schools.length; m++) {
                schoolUsers.add(users.get(m));
                System.out.println(users.get(m) + "assholes in albania");
            }
        }
        System.out.println(schoolUsers.toString() + "School Users");
        return schoolUsers;
    }

    public HashMap<Integer, User> getPossibleTeachers(User school) throws TwitterException {
        int nextCursor = -1;
        ArrayList<User> followers = new ArrayList<User>();
        do {
            PagableResponseList<User> userResponse = twitter.getFollowersList(school.getName(), nextCursor);
            nextCursor = (int) userResponse.getNextCursor();
            followers.addAll(userResponse);
        }
        while (nextCursor > 0);
        HashMap<Integer, User> potentialTeacher = new HashMap<>();

        for (User user : followers) {
            String bio = user.getDescription().toLowerCase();
            if (bio.contains("education") ||
                    bio.contains("teacher") ||
                    bio.contains("school") ||
                    bio.contains("professor") ||
                    bio.contains("learning") ||
                    bio.contains("educator") ||
                    bio.contains("mentor") ||
                    bio.contains("coach") ||
                    bio.contains("researcher") ||
                    bio.contains("trainer") ||
                    bio.contains("ed") ||
                    bio.contains("int'l") ||
                    bio.contains("ibpyp") ||
                    bio.contains("myp") ||
                    bio.contains("ib") ||
                    bio.contains("nerd") ||
                    bio.contains("history") ||
                    bio.contains("english") ||
                    bio.contains("bio") ||
                    bio.contains("biology") ||
                    bio.contains("chemistry") ||
                    bio.contains("cs") ||
                    bio.contains("physics") ||
                    bio.contains("math") ||
                    bio.contains("counselor")) {
                potentialTeacher.put(user.getFollowersCount(), user);
            }
        }
        System.out.println(potentialTeacher);
        return potentialTeacher;
    }

    public List<User> getTopThreeTeachers(HashMap<Integer, User> popularTeacher) {
        Set<Integer> popularTeacherKeySet = popularTeacher.keySet();
        ArrayList<Integer> numberOfFollowers = new ArrayList<Integer>(popularTeacherKeySet);
        Collections.sort(numberOfFollowers);
        ArrayList<User> teachers = new ArrayList<User>();
        for (int i = 0; i < 3; i++) {
            teachers.add(popularTeacher.get(numberOfFollowers.get(i)));
        }
        System.out.println(teachers + "should be teachers");
        return teachers;
    }

    public List<User> getRecommendations() throws TwitterException {
        List<User> schools = this.searchSchools();
        List<User> recommendation = new ArrayList<User>();
        for (User school : schools) {
            HashMap<Integer, User> potentialTeacher = this.getPossibleTeachers(school);
            List<User> topThree = this.getTopThreeTeachers(potentialTeacher);
            recommendation.addAll(topThree);
        }
//        System.out.println(topThree.toString());
        return recommendation;
    }
}

