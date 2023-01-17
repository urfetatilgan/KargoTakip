package com.example.kargotakip;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitInterface {
    @POST("/api/mail/user/tokenId")
    Call<Void> executeLogin(@Body HashMap<String, String> map);

    @POST("/api/mail/user/tokenId")
    Call<Void> executeSignup (@Body HashMap<String, String> map);

    @GET("/api/cargos")
    Call<List<CargoResults>> doGetUserList();
    @POST("/api/cargo/add")
    Call<Void> insertCargo(@Body CargoResults cargoResults);
}
