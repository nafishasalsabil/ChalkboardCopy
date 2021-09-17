package com.example.chalkboard_copy.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA8WzmvLQ:APA91bG8wYeNn4Ys9ICCaXGxVqm4YJggH89peZTQMhmroLU1ZyXGGAFS-DEyGlrhJvZdDq8qE4-zKj4DX9tCH3AesxsF4XKAWjTdEwWkwELYLRzc3b8XoQCNozb2Y6RYQTbUIkEZxyy8"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotifications(@Body Sender body);
}
