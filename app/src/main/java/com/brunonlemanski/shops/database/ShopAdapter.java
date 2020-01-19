package com.brunonlemanski.shops.database;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brunonlemanski.shops.R;


public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder>{

    private Context context;
    private Cursor mCursor;


    public ShopAdapter(Cursor mCursor, Context context) {
        this.context = context;
        this.mCursor = mCursor;
    }

    /**
     * Viewholder implementation.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public TextView desc;
        public TextView radius;
        public ImageView delete;



        @Override
        public void onClick(View v) {

        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tvName);
            desc = itemView.findViewById(R.id.tvDesc);
            radius = itemView.findViewById(R.id.tvRadius);

            //edycja po kliknieciu w element
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, "Item: ", Toast.LENGTH_LONG).show();
                }
            });



        }
    }


    @Override
    public ShopAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if(!mCursor.moveToPosition(position)) {
            return;
        }

        //ShopModel model = list.get(position);
        holder.name.setText(mCursor.getString(mCursor.getColumnIndex(DbAdapter.DbEntry.COLUMN_NAME)));
        holder.desc.setText(mCursor.getString(mCursor.getColumnIndex(DbAdapter.DbEntry.COLUMN_DESC)));
        holder.radius.setText(mCursor.getString(mCursor.getColumnIndex(DbAdapter.DbEntry.COLUMN_RADIUS)));

        long id = mCursor.getLong(mCursor.getColumnIndex(DbAdapter.DbEntry._ID));



    }


    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
}
