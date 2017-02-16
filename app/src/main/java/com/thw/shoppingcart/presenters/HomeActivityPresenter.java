package com.thw.shoppingcart.presenters;

import android.app.Activity;

import android.widget.Spinner;
import com.thw.shoppingcart.R;
import com.thw.shoppingcart.app.App;
import com.thw.shoppingcart.db.Category;
import com.thw.shoppingcart.db.CategoryDao;
import com.thw.shoppingcart.db.DaoSession;
import com.thw.shoppingcart.db.Product;
import com.thw.shoppingcart.db.ProductDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

/*
  Insert products & Categories
  in DB using greenDao
 */

public class HomeActivityPresenter {

    private Activity mActivity;

    public HomeActivityPresenter(Activity currActivity){
        mActivity = currActivity;
    }

    public void insertIntoDB() throws JSONException {

        DaoSession daoSession = ((App) mActivity.getApplication()).getDaoSession();
        CategoryDao categoryDao = daoSession.getCategoryDao();
        ProductDao productDao = daoSession.getProductDao();

        if (productDao.loadAll().size() > 0) {
            return;
        }

        //TODO:The response will be received from a web service (hard-coded for now)
        //TODO: the images are kept in the drawable folder

        String data = "{\"categories\":[{\"category_id\":\"1\",\"category_name\":\"Electronics\",\"products\":[{\"product_id\":\"1\",\"product_name\":\"Microwave oven\",\"product_price\":\"6000\",\"image\":\"drawable/ic_microwave_oven\"},{\"product_id\":\"2\",\"product_name\":\"Television\",\"product_price\":\"40000\",\"image\":\"drawable/ic_led_tv\"},{\"product_id\":\"3\",\"product_name\":\"Vaccum Cleaner\",\"product_price\":\"3000\", \"image\":\"drawable/ic_vaccum_cleaner\"}]},{\"category_id\":\"2\",\"category_name\":\"Furniture\",\"products\":[{\"product_id\":\"4\",\"product_name\":\"Table\",\"product_price\":\"7000\", \"image\":\"drawable/ic_table\"},{\"product_id\":\"5\",\"product_name\":\"Chair\",\"product_price\":\"2000\", \"image\":\"drawable/ic_chair\"},{\"product_id\":\"6\",\"product_name\":\"Almirah\",\"product_price\":\"10000\", \"image\":\"drawable/ic_almirah\"}]}]}";

        JSONObject json = new JSONObject(data);
        JSONArray categoryArray = json.getJSONArray("categories");
        int categoryLength = categoryArray.length();

        //Parse the json to get categories and products
        for (int i = 0; i < categoryLength; i++) {
            JSONObject categoryObj = categoryArray.getJSONObject(i);
            Category category = new Category();
            category.setCategoryId(categoryObj.getLong("category_id"));
            category.setName(categoryObj.getString("category_name"));
            categoryDao.insertOrReplace(category);

            JSONArray productArray = categoryObj.getJSONArray("products");
            int productLength = productArray.length();
            for (int j = 0; j < productLength; j++) {
                JSONObject productObj = productArray.getJSONObject(j);
                Product product = new Product();
                product.setCategory(category);
                product.setCategoryId(product.getCategoryId());
                product.setProduct_name(productObj.getString("product_name"));
                product.setProductId(productObj.getLong("product_id"));
                product.setProduct_price(productObj.getInt("product_price"));
                String image = productObj.getString("image");
                product.setImage(image);
                productDao.insertOrReplace(product);
            }
        }

    }

    public ArrayList<String> getCategoryList() {

        List<Category> categories = getCategories();

        int size = getCategorySize();

        ArrayList<String> categoryList = new ArrayList<>(size);
        if (size > 0) {
            //adding label- select category
            categoryList.add(mActivity.getString(R.string.label_select_category));
            for (int i = 0; i < size; i++) {
                categoryList.add(categories.get(i).getName());
            }
        }

        return categoryList;
    }

    public List<Category> getCategories(){

        DaoSession daoSession = ((App) mActivity.getApplication()).getDaoSession();
        CategoryDao categoryDao = daoSession.getCategoryDao();
        List<Category> categories = categoryDao.loadAll();
        return categories;
    }

    public int getCategorySize(){
        List<Category> categories = getCategories();
        return categories.size();
    }

    public List<Product> getProductList(Spinner mCategorySpinner) {
        DaoSession daoSession = ((App) mActivity.getApplication()).getDaoSession();
        ProductDao productDao = daoSession.getProductDao();
        //Query the products for the selected category
        List<Product> products = productDao._queryCategory_Products((Long) mCategorySpinner.getTag());
        return products;
    }
}
