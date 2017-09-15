package com.geophotos.example.silich.vladislav.geophotosrepository.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import com.geophotos.example.silich.vladislav.geophotosrepository.manager.DataManager;
import com.geophotos.example.silich.vladislav.geophotosrepository.R;
import com.geophotos.example.silich.vladislav.geophotosrepository.database.HelperFactory;
import com.geophotos.example.silich.vladislav.geophotosrepository.database.tables.Users;
import com.geophotos.example.silich.vladislav.geophotosrepository.database.DAO.UsersDAO;
import com.geophotos.example.silich.vladislav.geophotosrepository.network.req.ModelSignUpInReq;
import com.geophotos.example.silich.vladislav.geophotosrepository.network.res.ModelSignUpInRes;
import com.geophotos.example.silich.vladislav.geophotosrepository.utils.InputValidation;
import com.geophotos.example.silich.vladislav.geophotosrepository.utils.NetworkStatusChecker;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener{
    DataManager manager;
    List<Users>list;
    TabHost tabHost;
    EditText edtLoginIn,edtLoginUp,edtPassIn,edtPassUp,edtRepPassUp;
    Button btnSignIn,btnSignUp;
    private final String signIn = "Login";
    private final String signUp = "Register";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        getSupportActionBar().hide();

        manager = DataManager.getInstance();

        edtLoginIn = (EditText)findViewById(R.id.edt_logSignIn);
        edtLoginUp = (EditText)findViewById(R.id.edt_loginSignUp);
        edtPassIn = (EditText)findViewById(R.id.edt_pasSignIn);
        edtPassUp = (EditText)findViewById(R.id.edt_pasSignUp);
        edtRepPassUp = (EditText)findViewById(R.id.edt_repPasSignUp);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator(signIn);
        tabSpec.setContent(R.id.tab1);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator(signUp);
        tabSpec.setContent(R.id.tab2);
        tabHost.addTab(tabSpec);


        tabHost.setCurrentTabByTag("tag1");
        tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#3DF53D"));
        tabHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.parseColor("#3DF53D"));

        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

        list = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSignIn:
                if (NetworkStatusChecker.isNetworkAvailable(this) == true) {
                    signIn();
                }
                else {
                    localSignIn();
                }
                break;
            case R.id.btnSignUp:
                if (NetworkStatusChecker.isNetworkAvailable(this) == true) {
                    signUp();
                }
                else {
                    Toast.makeText(AuthActivity.this,"No internet connection",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void localSignIn() {
        String login = edtLoginIn.getText().toString();
        if (checkDatabaseUser(login) == false){
            manager.getPreferencesManager().saveUserLogin(login);
            Intent intent = new Intent(AuthActivity.this, GeneralActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(this,"User with this name or password doesn't exist",Toast.LENGTH_LONG).show();
        }
    }

    private void signUp() {
        String nameUp = edtLoginUp.getText().toString();
        String pasUp = edtPassUp.getText().toString();
        String repPasUp = edtRepPassUp.getText().toString();

                if (InputValidation.checkEmptyData(nameUp, pasUp, this)
                        && InputValidation.checkLengthPass(nameUp, pasUp, this) && InputValidation.checkPasswordEquals(pasUp, repPasUp, this)) {
                    Call<ModelSignUpInRes> call = manager.signUpUser(new ModelSignUpInReq(nameUp, pasUp));
                    call.enqueue(new Callback<ModelSignUpInRes>() {
                        @Override
                        public void onResponse(Call<ModelSignUpInRes> call, Response<ModelSignUpInRes> response) {
                            if (response.code() == 200) {
                                addUserDatabase(response);
                            } else if (response.code() == 400) {
                                Toast.makeText(AuthActivity.this,
                                        "This login is used", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<ModelSignUpInRes> call, Throwable t) {
                        }
                    });
                }
            }
    private void signIn()  {
       String nameIn = edtLoginIn.getText().toString();
        String pasIn = edtPassIn.getText().toString();
        if (InputValidation.checkEmptyData(nameIn,pasIn,this) && InputValidation.checkLengthPass(nameIn,pasIn,this)){
            Call<ModelSignUpInRes> call = manager.signInUser(new ModelSignUpInReq(nameIn,pasIn));
            call.enqueue(new Callback<ModelSignUpInRes>() {
                @Override
                public void onResponse(Call<ModelSignUpInRes> call, Response<ModelSignUpInRes> response) {
                    if (response.code() == 200){
                        if (checkDatabaseUser(response.body().getData().getLogin()) == true){
                            addUserDatabase(response);
                        }
                        else {
                            String token = response.body().getData().getToken();
                            manager.getPreferencesManager().saveUserLogin(response.body().getData().getLogin());
                            manager.getPreferencesManager().saveUserToken(response.body().getData().getToken());
                            Intent intent = new Intent(AuthActivity.this, GeneralActivity.class);
                            startActivity(intent);
                        }
                    }
                    else if(response.code() == 400){
                        Toast.makeText(AuthActivity.this,"Login not found",Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<ModelSignUpInRes> call, Throwable t) {
                    int a = 4;
                }
            });
        }
    }
    public void addUserDatabase(Response<ModelSignUpInRes> response){
        Users a = new Users();
        a.setLogin(response.body().getData().getLogin().toString());
        a.setUserId(response.body().getData().getUserId());
        try {
            HelperFactory.getHelper().getUsersDAO().create(a);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        manager.getPreferencesManager().saveUserLogin(response.body().getData().getLogin().toString());
        manager.getPreferencesManager().saveUserToken(response.body().getData().getToken().toString());
        Intent intent = new Intent(AuthActivity.this, GeneralActivity.class);
        startActivity(intent);
    }

    public boolean checkDatabaseUser(String login){
        try {
            UsersDAO usersDAO = HelperFactory.getHelper().getUsersDAO();
            list.addAll(usersDAO.getUserByName(login));
            if (list.size() != 0){
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
