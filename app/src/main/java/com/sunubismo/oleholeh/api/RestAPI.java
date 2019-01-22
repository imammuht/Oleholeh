package com.sunubismo.oleholeh.api;

import com.sunubismo.oleholeh.model.DataResponse;
import com.sunubismo.oleholeh.model.ImageResponse;
import com.sunubismo.oleholeh.model.rating.RatingResponse;
import com.sunubismo.oleholeh.model.toko.TokoResponse;
import com.sunubismo.oleholeh.model.user.LoginResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RestAPI {

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse> login(@Field("email") String email,
                              @Field("password") String password);

    @GET("get-toko.php")
    Call<TokoResponse> loadToko(@Query("keyword") String keyword,
                                @Query("sorting") String sorting);

//    @GET("profile_toko.php")
//    Call<Toko> loadProfileToko(@Query("id") String id);

    @GET("get-rating.php")
    Call<RatingResponse> loadRating(@Query("id") String idtoko);

    @Multipart
    @POST("upload.php")
    Call<ImageResponse> uploadGambar(@Part MultipartBody.Part photo);

    @FormUrlEncoded
    @POST("register.php")
    Call<DataResponse> signup(@Field("gambar") String image,
                              @Field("nama") String name,
                              @Field("email") String email,
                              @Field("password") String password);

    @FormUrlEncoded
    @POST("post-rating.php")
    Call<DataResponse> postRating(@Field("token") String token,
                                  @Field("user_id") String id_user,
                                  @Field("toko_id") String id_toko,
                                  @Field("nilai_rating") String nilai,
                                  @Field("komentar") String komentar);

}