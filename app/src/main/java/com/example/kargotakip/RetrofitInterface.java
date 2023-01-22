package com.example.kargotakip;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitInterface {
    @POST("/api/mail/user/tokenId")
    Call<UserResult> executeLogin(@Body HashMap<String, String> map);

    @GET("/api/mail/user/Trendyol")
    Call<Void> getMailsTrendyol (@Query("user_id") int id);
    @POST("/api/mail/user/tokenId")
    Call<Void> executeSignup (@Body HashMap<String, String> map);
    @POST("/api/logout")
    Call<Void> logout();
    @GET("/api/cargos")
    Call<List<CargoResults>> doGetUserList(@Query("user_id") int id);
    @GET("/api/cargos/cargoPath")
    Call<List<CargoResults>> doGetUserListCargoPath(@Query("user_id") int user_id,@Query("cargo_id") String cargo_id);
    @POST("/api/cargo/add")
    Call<Void> insertCargo(@Body CargoResults cargoResults,@Query("user_id")  int id);
}
