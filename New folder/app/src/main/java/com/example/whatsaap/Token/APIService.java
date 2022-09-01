package com.example.whatsaap.Token;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAdkfcchA:APA91bEHslE_mZnCF6oCF-I-kgtABe6f2Yij09r-pofpKD2lDbKk0BepWHTbk3nza1ohxAjjCNOoqVpPF9kYh9021hLkA0fbOXLvxOJx9JqSW8mM_T2tXIpkFrvlhx1MPT-QYk-P2v76"
    })

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);
}
