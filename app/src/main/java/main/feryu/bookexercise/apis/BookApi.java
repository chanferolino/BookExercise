package main.feryu.bookexercise.apis;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import main.feryu.bookexercise.models.Book;

/**
 * Created by Christian on 3/4/2016.
 */

public class BookApi {

    public static ArrayList<Book> LBook = new ArrayList<>();

    public static ArrayList<Book> getBook(String uri, @NonNull String requestMethod) {
        String json = HttpUtils.getResponse(uri, requestMethod);

        if (TextUtils.isEmpty(json)) {
            return null;
        }

        String id;
        String title;
        String genre;
        String author;
        boolean isRead;


        try {
            JSONArray jsonarray = new JSONArray(json);
            for(int i=0;i<jsonarray.length();i++) {

                JSONObject jsonobject = jsonarray.getJSONObject(i);
                id=jsonobject.getString("_id");
                title=jsonobject.getString("title");
                genre=jsonobject.getString("genre");
                author=jsonobject.getString("author");
                isRead=jsonobject.getBoolean("isRead");

                LBook.add(new Book(id,title,genre,author,isRead));
            }
            return LBook;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }




    }


}
