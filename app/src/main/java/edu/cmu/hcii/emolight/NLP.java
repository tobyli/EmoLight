package edu.cmu.hcii.emolight;

import android.util.Log;

import java.util.List;
import java.util.Locale;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;

/**
 * Created by yuanchun on 29/10/2016.
 * Perform NLP on sentence
 */

public class NLP {
    private static NLP sentimentAnalysis;
    private StanfordCoreNLP pipeline;
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
        pipeline = new StanfordCoreNLP(
                PropertiesUtils.asProperties(
                        "annotators", "tokenize,ssplit,pos,lemma,parse,natlog,sentiment",
                        "ssplit.isOneSentence", "true",
                        "parse.model", "edu/stanford/nlp/models/srparser/englishSR.ser.gz",
                        "tokenize.language", "en"));
        Log.d(LOGTAG, "Initialized.");
    }

    public int getSentiment(String text) {
        if (text == null || text.length() == 0) {
            Log.e(LOGTAG, "Input text is null/empty.");
        }

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        int mainSentiment = 0;
        int longest = 0;

        for(CoreMap sentence: sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
//            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
//                // this is the text of the token
//                String word = token.get(TextAnnotation.class);
//                // this is the POS tag of the token
//                String pos = token.get(PartOfSpeechAnnotation.class);
//                // this is the NER label of the token
//                String ne = token.get(NamedEntityTagAnnotation.class);
//            }

            // this is the parse tree of the current sentence
            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
            String partText = sentence.toString();
            if (partText.length() > longest) {
                mainSentiment = sentiment;
                longest = partText.length();
            }

            // this is the Stanford dependency graph of the current sentence
//            SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
        }

        // This is the coreference link graph
        // Each chain stores a set of mentions that link to each other,
        // along with a method for getting the most representative mention
        // Both sentence and token offsets start at 1!
//        Map<Integer, CorefChain> graph = document.get(CorefChainAnnotation.class);
        return mainSentiment;
    }

    public static void main(String[] args) {
        String positiveText = "very nice";
        String negativeText = "pretty bad";
        int positiveTextSenti = NLP.v().getSentiment(positiveText);
        int negativeTextSenti = NLP.v().getSentiment(negativeText);

        System.out.println(String.format(new Locale("%s - %d"), positiveText, positiveTextSenti));
        System.out.println(String.format(new Locale("%s - %d"), negativeText, negativeTextSenti));
    }

}
