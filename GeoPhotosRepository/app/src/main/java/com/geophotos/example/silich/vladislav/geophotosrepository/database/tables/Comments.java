package com.geophotos.example.silich.vladislav.geophotosrepository.database.tables;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Lenovo on 17.09.2017.
 */
@DatabaseTable(tableName = "comments")
public class Comments {
    public final static String COMMENTS_IMAGE_ID = "imageId";
    public final static String  COMMENTS_COMMENT_TEXT = "commentText";
    public final static String COMMENTS_COMMENT_ID = "commentId";

    @DatabaseField(generatedId = true)
    private int id;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    @DatabaseField(columnName = COMMENTS_IMAGE_ID)
    private int imageId;
    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = COMMENTS_COMMENT_TEXT)
    private String commentText;
    @DatabaseField(columnName = COMMENTS_COMMENT_ID)
    private int commentId;
}
