package com.neu.edu.schedule.recordFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.neu.edu.schedule.BaseFragment;
import com.neu.edu.schedule.R;

public class RecordFragment extends BaseFragment {
    private View rootview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_2main,container,false);
        return rootview;
    }
}
