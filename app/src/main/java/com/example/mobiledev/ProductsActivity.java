package com.example.mobiledev;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class ProductsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private SearchView searchView;
    private ProgressBar progressBar;
    private Product productModel;
    private Spinner brandSpinner, typeSpinner, ageSpinner;
    private SQLiteDatabase db;
    private int quantity = 1;
    private TextView quantityTextView;
    private int productId;
    private Product product;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_products);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.primaryDarkColor));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        db = dbHelper.getReadableDatabase();

        recyclerView = findViewById(R.id.recycler_view);
        searchView = findViewById(R.id.search_view);
        progressBar = findViewById(R.id.progress_bar_p);

        brandSpinner = findViewById(R.id.spinner_brand);
        typeSpinner = findViewById(R.id.spinner_type);
        ageSpinner = findViewById(R.id.spinner_age);

        setupSpinner(brandSpinner, Product.getUniqueBrands(this));
        setupSpinner(typeSpinner, Product.getUniqueTypes(this));
        setupSpinner(ageSpinner, Product.getUniqueAges(this));

        brandSpinner.setSelection(0);
        typeSpinner.setSelection(0);
        ageSpinner.setSelection(0);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productModel = new Product(this);

        loadProducts();

        setupSearchView();
        setupBottomNavigation();

        brandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterProducts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterProducts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterProducts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    private void loadProducts() {
        progressBar.setVisibility(View.VISIBLE);

        new Thread(() -> {
            List<Product> products = productModel.getAllProducts();
            runOnUiThread(() -> {
                productAdapter = new ProductAdapter(products, this);
                recyclerView.setAdapter(productAdapter);
                progressBar.setVisibility(View.GONE);
            });
        }).start();
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchProducts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchProducts(newText);
                return true;
            }
        });
    }

    private void searchProducts(String searchTerm) {
        progressBar.setVisibility(View.VISIBLE);

        new Thread(() -> {
            List<Product> products = productModel.searchProductsByName(searchTerm);
            runOnUiThread(() -> {
                if (productAdapter != null) {
                    productAdapter.updateProductList(products);
                } else {
                    productAdapter = new ProductAdapter(products, this);
                    recyclerView.setAdapter(productAdapter);
                }
                progressBar.setVisibility(View.GONE);
            });
        }).start();
    }

    private void setupSpinner(Spinner spinner, List<String> values) {
        values.add(0, "All");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void filterProducts() {
        progressBar.setVisibility(View.VISIBLE);

        new Thread(() -> {
            String selectedBrand = brandSpinner.getSelectedItem().toString();
            String selectedType = typeSpinner.getSelectedItem().toString();
            String selectedAge = ageSpinner.getSelectedItem().toString();

            List<Product> products = productModel.filterProducts(selectedBrand, selectedType, selectedAge);
            runOnUiThread(() -> {
                if (productAdapter != null) {
                    productAdapter.updateProductList(products);
                } else {
                    productAdapter = new ProductAdapter(products, this);
                    recyclerView.setAdapter(productAdapter);
                }
                progressBar.setVisibility(View.GONE);
            });
        }).start();
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

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::handleNavigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_products);
    }

    private boolean handleNavigation(MenuItem item) {
        Intent intent = null;
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            intent = new Intent(this, HomeActivity.class);
        } else if (itemId == R.id.nav_products) {
            return true;
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

