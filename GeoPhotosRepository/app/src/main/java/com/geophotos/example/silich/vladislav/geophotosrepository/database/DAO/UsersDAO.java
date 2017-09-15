package com.geophotos.example.silich.vladislav.geophotosrepository.database.DAO;

import com.geophotos.example.silich.vladislav.geophotosrepository.database.tables.Users;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Lenovo on 12.09.2017.
 */

public class UsersDAO extends BaseDaoImpl <Users,Integer> {

    public UsersDAO(ConnectionSource connectionSource, Class<Users> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
    public List<Users> getAllUsers()throws SQLException{
        return this.queryForAll();
    }
    public List<Users> getUserByName(String name)  throws SQLException{
        QueryBuilder<Users, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq(Users.USERS_USER_LOGIN, name);
        PreparedQuery<Users> preparedQuery = queryBuilder.prepare();
        List<Users> userList =query(preparedQuery);
        return userList;
    }
}
