package com.google.commerce.tapandpay.merchantapp.validation;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.commerce.tapandpay.merchantapp.validation.ValidationResults.Status;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public abstract class Session implements Parcelable {
    public static final Creator<Session> CREATOR = new Creator<Session>() {
        public Session createFromParcel(Parcel parcel) {
            return Session.create(parcel);
        }

        public Session[] newArray(int i) {
            return new Session[i];
        }
    };

    public static abstract class Builder {
        public abstract Session build();

        public abstract Builder setApdus(ArrayList<Apdu> arrayList);

        public abstract Builder setName(String str);

        public static Builder builder() {
            return new Builder().setName("Session").setApdus(new ArrayList());
        }
    }

    public abstract ArrayList<Apdu> apdus();

    public abstract String name();

    public static Session create(String str, ArrayList<Apdu> arrayList) {
        return Builder.builder().setName(str).setApdus(arrayList).build();
    }

    public static Session create(String str, Apdu... apduArr) {
        return create(str, new ArrayList(Arrays.asList(apduArr)));
    }

    public static Session create(Parcel parcel) {
        return create(parcel.readString(), (Apdu[]) parcel.createTypedArray(Apdu.CREATOR));
    }

    public ValidationResults validate(List<byte[]> list) {
        if (apdus() == null || apdus().isEmpty()) {
            return ValidationResults.create(Status.INVALID, "%s: Session contains no APDU schemas.", name());
        } else if (list.size() != apdus().size()) {
            return ValidationResults.create(Status.FAILURE, "%s: Session: Different number of APDUs and APDU schemas to validate against. Number of APDUs: %s. Number of APDU schemas: %s.", name(), Integer.valueOf(list.size()), Integer.valueOf(apdus().size()));
        } else {
            Iterator it = apdus().iterator();
            com.google.commerce.tapandpay.merchantapp.validation.ValidationResults.Builder status = com.google.commerce.tapandpay.merchantapp.validation.ValidationResults.Builder.builder().setMessage(name()).setStatus(Status.SUCCESS);
            for (byte[] validate : list) {
                status.addNestedResult(((Apdu) it.next()).validate(validate));
            }
            return status.build();
        }
    }

    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        Session session = (Session) obj;
        if (apdus() == null || session.apdus() == null || !Objects.equals(name(), session.name()) || !Arrays.equals(apdus().toArray(new Apdu[0]), session.apdus().toArray(new Apdu[0]))) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return com.google.common.base.Objects.hashCode(apdus());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        Parcelable[] parcelableArr;
        parcel.writeString(name());
        if (apdus() == null) {
            parcelableArr = null;
        } else {
            Apdu[] apduArr = (Apdu[]) apdus().toArray(new Apdu[0]);
        }
        parcel.writeTypedArray(parcelableArr, i);
    }

    public static TypeAdapter<Session> typeAdapter(Gson gson) {
        return new AutoValue_Session$GsonTypeAdapter(gson);
    }
}
