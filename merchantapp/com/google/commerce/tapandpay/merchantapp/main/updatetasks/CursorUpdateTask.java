package com.google.commerce.tapandpay.merchantapp.main.updatetasks;

import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.CursorAdapter;

public abstract class CursorUpdateTask extends AsyncTask<DbOperation, Void, Cursor> {
    private final CursorAdapter cursorAdapter;

    public interface DbOperation {
        void run();
    }

    public abstract Cursor getCursor();

    public CursorUpdateTask(CursorAdapter cursorAdapter) {
        this.cursorAdapter = cursorAdapter;
    }

    protected Cursor doInBackground(DbOperation... dbOperationArr) {
        for (DbOperation run : dbOperationArr) {
            run.run();
        }
        return getCursor();
    }

    protected void onPostExecute(Cursor cursor) {
        this.cursorAdapter.changeCursor(cursor);
    }
}
