package com.geophotos.example.silich.vladislav.geophotosrepository.database.DAO;

import com.geophotos.example.silich.vladislav.geophotosrepository.database.tables.Comments;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Lenovo on 17.09.2017.
 */

public class CommentsDAO extends BaseDaoImpl<Comments,Integer> {
    public CommentsDAO(ConnectionSource connectionSource, Class<Comments> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
    public List<Comments> getCommentsByImageId(int imageId) throws SQLException {
        QueryBuilder<Comments, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq(Comments.COMMENTS_IMAGE_ID, imageId);
        PreparedQuery<Comments> preparedQuery = queryBuilder.prepare();
        List<Comments> photoCommentList = query(preparedQuery);
        return photoCommentList;
    }

}

