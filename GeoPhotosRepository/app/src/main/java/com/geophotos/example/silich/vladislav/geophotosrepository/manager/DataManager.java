package com.geophotos.example.silich.vladislav.geophotosrepository.manager;

import com.geophotos.example.silich.vladislav.geophotosrepository.network.RestService;
import com.geophotos.example.silich.vladislav.geophotosrepository.network.ServiceGenerator;
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

/**
 * Created by Lenovo on 11.09.2017.
 */

 public class DataManager {
    private static DataManager INSTANCE = null;
    private RestService mRestService;
    private PreferencesManager mPreferencesManager;

    private DataManager() {
        this.mRestService = ServiceGenerator.createService(RestService.class);
        this.mPreferencesManager = new PreferencesManager();
    }

    public static DataManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public PreferencesManager getPreferencesManager(){
        return mPreferencesManager;
    }
    public Call<ModelSignUpInRes> signUpUser(ModelSignUpInReq signInReq){
        return mRestService.signUp(signInReq);
    }
    public Call<ModelSignUpInRes> signInUser(ModelSignUpInReq signInReq){
        return mRestService.signIn(signInReq);
    }
    public  Call<ModelImageRes> sendImages(String token, ModelImageReq modelImageReq){
        return mRestService.sendImage(token,modelImageReq);
    }
    public Call<ModelGetPhotoRes> getImageUser(int page, String token){
        return mRestService.getImage(page,token);
    }
    public Call<ModelDeleteImage> deleteImageById(int id, String token){
        return mRestService.deleteImage(id,token);
    }
    public Call<ModelSendCommentRes> sendComments(int id, ModelCommentReq modelCommentReq, String token){
        return mRestService.sendComment(id,modelCommentReq,token);
    }
    public Call<ModelGetCommentRes> getComments(int id, int page, String token){
        return mRestService.getComment(id,page,token);
    }
    public Call<ModelGetCommentRes> deleteComments(int idImage,int idComment,String token){
        return mRestService.deleteComment(idImage,idComment,token);
    }
}

