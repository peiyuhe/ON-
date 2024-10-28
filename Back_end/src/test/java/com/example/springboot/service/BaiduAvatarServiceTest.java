package com.example.springboot.service;

import com.example.springboot.model.Avatar;
import com.example.springboot.repository.AvatarRepository;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BaiduAvatarServiceTest {

    @Mock
    private AvatarRepository avatarRepository;

    @Mock
    private OkHttpClient mockHttpClient;

    @InjectMocks
    private BaiduAvatarService baiduAvatarService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateTemporaryAvatar_Success() throws Exception {
        String keyword = "test";
        String expectedImageUrl = "http://image-url.com/avatar.png";

        BaiduAvatarService spyService = spy(baiduAvatarService);
        doReturn("mockTaskId").when(spyService).submitAvatarRequest(keyword);
        doReturn(expectedImageUrl).when(spyService).queryAvatarResult("mockTaskId");

        String imageUrl = spyService.generateTemporaryAvatar(keyword);

        assertEquals(expectedImageUrl, imageUrl);
    }

    @Test
    void testSaveAvatar_NewAvatar() {
        Long userId = 1L;
        String imageUrl = "http://image-url.com/avatar.png";
        String keyword = "test";

        when(avatarRepository.findByUserId(userId)).thenReturn(null);

        String result = baiduAvatarService.saveAvatar(userId, imageUrl, keyword);

        assertEquals(imageUrl, result);
        verify(avatarRepository, times(1)).save(any(Avatar.class));
    }

    @Test
    void testSaveAvatar_UpdateExistingAvatar() {
        Long userId = 1L;
        String imageUrl = "http://image-url.com/avatar.png";
        String keyword = "test";

        Avatar existingAvatar = new Avatar();
        existingAvatar.setUserId(userId);
        when(avatarRepository.findByUserId(userId)).thenReturn(existingAvatar);

        String result = baiduAvatarService.saveAvatar(userId, imageUrl, keyword);

        assertEquals(imageUrl, result);
        verify(avatarRepository, times(1)).save(existingAvatar);
    }

    @Test
    void testGetAvatarByUserId_WhenAvatarExists() {
        Long userId = 1L;
        Avatar avatar = new Avatar();
        avatar.setUserId(userId);

        when(avatarRepository.findByUserId(userId)).thenReturn(avatar);

        Optional<Avatar> result = baiduAvatarService.getAvatarByUserId(userId);

        assertTrue(result.isPresent());
        assertEquals(avatar, result.get());
    }

    @Test
    void testGetAvatarByUserId_WhenAvatarDoesNotExist() {
        Long userId = 1L;

        when(avatarRepository.findByUserId(userId)).thenReturn(null);

        Optional<Avatar> result = baiduAvatarService.getAvatarByUserId(userId);

        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteAvatarByUserId_WhenAvatarExists() {
        Long userId = 1L;
        Avatar avatar = new Avatar();
        avatar.setUserId(userId);

        when(avatarRepository.findByUserId(userId)).thenReturn(avatar);

        boolean result = baiduAvatarService.deleteAvatarByUserId(userId);

        assertTrue(result);
        verify(avatarRepository, times(1)).delete(avatar);
    }

    @Test
    void testDeleteAvatarByUserId_WhenAvatarDoesNotExist() {
        Long userId = 1L;

        when(avatarRepository.findByUserId(userId)).thenReturn(null);

        boolean result = baiduAvatarService.deleteAvatarByUserId(userId);

        assertFalse(result);
        verify(avatarRepository, never()).delete(any(Avatar.class));
    }

    @Test
    void testSubmitAvatarRequest_Success() throws Exception {
        String keyword = "test";
        String expectedTaskId = "12345";

        Method getAccessTokenMethod = BaiduAvatarService.class.getDeclaredMethod("getAccessToken");
        getAccessTokenMethod.setAccessible(true);
        String accessToken = (String) getAccessTokenMethod.invoke(baiduAvatarService);

        JSONObject dataObject = new JSONObject();
        dataObject.put("taskId", expectedTaskId);
        JSONObject responseJson = new JSONObject();
        responseJson.put("data", dataObject);

        ResponseBody responseBody = ResponseBody.create(responseJson.toString(), MediaType.parse("application/json"));
        Response mockResponse = new Response.Builder()
                .code(200)
                .message("OK")
                .body(responseBody)
                .protocol(Protocol.HTTP_1_1)
                .request(new Request.Builder().url("http://localhost/").build())
                .build();

        Call mockCall = mock(Call.class);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);

        String taskId = baiduAvatarService.submitAvatarRequest(keyword);
        assertEquals(expectedTaskId, taskId);
    }

    @Test
    void testQueryAvatarResult_Success() throws Exception {
        String taskId = "12345";
        String expectedImageUrl = "http://image-url.com/avatar.png";

        Method getAccessTokenMethod = BaiduAvatarService.class.getDeclaredMethod("getAccessToken");
        getAccessTokenMethod.setAccessible(true);
        String accessToken = (String) getAccessTokenMethod.invoke(baiduAvatarService);

        JSONArray imgUrls = new JSONArray();
        JSONObject imgUrlObject = new JSONObject();
        imgUrlObject.put("image", expectedImageUrl);
        imgUrls.put(imgUrlObject);

        JSONObject dataObject = new JSONObject();
        dataObject.put("imgUrls", imgUrls);

        JSONObject responseJson = new JSONObject();
        responseJson.put("data", dataObject);

        ResponseBody responseBody = ResponseBody.create(responseJson.toString(), MediaType.parse("application/json"));
        Response mockResponse = new Response.Builder()
                .code(200)
                .message("OK")
                .body(responseBody)
                .protocol(Protocol.HTTP_1_1)
                .request(new Request.Builder().url("http://localhost/").build())
                .build();

        Call mockCall = mock(Call.class);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);

        String imageUrl = baiduAvatarService.queryAvatarResult(taskId);
        assertEquals(expectedImageUrl, imageUrl);
    }
}