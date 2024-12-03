package com.example.mobiledev;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String address;
    private String paymentMethod;
    private final DatabaseHelper dbHelper;
    private final Context context;

    public User(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
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
        updateUser("name", name);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        updateUser("password", password);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        updateUser("address", address);
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        updateUser("payment_method", paymentMethod);
    }

    private void updateUser(String column, String value) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(column, value);

        db.update("users", values, "id = ?", new String[]{String.valueOf(this.id)});
        db.close();
    }

    public boolean loginUser(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", new String[]{email, password});

        if (cursor.moveToFirst()) {
            this.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            this.name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            this.email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            this.password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            this.address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
            this.paymentMethod = cursor.getString(cursor.getColumnIndexOrThrow("payment_method"));
            cursor.close();
            db.close();
            return true;
        } else {
            cursor.close();
            db.close();
            return false;
        }
    }

    public boolean registerUser(String name, String email, String password) {
        if (!isValidEmail(email)) {
            Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (isEmailExists(email)) {
            Toast.makeText(context, "Email already exists", Toast.LENGTH_SHORT).show();
            return false;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);

        long id = db.insert("users", null, values);
        db.close();

        if (id != -1) {
            this.id = (int) id;
            this.name = name;
            this.email = email;
            this.password = password;
            Toast.makeText(context, "User registered successfully", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean loadUser(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE id = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            this.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            this.name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            this.email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            this.password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            this.address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
            this.paymentMethod = cursor.getString(cursor.getColumnIndexOrThrow("payment_method"));
            cursor.close();
            db.close();
            return true;
        } else {
            cursor.close();
            db.close();
            return false;
        }
    }

    private boolean isEmailExists(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }


    private boolean isValidEmail(String email) {
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
