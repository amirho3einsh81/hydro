package com.nitrogen.hydro.fragements;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.nitrogen.hydro.R;


public class ShopFragment extends Fragment {


    private TextView tabItem1, tabItem2;

    private int selectedTabNumber = 1;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shop, container, false);

        tabItem1 = view.findViewById(R.id.tabItem1);
        tabItem2 = view.findViewById(R.id.tabItem2);

        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainer, WaterFragment.class, null)
                .commit();
        /*FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer ,  Base.class , null)
                .setReorderingAllowed(true)
                .commit();*/
        tabItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                selectTab(1);
            }
        });
        tabItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                selectTab(2);

            }
        });

        return view;
    }

    private void selectTab(int tabNumber) {
        TextView selectedTextView;

        TextView nonSelectedTextView1;


        if (tabNumber == 1) {
            selectedTextView = tabItem1;

            nonSelectedTextView1 = tabItem2;

            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new WaterFragment(), null)
                    .commit();

        } else if (tabNumber == 2) {

            selectedTextView = tabItem2;

            nonSelectedTextView1 = tabItem1;

            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer,new PurifierFragment(), null)
                    .commit();


            float slideTo = (tabNumber - selectedTabNumber) * selectedTextView.getWidth();
            TranslateAnimation translateAnimation = new TranslateAnimation(0, slideTo, 0, 0);
            translateAnimation.setDuration(100);
            if (selectedTabNumber == 1) {
                tabItem1.startAnimation(translateAnimation);
            } else if (selectedTabNumber == 2) {
                tabItem2.startAnimation(translateAnimation);

            }
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {

                    selectedTextView.setBackgroundResource(R.drawable.round_back_white_100);
                    selectedTextView.setTextColor(Color.WHITE);

                    nonSelectedTextView1.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    nonSelectedTextView1.setTextColor(ContextCompat.getColor(getContext(),R.color.black)/*Color.parseColor("#80FFFFFF")*/);

                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            selectedTabNumber = tabNumber;
        }
    }}
