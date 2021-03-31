package com.android.shoppinglist.viewmodel;

import android.app.Application;
import android.content.ClipData;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.shoppinglist.db.AppDatabase;
import com.android.shoppinglist.db.Category;
import com.android.shoppinglist.db.Items;

import java.util.List;

public class ShowItemListActivityViewModel extends AndroidViewModel {

    private MutableLiveData<List<Items>> listOfItems;
    private AppDatabase appDatabase;

    public ShowItemListActivityViewModel(Application application) {
        super(application);
        listOfItems = new MutableLiveData<>();

        appDatabase = AppDatabase.getDBinstance(getApplication().getApplicationContext());
    }

    public MutableLiveData<List<Items>>  getItemsListObserver() {
        return listOfItems;
    }

    public void getAllItemsList(int categoryID) {
        List<Items> itemsList=  appDatabase.shoppingListDao().getAllItemsList(categoryID);
        if(itemsList.size() > 0)
        {
            listOfItems.postValue(itemsList);
        }else {
            listOfItems.postValue(null);
        }
    }

    public void insertItems(Items item) {

        appDatabase.shoppingListDao().insertItems(item);
        getAllItemsList(item.categoryId);
    }

    public void updateItems(Items item) {
        appDatabase.shoppingListDao().updateItems(item);
        getAllItemsList(item.categoryId);
    }

    public void deleteItems(Items item) {
        appDatabase.shoppingListDao().deleteItem(item);
        getAllItemsList(item.categoryId);
    }

}
