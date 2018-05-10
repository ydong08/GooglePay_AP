package com.google.commerce.tapandpay.merchantapp.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import javax.inject.Singleton;

@Singleton
public class MerchantAppDbHelper extends SQLiteOpenHelper {
    public MerchantAppDbHelper(Context context) {
        super(context, "MerchantApp.db", null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE test_case (_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,expected_terminal_behavior TEXT,support_generic_key_auth INTEGER,support_ecies INTEGER,override_transaction_mode INTEGER,payment_enabled INTEGER,payment_requested INTEGER,vas_enabled INTEGER,vas_requested INTEGER,use_ose INTEGER,ose_smarttap_1_3 TEXT,ose_smarttap_1_3_priority INTEGER,ose_smarttap_2_0 TEXT,ose_smarttap_2_0_priority INTEGER,allow_skipping_smart_tap_2_select INTEGER,include_master_nonce INTEGER,include_nonce_in_proprietary_data INTEGER,ose_custom INTEGER,ose_custom_aid BLOB,ose_custom_priority INTEGER,support_smarttap_1_3 INTEGER,support_smarttap_2_0 INTEGER,smarttap_min_version BLOB,smarttap_max_version BLOB,consumer_id TEXT,tap_id TEXT,device_id TEXT,use_merchant_capabilities INTEGER,check_merchant_id INTEGER,merchant_id INTEGER,require_encryption INTEGER,require_live_auth INTEGER,get_data_response_status TEXT,stop_payment_command_type TEXT,description TEXT,test_suite_id INTEGER,merchant_public_key BLOB,validation_schema TEXT,tap_video_uri TEXT,use_custom_ose_status_word INTEGER,custom_ose_status_word BLOB,FOREIGN KEY(test_suite_id) REFERENCES test_suite(_id) ON DELETE CASCADE )");
        sQLiteDatabase.execSQL("CREATE TABLE test_suite (_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT UNIQUE )");
        sQLiteDatabase.execSQL("CREATE TABLE custom_statuses (_id INTEGER PRIMARY KEY AUTOINCREMENT,command INTEGER,status BLOB,test_case_id INTEGER,FOREIGN KEY(test_case_id) REFERENCES test_case(_id) ON DELETE CASCADE )");
        sQLiteDatabase.execSQL("CREATE TABLE max_custom_statuses (_id INTEGER PRIMARY KEY AUTOINCREMENT,command INTEGER,max_status_overrides INTEGER,test_case_id INTEGER,FOREIGN KEY(test_case_id) REFERENCES test_case(_id) ON DELETE CASCADE )");
        sQLiteDatabase.execSQL("CREATE TABLE added_ndefs (_id INTEGER PRIMARY KEY AUTOINCREMENT,parent_id BLOB,ndef_payload BLOB,test_case_id INTEGER,FOREIGN KEY(test_case_id) REFERENCES test_case(_id) ON DELETE CASCADE )");
        sQLiteDatabase.execSQL("CREATE TABLE removed_ndefs (_id INTEGER PRIMARY KEY AUTOINCREMENT,ndef_id BLOB,test_case_id INTEGER,FOREIGN KEY(test_case_id) REFERENCES test_case(_id) ON DELETE CASCADE )");
        sQLiteDatabase.execSQL("CREATE TABLE smart_tap (_id INTEGER PRIMARY KEY AUTOINCREMENT,type INTEGER,merchant_id INTEGER,issuer INTEGER,value TEXT,valueencoding TEXT,pin TEXT,pinencoding TEXT,cvc TEXT,cvcencoding TEXT,expmonth INTEGER,expyear INTEGER,test_case_id INTEGER,FOREIGN KEY(test_case_id) REFERENCES test_case(_id) ON DELETE CASCADE )");
        sQLiteDatabase.execSQL("CREATE TABLE result (_id INTEGER PRIMARY KEY AUTOINCREMENT,result_blob BLOB,test_case_id INTEGER,expected_result INTEGER,FOREIGN KEY(test_case_id) REFERENCES test_case(_id) ON DELETE CASCADE )");
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS test_case");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS test_suite");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS custom_statuses");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS max_custom_statuses");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS added_ndefs");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS removed_ndefs");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS smart_tap");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS result");
        onCreate(sQLiteDatabase);
    }

    public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        onUpgrade(sQLiteDatabase, i, i2);
    }

    public void onConfigure(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.setForeignKeyConstraintsEnabled(true);
    }
}
