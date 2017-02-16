package com.thw.shoppingcart.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thw.shoppingcart.R;
import com.thw.shoppingcart.activities.ProductDetailActivity;
import com.thw.shoppingcart.app.Constants;
import com.thw.shoppingcart.db.Product;

import java.util.List;

/*
    Adapter for product list
 */
public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {

    private Activity mActivity;
    private List<Product> mProductList;

    public ProductListAdapter(Activity currActivity, List<Product> items) {
        mActivity = currActivity;
        mProductList = items;
    }

    @Override
    public ProductListAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductListAdapter.ProductViewHolder holder, int position) {
        holder.mProductName.setText(mProductList.get(position).getProduct_name());

        View.OnClickListener listener = itemClickListener();
        holder.mProductContainer.setTag(mProductList.get(position));
        holder.mProductContainer.setOnClickListener(listener);

    }

    private View.OnClickListener itemClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product product = (Product) view.getTag();
                Intent intent = new Intent(mActivity, ProductDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.PRODUCT_SERIALIZABLE_EXTRA, product);
                intent.putExtras(bundle);
                mActivity.startActivity(intent);
            }
        };
    }

    @Override
    public int getItemCount() {
        if (mProductList != null)
            return mProductList.size();
        else
            return 0;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView mProductName;
        private RelativeLayout mProductContainer;

        public ProductViewHolder(View itemView) {
            super(itemView);
            mProductName = (TextView) itemView.findViewById(R.id.product_list_product_name);
            mProductContainer = (RelativeLayout) itemView.findViewById(R.id.product_list_container);
        }
    }
}
