package com.example.mobiledev;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private final DatabaseHelper dbHelper;

    public Cart(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    public void addToCart(int userId, int productId, int quantity) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put("user_id", userId);
            values.put("product_id", productId);
            values.put("quantity", quantity);

            long result = db.insert("cart", null, values);
            if (result == -1) {
                throw new SQLException("Failed to insert row into cart");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateQuantity(int userId, int productId, int quantity) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put("quantity", quantity);

            int rowsUpdated = db.update("cart", values, "user_id = ? AND product_id = ?",
                    new String[]{String.valueOf(userId), String.valueOf(productId)});
            if (rowsUpdated == 0) {
                throw new SQLException("Failed to update row in cart");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeFromCart(int userId, int productId) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            int rowsDeleted = db.delete("cart", "user_id = ? AND product_id = ?",
                    new String[]{String.valueOf(userId), String.valueOf(productId)});
            if (rowsDeleted == 0) {
                throw new SQLException("Failed to delete row from cart");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<CartItem> getCartItems(int userId) {
        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            String query = "SELECT c.product_id, c.quantity, p.name, p.price " +
                    "FROM cart c INNER JOIN products p ON c.product_id = p.id " +
                    "WHERE c.user_id = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

            if (cursor.moveToFirst()) {
                do {
                    int productId = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                    int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));

                    CartItem item = new CartItem(productId, name, price, quantity);
                    cartItems.add(item);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return cartItems;
    }

    public double getTotalPrice(int userId) {
        double totalPrice = 0.0;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            String query = "SELECT SUM(p.price * c.quantity) AS total " +
                    "FROM cart c INNER JOIN products p ON c.product_id = p.id " +
                    "WHERE c.user_id = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

            if (cursor.moveToFirst()) {
                totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return totalPrice;
    }

    public void clearCart(int userId) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            int rowsDeleted = db.delete("cart", "user_id = ?", new String[]{String.valueOf(userId)});
            if (rowsDeleted == 0) {
                throw new SQLException("Failed to clear cart");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static class CartItem {
        private final int productId;
        private final String name;
        private final double price;
        private int quantity;

        public CartItem(int productId, String name, double price, int quantity) {
            this.productId = productId;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        public int getProductId() {
            return productId;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
