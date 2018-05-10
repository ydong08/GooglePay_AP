package com.google.commerce.tapandpay.merchantapp.validation;

import android.nfc.NdefRecord;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.commerce.tapandpay.merchantapp.validation.ValidationResults.Status;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Record implements Parcelable {
    public static final Creator<Record> CREATOR = new Creator<Record>() {
        public Record createFromParcel(Parcel parcel) {
            return Record.create(parcel);
        }

        public Record[] newArray(int i) {
            return new Record[i];
        }
    };

    public static abstract class Builder {
        public abstract Record build();

        public abstract Builder setId(String str);

        public abstract Builder setName(String str);

        public abstract Builder setPayload(String str);

        public abstract Builder setPrefix(String str);

        public abstract Builder setPrefixLength(int i);

        public abstract Builder setRecords(List<Record> list);

        public abstract Builder setSuffix(String str);

        public abstract Builder setSuffixLength(int i);

        public abstract Builder setTnf(String str);

        public abstract Builder setType(String str);

        public static Builder builder() {
            return new Builder().setName("Record").setId("").setTnf("").setType("").setPrefix("").setPrefixLength(0).setSuffix("").setSuffixLength(0).setPayload("").setRecords(Collections.emptyList());
        }
    }

    public abstract String id();

    public abstract String name();

    public abstract String payload();

    public abstract String prefix();

    public abstract int prefixLength();

    public abstract List<Record> records();

    public abstract String suffix();

    public abstract int suffixLength();

    public abstract String tnf();

    public abstract String type();

    public static Record create(String str, String str2, String str3, String str4, String str5, int i, String str6, int i2, String str7, List<Record> list) {
        return Builder.builder().setName(str).setId(str2).setTnf(str3).setType(str4).setPrefix(str5).setPrefixLength(i).setSuffix(str6).setSuffixLength(i2).setPayload(str7).setRecords(list).build();
    }

    public static Record create(String str, String str2, String str3, String str4, String str5, int i, String str6, int i2, String str7, Record... recordArr) {
        return create(str, str2, str3, str4, str5, i, str6, i2, str7, Arrays.asList(recordArr));
    }

    public static Record create(Parcel parcel) {
        return create(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readInt(), parcel.readString(), parcel.readInt(), parcel.readString(), (Record[]) parcel.createTypedArray(CREATOR));
    }

    public ValidationResults validate(NdefRecord ndefRecord) {
        com.google.commerce.tapandpay.merchantapp.validation.ValidationResults.Builder status = com.google.commerce.tapandpay.merchantapp.validation.ValidationResults.Builder.builder().setMessage(name()).setStatus(Status.SUCCESS);
        status.addNestedResult(BytePayloadHelper.validateNdefId(id(), ndefRecord.getId()));
        status.addNestedResult(BytePayloadHelper.validateNdefTnf(tnf(), ndefRecord.getTnf()));
        status.addNestedResult(BytePayloadHelper.validateNdefType(type(), ndefRecord.getType()));
        byte[] payload = ndefRecord.getPayload();
        status.addNestedResult(BytePayloadHelper.validatePrefix(prefix(), prefixLength(), payload));
        status.addNestedResult(BytePayloadHelper.validateSuffix(suffix(), suffixLength(), payload));
        if (payload.length < prefixLength() + suffixLength()) {
            status.addNestedResult(ValidationResults.create(Status.FAILURE, "%s: The combined length of the prefix (%s) and suffix (%s) is greater than the NDEF record payload length (%s). Bytes: %s", name(), Integer.valueOf(prefixLength()), Integer.valueOf(suffixLength()), Integer.valueOf(payload.length), Hex.encodeUpper(payload)));
            return status.build();
        }
        payload = BytePayloadHelper.getPayload(prefixLength(), suffixLength(), payload);
        status.addNestedResult(BytePayloadHelper.validatePayload(payload(), payload));
        status.addNestedResult(RecordsHelper.validate(records(), payload));
        return status.build();
    }

    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        Record record = (Record) obj;
        if (Objects.equals(name(), record.name()) && Objects.equals(id(), record.id()) && Objects.equals(tnf(), record.tnf()) && Objects.equals(type(), record.type()) && Objects.equals(prefix(), record.prefix()) && Objects.equals(suffix(), record.suffix()) && prefixLength() == record.prefixLength() && suffixLength() == record.suffixLength() && Objects.equals(payload(), record.payload()) && Arrays.equals(records().toArray(new Record[0]), record.records().toArray(new Record[0]))) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return com.google.common.base.Objects.hashCode(name(), id(), tnf(), type(), prefix(), Integer.valueOf(prefixLength()), suffix(), Integer.valueOf(suffixLength()), payload(), records());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        Parcelable[] parcelableArr;
        parcel.writeString(name());
        parcel.writeString(id());
        parcel.writeString(tnf());
        parcel.writeString(type());
        parcel.writeString(prefix());
        parcel.writeInt(prefixLength());
        parcel.writeString(suffix());
        parcel.writeInt(suffixLength());
        parcel.writeString(payload());
        if (records() == null) {
            parcelableArr = null;
        } else {
            Record[] recordArr = (Record[]) records().toArray(new Record[0]);
        }
        parcel.writeTypedArray(parcelableArr, i);
    }

    public static TypeAdapter<Record> typeAdapter(Gson gson) {
        return new AutoValue_Record$GsonTypeAdapter(gson);
    }
}
