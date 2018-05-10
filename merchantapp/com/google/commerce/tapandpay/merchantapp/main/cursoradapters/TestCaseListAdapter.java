package com.google.commerce.tapandpay.merchantapp.main.cursoradapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.common.base.Strings;

public class TestCaseListAdapter extends ActiveCursorAdapter {
    public TestCaseListAdapter(Context context) {
        super(context);
    }

    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.test_case_view, viewGroup, false);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = (TextView) view.findViewById(R.id.test_case_description);
        TextView textView2 = (TextView) view.findViewById(R.id.test_case_expected_terminal_behavior);
        TextView textView3 = (TextView) view.findViewById(R.id.test_case_id);
        ((TextView) view.findViewById(R.id.test_case_name)).setText(cursor.getString(cursor.getColumnIndex("name")));
        textView.setText(cursor.getString(cursor.getColumnIndex("description")));
        if (!Strings.isNullOrEmpty(cursor.getString(cursor.getColumnIndex("expected_terminal_behavior")))) {
            textView2.setText(context.getString(R.string.test_case_expected_terminal_behavior, new Object[]{r0}));
        }
        long j = cursor.getLong(cursor.getColumnIndex("_id"));
        textView3.setText(context.getString(R.string.test_case_id, new Object[]{Long.valueOf(j)}));
        if (getActiveId() == j) {
            view.setBackground(ContextCompat.getDrawable(context, R.drawable.active_test_case_bg));
        } else {
            view.setBackground(ContextCompat.getDrawable(context, R.drawable.test_case_bg));
        }
    }

    protected String getSharedPrefsKey() {
        return "active_testcase";
    }
}
