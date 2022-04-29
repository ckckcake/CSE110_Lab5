package com.example.lab5;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.RoomDatabase;
import androidx.room.Update;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

@Entity(tableName = "todo_list_items")
public class TodoListItem {
    // 1. Public fields.

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String text;
    public boolean completed;
    public int order;

    // 2.  Constructor matching fields above.
    TodoListItem(@NonNull String text,  boolean completed, int order) {
        this.text = text;
        this.completed = completed;
        this.order = order;
    }

    // 3. Factory method for loading our JSON
    public static List<TodoListItem> loadJSON(Context context, String path) {
        try {
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<TodoListItem>>(){}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public String toString() {
        return "TodoListItem{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", completed=" + completed +
                ", order=" + order +
                '}';
    }

    @Dao
    public interface TodoListItemDao {
        @Insert
        long insert(TodoListItem todoListItem);

        @Query("SELECT * FROM 'todo_list_items' WHERE 'id'=:id")
        TodoListItem get(long id);

        @Query("SELECT * FROM 'todo_list_items' ORDER BY 'order'")
        List<TodoListItem> getAll();

        @Update
        int update(TodoListItem todoListItem);

        @Delete
        int delete(TodoListItem todoListItem);
    }

    @Database(entities = {TodoListItem.class}, version = 1)
    public abstract class TodoDatabase extends RoomDatabase {
        public abstract TodoListItemDao todoListItemDao();
    }
}
