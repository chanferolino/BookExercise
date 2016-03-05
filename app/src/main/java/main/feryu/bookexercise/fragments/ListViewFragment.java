package main.feryu.bookexercise.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import main.feryu.bookexercise.BookDetails;
import main.feryu.bookexercise.R;
import main.feryu.bookexercise.adapter.BookAdapter;
import main.feryu.bookexercise.apis.BookApi;
import main.feryu.bookexercise.models.Book;

/**
 * Created by Christian on 3/4/2016.
 */
public class ListViewFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private TextView mTvEmpty;
    private List<Book> LBook = new ArrayList<>();
    private BookAdapter adapter;
    private ProgressBar mProgressBar;
    private ProgressDialog dialog;
    public static ListViewFragment newInstance() {
        return new ListViewFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("chan", "check");
        dialog = new ProgressDialog(getContext());

        FetchBooks ft= new FetchBooks();
        ft.execute();

    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_listview, container, false);
    }


    @Override
    public void onViewCreated (View view, @Nullable Bundle savedInstanceState){
        // find all the views
        mListView = (ListView) view.findViewById(R.id.listView);
        mProgressBar = (ProgressBar)view.findViewById(R.id.progressBar);

        // create a new instance of adapter
        adapter = new BookAdapter(getActivity(),
                R.layout.list_item, LBook);

        // set the adapter
        mListView.setAdapter(adapter);





        // set item click listener
        mListView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick (AdapterView < ? > parent, View view,int position, long id){


        Intent i = new Intent(getContext(), BookDetails.class);
        Log.d("chan", String.valueOf(id));
        Log.d("chan", String.valueOf(position));

        i.putExtra("position",position);
        i.putExtra("title", LBook.get(position).getTitle());
        startActivity(i);


    }


    public class FetchBooks extends AsyncTask<String, Void, ArrayList<Book>> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            dialog.setMessage("Getting Book Data");
            dialog.show();

        }

        @Override
        protected ArrayList<Book> doInBackground(String... params) {

            return BookApi.getBook("http://joseniandroid.herokuapp.com/api/books", "GET");
        }

        @Override
        protected void onPostExecute(ArrayList<Book> books) {

            super.onPostExecute(books);
            dialog.dismiss();
            if(books==null) {
                Log.d("chan","way sud");
                return;
            }
            Log.d("chan", String.valueOf(books));
            adapter.addAll(books);
            adapter.notifyDataSetChanged();
            mProgressBar.setVisibility(View.GONE);
            dialog.dismiss();
        }
    }

}

