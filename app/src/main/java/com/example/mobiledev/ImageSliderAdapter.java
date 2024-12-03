package com.example.mobiledev;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

public class ImageSliderAdapter extends PagerAdapter {

    private Context context;
    private int[] images;
    private LayoutInflater inflater;

    public ImageSliderAdapter(Context context, int[] images, ViewPager viewPager) {
        this.context = context;
        this.images = images;
        this.inflater = LayoutInflater.from(context);
        setViewPagerScroller(viewPager);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.image_slide, container, false);
        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setImageResource(images[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    private void setViewPagerScroller(ViewPager viewPager) {
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            ViewPagerScroller scroller = new ViewPagerScroller(viewPager.getContext());
            scroller.setDuration(800); // Set the duration for the smooth scroll
            scrollerField.set(viewPager, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ViewPagerScroller extends Scroller {

        private int mDuration = 800;

        public ViewPagerScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }

        public void setDuration(int duration) {
            this.mDuration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }
}
