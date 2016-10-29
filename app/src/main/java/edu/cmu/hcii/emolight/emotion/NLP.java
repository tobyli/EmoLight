package edu.cmu.hcii.emolight.emotion;

import android.util.Log;

import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentEmotion;
import com.ibm.watson.developer_cloud.http.ServiceCall;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by yuanchun on 29/10/2016.
 * Perform NLP on sentence
 */

public class NLP {
    private static NLP sentimentAnalysis;
    private AlchemyLanguage service;
    private String LOGTAG = "NLP";

    public static NLP v() {
        if (sentimentAnalysis == null) {
            sentimentAnalysis = new NLP();
        }
        return sentimentAnalysis;
    }

    private NLP() {
        // initialize sentiment analysis module
        // build pipeline
        service = new AlchemyLanguage();
        service.setApiKey("97434bb11fae2e72f3935df19883fc453ce10d81");
//        Log.d(LOGTAG, "Initialized.");
    }

    public DocumentEmotion.Emotion getEmotion(String text) {
        if (text == null || text.length() == 0) {
            Log.e(LOGTAG, "Input text is null/empty.");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("text", text);
        ServiceCall<DocumentEmotion> emotionCall = service.getEmotion(params);
        DocumentEmotion.Emotion emotion = emotionCall.execute().getEmotion();
        return emotion;
    }

    public static void main(String[] args) {
        String text = "I'm tryna put you in the worst mood, ah";
        DocumentEmotion.Emotion emotion = NLP.v().getEmotion(text);
        String color = EmotionUtils.emotion2Color2(emotion);

        System.out.println(String.format("text: %s\nemotion:\n%s\ncolor: %s\n",
                text,
                EmotionUtils.emotion2String(emotion),
                color));
    }

}
