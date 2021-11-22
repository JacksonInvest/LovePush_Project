package com.lovepushapp.callbacks;

import android.view.View;

public interface OnItemClickListenerCutom<T> {

    public void onItemClick(View view, int position, int type, T t);

}
