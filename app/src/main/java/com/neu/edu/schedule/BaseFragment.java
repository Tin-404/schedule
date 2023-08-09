package com.neu.edu.schedule;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class BaseFragment extends Fragment {
    protected Bundle data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = getArguments();//
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initEvents();
        setListener();
        setData();

    }

    protected void setListener() {

    }

    protected void initEvents() {

    }

    protected void initView(View view) {
        view.setBackgroundColor(getResources().getColor(
                android.R.color.background_light));
    }

    protected void setData() {

    }

    public void show(String s) {
        if (getActivity() != null) {
            Toast.makeText(getActivity().getApplicationContext(), s,
                    Toast.LENGTH_SHORT).show();
        }
    }

}