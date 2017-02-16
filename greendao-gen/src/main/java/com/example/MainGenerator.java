package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class MainGenerator {

    private static final String PROJECT_DIR = System.getProperty("user.dir");

    public static void main(String[] args) {
        Schema schema = new Schema(1, "com.thw.shoppingcart.db");
        schema.enableKeepSectionsByDefault();

        addTables(schema);

        try {
            new DaoGenerator().generateAll(schema, PROJECT_DIR + "\\app\\src\\main\\java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTables(final Schema schema) {
        Entity category = addCategory(schema);
        Entity product = addProduct(schema);
        Entity cart = addCart(schema);

        //Creating One-To-Many relation, Category has many products
        Property categoryId = product.addLongProperty("categoryId").notNull().getProperty();
        product.addToOne(category, categoryId);
        category.addToMany(product, categoryId, "products");

        //Creating One-To-Many relation, Product has many carts
        Property cartproductId = cart.addLongProperty("productId").notNull().getProperty();
        cart.addToOne(product, cartproductId);
        product.addToMany(cart, cartproductId, "carts");
    }

    private static Entity addCategory(final Schema schema) {
        Entity category = schema.addEntity("Category");
        category.addLongProperty("categoryId").primaryKey();
        category.addStringProperty("name").notNull();
        category.implementsSerializable();
        return category;
    }

    private static Entity addProduct(final Schema schema) {
        Entity product = schema.addEntity("Product");
        product.addLongProperty("productId").primaryKey();
        product.addStringProperty("product_name").notNull();
        product.addIntProperty("product_price");
        product.addStringProperty("image");
        product.implementsSerializable();
        return product;
    }

    private static Entity addCart(final Schema schema) {
        Entity cart = schema.addEntity("Cart");
        cart.addIdProperty();
        cart.addStringProperty("product_name").notNull();
        cart.addIntProperty("product_price");
        return cart;
    }
}
