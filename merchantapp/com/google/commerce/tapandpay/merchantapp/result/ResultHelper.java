package com.google.commerce.tapandpay.merchantapp.result;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.api.client.util.IOUtils;
import com.google.commerce.tapandpay.merchantapp.sqlite.MerchantAppDbHelper;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCaseHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class ResultHelper {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private final SQLiteDatabase database;
    private final TestCaseHelper testCaseHelper;

    @Inject
    public ResultHelper(MerchantAppDbHelper merchantAppDbHelper, TestCaseHelper testCaseHelper) {
        this.database = merchantAppDbHelper.getWritableDatabase();
        this.testCaseHelper = testCaseHelper;
    }

    public long insertUserResult(Result result) {
        return insertResult(result, result.testCaseId(), false);
    }

    public long insertExpectedResult(Result result, long j) {
        return insertResult(result, j, true);
    }

    private void deleteOldestResult(long j, boolean z) {
        int i = 1;
        try {
            this.database.beginTransaction();
            String[] strArr = new String[]{"_id"};
            String[] strArr2 = new String[2];
            strArr2[0] = String.valueOf(j);
            if (!z) {
                i = 0;
            }
            strArr2[1] = String.valueOf(i);
            Cursor query = this.database.query("result", strArr, "test_case_id=? AND expected_result=?", strArr2, null, null, "test_case_id ASC", "1");
            if (query.moveToFirst()) {
                long j2 = query.getLong(query.getColumnIndex("_id"));
                strArr2 = new String[]{String.valueOf(j2)};
                this.database.delete("result", "_id=?", strArr2);
            }
            query.close();
            this.database.setTransactionSuccessful();
        } finally {
            this.database.endTransaction();
        }
    }

    private long insertResult(Result result, long j, boolean z) {
        int i = 1;
        byte[] serialize = serialize(result);
        long j2 = -1;
        if (serialize != null) {
            try {
                this.database.beginTransaction();
                ContentValues contentValues = new ContentValues();
                contentValues.put("test_case_id", Long.valueOf(j));
                contentValues.put("result_blob", serialize);
                contentValues.put("expected_result", Boolean.valueOf(z));
                j2 = this.database.insert("result", null, contentValues);
                if (j2 >= 0) {
                    String[] strArr = new String[2];
                    strArr[0] = String.valueOf(j);
                    if (!z) {
                        i = 0;
                    }
                    strArr[1] = String.valueOf(i);
                    if (!z && DatabaseUtils.queryNumEntries(this.database, "result", "test_case_id=? AND expected_result=?", strArr) > 5) {
                        deleteOldestResult(j, z);
                    } else if (z) {
                        if (DatabaseUtils.queryNumEntries(this.database, "result", "test_case_id=? AND expected_result=?", strArr) > 1) {
                            deleteOldestResult(j, z);
                        }
                    }
                    this.database.setTransactionSuccessful();
                }
                this.database.endTransaction();
            } catch (Throwable th) {
                this.database.endTransaction();
            }
        }
        return j2;
    }

    public Result readExpectedResult(long j) {
        List readResults = readResults("test_case_id=? AND expected_result=?", new String[]{String.valueOf(j), String.valueOf(1)});
        if (readResults.size() > 0) {
            return (Result) readResults.get(0);
        }
        return null;
    }

    public List<Result> readUserResults(long j) {
        return readResults("test_case_id=? AND expected_result=?", new String[]{String.valueOf(j), String.valueOf(0)});
    }

    public List<Result> readUserResultsFromTestSuite(long j) {
        List<Result> arrayList = new ArrayList();
        Cursor queryTestCaseSuite = this.testCaseHelper.queryTestCaseSuite(j);
        while (queryTestCaseSuite.moveToNext()) {
            arrayList.add(readRecentUserResult(queryTestCaseSuite.getLong(queryTestCaseSuite.getColumnIndex("_id"))));
        }
        queryTestCaseSuite.close();
        return arrayList;
    }

    private List<Result> readResults(String str, String[] strArr) {
        List<Result> arrayList = new ArrayList();
        try {
            this.database.beginTransaction();
            Cursor query = this.database.query("result", new String[]{"result_blob", "test_case_id", "_id"}, str, strArr, null, null, "_id ASC");
            while (query.moveToNext()) {
                Result deserialize = deserialize(query.getBlob(query.getColumnIndex("result_blob")));
                if (deserialize != null) {
                    arrayList.add(deserialize.toBuilder().setId(query.getLong(query.getColumnIndex("_id"))).setTestCaseId(query.getLong(query.getColumnIndex("test_case_id"))).build());
                }
            }
            query.close();
            return arrayList;
        } finally {
            this.database.endTransaction();
        }
    }

    private Result readRecentUserResult(long j) {
        String[] strArr = new String[]{String.valueOf(j), String.valueOf(0)};
        try {
            Result deserialize;
            this.database.beginTransaction();
            Cursor query = this.database.query("result", new String[]{"result_blob", "_id"}, "test_case_id=? AND expected_result=?", strArr, null, null, "_id DESC", "1");
            if (query.moveToFirst()) {
                deserialize = deserialize(query.getBlob(query.getColumnIndex("result_blob")));
                if (deserialize != null) {
                    deserialize = deserialize.toBuilder().setId(query.getLong(query.getColumnIndex("_id"))).build();
                }
            } else {
                deserialize = null;
            }
            query.close();
            return deserialize;
        } finally {
            this.database.endTransaction();
        }
    }

    private Result deserialize(byte[] bArr) {
        try {
            return (Result) IOUtils.deserialize(bArr);
        } catch (IOException e) {
            LOG.d("Failed to deserialize result", new Object[0]);
            return null;
        }
    }

    private byte[] serialize(Result result) {
        byte[] bArr = null;
        if (result != null) {
            try {
                bArr = IOUtils.serialize(result);
            } catch (IOException e) {
                LOG.d("Failed to serialize result", new Object[0]);
            }
        }
        return bArr;
    }
}
