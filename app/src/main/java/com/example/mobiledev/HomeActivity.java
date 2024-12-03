package com.example.mobiledev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_home);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.primaryDarkColor));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setupBottomNavigation();

        ViewPager viewPager = findViewById(R.id.viewPager);
        int[] images = {
                R.drawable.img1,
                R.drawable.img2,
                R.drawable.img3,
                R.drawable.img4,
                R.drawable.img5,
                R.drawable.img6
        };
        ImageSliderAdapter adapter = new ImageSliderAdapter(this, images, viewPager);
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(0);
        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                int nextItem = (viewPager.getCurrentItem() + 1) % images.length;
                viewPager.setCurrentItem(nextItem, true);
                viewPager.postDelayed(this, 3000);
            }
        }, 3000);

        Button buyNutritionButton = findViewById(R.id.buy_button);
        Button manageCartButton = findViewById(R.id.cart_button);
        Button educationalContentButton = findViewById(R.id.content_button);
        ImageView profileButton = findViewById(R.id.profile);

        buyNutritionButton.setOnClickListener(this);
        manageCartButton.setOnClickListener(this);
        educationalContentButton.setOnClickListener(this);
        profileButton.setOnClickListener(this);
    }


    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::handleNavigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        int viewId = v.getId();

        if (viewId == R.id.buy_button) {
            intent = new Intent(HomeActivity.this, ProductsActivity.class);
        } else if (viewId == R.id.cart_button) {
            intent = new Intent(HomeActivity.this, CartActivity.class);
        } else if (viewId == R.id.content_button) {
            intent = new Intent(HomeActivity.this, EducationalActivity.class);
        } else if (viewId == R.id.profile) {
            intent = new Intent(HomeActivity.this, ProfileActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    private boolean handleNavigation(MenuItem item) {
        Intent intent = null;
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            return true;
        } else if (itemId == R.id.nav_products) {
            intent = new Intent(this, ProductsActivity.class);
        } else if (itemId == R.id.nav_cart) {
            intent = new Intent(this, CartActivity.class);
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

