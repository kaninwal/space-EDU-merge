package com.spacECE.spaceceedu.LibForSmall;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spacECE.spaceceedu.LibForSmall.books;
import com.spacECE.spaceceedu.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class library_RecycleAdapter extends RecyclerView.Adapter<library_RecycleAdapter.MyViewHolder>{

    ArrayList<books> list;
    private final library_RecycleAdapter.RecyclerViewClickListener listener;

    public library_RecycleAdapter(ArrayList<books> myList, RecyclerViewClickListener listener) {
        this.list = myList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public library_RecycleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.library_list_listitem, parent, false);
        return new library_RecycleAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        books currentBook = list.get(position);
        holder.book_name.setText(currentBook.getProduct_title());
        holder.book_category.setText(currentBook.getProduct_desc());
        holder.book_price.setText(currentBook.getExchange_price());

        String imageUrl = currentBook.getProduct_image();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            if (!imageUrl.startsWith("http")) {
                imageUrl = "https://hustle-7c68d043.mileswebhosting.com/spacece/libforsmall/product_images/" + imageUrl;
            }
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.book_image);
        } else {
            holder.book_image.setImageResource(R.drawable.logo);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView book_name;
        private final TextView book_category ;
        private final TextView book_price;
        private final ImageView book_image;

        public MyViewHolder(@NonNull View view) {
            super(view);
            book_name=view.findViewById(R.id.cardview_bookname);
            book_price=view.findViewById(R.id.cardview_price);
            book_category=view.findViewById(R.id.cardview_category);
            book_image = view.findViewById(R.id.cardview_bookimage);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getBindingAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }
}
