package com.sunubismo.oleholeh.api;

import com.sunubismo.oleholeh.model.DataResponse;
import com.sunubismo.oleholeh.model.Toko;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RestAPI{

    @GET("daftar_toko.php")
    Call<DataResponse> loadToko();

    @GET("profile_toko.php")
    Call<Toko> loadProfileToko(@Query("id") String id);

    @GET("rating.php")
    Call<DataResponse> loadRating(@Query("idtoko") String idtoko);

    @GET("all_rating.php")
    Call<DataResponse> loadAllRating(@Query("idtoko") String idtoko);

    @Multipart
    @POST("signup.php")
    Call<DataResponse> signup(@Part("image") RequestBody image,
                              @Part("name") RequestBody name,
                              @Part("email") RequestBody email,
                              @Part("password") RequestBody password);

    @Multipart
    @POST("login.php")
    Call<DataResponse> login(@Part("email") RequestBody email,
                             @Part("password") RequestBody password);

    @Multipart
    @POST("add_rating.php")
    Call<DataResponse> postRating(@Part("id_user") RequestBody id_user,
                                  @Part("id_toko") RequestBody id_toko,
                                  @Part("nilai") RequestBody nilai,
                                  @Part("komentar") RequestBody komentar);

}