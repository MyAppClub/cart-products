package com.thw.shoppingcart.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.thw.shoppingcart.R;
import com.thw.shoppingcart.adapters.ProductListAdapter;
import com.thw.shoppingcart.app.App;
import com.thw.shoppingcart.db.CategoryDao;
import com.thw.shoppingcart.db.DaoSession;
import com.thw.shoppingcart.db.Product;
import com.thw.shoppingcart.presenters.HomeActivityPresenter;
import com.thw.shoppingcart.utils.Utils;

import org.json.JSONException;


import java.util.ArrayList;
import java.util.List;

/*
  View products based on categories
 */

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private Resources mResources;
    private Spinner mCategorySpinner;
    private TextView mSelectProductLabel;
    private RecyclerView mProdRecyclerView;
    private HomeActivityPresenter mHomePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar homeToolbar = (Toolbar) findViewById(R.id.activity_home_toolbar);
        setSupportActionBar(homeToolbar);

        init();
        mHomePresenter = new HomeActivityPresenter(HomeActivity.this);

        try {
            mHomePresenter.insertIntoDB();
            mHomePresenter.getCategoryList();
            populateCategories();
        } catch (JSONException e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Utils.showToast(HomeActivity.this, mResources.getString(R.string.home_first_time_load_error), Toast.LENGTH_SHORT);
                    finish();
                    return;
                }
            });
        }
    }

    private void init() {
        mResources = getResources();
        mCategorySpinner = (Spinner) findViewById(R.id.activity_home_category_spinner);
        mCategorySpinner.setOnItemSelectedListener(onCategorySelected());

        mSelectProductLabel = (TextView) findViewById(R.id.activity_home_static_product_label);
        mProdRecyclerView = (RecyclerView) findViewById(R.id.activity_home_product_recyclerview);
        mProdRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mProdRecyclerView.setLayoutManager(layoutManager);
    }

    private void populateCategories() {

        int size = mHomePresenter.getCategorySize();

        if (size == 0) {
            Utils.showToast(HomeActivity.this, mResources.getString(R.string.category_load_error), Toast.LENGTH_LONG);
            return;
        }

        ArrayList<String> categoryList = mHomePresenter.getCategoryList();

        final ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                mCategorySpinner.setAdapter(categoryAdapter);
            }
        });

    }

    private void setProducts() {

        List<Product> products = mHomePresenter.getProductList(mCategorySpinner);

        RecyclerView.Adapter adapter = new ProductListAdapter(HomeActivity.this, products);
        mProdRecyclerView.setAdapter(adapter);
    }

    private AdapterView.OnItemSelectedListener onCategorySelected() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mSelectProductLabel.setVisibility(View.GONE);
                    mProdRecyclerView.setVisibility(View.GONE);
                    mCategorySpinner.setTag(null);
                } else {
                    mSelectProductLabel.setVisibility(View.VISIBLE);
                    mProdRecyclerView.setVisibility(View.VISIBLE);

                    mCategorySpinner.setTag(mHomePresenter.getCategories().get(position - 1).getCategoryId());
                    setProducts();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                if (!isFinishing()) {
                    Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intent);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}
