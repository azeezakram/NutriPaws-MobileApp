package com.example.mobiledev;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dog_food_app.db";
    private static final int DATABASE_VERSION = 1;

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        DatabaseHelper instance = null;
        instance = new DatabaseHelper(context.getApplicationContext());
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL, " +
                "name TEXT, " +
                "address TEXT, " +
                "payment_method TEXT)");

        db.execSQL("CREATE TABLE products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "description TEXT, " +
                "price REAL, " +
                "brand TEXT, " +
                "type TEXT, " +
                "age TEXT, " +
                "reviews TEXT, " +
                "image TEXT)");

        db.execSQL("CREATE TABLE cart (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "product_id INTEGER, " +
                "quantity INTEGER, " +
                "FOREIGN KEY(user_id) REFERENCES users(id), " +
                "FOREIGN KEY(product_id) REFERENCES products(id))");

        db.execSQL("CREATE TABLE content (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "type TEXT, " +
                "title TEXT, " +
                "description TEXT, " +
                "url TEXT)");


        insertInitialProducts(db);
        insertInitialEducationalContent(db);
    }

    private void insertInitialProducts(SQLiteDatabase db) {
        db.execSQL("INSERT INTO products (name, description, price, brand, type, age, reviews, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                new Object[]{"Chicken and Rice", "Nutritious chicken and rice formula.", 6300, "NutriPet", "Wet", "Puppy", "Loved by puppies, great taste!" , "product_pictures/1.webp"});
        db.execSQL("INSERT INTO products (name, description, price, brand, type, age, reviews, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                new Object[]{"SALMON & OCEAN FISH", "Salmon-based dog food with added vitamins.", 4750, "FishyTreats", "Wet", "Senior", "Perfect for older dogs with joint issues.", "product_pictures/2.webp"});
        db.execSQL("INSERT INTO products (name, description, price, brand, type, age, reviews, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                new Object[]{"Beef & Barley Recipe", "Beef and barley formula with added vitamins and minerals.", 7423, "Performatrin Naturals", "Dry", "Adult", "Great for active dogs, high protein content.", "product_pictures/3.webp"});
        db.execSQL("INSERT INTO products (name, description, price, brand, type, age, reviews, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                new Object[]{"Lamb & Sweet Potato", "Merrick Grain-Free Real Lamb & Sweet Potato Recipe Dry Dog Food 22 lbs.", 7124, "Merrick", "Dry", "Puppy", "Good for sensitive stomachs.", "product_pictures/4.webp"});
        db.execSQL("INSERT INTO products (name, description, price, brand, type, age, reviews, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                new Object[]{"Puppy Growth Formula", "SFidele+ Veterinary Growth Formula 1.5kg/4kg puppy food.", 12500, "SFidele", "Dry", "Puppy", "Helps with brain and eye development.", "product_pictures/5.webp"});
        db.execSQL("INSERT INTO products (name, description, price, brand, type, age, reviews, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                new Object[]{"Senior Care Chicken", "BIXBI Hip & Joint Support Chicken Jerky Dog Treats, 12 oz - USA Made Grain Free Dog Treats - Glucosamine, Chondroitin for Dogs - High in Protein, Antioxidant Rich, Whole Food Nutrition, No Fillers", 8240, "BIXBI", "Dry", "Senior", "Supports joint health and overall well-being.", "product_pictures/6.webp"});
        db.execSQL("INSERT INTO products (name, description, price, brand, type, age, reviews, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                new Object[]{"Grain-Free Turkey", "With premium cage-free turkey as a single source of poultry protein, this tasty Go! Solutions Limited Ingredient recipe is specially designed to support dogs with sensitive dietary needs, providing perfectly-balanced nutrition using as few ingredients as possible.",
                        5900, "Pet Curean", "Dry", "Adult", "Ideal for dogs with grain allergies.", "product_pictures/7.webp"});
        db.execSQL("INSERT INTO products (name, description, price, brand, type, age, reviews, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                new Object[]{"Dental Care Chews", "Dental chews to promote oral health and fresh breath.", 3250, "Health Extension", "Chew", "General", "Helps reduce tartar and plaque.", "product_pictures/8.webp"});
        db.execSQL("INSERT INTO products (name, description, price, brand, type, age, reviews, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                new Object[]{"Salmon Oil Supplement", "Nature's Protection Salmon Oil for Adult Dogs and Cats is a specifically formulated complementary feed made of 100% pure salmon oil, rich in Omega fatty acids EPA and DHA. It is designed to help ensure healthy metabolism for your dog. It provides numerous benefits, including improving skin and coat health, preventing skin allergies, and promoting overall well-being. This complementary feed can help ensure your dog receives the essential nutrients to maintain overall health and vitality.",
                        1400, "HealthyCoat", "Supplement", "General", "Great for dogs with dry skin or shedding issues.", "product_pictures/9.webp"});
        db.execSQL("INSERT INTO products (name, description, price, brand, type, age, reviews, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                new Object[]{"Turkey & Vegetable Stew", "Delicious, chunky, slow-cooked homestyle stew containing tender cuts of turkey with barley and carrots in a savory gravy. Complete and balanced for everyday feeding, mixing or snacking!",
                        4800, "Wellness", "Wet", "Adult", "Dogs love the taste, perfect for occasional meals.", "product_pictures/10.webp"});
        db.execSQL("INSERT INTO products (name, description, price, brand, type, age, reviews, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                new Object[]{"Stewart Chicken Liver Freeze-Dried Dog Treats, 3-oz pouch", "Single ingredient treats—Made from 100% pure chicken liver, these freeze-dried dog treats are gluten-free, grain-free, and are packed with protein, vitamin D, and well-balanced amino acids to support strong muscles.",
                        11300, "Stewart ", "Dry", "General", "High-value treats that dogs go crazy for.", "product_pictures/11.webp"});
    }

    private void insertInitialEducationalContent(SQLiteDatabase db) {
        db.execSQL("INSERT INTO content (type, title, description, url) VALUES (?, ?, ?, ?)",
                new Object[]{"Article", "Dog Nutrition 101", "A comprehensive guide to understanding dog nutrition.", "https://petisfriend.com/dog-nutrition/dogs/nutrition/"});
        db.execSQL("INSERT INTO content (type, title, description, url) VALUES (?, ?, ?, ?)",
                new Object[]{"Video", "Top 5 Dog Breeds", "Learn about the top 5 most popular dog breeds.", "https://www.youtube.com/watch?v=gfcM1G8y5k0"});
        db.execSQL("INSERT INTO content (type, title, description, url) VALUES (?, ?, ?, ?)",
                new Object[]{"Guide", "Puppy Feeding Schedule", "A guide to feeding your puppy correctly.", "https://www.akc.org/expert-advice/health/puppy-feeding-fundamentals/#:~:text=Feeding%20your%20puppy%20with%20adult%20dog%20food%20will,decrease%20feedings%20from%20four%20to%20three%20a%20day."});
        db.execSQL("INSERT INTO content (type, title, description, url) VALUES (?, ?, ?, ?)",
                new Object[]{"Article", "Understanding Dog Dietary Requirements", "This article explores the dietary needs of different dog breeds.", "https://www.furlifevet.com.au/understanding-your-dogs-nutritional-requirements/"});
        db.execSQL("INSERT INTO content (type, title, description, url) VALUES (?, ?, ?, ?)",
                new Object[]{"Guide", "Senior Dog Health Tips", "Tips for maintaining the health of senior dogs.", "https://www.thesprucepets.com/aging-pet-care-awareness-3384785#:~:text=10%20Tips%20for%20Taking%20Care%20of%20a%20Senior,Add%20in%20More%20Grooming%20Sessions%20...%20More%20items"});
        db.execSQL("INSERT INTO content (type, title, description, url) VALUES (?, ?, ?, ?)",
                new Object[]{"Video", "The First 7 Things You NEED To Teach Your Puppy", "Getting A New Puppy Can Be STRESSFUL! We Make It EASY With The FIRST 7 Things You Need To Teach Your Puppy!","https://www.youtube.com/watch?v=HTXajoc4a3k"});

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS products");
        db.execSQL("DROP TABLE IF EXISTS cart");
        db.execSQL("DROP TABLE IF EXISTS content");
        onCreate(db);
    }

    public void closeDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }


}
