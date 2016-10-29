package edu.cmu.hcii.emolight.emotion;

import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentEmotion;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by yuanchun on 29/10/2016.
 * Convert emotion to color
 */

public class EmotionUtils {
    public static String EMO_JOY = "joy";
    public static String[] EMO_JOY_KEYWORDS = {"happy", "great", "amazing"};
    public static String EMO_JOY_COLOR = "#00FF00";

    public static String EMO_SADNESS = "sadness";
    public static String[] EMO_SADNESS_KEYWORDS = {"sad", "pity", "cry"};
    public static String EMO_SADNESS_COLOR = "#00FFFF";

    public static String EMO_ANGER = "anger";
    public static String[] EMO_ANGER_KEYWORDS = {"angry", "anger", "mad"};
    public static String EMO_ANGER_COLOR = "#FF0000";

    public static String EMO_DISGUST = "disgust";
    public static String[] EMO_DISGUST_KEYWORDS = {"disgust", "disguising"};
    public static String EMO_DISGUST_COLOR = "#FFFF00";

    public static String EMO_FEAR = "fear";
    public static String[] EMO_FEAR_KEYWORDS = {"terrible", "fear", "freak"};
    public static String EMO_FEAR_COLOR = "#808080";

    public static String DEFAULT_COLOR = "#FFFFFF";

    private static Map<String, String> EMOTION2COLOR = null;
    public static Map<String, String> getEmotion2Color() {
        if (EMOTION2COLOR == null) {
            EMOTION2COLOR = new HashMap<>();
            EMOTION2COLOR.put(EMO_JOY, EMO_JOY_COLOR);
            EMOTION2COLOR.put(EMO_SADNESS, EMO_SADNESS_COLOR);
            EMOTION2COLOR.put(EMO_ANGER, EMO_ANGER_COLOR);
            EMOTION2COLOR.put(EMO_DISGUST, EMO_DISGUST_COLOR);
            EMOTION2COLOR.put(EMO_FEAR, EMO_FEAR_COLOR);
        }
        return EMOTION2COLOR;
    }

    public static String emotion2Color(DocumentEmotion.Emotion emotion) {
        String mainEmotion = getMainEmotion(emotion);
        EMOTION2COLOR = getEmotion2Color();
        if (EMOTION2COLOR.containsKey(mainEmotion)) {
            return EMOTION2COLOR.get(mainEmotion);
        }
        return DEFAULT_COLOR;
    }

    public static String emotion2Color2(DocumentEmotion.Emotion emotion) {
        if (emotion == null) {
            return null;
        }
        double joy = emotion.getJoy();
        double sadness = emotion.getSadness();
        double anger = emotion.getAnger();
        int r = (int) (256 * anger);
        int g = (int) (256 * joy);
        int b = (int) (256 * sadness);
        String hex = String.format("#%02x%02x%02x", r, g, b);
        return hex;
    }

    public static String emotion2String(DocumentEmotion.Emotion emotion) {
        String emoStr = String.format("\t%s: %f\n\t%s: %f\n\t%s: %f\n\t%s: %f\n\t%s: %f\n",
                "joy", emotion.getJoy(),
                "anger", emotion.getAnger(),
                "disgust", emotion.getDisgust(),
                "fear", emotion.getFear(),
                "sadness", emotion.getSadness());
        return emoStr;
    }

    public static String getMainEmotion(DocumentEmotion.Emotion emotion) {
        String mainEmotion = "joy";
        double mostSignificance = 0;
        double emoSignificance = 0;

        emoSignificance = emotion.getJoy();
        if (emoSignificance > mostSignificance) {
            mainEmotion = EMO_JOY;
            mostSignificance = emoSignificance;
        }

        emoSignificance = emotion.getAnger();
        if (emoSignificance > mostSignificance) {
            mainEmotion = EMO_ANGER;
            mostSignificance = emoSignificance;
        }

        emoSignificance = emotion.getDisgust();
        if (emoSignificance > mostSignificance) {
            mainEmotion = EMO_DISGUST;
            mostSignificance = emoSignificance;
        }

        emoSignificance = emotion.getSadness();
        if (emoSignificance > mostSignificance) {
            mainEmotion = EMO_SADNESS;
            mostSignificance = emoSignificance;
        }

        return mainEmotion;
    }
}
