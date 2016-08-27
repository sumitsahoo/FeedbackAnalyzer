package com.sumit.feedbackanalyzer.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sumit.feedbackanalyzer.Utils;
import com.sumit.feedbackanalyzer.parser.CognitiveApiResponseJsonParser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by sumit on 6/14/2016.
 */
public class MicrosoftCognitiveAPI {

    // Detect Sentiment

    public static float detectSentiment(String textToAnalyse) {

        try {

            // Prepare Image Recognition URL with parameters
            // End Point URL (Sentiment) : https://westus.api.cognitive.microsoft.com/text/analytics/v2.0/sentiment

            HttpUrl url = new HttpUrl.Builder()
                    .scheme("https")
                    .host("westus.api.cognitive.microsoft.com")
                    .addPathSegment("text")
                    .addPathSegment("analytics")
                    .addPathSegment("v2.0")
                    .addPathSegment("sentiment")
                    .build();


            JsonObject rootJson = new JsonObject();
            JsonArray documentJsonArray = new JsonArray();
            JsonObject documentJsonObject = new JsonObject();

            documentJsonObject.addProperty("language", "en");
            documentJsonObject.addProperty("id", "string");
            documentJsonObject.addProperty("text", textToAnalyse);

            documentJsonArray.add(documentJsonObject);
            rootJson.add("documents", documentJsonArray);

            RequestBody requestBody = RequestBody.create(Utils.MEDIA_TYPE_JSON, rootJson.toString());

            // Build request and add subscription key header

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .addHeader("Ocp-Apim-Subscription-Key", Utils.MS_TEXT_ANALYSIS_SUBSCRIPTION_KEY)
                    .addHeader("Content-Type", "application/json")
                    .url(url)
                    .post(requestBody)
                    .build();

            Response response = null;

            // Initiate REST call

            response = client.newCall(request).execute();
            if (response != null) {
                JsonObject responseArray = new JsonParser().parse(response.body().string()).getAsJsonObject();
                return CognitiveApiResponseJsonParser.parseSentimentResponseJson(responseArray);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }

    // Detect Topic

    public static String detectTopic(String textToAnalyse) {

        try {

            // Prepare Image Recognition URL with parameters
            // End Point URL (Sentiment) : https://westus.api.cognitive.microsoft.com/text/analytics/v2.0/topics[?minDocumentsPerWord][&maxDocumentsPerWord]

            HttpUrl url = new HttpUrl.Builder()
                    .scheme("https")
                    .host("westus.api.cognitive.microsoft.com")
                    .addPathSegment("text")
                    .addPathSegment("analytics")
                    .addPathSegment("v2.0")
                    .addPathSegment("topics")
                    .addQueryParameter("minDocumentsPerWord", "0")
                    .build();

            /*{
                "stopWords": [
                "string"
                ],
                "topicsToExclude": [
                "string"
                ],
                "documents": [
                {
                    "id": "string",
                        "text": "string"
                }
                ]
            }*/

            JsonObject rootJson = new JsonObject();

            JsonArray stopWordJsonArray = new JsonArray();
            JsonArray topicToExcludeJsonArray = new JsonArray();
            JsonArray documentJsonArray = new JsonArray();

            stopWordJsonArray.add("string");
            topicToExcludeJsonArray.add("");

            JsonObject documentJsonObject = new JsonObject();

            //documentJsonObject.addProperty("language", "en");
            documentJsonObject.addProperty("id", "string");
            documentJsonObject.addProperty("text", textToAnalyse);

            documentJsonArray.add(documentJsonObject);

            rootJson.add("stopWords",stopWordJsonArray);
            rootJson.add("topicsToExclude",topicToExcludeJsonArray);
            rootJson.add("documents", documentJsonArray);

            RequestBody requestBody = RequestBody.create(Utils.MEDIA_TYPE_JSON, rootJson.toString());

            // Build request and add subscription key header

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .addHeader("Ocp-Apim-Subscription-Key", Utils.MS_TEXT_ANALYSIS_SUBSCRIPTION_KEY)
                    .addHeader("Content-Type", "application/json")
                    .url(url)
                    .post(requestBody)
                    .build();

            Response response = null;

            // Initiate REST call

            response = client.newCall(request).execute();
            if (response != null) {
                JsonObject responseArray = new JsonParser().parse(response.body().string()).getAsJsonObject();
                return CognitiveApiResponseJsonParser.parseTopicResponseJson(responseArray);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    // Key Phrases

    public static ArrayList<String> detectKeyPhrases(String textToAnalyse) {

        try {

            // Prepare Image Recognition URL with parameters
            // End Point URL (Sentiment) : https://westus.api.cognitive.microsoft.com/text/analytics/v2.0/keyPhrases

            HttpUrl url = new HttpUrl.Builder()
                    .scheme("https")
                    .host("westus.api.cognitive.microsoft.com")
                    .addPathSegment("text")
                    .addPathSegment("analytics")
                    .addPathSegment("v2.0")
                    .addPathSegment("keyPhrases")
                    .build();

            JsonObject rootJson = new JsonObject();

            JsonArray documentJsonArray = new JsonArray();
            JsonObject documentJsonObject = new JsonObject();

            documentJsonObject.addProperty("language", "en");
            documentJsonObject.addProperty("id", "string");
            documentJsonObject.addProperty("text", textToAnalyse);

            documentJsonArray.add(documentJsonObject);

            rootJson.add("documents", documentJsonArray);

            RequestBody requestBody = RequestBody.create(Utils.MEDIA_TYPE_JSON, rootJson.toString());

            // Build request and add subscription key header

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .addHeader("Ocp-Apim-Subscription-Key", Utils.MS_TEXT_ANALYSIS_SUBSCRIPTION_KEY)
                    .addHeader("Content-Type", "application/json")
                    .url(url)
                    .post(requestBody)
                    .build();

            Response response = null;

            // Initiate REST call

            response = client.newCall(request).execute();
            if (response != null) {
                JsonObject responseObject = new JsonParser().parse(response.body().string()).getAsJsonObject();
                return CognitiveApiResponseJsonParser.parseKeyPhrasesResponseJson(responseObject);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


}
