package com.google.commerce.tapandpay.merchantapp.testcase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.google.commerce.tapandpay.merchantapp.sqlite.MerchantAppDbHelper;
import com.google.common.base.Optional;
import javax.inject.Inject;

public class TestSuiteHelper {
    private final SQLiteDatabase database;

    @Inject
    public TestSuiteHelper(MerchantAppDbHelper merchantAppDbHelper) {
        this.database = merchantAppDbHelper.getWritableDatabase();
    }

    public Cursor queryAllTestSuites() {
        return this.database.query("test_suite", null, null, null, null, null, "name ASC");
    }

    public long insertTestSuite(String str) {
        try {
            this.database.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", str);
            long insert = this.database.insert("test_suite", null, contentValues);
            if (insert >= 0) {
                this.database.setTransactionSuccessful();
            }
            this.database.endTransaction();
            return insert;
        } catch (Throwable th) {
            this.database.endTransaction();
        }
    }

    public void deleteTestSuite(long j) {
        try {
            this.database.beginTransaction();
            String[] strArr = new String[]{String.valueOf(j)};
            this.database.delete("test_suite", "_id=?", strArr);
            this.database.setTransactionSuccessful();
        } finally {
            this.database.endTransaction();
        }
    }

    public void deleteAllTestSuites() {
        try {
            this.database.beginTransaction();
            this.database.delete("test_suite", null, null);
            this.database.setTransactionSuccessful();
        } finally {
            this.database.endTransaction();
        }
    }

    public Optional<Long> getFirstTestSuiteId() {
        Optional<Long> absent = Optional.absent();
        try {
            Optional<Long> of;
            this.database.beginTransaction();
            Cursor query = this.database.query("test_suite", new String[]{"_id"}, null, null, null, null, "name ASC", "1");
            if (query.moveToNext()) {
                of = Optional.of(Long.valueOf(query.getLong(query.getColumnIndex("_id"))));
            } else {
                of = absent;
            }
            query.close();
            return of;
        } finally {
            this.database.endTransaction();
        }
    }

    public Optional<Long> getTestSuiteId(String str) {
        String[] strArr = new String[]{"_id"};
        String[] strArr2 = new String[]{str};
        Cursor query = this.database.query("test_suite", strArr, "name=?", strArr2, null, null, null, "1");
        Optional<Long> absent = Optional.absent();
        if (query.moveToFirst()) {
            absent = Optional.of(Long.valueOf(query.getLong(query.getColumnIndex("_id"))));
        }
        query.close();
        return absent;
    }
}
