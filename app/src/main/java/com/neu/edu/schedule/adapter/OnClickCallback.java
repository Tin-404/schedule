package com.neu.edu.schedule.adapter;

import android.view.View;

public interface OnClickCallback {

    //
    void onClick(View v, int position, BaseViewHolder viewHolder);

    //
    boolean onLongClick(View v, int position, BaseViewHolder viewHolder);

}
