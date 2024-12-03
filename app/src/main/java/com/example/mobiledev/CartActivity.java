package com.example.mobiledev;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class CartActivity extends AppCompatActivity {
    private int userId;
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private Cart cart;
    private TextView totalPriceTextView;
    private Button clearCartButton;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_cart);


        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.primaryDarkColor));

        recyclerView = findViewById(R.id.recycler_view_cart);
        totalPriceTextView = findViewById(R.id.total_price);
        clearCartButton = findViewById(R.id.clear_cart_button);
        progressBar = findViewById(R.id.progress_bar);

        userId = getUserIdFromSession();
        cart = new Cart(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadCartItems(userId);

        clearCartButton.setOnClickListener(v -> {
            cart.clearCart(userId);
            loadCartItems(userId);
            Toast.makeText(this, "Cart cleared", Toast.LENGTH_SHORT).show();
        });

        setupBottomNavigation();
    }

    private void loadCartItems(int userId) {
        progressBar.setVisibility(View.VISIBLE);

        new Thread(() -> {
            List<Cart.CartItem> cartItems = cart.getCartItems(userId);
            runOnUiThread(() -> {
                cartAdapter = new CartAdapter(cartItems, this, userId);
                recyclerView.setAdapter(cartAdapter);
                updateTotalPrice();
                progressBar.setVisibility(View.GONE);
            });
        }).start();
    }

    public void updateTotalPrice() {
        double totalPrice = cart.getTotalPrice(userId);
        totalPriceTextView.setText(String.format("Grand Total: LKR %.2f", totalPrice));
    }

    private int getUserIdFromSession() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::handleNavigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_cart);
    }



    private boolean handleNavigation(MenuItem item) {
        Intent intent = null;
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            intent = new Intent(this, HomeActivity.class);
        } else if (itemId == R.id.nav_products) {
            intent = new Intent(this, ProductsActivity.class);
        } else if (itemId == R.id.nav_cart) {
            return true;
        } else if (itemId == R.id.nav_educational) {
            intent = new Intent(this, EducationalActivity.class);
        } else if (itemId == R.id.nav_profile) {
            intent = new Intent(this, ProfileActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}



