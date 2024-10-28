package com.example.springboot.service;

import com.example.springboot.model.Avatar;
import com.example.springboot.repository.AvatarRepository;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Optional;


@Service
public class BaiduAvatarService {

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    private static final String API_KEY = "g2PaXNYM34sRPYKI3MkdCj9Z";
    private static final String SECRET_KEY = "vo73vtT5Weh2jYj6SurnwFMc7aRtgggD";

    @Autowired
    private AvatarRepository avatarRepository;

    public String generateTemporaryAvatar(String keyword) throws IOException {
        String taskId = submitAvatarRequest(keyword);

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String imageUrl = queryAvatarResult(taskId);
        return imageUrl;
    }
    public String saveAvatar(Long userId, String imageUrl, String keyword) {
        Avatar existingAvatar = avatarRepository.findByUserId(userId);

        if (existingAvatar == null) {
            Avatar newAvatar = new Avatar();
            newAvatar.setUserId(userId);
            newAvatar.setAvatarKeyword(keyword);
            newAvatar.setAvatarData(imageUrl);
            avatarRepository.save(newAvatar);
        } else {
            existingAvatar.setAvatarKeyword(keyword);
            existingAvatar.setAvatarData(imageUrl);
            avatarRepository.save(existingAvatar);
        }

        return imageUrl;
    }

    String submitAvatarRequest(String keyword) throws IOException {
        String accessToken = getAccessToken();
        String apiUrl = "https://aip.baidubce.com/rpc/2.0/wenxin/v1/basic/textToImage?access_token=" + accessToken;

        JSONObject json = new JSONObject();
        json.put("text", keyword);
        json.put("resolution", "512*512");

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(apiUrl)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();
            JSONObject responseJson = new JSONObject(responseBody);

            JSONObject dataObject = responseJson.getJSONObject("data");
            long taskId = dataObject.getLong("taskId");

            return String.valueOf(taskId);
        }
    }

    String queryAvatarResult(String taskId) throws IOException {
        String accessToken = getAccessToken();
        String apiUrl = "https://aip.baidubce.com/rpc/2.0/wenxin/v1/basic/getImg?access_token=" + accessToken;

        JSONObject json = new JSONObject();
        json.put("taskId", taskId);

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(apiUrl)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();
            JSONObject responseJson = new JSONObject(responseBody);

            JSONObject data = responseJson.getJSONObject("data");

            JSONArray imgUrls = data.getJSONArray("imgUrls");

            if (!imgUrls.isEmpty()) {
                return imgUrls.getJSONObject(0).getString("image");
            } else {
                throw new IOException("No image URL found in the response");
            }
        }
    }


    private String getAccessToken() throws IOException {
        String apiUrl = "https://aip.baidubce.com/oauth/2.0/token";
        String credentials = "grant_type=client_credentials&client_id=" + API_KEY + "&client_secret=" + SECRET_KEY;
        RequestBody body = RequestBody.create(credentials, MediaType.parse("application/x-www-form-urlencoded"));

        Request request = new Request.Builder()
                .url(apiUrl)
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to get access token");
            }

            String responseBody = response.body().string();
            JSONObject responseJson = new JSONObject(responseBody);
            return responseJson.getString("access_token");
        }
    }


    public Optional<Avatar> getAvatarByUserId(Long userId) {
        return Optional.ofNullable(avatarRepository.findByUserId(userId));
    }

    public boolean deleteAvatarByUserId(Long userId) {
        Optional<Avatar> avatar = Optional.ofNullable(avatarRepository.findByUserId(userId));
        if (avatar.isPresent()) {
            avatarRepository.delete(avatar.get());
            return true;
        }
        return false;
    }
}