package com.example.mobiledev;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class Content {
    private int id;
    private final String type;
    private final String title;
    private final String description;
    private final String url;

    public Content(int id, String type, String title, String description, String url) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.description = description;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description + "<br><br><font color='#717171'>Click to see more...</font>";
    }

    public static List<Content> fetchAllContent(SQLiteDatabase db) {
        List<Content> contentList = new ArrayList<>();
        Cursor cursor = db.query("content", null, null, null, null, null, null);

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                        String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                        String url = cursor.getString(cursor.getColumnIndexOrThrow("url"));

                        Content content = new Content(id, type, title, description, url);
                        contentList.add(content);
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }

        return contentList;
    }

}
