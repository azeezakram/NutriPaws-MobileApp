package com.example.mobiledev;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;
    private Cart cart;

    public ProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
        this.cart = new Cart(context);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.nameTextView.setText(product.getName());
        holder.brandTextView.setText("Brand: " + product.getBrand());
        holder.priceTextView.setText(String.format("LKR %.2f", product.getPrice()));
        holder.typeTextView.setText("Type: " + product.getType());
        holder.ageTextView.setText("Age: " + product.getAge());

        holder.quantityTextView.setText("1");

        holder.increaseQuantityButton.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.quantityTextView.getText().toString());
            holder.quantityTextView.setText(String.valueOf(currentQuantity + 1));
        });

        holder.decreaseQuantityButton.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.quantityTextView.getText().toString());
            if (currentQuantity > 1) {
                holder.quantityTextView.setText(String.valueOf(currentQuantity - 1));
            }
        });

        holder.addToCartButton.setOnClickListener(v -> {
            int quantity = Integer.parseInt(holder.quantityTextView.getText().toString());
            addToCart(product.getId(), quantity, product.getName());
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("product", product);
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    private void addToCart(int productId, int quantity, String productName) {
        int userId = getUserIdFromSession();
        if (userId != -1) {
            cart.addToCart(userId, productId, quantity);
            Toast.makeText(context, "Added to cart: " + productName + " (Quantity: " + quantity + ")", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "You need to be logged in to add items to the cart.", Toast.LENGTH_SHORT).show();
        }
    }


    private int getUserIdFromSession() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1);
    }

    public void updateProductList(List<Product> newProductList) {
        this.productList = newProductList;
        notifyDataSetChanged();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView brandTextView;
        TextView priceTextView;
        TextView typeTextView;
        TextView ageTextView;
        TextView quantityTextView;
        TextView increaseQuantityButton;
        TextView decreaseQuantityButton;
        Button addToCartButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.product_name);
            brandTextView = itemView.findViewById(R.id.product_brand);
            priceTextView = itemView.findViewById(R.id.product_price);
            typeTextView = itemView.findViewById(R.id.product_type);
            ageTextView = itemView.findViewById(R.id.product_age);
            quantityTextView = itemView.findViewById(R.id.quantity_text);
            increaseQuantityButton = itemView.findViewById(R.id.increase_quantity_button);
            decreaseQuantityButton = itemView.findViewById(R.id.decrease_quantity_button);
            addToCartButton = itemView.findViewById(R.id.add_to_cart_button);
        }
    }
}
