package morningsignout.phq9transcendi.activities.HelperClasses;

import android.util.Log;
import android.util.Pair;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import morningsignout.phq9transcendi.activities.PHQApplication;

/**
 * Created by pokeforce on 5/26/16.
 * Contains information about answer-categorization and answer-values for quiz
 * Must be updated when questions are changed around
 *
 * What to update when question added: String[] questions, categoryNames, int[] categoryIndices
 */
public class Scores {
    // Appended to saved scores string to ensure string corresponds with this order of questions
    // It could change in the future.
    static private final int VERSION_OF_ORDER_NUM = 2;

    // Index where red flag starts
    static private final int RED_FLAG_QUESTION = 16;

    // Keys used in uploading to Firebase (Note: false = a, true = b).
    static private final String[] firebaseAnswerStrings = {
            "answer-a", "answer-b", "answer-c", "answer-d", "answer-e"
    };

    // Relevant question data variables
    // Using names instead of numbers for FireBase on the off-chance that
    // the order of questions is not fixed (would be bad for database to use #).
    //
    // Ordered by current order of questions, arranged by category
    private static final String[] questions = {
            "anhedoniainterest", "anhedoniaenjoy",
            "mooddepress", "moodhopeless",
            "sleeplow", "sleephigh",
            "fatigue",
            "appetitelow", "appetitehigh",
            "guilt",
            "concentrationpoor", "concentrationdistracted",
            "psychomotorslowself", "psychomotorslowother",
            "suicidalityactive", "suicidalitypassive",

            // red flag
            "continuousdepression_flag", "longdepression_flag", "interference", "suicidality_flag",
            "suicideaction_flag",

            // research
            "familyunderstands", "familysituation", "culturalbackground", "appointment",
            "fearofstranger", "adequateresources"
    };

    // Categories of questions
    private static final String[] categoryNames = {
            "anhedonia", "mood", "sleep-disturbance", "energy", "appetite", "guilt", "cognition-concentration",
            "psychomotor", "suicide", "red-flag", "research"
    };

    // Array of which category each question is associated with
    private static final int[] categoryIndices = {
            0, 0, 1, 1, 2, 2, 3, 4, 4, 5, 6, 6, 7, 7, 8, 8, 9, 9, 9, 9, 9, 10, 10, 10, 10, 10, 10
    };

    // Score value that would trigger a red flag alert
    private static final int[] redFlagThreshold = {
            1, 1, 1, 1, 1
    };

    // Container of score values and if question is "visited"
    private TreeMap<String, Integer> scoreDictionary;
    private TreeMap<String, Boolean> questionIsVisited; // Visited = seen (for seekbar q's) or answered (yes/no q's)

    public Scores() {
        scoreDictionary = new TreeMap<>();
        questionIsVisited = new TreeMap<>();

        for (String q : questions) {
            scoreDictionary.put(q, 0);
            questionIsVisited.put(q, false);
        }
    }

    public Scores(String savedScore, String savedVisit) {
        scoreDictionary = new TreeMap<>();
        questionIsVisited = new TreeMap<>();

        if (savedScore != null && savedVisit != null &&
                savedScore.endsWith(String.valueOf(VERSION_OF_ORDER_NUM)) &&
                savedVisit.endsWith(String.valueOf(VERSION_OF_ORDER_NUM)))
            for (int i = 0; i < questions.length; i++) {
                scoreDictionary.put(questions[i], savedScore.charAt(i) - 0x30);     // char to int (0-3)
                questionIsVisited.put(questions[i], savedVisit.charAt(i) != '0');   // char to bool (0 or 1)
            }
        else
            for (String q : questions) {
                scoreDictionary.put(q, 0);
                questionIsVisited.put(q, false);
            }
    }

    public void putScore(int index, int value) {
        scoreDictionary.put(questions[index], value);
        questionIsVisited.put(questions[index], true);
    }

    public int getQuestionScore(int i) {
        if (i >= 0 && i <= questions.length)
            return scoreDictionary.get(questions[i]);

        return -1;
    }

