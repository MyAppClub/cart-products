package com.thw.shoppingcart.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thw.shoppingcart.R;
import com.thw.shoppingcart.activities.ProductDetailActivity;
import com.thw.shoppingcart.app.App;
import com.thw.shoppingcart.app.Constants;
import com.thw.shoppingcart.db.Cart;
import com.thw.shoppingcart.db.CartDao;
import com.thw.shoppingcart.db.DaoSession;
import com.thw.shoppingcart.presenters.CartPresenter;
import com.thw.shoppingcart.utils.Utils;

import java.util.List;

/*
    Adapter for cart list
 */
public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.CartViewHolder> {

    private static final String TAG = "CartListAdapter";
    private Activity mActivity;
    private CartPresenter mCartPresenter;
    private List<Cart> mCartList;
    private CartProductDelete mListener;
    private String mRupeeSymbol;

    public CartListAdapter(Activity currActivity, CartPresenter cartPresenter, List<Cart> items) {
        mActivity = currActivity;
        mCartPresenter = cartPresenter;
        mCartList = items;
        mListener = (CartProductDelete) mActivity;
        mRupeeSymbol = mActivity.getResources().getString(R.string.indian_rupee);
    }

    @Override
    public CartListAdapter.CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        holder.mCartProductName.setText(mCartList.get(position).getProduct_name());
        holder.mCartProductPrice.setText(mRupeeSymbol + Constants.EMPTY_SPACE + mCartList.get(position).getProduct_price().toString());

        View.OnClickListener deleteListener = deleteClickListener();
        holder.mDeleteIcon.setTag(mCartList.get(position));
        holder.mDeleteIcon.setOnClickListener(deleteListener);

        View.OnClickListener listener = cartItemClickListener();
        holder.mCartListContainer.setTag(mCartList.get(position));
        holder.mCartListContainer.setOnClickListener(listener);
    }

    private View.OnClickListener deleteClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCartPresenter.deleteProductFromCart(view);

                mCartList = mCartPresenter.getCarts();
                notifyDataSetChanged();
                mListener.onCartProductDelete();
                Utils.showToast(mActivity, mActivity.getResources().getString(R.string.label_product_cart_delete), Toast.LENGTH_SHORT);

            }
        };
    }

    private View.OnClickListener cartItemClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cart cart = (Cart) view.getTag();
                Intent intent = new Intent(mActivity, ProductDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.PRODUCT_SERIALIZABLE_EXTRA, cart.getProduct());
                intent.putExtras(bundle);
                mActivity.startActivity(intent);

            }
        };
    }

    @Override
    public int getItemCount() {
        if (mCartList != null)
            return mCartList.size();
        else
            return 0;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mCartListContainer;
        private TextView mCartProductName;
        private TextView mCartProductPrice;
        private ImageButton mDeleteIcon;

        public CartViewHolder(View itemView) {
            super(itemView);
            mCartListContainer = (RelativeLayout) itemView.findViewById(R.id.cart_list_container);
            mCartProductName = (TextView) itemView.findViewById(R.id.cart_product_name);
            mCartProductPrice = (TextView) itemView.findViewById(R.id.cart_product_price);
            mDeleteIcon = (ImageButton) itemView.findViewById(R.id.cart_delete_item);
        }
    }

    public interface CartProductDelete {
        void onCartProductDelete();
    }


}
