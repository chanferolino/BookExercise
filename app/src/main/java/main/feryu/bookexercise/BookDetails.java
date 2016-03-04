package main.feryu.bookexercise;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import main.feryu.bookexercise.apis.BookApi;
import main.feryu.bookexercise.models.Book;
import main.feryu.bookexercise.utils.HttpUtils;

public class BookDetails extends AppCompatActivity {
private long id;
    private EditText mEtTitle;
    private EditText mEtGenre;
    private EditText mEtAuthor;
    private MenuItem menuItem;
    private CheckBox isRead;
    private String title;
    private String genre;
    private String author;
    private boolean read;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mEtTitle = (EditText) findViewById(R.id.title);
        mEtGenre = (EditText) findViewById(R.id.genre);
        mEtAuthor = (EditText) findViewById(R.id.author);
        isRead = (CheckBox)findViewById(R.id.checkBox);
       menuItem = (MenuItem)findViewById(R.id.action_edit);


        mEtTitle.setEnabled(false);
        mEtGenre.setEnabled(false);
        mEtAuthor.setEnabled(false);
       isRead.setChecked(true);

        Intent i = this.getIntent();
        i.getLongExtra("position",id);
        if(id != 111) {
            fetchBooks ft = new fetchBooks();
            ft.execute();
            Log.d("Chan", String.valueOf(id));
        }
        else{
            mEtTitle.setEnabled(true);
            mEtGenre.setEnabled(true);
            mEtAuthor.setEnabled(true);
            isRead.setEnabled(false);

        }
    }
    public class fetchBooks extends AsyncTask<String, Void, ArrayList<Book>> {

        @Override
        protected ArrayList<Book> doInBackground(String... params) {

            return BookApi.getBook("joseniandroid.herokuapp.com/api/books/?_id="+id, "GET");
        }

        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            boolean read;
            toolbar.setTitle(books.get(0).getTitle());
            mEtTitle.setText(books.get(0).getTitle());
            mEtGenre.setText(books.get(0).getGenre());
            mEtAuthor.setText(books.get(0).getAuthor());

            if(books.get(0).isRead()){
                isRead.setChecked(true);
            }else{
                isRead.setChecked(false);
            }
            super.onPostExecute(books);

        }
    }
    public class addBooks extends AsyncTask<String, Void, Book> {

        @Override
        protected void onPreExecute() {
            title = mEtTitle.getText().toString();
            genre = mEtGenre.getText().toString();
            author = mEtAuthor.getText().toString();
            super.onPreExecute();
        }

        @Override
        public Book doInBackground(String... params) {
            String url = "http://joseniandroid.herokuapp.com/api/books/{bookId}";
            JSONObject J = new JSONObject();
            try {
                J.put("title",title);
                J.put("genre",genre);
                J.put("author",author);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            HttpUtils.PUT(url, J);

            return new Book(title,genre,author,false);
        }

        @Override
        protected void onPostExecute(Book book) {
          Intent e = new Intent(BookDetails.this,MainActivity.class);
            startActivity(e);
            super.onPostExecute(book);
        }
    }
    public class deleteBook extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            String url2 = "http://joseniandroid.herokuapp.com/api/books/{bookId}";
            HttpUtils.DELETE(url2);
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bookdetails, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {


            mEtTitle.setEnabled(true);
            mEtGenre.setEnabled(true);
            mEtAuthor.setEnabled(true);
            isRead.setEnabled(true);

            menuItem.setIcon((getResources().getDrawable(R.drawable.ic_done)));
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    read = isRead.isChecked();
                    addBooks ft= new addBooks();
                    ft.execute();



                    return true;
                }
            });

            return true;
        }
        if(id == R.id.action_delete){
//            deleteBook del = new deleteBook();
//            del.execute();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

}
