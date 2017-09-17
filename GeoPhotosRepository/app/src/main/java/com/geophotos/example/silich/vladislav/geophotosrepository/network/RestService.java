package com.geophotos.example.silich.vladislav.geophotosrepository.network;

import com.geophotos.example.silich.vladislav.geophotosrepository.network.req.ModelCommentReq;
import com.geophotos.example.silich.vladislav.geophotosrepository.network.req.ModelImageReq;
import com.geophotos.example.silich.vladislav.geophotosrepository.network.req.ModelSignUpInReq;
import com.geophotos.example.silich.vladislav.geophotosrepository.network.res.ModelGetCommentRes;
import com.geophotos.example.silich.vladislav.geophotosrepository.network.res.ModelSendCommentRes;
import com.geophotos.example.silich.vladislav.geophotosrepository.network.res.ModelDeleteImage;
import com.geophotos.example.silich.vladislav.geophotosrepository.network.res.ModelGetPhotoRes;
import com.geophotos.example.silich.vladislav.geophotosrepository.network.res.ModelImageRes;
import com.geophotos.example.silich.vladislav.geophotosrepository.network.res.ModelSignUpInRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Lenovo on 11.09.2017.
 */

public interface RestService {
    @POST("account/signup")
    Call<ModelSignUpInRes> signUp (@Body ModelSignUpInReq reqSignUp);

    @POST("account/signin")
    Call<ModelSignUpInRes> signIn (@Body ModelSignUpInReq reqSignIn);

    @POST("image")
    Call<ModelImageRes> sendImage(@Header("Access-Token")String token, @Body ModelImageReq modelImageReq);

    @GET("image")
    Call<ModelGetPhotoRes> getImage (@Query("page") int page, @Header("Access-Token") String token);

    @DELETE("image/{id}")
    Call<ModelDeleteImage> deleteImage(@Path("id") int id, @Header("Access-Token") String token);

    @POST("image/{imageId}/comment")
    Call<ModelSendCommentRes> sendComment(@Path("imageId") int idImage, @Body ModelCommentReq modelCommentReq, @Header("Access-Token") String token);

    @GET("image/{imageId}/comment")
    Call<ModelGetCommentRes> getComment(@Path("imageId") int idImage, @Query("page") int page, @Header("Access-Token") String token);
}
