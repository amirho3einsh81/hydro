package com.nitrogen.hydro.fragements;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nitrogen.hydro.MyAdapter;
import com.nitrogen.hydro.MyModel;
import com.nitrogen.hydro.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MountlyFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    SwipeRefreshLayout refresh;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_mountly, container, false);
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
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        List<MyModel> model = new ArrayList<>();

        for (int i=0 ; i<=5 ; i++){
            MyModel myModel = new MyModel();
            myModel.setShenase(""+new Random().nextInt(55555555));
            myModel.setDate("1402"+"/"+new Random().nextInt(30)+"/"+new Random().nextInt(12));
            myModel.setScore(""+new Random().nextInt(5555));
            myModel.setThrift(new Random().nextInt(90));
            model.add(myModel);
        }
        recyclerView.setAdapter(new MyAdapter(getContext(), model));
        return view;
    }
}