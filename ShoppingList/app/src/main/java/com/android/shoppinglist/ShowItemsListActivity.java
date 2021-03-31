package com.android.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.shoppinglist.db.Items;
import com.android.shoppinglist.viewmodel.ShowItemListActivityViewModel;

import java.util.List;

public class ShowItemsListActivity extends AppCompatActivity implements ItemsListAdapter.HandleItemsClick {

    private int category_id;
    private ItemsListAdapter itemsListAdapter;
    private ShowItemListActivityViewModel viewModel;
    private RecyclerView recyclerView;
    private Items itemToUpdate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_items_list);

        category_id = getIntent().getIntExtra("category_id", 0);
        String categoryName = getIntent().getStringExtra("category_name");

        getSupportActionBar().setTitle(categoryName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText addNewItemInput  = findViewById(R.id.addNewItemInput);
        ImageView saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = addNewItemInput.getText().toString();
                if(TextUtils.isEmpty(itemName)) {
                    Toast.makeText(ShowItemsListActivity.this, "Enter Item Nane", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(itemToUpdate == null)
                    saveNewItem(itemName);
                else
                    updateNewItem(itemName);
            }
        });
        initRecyclerView();
        initViewModel();
        viewModel.getAllItemsList(category_id);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(ShowItemListActivityViewModel.class);
        viewModel.getItemsListObserver().observe(this, new Observer<List<Items>>() {
            @Override
            public void onChanged(List<Items> items) {
                if(items == null) {
                    recyclerView.setVisibility(View.GONE);
                    findViewById(R.id.noResult).setVisibility(View.VISIBLE);
                } else {
                    itemsListAdapter.setCategoryList(items);
                    findViewById(R.id.noResult).setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemsListAdapter = new ItemsListAdapter(this, this);
        recyclerView.setAdapter(itemsListAdapter);
    }

    private void saveNewItem(String itemName) {
        Items item = new Items();
        item.itemName  = itemName;
        item.categoryId = category_id;
        viewModel.insertItems(item);
        ((EditText) findViewById(R.id.addNewItemInput)).setText("");
    }

    @Override
    public void itemClick(Items item) {
        if(item.completed) {
            item.completed = false;
        }
         else {
             item.completed = true;
        }
        viewModel.updateItems(item);
    }

    @Override
    public void removeItem(Items item) {
        viewModel.deleteItems(item);
    }

    @Override
    public void editItem(Items item) {
        this.itemToUpdate = item;
        ((EditText) findViewById(R.id.addNewItemInput)).setText(item.itemName);
    }

    private void updateNewItem(String newName) {
        itemToUpdate.itemName = newName;
        viewModel.updateItems(itemToUpdate);
        ((EditText) findViewById(R.id.addNewItemInput)).setText("");
        itemToUpdate = null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}