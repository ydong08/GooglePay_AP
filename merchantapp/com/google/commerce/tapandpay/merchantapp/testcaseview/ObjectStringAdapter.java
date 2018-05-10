package com.google.commerce.tapandpay.merchantapp.testcaseview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public abstract class ObjectStringAdapter<T> extends ArrayAdapter<T> {

    static class ViewHolder {
        TextView text;

        private ViewHolder() {
        }
    }

    public abstract String getTitle(T t);

    public ObjectStringAdapter(Context context, List<T> list) {
        super(context, 17367043, list);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(17367043, viewGroup, false);
            ViewHolder viewHolder2 = new ViewHolder();
            viewHolder2.text = (TextView) view.findViewById(16908308);
            view.setTag(viewHolder2);
            viewHolder = viewHolder2;
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.text.setText(getTitle(getItem(i)));
        return view;
    }

    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        return getView(i, view, viewGroup);
    }
}
