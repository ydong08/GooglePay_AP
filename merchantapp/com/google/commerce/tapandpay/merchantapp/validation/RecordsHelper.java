package com.google.commerce.tapandpay.merchantapp.validation;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.commerce.tapandpay.merchantapp.validation.ValidationResults.Builder;
import com.google.commerce.tapandpay.merchantapp.validation.ValidationResults.Status;
import java.util.Iterator;
import java.util.List;

public class RecordsHelper {
    private RecordsHelper() {
    }

    public static ValidationResults validate(List<Record> list, byte[] bArr) {
        int i = 0;
        if (list == null || list.isEmpty()) {
            return ValidationResults.create(Status.SUCCESS, "No records to validate against.", new Object[0]);
        }
        try {
            NdefRecord[] records = new NdefMessage(bArr).getRecords();
            if (list.size() != records.length) {
                return ValidationResults.create(Status.FAILURE, "Bytes contain %s NDEF records, expected %s. Bytes: %s", Integer.valueOf(records.length), Integer.valueOf(list.size()), Hex.encodeUpper(bArr));
            }
            Builder status = Builder.builder().setMessage("Records").setStatus(Status.SUCCESS);
            Iterator it = list.iterator();
            int length = records.length;
            while (i < length) {
                status.addNestedResult(((Record) it.next()).validate(records[i]));
                i++;
            }
            return status.build();
        } catch (Throwable e) {
            return ValidationResults.create(Status.FAILURE, e, "Bytes are not a valid NDEF message. Bytes: %s", Hex.encodeUpper(bArr));
        }
    }
}
