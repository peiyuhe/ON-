package com.example.springboot.service;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class QianfanService {

    public static final String API_KEY = "8zp120frXR9nMj6byRj8xrPK";
    public static final String SECRET_KEY = "z70KANIwGghNdGP3V8T0KZeKxKnRbQvT";

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    public String getAIAnswer(String question) throws IOException {
        JSONObject requestBodyJson = new JSONObject();
        requestBodyJson.put("temperature", 0.95);
        requestBodyJson.put("top_p", 0.8);
        requestBodyJson.put("penalty_score", 1);
        requestBodyJson.put("enable_system_memory", false);
        requestBodyJson.put("disable_search", false);
        requestBodyJson.put("enable_citation", false);
        requestBodyJson.put("response_format", "text");

        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", "Now you are a teaching Q&A robot. You will learn relevant network information by searching and answering the questions I ask in short English. Note that the questions I ask are:\"" + question + "\"");

        requestBodyJson.put("messages", new org.json.JSONArray().put(message));

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestBodyJson.toString());

        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions?access_token=" + getAccessToken())
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();


        Response response = HTTP_CLIENT.newCall(request).execute();

        String responseBodyString = response.body().string();
        JSONObject jsonResponse = new JSONObject(responseBodyString);
        if (jsonResponse.has("result")) {
            return jsonResponse.getString("result");
        } else {
            return "AI 无法生成回答。";
        }
    }

    private String getAccessToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY
                + "&client_secret=" + SECRET_KEY);


        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();


        Response response = HTTP_CLIENT.newCall(request).execute();
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            String responseBodyString = responseBody.string();
            JSONObject jsonResponse = new JSONObject(responseBodyString);
            return jsonResponse.getString("access_token");
        } else {
            throw new IOException("无法获取 Access Token");
        }
    }
}
