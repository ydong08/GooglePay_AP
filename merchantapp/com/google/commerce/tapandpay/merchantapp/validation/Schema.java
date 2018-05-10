package com.google.commerce.tapandpay.merchantapp.validation;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.commerce.tapandpay.merchantapp.validation.ValidationResults.Status;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public abstract class Schema implements Parcelable {
    public static final Creator<Schema> CREATOR = new Creator<Schema>() {
        public Schema createFromParcel(Parcel parcel) {
            return Schema.create(parcel);
        }

        public Schema[] newArray(int i) {
            return new Schema[i];
        }
    };
    private static final Gson GSON = new GsonBuilder().registerTypeAdapterFactory(ValidationAutoValueTypeAdapterFactory.create()).setPrettyPrinting().create();
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();

    public static abstract class Builder {
        public abstract Schema build();

        public abstract Builder setName(String str);

        public abstract Builder setSessions(ArrayList<Session> arrayList);

        public abstract Builder setType(Type type);

        public static Builder builder() {
            return new Builder().setName("Schema").setType(Type.UNSPECIFIED).setSessions(new ArrayList());
        }
    }

    enum Type {
        UNSPECIFIED,
        ALL,
        ONE_OF,
        NONE
    }

    public abstract String name();

    public abstract ArrayList<Session> sessions();

    public abstract Type type();

    public static Schema create(String str, Type type, ArrayList<Session> arrayList) {
        return Builder.builder().setName(str).setType(type).setSessions(arrayList).build();
    }

    public static Schema create(String str, Type type, Session... sessionArr) {
        return create(str, type, new ArrayList(Arrays.asList(sessionArr)));
    }

    public static Schema create(String str, ArrayList<Session> arrayList) {
        return create(str, Type.ALL, (ArrayList) arrayList);
    }

    public static Schema create(String str, Session... sessionArr) {
        return create(str, new ArrayList(Arrays.asList(sessionArr)));
    }

    public static Schema create(Parcel parcel) {
        String readString = parcel.readString();
        String readString2 = parcel.readString();
        if (readString2 == null) {
            return create(readString, (Session[]) parcel.createTypedArray(Session.CREATOR));
        }
        return create(readString, Type.valueOf(readString2), (Session[]) parcel.createTypedArray(Session.CREATOR));
    }

    public static Schema fromUri(ContentResolver contentResolver, Uri uri) throws IOException, JsonSyntaxException {
        InputStream openInputStream = contentResolver.openInputStream(uri);
        try {
            Schema fromStream = fromStream(openInputStream);
            return fromStream;
        } finally {
            if (openInputStream != null) {
                openInputStream.close();
            }
        }
    }

    public static Schema fromStream(InputStream inputStream) throws JsonSyntaxException {
        Scanner useDelimiter = new Scanner(inputStream).useDelimiter("\\A");
        if (useDelimiter.hasNext()) {
            return fromString(useDelimiter.next());
        }
        LOG.d("Validation file was empty.", new Object[0]);
        return null;
    }

    public static Schema fromString(String str) throws JsonSyntaxException {
        if (Strings.isNullOrEmpty(str)) {
            return null;
        }
        return (Schema) GSON.fromJson(str, Schema.class);
    }

    public ValidationResults validate(List<byte[]> list) {
        if (sessions() == null || sessions().isEmpty()) {
            return ValidationResults.create(Status.NO_SCHEMA, "%s: Schema contains no sessions.", name());
        }
        com.google.commerce.tapandpay.merchantapp.validation.ValidationResults.Builder status = com.google.commerce.tapandpay.merchantapp.validation.ValidationResults.Builder.builder().setMessage(name()).setStatus(Status.SUCCESS);
        Iterator it = sessions().iterator();
        while (it.hasNext()) {
            status.addNestedResult(((Session) it.next()).validate(list));
        }
        if (status.status() == Status.INVALID) {
            return status.build();
        }
        switch (type()) {
            case ALL:
                break;
            case ONE_OF:
                it = status.nestedResults().iterator();
                while (it.hasNext()) {
                    if (((ValidationResults) it.next()).status() == Status.SUCCESS) {
                        status.setStatus(Status.SUCCESS);
                        break;
                    }
                }
                break;
            case NONE:
                if (status.status() == Status.FAILURE) {
                    status.setStatus(Status.SUCCESS);
                }
                it = status.nestedResults().iterator();
                while (it.hasNext()) {
                    if (((ValidationResults) it.next()).status() == Status.SUCCESS) {
                        status.setStatus(Status.FAILURE);
                        break;
                    }
                }
                break;
            default:
                return ValidationResults.create(Status.INVALID, "%s: Unsupprted schema validation type for sessions.", name());
        }
        return status.build();
    }

    public String toJson() {
        return GSON.toJson((Object) this);
    }

    public String toString() {
        return toJson();
    }

    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        Schema schema = (Schema) obj;
        if (sessions() != null && schema.sessions() != null && Objects.equals(name(), schema.name()) && Objects.equals(type(), schema.type()) && Arrays.equals(sessions().toArray(new Session[0]), schema.sessions().toArray(new Session[0]))) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return com.google.common.base.Objects.hashCode(name(), type(), sessions());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        Parcelable[] parcelableArr = null;
        parcel.writeString(name());
        parcel.writeString(type() == null ? null : type().name());
        if (sessions() != null) {
            Object[] objArr = (Session[]) sessions().toArray(new Session[0]);
        }
        parcel.writeTypedArray(parcelableArr, i);
    }

    public static TypeAdapter<Schema> typeAdapter(Gson gson) {
        return new AutoValue_Schema$GsonTypeAdapter(gson);
    }
}
