package com.example.vpshareapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapterIntro extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapterIntro(Context context){
        this.context=context;
    }
    //array of images
    public String[] slide_images={

            "welcomeanimate.json",

            "shareanimate.json",

            "secureanimate.json",

            "locationanimate.json",

            "notificationanimate.json"


    };

    //array of images
    public String[] slide_title={
            "Welcome To V Share Application"
            ,"Sharing Brings Joy"
            ,"Secure Donation"
            ,"Live Bag Location"
            ,"Get Notified"
    };

    //array of images
    public String[] slide_description={
            "This project is about V-Share application that allows users to donate educational,resources to different schools and NGOs that provides free education to,childrens.This capstone project aims toward developing an efficient and user-friendly andriod application that will be able to provide NGOs and Schools, staffs that are useful for students with all the needed material being used during education. It is an andriod app that allows users to login and register themselves where they can simply donate the materials which they want to provide to the needy children effortlessly. Thereafter the login and registration process, a commander will be reporting to the registered user’s locality where the users will be provided a bag/pouch in which they are allowed donate the materials as they want "
            ,"Donate Stationary Things To Needy Student"
            ,"Secured by SSL/TLS and PCI compliant\n There Is Multiple Authentication By Commander ,User ,School"
            ,"User Can Monitor 24/7 live bag(Donated Stuff) Location"
            ,"User Will Receive Confirmation Mail After His/Her Donated Stuff Collected By NGO/School "
    };

    @Override
    public int getCount() {
        return slide_title.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(RelativeLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view =layoutInflater.inflate(R.layout.slide_layout,container,false);

        //ImageView slideImageView=(ImageView)view.findViewById(R.id.intro_img);
        TextView slideTitle=(TextView) view.findViewById(R.id.intro_title);
        LottieAnimationView slideImageView=(LottieAnimationView)view.findViewById(R.id.intro_img);
       //slideImageView.setSpeed(200f);


        TextView slideDescription=(TextView) view.findViewById(R.id.intro_description);

        slideImageView.setAnimation(slide_images[position]);
        slideTitle.setText(slide_title[position]);
        slideDescription.setText(slide_description[position]);

        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
