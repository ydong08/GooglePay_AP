package com.google.commerce.tapandpay.merchantapp.main.cursoradapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TestSuiteListAdapter extends ActiveCursorAdapter {
    public TestSuiteListAdapter(Context context) {
        super(context);
    }

    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.test_suite_view, viewGroup, false);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = (TextView) view.findViewById(R.id.test_suite_name);
        textView.setText(cursor.getString(cursor.getColumnIndex("name")));
        if (getActiveId() == ((long) cursor.getInt(cursor.getColumnIndex("_id")))) {
            view.setBackgroundColor(context.getResources().getColor(R.color.quantum_grey300));
            textView.setTypeface(null, 1);
            return;
        }
        view.setBackgroundColor(0);
        textView.setTypeface(null, 0);
    }

    protected String getSharedPrefsKey() {
        return "active_testsuite";
    }
}
