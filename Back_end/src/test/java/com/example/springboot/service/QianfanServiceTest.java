package com.example.springboot.service;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class QianfanServiceTest {

    @Mock
    private OkHttpClient mockHttpClient;

    @InjectMocks
    private QianfanService qianfanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAIAnswer_Success() throws Exception {
        String question = "What is AI?";
        String expectedAnswer = "AI is artificial intelligence.";


        Method getAccessTokenMethod = QianfanService.class.getDeclaredMethod("getAccessToken");
        getAccessTokenMethod.setAccessible(true);
        String mockAccessToken = (String) getAccessTokenMethod.invoke(qianfanService);


        JSONObject responseJson = new JSONObject();
        responseJson.put("result", expectedAnswer);

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


        String aiAnswer = qianfanService.getAIAnswer(question);
        assertEquals(expectedAnswer, aiAnswer);
    }

    @Test
    void testGetAIAnswer_Failure() throws Exception {
        String question = "What is AI?";


        Method getAccessTokenMethod = QianfanService.class.getDeclaredMethod("getAccessToken");
        getAccessTokenMethod.setAccessible(true);
        String mockAccessToken = (String) getAccessTokenMethod.invoke(qianfanService);


        JSONObject responseJson = new JSONObject();
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


        String aiAnswer = qianfanService.getAIAnswer(question);
        assertEquals("AI 无法生成回答。", aiAnswer);
    }

    @Test
    void testGetAccessToken_Success() throws Exception {

        JSONObject responseJson = new JSONObject();
        responseJson.put("access_token", "mockAccessToken");

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


        Method getAccessTokenMethod = QianfanService.class.getDeclaredMethod("getAccessToken");
        getAccessTokenMethod.setAccessible(true);
        String accessToken = (String) getAccessTokenMethod.invoke(qianfanService);

        assertEquals("mockAccessToken", accessToken);
    }

    @Test
    void testGetAccessToken_Failure() throws Exception {

        Call mockCall = mock(Call.class);
        when(mockCall.execute()).thenReturn(new Response.Builder()
                .code(200)
                .message("OK")
                .body(null)
                .protocol(Protocol.HTTP_1_1)
                .request(new Request.Builder().url("http://localhost/").build())
                .build());

        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);

        Method getAccessTokenMethod = QianfanService.class.getDeclaredMethod("getAccessToken");
        getAccessTokenMethod.setAccessible(true);

        assertThrows(IOException.class, () -> getAccessTokenMethod.invoke(qianfanService), "无法获取 Access Token");
    }
}