package com.thw.shoppingcart.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.thw.shoppingcart.R;
import com.thw.shoppingcart.adapters.CartListAdapter;

import com.thw.shoppingcart.app.Constants;
import com.thw.shoppingcart.db.Cart;
import com.thw.shoppingcart.presenters.CartPresenter;
import com.thw.shoppingcart.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;


/*
    Displays all products added to the cart
 */
public class CartActivity extends AppCompatActivity implements View.OnClickListener, CartListAdapter.CartProductDelete {

    private static final String TAG = "CartActivity";
    private Resources mResources;
    private TextView mCartSubTotal, mEmptyCartLabel, mEmptyCartBtn;
    private RecyclerView mCartRecyclerView;
    private Boolean isAdapterSet = false;
    private CartPresenter mCartPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        init();
        mCartPresenter = new CartPresenter(this);

        Toolbar cartToolbar = (Toolbar) findViewById(R.id.activity_cart_toolbar);
        setSupportActionBar(cartToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(mResources.getString(R.string.activity_cart_title));
        actionBar.setDisplayHomeAsUpEnabled(true);
        Utils.showProgressDialog(CartActivity.this, mResources.getString(R.string.loading_cart_products));
        populateCart();
    }

    private void init() {

        mResources = getResources();
        mCartSubTotal = (TextView) findViewById(R.id.activity_cart_subtotal);
        mEmptyCartLabel = (TextView) findViewById(R.id.activity_cart_label_empty_cart);
        mEmptyCartBtn = (Button) findViewById(R.id.activity_cart_emptyCartBtn);
        mEmptyCartBtn.setOnClickListener(this);
        mCartRecyclerView = (RecyclerView) findViewById(R.id.activity_cart_recyclerview);
        mCartRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mCartRecyclerView.setLayoutManager(layoutManager);

    }

    private void populateCart() {

        Utils.hideProgressDialog(CartActivity.this);
        ArrayList<Cart> cartItems = null;
        int cartSubtotal = 0;

        int size = mCartPresenter.getCartSize();

        if (size == 0) {
            mEmptyCartLabel.setVisibility(View.VISIBLE);
            mEmptyCartBtn.setVisibility(View.VISIBLE);
            mCartSubTotal.setVisibility(View.GONE);
            mCartRecyclerView.setVisibility(View.GONE);
        } else {
            mEmptyCartLabel.setVisibility(View.GONE);
            mEmptyCartBtn.setVisibility(View.GONE);
            mCartSubTotal.setVisibility(View.VISIBLE);
            mCartRecyclerView.setVisibility(View.VISIBLE);

            HashMap<Integer,ArrayList<Cart>> map = mCartPresenter.getCartProductsSubTotal();

            for (Map.Entry<Integer, ArrayList<Cart>> ee : map.entrySet()) {
                cartSubtotal = ee.getKey();
                cartItems = ee.getValue();
            }

            mCartSubTotal.setText(mResources.getString(R.string.label_cart_subtotal) + Constants.EMPTY_SPACE + mResources.getString(R.string.indian_rupee) + Constants.EMPTY_SPACE + cartSubtotal);
            //Prevent adapter being set mutiple times
            if (!isAdapterSet) {
                RecyclerView.Adapter adapter = new CartListAdapter(CartActivity.this, mCartPresenter, cartItems);
                mCartRecyclerView.setAdapter(adapter);
            }

        }

    }

    //Refresh view when product is deleted from cart
    @Override
    public void onCartProductDelete() {
        isAdapterSet = true;
        populateCart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.activity_cart_emptyCartBtn:
                if (!isFinishing()) {
                    Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                    finishAffinity();
                    startActivity(intent);
                }
                break;
        }
    }
}
