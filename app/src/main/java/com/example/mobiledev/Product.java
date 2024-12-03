package com.example.mobiledev;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Product implements Serializable {
    private int id;
    private String name;
    private String description;
    private double price;
    private String brand;
    private String type;
    private String age;
    private String reviews;
    private String image;
    private DatabaseHelper dbHelper;

    public Product(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public Product(int id, String name, String description, double price, String brand, String type, String age, String reviews, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.brand = brand;
        this.type = type;
        this.age = age;
        this.reviews = reviews;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getBrand() {
        return brand;
    }

    public String getType() {
        return type;
    }

    public String getAge() {
        return age;
    }

    public String getReviews() {
        return reviews;
    }

    public String getImage() {
        return image;
    }

    private Product extractProductFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
        double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
        String brand = cursor.getString(cursor.getColumnIndexOrThrow("brand"));
        String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
        String age = cursor.getString(cursor.getColumnIndexOrThrow("age"));
        String reviews = cursor.getString(cursor.getColumnIndexOrThrow("reviews"));
        String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));

        return new Product(id, name, description, price, brand, type, age, reviews, image);
    }

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query("products", null, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Product product = extractProductFromCursor(cursor);
                    productList.add(product);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("Product", "Error fetching products", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return productList;
    }

    public List<Product> searchProductsByName(String searchTerm) {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM products WHERE name LIKE ?";
            String[] selectionArgs = new String[]{"%" + searchTerm + "%"};
            cursor = db.rawQuery(query, selectionArgs);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Product product = extractProductFromCursor(cursor);
                    productList.add(product);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("Product", "Error searching products by name", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return productList;
    }

    public static List<String> getUniqueBrands(Context context) {
        List<String> brands = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT brand FROM Products", null);
        if (cursor.moveToFirst()) {
            do {
                brands.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return brands;
    }

    public static List<String> getUniqueTypes(Context context) {
        List<String> types = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT type FROM Products", null);
        if (cursor.moveToFirst()) {
            do {
                types.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return types;
    }

    public static List<String> getUniqueAges(Context context) {
        List<String> ages = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT age FROM Products", null);
        if (cursor.moveToFirst()) {
            do {
                ages.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return ages;
    }

    public List<Product> filterProducts(String brand, String type, String age) {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM products WHERE brand LIKE ? AND type LIKE ? AND age LIKE ?";
            String[] selectionArgs = new String[]{
                    brand.equals("All") ? "%" : brand,
                    type.equals("All") ? "%" : type,
                    age.equals("All") ? "%" : age
            };
            cursor = db.rawQuery(query, selectionArgs);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Product product = extractProductFromCursor(cursor);
                    productList.add(product);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("Product", "Error filtering products", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return productList;
    }
}