    public int getFinalScore() {
        int sum = 0;
        int max = 0;
        int currentCategory = categoryIndices[0];

        for (int i = 0; i < questions.length; i++) {
            max = Math.max(scoreDictionary.get(questions[i]), max);

            // Next category
            if (i + 1 < categoryIndices.length && currentCategory != categoryIndices[i + 1]) {
                // 9 = red flag, 10 = research
                if (currentCategory < 9) {
                    sum += max;
//                    Log.d("Scores", "for " + currentCategory + ", max: " + max);
                }
                max = 0;
                currentCategory = categoryIndices[i + 1];
            }
            Log.d("Scores", i + ": " + categoryIndices[i] + ", " + scoreDictionary.get(questions[i]));
        }

        for (String q : questions)
            Log.d("Scores", q + ": " + scoreDictionary.get(q));

        for (int i = 0; i < categoryNames.length; i++)
            Log.d("Scores", categoryNames[i] + ": " + getCategoryScore(i));

        //Log.d("Scores", getScoreString());
        //Log.d("Scores", getVisitedString());
        //Log.d("Scores", "---------------------------------------");

        return sum;
    }

    public boolean containsRedFlag() {
        int redFlagNum = 0;
        int end = RED_FLAG_QUESTION + redFlagThreshold.length;

        // Assumes that red flag questions are consecutive
        for (int i = RED_FLAG_QUESTION; i < end; i++, redFlagNum++)
            if (scoreDictionary.get(questions[i]) >= redFlagThreshold[redFlagNum])
                return true;

        //Log.d("Scores", "---------------------------------------");

        return false;
    }

    public boolean questionIsVisited(int i) {
        if (questionIsVisited.containsKey(questions[i]))
            return questionIsVisited.get(questions[i]);

        throw new IndexOutOfBoundsException(); // Should not happen
    }

    private int getCategoryScore(int category) {
        int max = 0;

        for (int i = 0; i < categoryIndices.length && categoryIndices[i] <= category; i++)
            if (categoryIndices[i] == category)
                max = Math.max(scoreDictionary.get(questions[i]), max);

        return max;
    }

//    // FIXME: upload scores to database
//    public void uploadDataToDatabase(String userID, String startTime, String endTime,
//                                        double latitude, double longitude) {
//        //Log.d("Scores", "In upload function");
//        FirebaseUser user = FirebaseAuth.getInstance(PHQApplication.getFirebaseAppInstance()).getCurrentUser();
//
//        if(user != null) {
//            DatabaseReference rootRef = FirebaseDatabase.getInstance(PHQApplication.getFirebaseAppInstance());
//            DatabaseReference userRef = rootRef.getReference("users/" + user.getUid()),
//                testsRef = ;
//        }
//
//        Firebase testRef = ref.child("tests").push(),
//                userRef = ref.child("users").child(userID),
//                questionsRef = ref.child("questions-analytics");
//        String testID = testRef.getKey();
//
//        // Questions analytics
//        for (String q : questions) {
//            StringBuilder path = new StringBuilder();
//            int currAnswer = scoreDictionary.get(q);
//
//            // question/answer-#/userID/"testID"
//            path.append(q).append("/")
//                    .append(firebaseAnswerStrings[currAnswer]).append("/")
//                    .append(userID).append("/")
//                    .append("testID");
//
//            // add "new val":testID to database
//            questionsRef.child(path.toString()).push().setValue(testID);
//        }
//
//        // Users
//        userRef.child("testIDs").push().setValue(testID);
//
//        // Tests
//        ArrayList<String> answers = new ArrayList<>(questions.length);
//        Map<String, Integer> scores = new HashMap<>(categoryNames.length);
//        for (String q : questions)
//            answers.add(firebaseAnswerStrings[scoreDictionary.get(q)]);
//
//        for (int i = 0; i < categoryNames.length; i++)
//            scores.put(categoryNames[i], getCategoryScore(i));
//
//        testRef.child("timestamp").setValue(endTime);
//        testRef.child("latitude").setValue(latitude);
//        testRef.child("longitude").setValue(longitude);
//        testRef.child("userID").setValue(userID);
//        testRef.child("answers").setValue(answers);
//        testRef.child("scores").setValue(scores);
//
//        // FIXME: add check for success
//    }

    public Pair<String, String> getScoreStateStrings() {
        return new Pair<>(getScoreString(), getVisitedString());
    }

    private String getScoreString() {
        String score = "";
        for (String q : questions) {
            score += Integer.toString(scoreDictionary.get(q));
        }

        score += "_" + VERSION_OF_ORDER_NUM;

        return score;
    }

    private String getVisitedString() {
        String visited = "";
        for (String q : questions) {
            visited += questionIsVisited.get(q).compareTo(false);   // Returns 1 or 0 (true/false)
        }

        visited += "_" + VERSION_OF_ORDER_NUM;

        return visited;
    }
}