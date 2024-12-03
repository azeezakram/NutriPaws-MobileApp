package com.example.mobiledev;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Cart.CartItem> cartItems;
    private Context context;
    private Cart cart;
    private int userId;

    public CartAdapter(List<Cart.CartItem> cartItems, Context context, int userId) {
        this.cartItems = cartItems;
        this.context = context;
        this.userId = userId;
        cart = new Cart(context);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart.CartItem item = cartItems.get(position);
        double subtotal = item.getPrice() * item.getQuantity();  // Calculate subtotal for this item

        holder.nameTextView.setText(item.getName());
        holder.priceTextView.setText(String.format("LKR %.2f", item.getPrice()));
        holder.quantityTextView.setText(String.valueOf(item.getQuantity()));
        holder.subtotalTextView.setText(String.format("Subtotal: LKR %.2f", subtotal));  // Show subtotal

        holder.increaseButton.setOnClickListener(v -> {
            int currentQuantity = item.getQuantity();
            cart.updateQuantity(userId, item.getProductId(), currentQuantity + 1);
            item.setQuantity(currentQuantity + 1);
            notifyItemChanged(position);
            updateTotalPrice();
        });

        holder.decreaseButton.setOnClickListener(v -> {
            int currentQuantity = item.getQuantity();
            if (currentQuantity > 1) {
                cart.updateQuantity(userId, item.getProductId(), currentQuantity - 1);
                item.setQuantity(currentQuantity - 1);
                notifyItemChanged(position);
                updateTotalPrice();
            }
        });

        holder.removeButton.setOnClickListener(v -> {
            cart.removeFromCart(userId, item.getProductId());
            cartItems.remove(position);
            notifyItemRemoved(position);
            updateTotalPrice();
            Toast.makeText(context, item.getName() + " removed from cart", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void updateCartItems(List<Cart.CartItem> newCartItems) {
        this.cartItems = newCartItems;
        notifyDataSetChanged();
    }

    private void updateTotalPrice() {
        if (context instanceof CartActivity) {
            ((CartActivity) context).updateTotalPrice();
        }
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView priceTextView;
        TextView quantityTextView;
        TextView subtotalTextView;
        TextView increaseButton;
        TextView decreaseButton;
        Button removeButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.product_name);
            priceTextView = itemView.findViewById(R.id.product_price);
            quantityTextView = itemView.findViewById(R.id.quantity_text);
            subtotalTextView = itemView.findViewById(R.id.product_subtotal);
            increaseButton = itemView.findViewById(R.id.increase_quantity_button);
            decreaseButton = itemView.findViewById(R.id.decrease_quantity_button);
            removeButton = itemView.findViewById(R.id.remove_item_button);
        }
    }
}





