package com.example.mobiledev;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;
import java.io.InputStream;

public class ProductDetailActivity extends AppCompatActivity {
    private int quantity = 1;
    private TextView quantityTextView;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_product_detail);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.primaryDarkColor));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        ImageView backImageView = findViewById(R.id.back_button);
        ImageView productImageView = findViewById(R.id.product_image);
        TextView productNameTextView = findViewById(R.id.product_name);
        TextView productBrandTextView = findViewById(R.id.product_brand);
        TextView productPriceTextView = findViewById(R.id.product_price);
        TextView productTypeTextView = findViewById(R.id.product_type);
        TextView productAgeTextView = findViewById(R.id.product_age);
        TextView productDescriptionTextView = findViewById(R.id.product_description);
        TextView productReviewsTextView = findViewById(R.id.product_reviews);
        Button addToCartButton = findViewById(R.id.add_to_cart_button);
        quantityTextView = findViewById(R.id.quantity_text);

        Intent intent = getIntent();
        product = (Product) intent.getSerializableExtra("product");

        if (product != null) {
            productNameTextView.setText(product.getName());
            productBrandTextView.setText("Brand: " + product.getBrand());
            productPriceTextView.setText(String.format("LKR %.2f", product.getPrice()));
            productTypeTextView.setText("Type: " + product.getType());
            productAgeTextView.setText("Age: " + product.getAge());
            productDescriptionTextView.setText(product.getDescription());
            productReviewsTextView.setText(product.getReviews());

            if (product.getImage().startsWith("product_pictures/")) {
                loadAssetImage(product.getImage(), productImageView);
            } else {
                Glide.with(this)
                        .load(product.getImage())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(productImageView);
            }

            addToCartButton.setOnClickListener(v -> {
                if (product != null) {
                    addToCart(product.getId(), quantity, product.getName());
                }
            });
        }

        backImageView.setOnClickListener(v -> {
            Intent i = new Intent(this, ProductsActivity.class);
            startActivity(i);
        });
    }

    private void loadAssetImage(String imagePath, ImageView imageView) {
        try {
            InputStream inputStream = getAssets().open(imagePath);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void increaseQuantity(View view) {
        quantity++;
        quantityTextView.setText(String.valueOf(quantity));
    }

    public void decreaseQuantity(View view) {
        if (quantity > 1) {
            quantity--;
            quantityTextView.setText(String.valueOf(quantity));
        }
    }

    private void addToCart(int productId, int quantity, String productName) {
        int userId = getUserIdFromSession();
        if (userId != -1) {
            Cart cart = new Cart(this);
            cart.addToCart(userId, productId, quantity);
            Toast.makeText(this, "Added to cart: " + productName + " (Quantity: " + quantity + ")", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "You need to be logged in to add items to the cart.", Toast.LENGTH_SHORT).show();
        }
    }

    private int getUserIdFromSession() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1);
    }
}
