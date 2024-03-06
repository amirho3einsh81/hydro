package com.nitrogen.hydro.fragements;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nitrogen.hydro.BestuserAdapter;
import com.nitrogen.hydro.R;
import com.nitrogen.hydro.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BestusersFragment extends Fragment {

    RecyclerView recyclerTopUser,recyclerView;
    View view ;
    ViewPager viewPager;
    Handler h = new Handler();
    Runnable r;
    SwipeRefreshLayout refresh;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bestusers, container, false);
        refresh = view.findViewById(R.id.refresh);
        refresh.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(false);
            }
        }, 1000);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        recyclerTopUser = view.findViewById(R.id.recyclerView);
        recyclerView = view.findViewById(R.id.recyclerView1);
        recyclerTopUser.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.VERTICAL , false));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.VERTICAL , false));
        viewPager = view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new Pager(getContext()));
        List<UserModel> models = new ArrayList<>();
        UserModel model = new UserModel();
        model.setName("امیر حسین شیری");
        model.setColor("D89B00");
        model.setPresent("90");
        UserModel model1 = new UserModel();
        model1.setName("جواد سپهری");
        model1.setColor("989898");
        model1.setPresent("111");
        UserModel model2 = new UserModel();
        model2.setName("علی فرهادی");
        model2.setColor("d79468");
        model2.setPresent("112");
        models.add(model);
        models.add(model1);
        models.add(model2);
        recyclerTopUser.setAdapter(new BestuserAdapter(getContext() , models));
        r = new Runnable() {
            @Override
            public void run() {
                if (viewPager.getCurrentItem() ==0 ){
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                }else{
                    viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
                }
            }
        };
        h.removeCallbacks(r);
        h.postDelayed(r , 3000);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                h.removeCallbacks(r);
                h.postDelayed(r , 3000);
            }
        });
        List<UserModel> models1 = new ArrayList<>();
        for (int i=0 ; i<= 15 ;i++){
            UserModel model12 = new UserModel();
            model12.setName("امیر"+new Random().nextInt(77779));
            model12.setColor("ffffff");
            models1.add(model12);
        }
        recyclerView.setAdapter(new BestuserAdapter(getContext(), models1));
        return view;
    }

    private class Pager extends PagerAdapter{

        Context ctx;

        public Pager(Context ctx) {
            this.ctx = ctx;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(ctx);
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_banner, container, false);
            container.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        h.removeCallbacks(r);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        h.removeCallbacks(r);
    }
}