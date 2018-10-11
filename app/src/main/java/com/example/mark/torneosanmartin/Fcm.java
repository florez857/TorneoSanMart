package com.example.mark.torneosanmartin;

import com.example.mark.torneosanmartin.FCM.FirebaseCloudMessage;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Fcm {
    @POST("send")
    Call<ResponseBody> send(
            @HeaderMap Map<String, String> headers,
            @Body FirebaseCloudMessage message
    );


}
