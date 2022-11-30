package com.tranthikieutrinh.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tranthikieutrinh.bookdb.MainActivity;
import com.tranthikieutrinh.bookdb.R;
import com.tranthikieutrinh.models.Book;

import java.util.List;

public class BookAdapter extends BaseAdapter {

    MainActivity activity;
    int layout;
    List<Book> books;

    public BookAdapter(MainActivity activity, int layout, List<Book> books) {
        this.activity = activity;
        this.layout = layout;
        this.books = books;
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Object getItem(int i) {
        return books.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,null);

            holder.imvThumb = view.findViewById(R.id.imv_Thumb);
            holder.txtName = view.findViewById(R.id.txt_Name);
            holder.txtPrice = view.findViewById(R.id.txt_Price);
            holder.txtPublisher = view.findViewById(R.id.txt_Publisher);

            view.setTag(holder);
        }
        else{
            holder = (ViewHolder) view.getTag();
        }
        //Biding data
        Book book = books.get(i);
        holder.imvThumb.setImageBitmap(book.getBookThumb());
        holder.txtName.setText(book.getBookName());
        holder.txtPrice.setText(String.valueOf(book.getPrice()));
        holder.txtPublisher.setText(book.getPublisher());

        return view;
    }

    private class ViewHolder {
        ImageView imvThumb;
        TextView txtName, txtPrice, txtPublisher;
    }
}
