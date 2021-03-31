package com.android.shoppinglist.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities =  {Category.class, Items.class}, version = 1)
public abstract class  AppDatabase extends RoomDatabase {

        public abstract  ShoppingListDao shoppingListDao();

        public static AppDatabase INSTANCE;

        public static AppDatabase getDBinstance(Context context) {
            if(INSTANCE == null ) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "AppDB")
                        .allowMainThreadQueries()
                        .build();

            }

            return INSTANCE;
        }
}
