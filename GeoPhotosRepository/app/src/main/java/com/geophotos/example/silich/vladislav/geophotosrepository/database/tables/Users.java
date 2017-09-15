package com.geophotos.example.silich.vladislav.geophotosrepository.database.tables;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Lenovo on 11.09.2017.
 */
@DatabaseTable(tableName = "users")
public class Users {

    public final static String USERS_ID_USER = "userId";
    public final static String USERS_USER_LOGIN = "login";

    @DatabaseField(generatedId = true)
        private int id;
    @DatabaseField(canBeNull = false, dataType = DataType.INTEGER, columnName = USERS_ID_USER)
        private int userId;
    @DatabaseField(columnName = USERS_USER_LOGIN)
        private String login;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Users(){

    }
}
