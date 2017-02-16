package com.thw.shoppingcart.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thw.shoppingcart.R;
import com.thw.shoppingcart.app.App;
import com.thw.shoppingcart.app.Constants;
import com.thw.shoppingcart.db.Cart;
import com.thw.shoppingcart.db.CartDao;
import com.thw.shoppingcart.db.DaoSession;
import com.thw.shoppingcart.db.Product;
import com.thw.shoppingcart.presenters.CartPresenter;
import com.thw.shoppingcart.utils.Utils;

/*
    Renders the details of the
    selected Product and adding to cart
 */

public class ProductDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ProductDetailActivity";
    private Resources mResources;
    private Product mCurrentProduct;
    private ImageView mCurrentProductImage;
    private TextView mCurrentProductName, mCurrentProductPrice;
    private Button mAddToCartBtn;
    private CartPresenter mCartPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        init();
        mCartPresenter = new CartPresenter(this);

        Toolbar detailToolbar = (Toolbar) findViewById(R.id.activity_product_detail_toolbar);
        setSupportActionBar(detailToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Constants.EMPTY_STRING);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mCurrentProduct = (Product) getIntent().getSerializableExtra(Constants.PRODUCT_SERIALIZABLE_EXTRA);
        if (mCurrentProduct == null) {
            Utils.showToast(ProductDetailActivity.this, mResources.getString(R.string.product_load_error), Toast.LENGTH_LONG);
            finish();
            return;
        }
        setData();
    }

    private void init() {
        mResources = getResources();
        mCurrentProductImage = (ImageView) findViewById(R.id.activity_product_detail_product_mage);
        mCurrentProductName = (TextView) findViewById(R.id.activity_product_detail_product_name);
        mCurrentProductPrice = (TextView) findViewById(R.id.activity_product_detail_product_price);

        mAddToCartBtn = (Button) findViewById(R.id.activity_product_detail_addCartBtn);
        mAddToCartBtn.setOnClickListener(this);
    }

    private void setData() {

        //Product place holder in case theere is error loading image
        try {
            int imageResource = mResources.getIdentifier(mCurrentProduct.getImage(), "drawable", getPackageName());
            Drawable image = ContextCompat.getDrawable(ProductDetailActivity.this, imageResource);
            mCurrentProductImage.setImageDrawable(image);
        } catch (Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCurrentProductImage.setImageResource(R.drawable.ic_product_placeholder);
                }
            });
        }

        mCurrentProductName.setText(Constants.EMPTY_SPACE + mCurrentProduct.getProduct_name());
        mCurrentProductPrice.setText(Constants.EMPTY_SPACE + mCurrentProduct.getProduct_price().toString());
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
                    Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                    startActivity(intent);
                }
                return true;

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_product_detail_addCartBtn:
                mCartPresenter.addProductToCart(mCurrentProduct);
                Utils.showToast(ProductDetailActivity.this, mResources.getString(R.string.label_product_added), Toast.LENGTH_SHORT);
                break;
        }
    }
}
