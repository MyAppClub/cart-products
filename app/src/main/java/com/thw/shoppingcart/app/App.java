package com.thw.shoppingcart.app;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.thw.shoppingcart.db.DaoMaster;
import com.thw.shoppingcart.db.DaoSession;


public class App extends Application {

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "user-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
