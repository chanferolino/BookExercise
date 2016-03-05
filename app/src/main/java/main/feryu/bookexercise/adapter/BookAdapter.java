package main.feryu.bookexercise.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import main.feryu.bookexercise.R;
import main.feryu.bookexercise.models.Book;

/**
 * Created by Christian on 3/4/2016.
 */
public class BookAdapter extends ArrayAdapter<Book> {

    private Context mContext;
    private int    mLayoutId;
    private List<Book> mBooks= new ArrayList<>();

    public BookAdapter(Context context, int resource, List<Book> movies) {
        super(context, resource, movies);

        mContext = context;
        mLayoutId = resource;
        mBooks = movies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            // Inflate the layout
            convertView = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);

            // create the view holder
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.txtBookName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Set the movie data
        Book book = mBooks.get(position);

        if (book != null) {
            if (viewHolder.tvName != null) {
                viewHolder.tvName.setText(book.getTitle());
                if(book.isRead()==true)
                {
                    viewHolder.tvName.setTextColor(Color.RED);
                    viewHolder.tvName.setPaintFlags(viewHolder.tvName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
        }

        return convertView;
    }

    private static class ViewHolder {
        public TextView  tvName;
    }



}
