package com.google.commerce.tapandpay.merchantapp.validation;

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

public abstract class Apdu implements Parcelable {
    public static final Creator<Apdu> CREATOR = new Creator<Apdu>() {
        public Apdu createFromParcel(Parcel parcel) {
            return Apdu.create(parcel);
        }

        public Apdu[] newArray(int i) {
            return new Apdu[i];
        }
    };

    public static abstract class Builder {
        public abstract Apdu build();

        public abstract Builder setName(String str);

        public abstract Builder setPayload(String str);

        public abstract Builder setPrefix(String str);

        public abstract Builder setPrefixLength(int i);

        public abstract Builder setRecords(List<Record> list);

        public abstract Builder setSuffix(String str);

        public abstract Builder setSuffixLength(int i);

        public static Builder builder() {
            return new Builder().setPrefix("APDU").setPrefix("").setPrefixLength(0).setSuffix("").setSuffixLength(0).setPayload("").setRecords(Collections.emptyList());
        }
    }

    public abstract String name();

    public abstract String payload();

    public abstract String prefix();

    public abstract int prefixLength();

    public abstract List<Record> records();

    public abstract String suffix();

    public abstract int suffixLength();

    public static Apdu create(String str, String str2, int i, String str3, int i2, String str4, List<Record> list) {
        return Builder.builder().setName(str).setPrefix(str2).setPrefixLength(i).setSuffix(str3).setSuffixLength(i2).setPayload(str4).setRecords(list).build();
    }

    public static Apdu create(String str, String str2, int i, String str3, int i2, String str4, Record... recordArr) {
        return create(str, str2, i, str3, i2, str4, Arrays.asList(recordArr));
    }

    public static Apdu create(Parcel parcel) {
        return create(parcel.readString(), parcel.readString(), parcel.readInt(), parcel.readString(), parcel.readInt(), parcel.readString(), (Record[]) parcel.createTypedArray(Record.CREATOR));
    }

    public ValidationResults validate(byte[] bArr) {
        com.google.commerce.tapandpay.merchantapp.validation.ValidationResults.Builder status = com.google.commerce.tapandpay.merchantapp.validation.ValidationResults.Builder.builder().setMessage(name()).setStatus(Status.SUCCESS);
        status.addNestedResult(BytePayloadHelper.validatePrefix(prefix(), prefixLength(), bArr));
        status.addNestedResult(BytePayloadHelper.validateSuffix(suffix(), suffixLength(), bArr));
        if (bArr.length < prefixLength() + suffixLength()) {
            status.addNestedResult(ValidationResults.create(Status.FAILURE, "%s: The combined length of the prefix (%s) and suffix (%s) is greater than the APDU length (%s). Bytes: %s", name(), Integer.valueOf(prefixLength()), Integer.valueOf(suffixLength()), Integer.valueOf(bArr.length), Hex.encodeUpper(bArr)));
            return status.build();
        }
        byte[] payload = BytePayloadHelper.getPayload(prefixLength(), suffixLength(), bArr);
        status.addNestedResult(BytePayloadHelper.validatePayload(payload(), payload));
        status.addNestedResult(RecordsHelper.validate(records(), payload));
        return status.build();
    }

    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        Apdu apdu = (Apdu) obj;
        if (Objects.equals(name(), apdu.name()) && Objects.equals(prefix(), apdu.prefix()) && Objects.equals(suffix(), apdu.suffix()) && prefixLength() == apdu.prefixLength() && suffixLength() == apdu.suffixLength() && Objects.equals(payload(), apdu.payload()) && Arrays.equals(records().toArray(new Record[0]), apdu.records().toArray(new Record[0]))) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return com.google.common.base.Objects.hashCode(name(), prefix(), Integer.valueOf(prefixLength()), suffix(), Integer.valueOf(suffixLength()), payload(), records());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        Parcelable[] parcelableArr;
        parcel.writeString(name());
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

    public static TypeAdapter<Apdu> typeAdapter(Gson gson) {
        return new AutoValue_Apdu$GsonTypeAdapter(gson);
    }
}
