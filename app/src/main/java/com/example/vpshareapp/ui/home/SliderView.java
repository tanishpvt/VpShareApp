package com.example.vpshareapp.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.vpshareapp.R;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;



public class SliderView extends PagerAdapter {

private Context context;
LayoutInflater inflater;


    int images[]={R.drawable.colleage,R.drawable.college1,R.drawable.college2,R.drawable.colleage3,R.drawable.colleage4,R.drawable.collleage5,R.drawable.colleage6};


    public SliderView(Context context, LayoutInflater inflater) {
        this.context = context;
        this.inflater = inflater;
    }

    public SliderView(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.sliderview,container,false);
        ImageView img=(ImageView)view.findViewById(R.id.imageviewtxt);
        img.setImageResource(images[position]);

        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view==(LinearLayout)object);
    }
}
