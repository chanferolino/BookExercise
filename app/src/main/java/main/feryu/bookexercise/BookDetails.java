package main.feryu.bookexercise;

import android.app.ProgressDialog;
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
import main.feryu.bookexercise.fragments.ListViewFragment;
import main.feryu.bookexercise.models.Book;
import main.feryu.bookexercise.utils.HttpUtils;

public class BookDetails extends AppCompatActivity {
private int position;
    private EditText mEtTitle;
    private EditText mEtGenre;
    private EditText mEtAuthor;
    private MenuItem menuItem;
    private CheckBox isRead;
    private String title;
    private String genre;
    private String author;
    private int ic;
    private boolean read;
    private Toolbar toolbar;
    private String taytol;
    private Menu mMenu;
    private MenuItem mItem;
    private ProgressDialog dialog;
    private ListViewFragment mListViewFragment;
    private int flag=0;

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
        mListViewFragment = ListViewFragment.newInstance();
       dialog = new ProgressDialog(BookDetails.this);


        mEtTitle.setEnabled(false);
        mEtGenre.setEnabled(false);
        mEtAuthor.setEnabled(false);
       isRead.setEnabled(false);

        Intent i = getIntent();
       position = i.getIntExtra("position",0);
        ic = i.getIntExtra("ic", 0);
       taytol= i.getStringExtra("title");

        if(ic != 100) {
            fetchBooks ft = new fetchBooks();
            ft.execute();
            flag=0;
            Log.d("Chan", String.valueOf(position));
            if(taytol!=null) {
                Log.d("Chan", taytol);
            }
            if(menuItem==null) {
                Log.d("Chan", "menu is null");

            }
        }
        else{
            mEtTitle.setEnabled(true);
            mEtGenre.setEnabled(true);
            mEtAuthor.setEnabled(true);
            isRead.setEnabled(true);
           flag=1;
        }
    }
    public class fetchBooks extends AsyncTask<String, Void, ArrayList<Book>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Getting Book Data");
            dialog.show();
        }

        @Override
        protected ArrayList<Book> doInBackground(String... params) {

            Log.d("haha","http://joseniandroid.herokuapp.com/api/books?title="+taytol);
            Log.d("haha", String.valueOf(BookApi.getBook("http://joseniandroid.herokuapp.com/api/books?title=" + taytol, "GET")));
            return BookApi.getBook("http://joseniandroid.herokuapp.com/api/books?title="+taytol, "GET");

        }

        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            boolean read;
            if(ic!=100) {
                toolbar.setTitle(books.get(position).getTitle());
                mEtTitle.setText(books.get(position).getTitle());
                mEtGenre.setText(books.get(position).getGenre());
                mEtAuthor.setText(books.get(position).getAuthor());

                if (books.get(position).isRead()) {
                    isRead.setChecked(true);
                } else {
                    isRead.setChecked(false);
                }

            }
            super.onPostExecute(books);
            dialog.dismiss();

        }
    }
    public class addBooks extends AsyncTask<String, Void, Book> {

        @Override
        protected void onPreExecute() {
            title = mEtTitle.getText().toString();
            genre = mEtGenre.getText().toString();
            author = mEtAuthor.getText().toString();
            read =  isRead.isChecked();

            super.onPreExecute();

            dialog.setMessage("Saving Book Data");
            dialog.show();


        }

        @Override
        public Book doInBackground(String... params) {
            String url = "http://joseniandroid.herokuapp.com/api/books";
            JSONObject J = new JSONObject();
            try {
                J.put("title",title);
                J.put("genre",genre);
                J.put("author",author);
                J.put("isRead",read);
                Log.d("Added", "Title:" + title);
                Log.d("Added","Genre:"+genre);
                Log.d("Added","Author:"+author);
                Log.d("Added","isRead:"+read);
                Log.d("Added","JSON: "+J);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            HttpUtils.PUT(url, J);

            return new Book(title,genre,author,false);
        }

        @Override
        protected void onPostExecute(Book book) {


            super.onPostExecute(book);
            dialog.dismiss();
            mListViewFragment = ListViewFragment.newInstance();

            getSupportFragmentManager()
                    .beginTransaction()
                    .detach(mListViewFragment)
                    .attach(mListViewFragment)
                    .commit();
            Intent e = new Intent(BookDetails.this,MainActivity.class);
            startActivity(e);
        }

    }
    public class deleteBook extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            String url2 = "http://joseniandroid.herokuapp.com/api/books";
            HttpUtils.DELETE(url2);
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bookdetails, menu);
        mMenu = menu;

        if(flag%2==1){
            mMenu.findItem(R.id.action_edit).setIcon(R.drawable.ic_done);

        }
        else{
            mMenu.findItem(R.id.action_edit).setIcon(R.drawable.ic_edit);
            flag++;
        }
        menuItem = menu.findItem(R.id.action_edit);
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


            if(flag%2==1){
                mMenu.findItem(R.id.action_edit).setIcon(R.drawable.ic_done);
                flag++;
            }
            else{
                mMenu.findItem(R.id.action_edit).setIcon(R.drawable.ic_edit);
                flag++;
            }



                mEtTitle.setEnabled(true);
                mEtGenre.setEnabled(true);
                mEtAuthor.setEnabled(true);
                isRead.setEnabled(true);


                menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    read = isRead.isChecked();
                    addBooks ft = new addBooks();
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
