package com.dut.banana.ui.adapter;

import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * Copyright by NE 2015.
 * Created by noem on 16/11/2015.
 */
public abstract class SupportContextMenuAdapter extends BaseAdapter implements View.OnClickListener {
    private ListView presentView;

    public void setPresentView(ListView view) {
        presentView = view;
    }

    @Override
    public void onClick(View v) {
        if (presentView != null) {
            int position = ((ViewHolder) v.getTag()).id;
            presentView.performItemClick(v, position, position);
        }
    }

    protected static class ViewHolder {
        int id;
    }
}
