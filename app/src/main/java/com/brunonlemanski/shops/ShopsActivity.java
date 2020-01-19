package com.brunonlemanski.shops;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brunonlemanski.shops.database.DbAdapter;
import com.brunonlemanski.shops.database.DbHelper;
import com.brunonlemanski.shops.database.ShopAdapter;


public class ShopsActivity extends Activity {

    //UI elements
    private Button addShop;
    private RecyclerView recyclerView;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shops_activity);

        DbHelper dbHelper = new DbHelper(this);
        sqLiteDatabase = dbHelper.getWritableDatabase();

        addShop = findViewById(R.id.addNewShopButton);

        recyclerView = findViewById(R.id.rvShops);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        addShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopsActivity.this, AddShopActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        ShopAdapter adapter = new ShopAdapter(getAllItems(), this);
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sqLiteDatabase.close();
    }

    private Cursor getAllItems() {
        return sqLiteDatabase.query(DbAdapter.DbEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                DbAdapter.DbEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }


    /**
     * Removing item from Recycler View and database.
     * @param tag Id of item.
     */
    private void removeItem(long tag) {
        sqLiteDatabase.delete(
                DbAdapter.DbEntry.TABLE_NAME,
                DbAdapter.DbEntry._ID + "=" + tag, null);
    }
}
