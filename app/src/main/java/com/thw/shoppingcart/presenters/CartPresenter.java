package com.thw.shoppingcart.presenters;

import android.app.Activity;
import android.view.View;

import com.thw.shoppingcart.app.App;
import com.thw.shoppingcart.db.Cart;
import com.thw.shoppingcart.db.CartDao;
import com.thw.shoppingcart.db.DaoSession;
import com.thw.shoppingcart.db.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Add and remove products from the cart
 */

public class CartPresenter {

    private Activity mActivity;
    private Cart cart;

    public CartPresenter(Activity currActivity){
        mActivity = currActivity;
    }

    public void addProductToCart(Product mCurrentProduct){
        cart = new Cart();
        cart.setProduct(mCurrentProduct);
        cart.setProduct_name(mCurrentProduct.getProduct_name());
        cart.setProduct_price(mCurrentProduct.getProduct_price());
        cart.setProductId(mCurrentProduct.getProductId());

        DaoSession daoSession = ((App) mActivity.getApplication()).getDaoSession();
        CartDao cartDao = daoSession.getCartDao();
        cartDao.insertOrReplace(cart);
    }

    public void deleteProductFromCart(View view){

        cart = (Cart) view.getTag();
        DaoSession daoSession = ((App) mActivity.getApplication()).getDaoSession();
        CartDao cartDao = daoSession.getCartDao();
        cartDao.deleteByKey(cart.getId());
    }

    public HashMap<Integer, ArrayList<Cart>> getCartProductsSubTotal() {

        int cartSubtotal = 0;
        ArrayList<Cart> cartItems = new ArrayList<>();
        int size = getCartSize();
        for (int i = 0; i < size; i++) {
            cartItems.add(getCarts().get(i));
            cartSubtotal += getCarts().get(i).getProduct_price();
        }

        HashMap<Integer, ArrayList<Cart>> map = new HashMap<>();
        map.put(cartSubtotal,cartItems);
        return map;
    }

    public List<Cart> getCarts(){
        DaoSession daoSession = ((App) mActivity.getApplication()).getDaoSession();
        CartDao cartDao = daoSession.getCartDao();
        return cartDao.loadAll();
    }

    public int getCartSize(){
        List<Cart> carts = getCarts();
        return carts.size();
    }
}
