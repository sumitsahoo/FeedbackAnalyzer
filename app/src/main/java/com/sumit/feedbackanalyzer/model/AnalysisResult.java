package com.sumit.feedbackanalyzer.model;

import java.util.ArrayList;

/**
 * Created by sumit on 8/24/2016.
 */
public class AnalysisResult {
    ArrayList<String> keyPhrases;
    float sentimentScore;
    String detectedLanguage;
    String detectedTopic;

    public ArrayList<String> getKeyPhrases() {
        return keyPhrases;
    }

    public void setKeyPhrases(ArrayList<String> keyPhrases) {
        this.keyPhrases = keyPhrases;
    }

    public float getSentimentScore() {
        return sentimentScore;
    }

    public void setSentimentScore(float sentimentScore) {
        this.sentimentScore = sentimentScore;
    }

    public String getDetectedLanguage() {
        return detectedLanguage;
    }

    public void setDetectedLanguage(String detectedLanguage) {
        this.detectedLanguage = detectedLanguage;
    }

    public String getDetectedTopic() {
        return detectedTopic;
    }

    public void setDetectedTopic(String detectedTopic) {
        this.detectedTopic = detectedTopic;
    }
}
