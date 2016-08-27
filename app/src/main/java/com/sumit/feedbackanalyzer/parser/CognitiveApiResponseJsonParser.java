package com.sumit.feedbackanalyzer.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Created by sumit on 6/14/2016.
 */
public class CognitiveApiResponseJsonParser {

    public static float parseSentimentResponseJson(JsonObject responseJson){

        float detectedSentiment;

        if(responseJson != null){

            JsonArray documentJsonArray = responseJson.getAsJsonArray("documents");

            if(documentJsonArray != null && documentJsonArray.size() > 0){
                JsonObject documentJsonObject = (JsonObject) documentJsonArray.get(0);

                if(documentJsonObject != null){

                    double sentimentScore = documentJsonObject.get("score").getAsDouble();
                    sentimentScore *= 100;
                    detectedSentiment = Math.round(sentimentScore);

                    return detectedSentiment;
                }
            }
        }

        return 0;
    }

    public static String parseTopicResponseJson(JsonObject responseJson){

        float detectedSentiment;

        if(responseJson != null){

            JsonArray documentJsonArray = responseJson.getAsJsonArray("documents");

            if(documentJsonArray != null && documentJsonArray.size() > 0){
                JsonObject documentJsonObject = (JsonObject) documentJsonArray.get(0);

                if(documentJsonObject != null){

                    double sentimentScore = documentJsonObject.get("score").getAsDouble();
                    sentimentScore *= 100;
                    detectedSentiment = Math.round(sentimentScore);

                    return "";
                }
            }
        }

        return null;
    }

    // Parse key phrases response

    public static ArrayList<String> parseKeyPhrasesResponseJson(JsonObject responseJson){

        ArrayList<String> keyPhrases = null;

        /*{
            "documents": [
            {
                "keyPhrases": [
                "string"
                ],
                "id": "string"
            }
            ],
            "errors": [
            {
                "id": "string",
                    "message": "string"
            }
            ]
        }*/

        if(responseJson != null){

            JsonArray documentJsonArray = responseJson.getAsJsonArray("documents");

            if(documentJsonArray != null && documentJsonArray.size() > 0){
                JsonObject documentJsonObject = (JsonObject) documentJsonArray.get(0);

                if(documentJsonObject != null){

                    JsonArray keyPhrasesArray = documentJsonObject.getAsJsonArray("keyPhrases");

                    if(keyPhrasesArray != null && keyPhrasesArray.size() > 0){
                        keyPhrases = new ArrayList<>();
                        for (JsonElement keyPhraseElement : keyPhrasesArray){
                            keyPhrases.add(keyPhraseElement.getAsString());
                        }
                    }

                    if(keyPhrases != null && keyPhrases.size() > 0)
                        return keyPhrases;
                }
            }
        }

        return null;
    }
}
