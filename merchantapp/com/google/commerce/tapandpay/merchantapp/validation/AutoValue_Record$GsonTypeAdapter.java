package com.google.commerce.tapandpay.merchantapp.validation;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public final class AutoValue_Record$GsonTypeAdapter extends TypeAdapter<Record> {
    private final TypeAdapter<String> idAdapter;
    private final TypeAdapter<String> nameAdapter;
    private final TypeAdapter<String> payloadAdapter;
    private final TypeAdapter<String> prefixAdapter;
    private final TypeAdapter<Integer> prefixLengthAdapter;
    private final TypeAdapter<List<Record>> recordsAdapter;
    private final TypeAdapter<String> suffixAdapter;
    private final TypeAdapter<Integer> suffixLengthAdapter;
    private final TypeAdapter<String> tnfAdapter;
    private final TypeAdapter<String> typeAdapter;

    public AutoValue_Record$GsonTypeAdapter(Gson gson) {
        this.nameAdapter = gson.getAdapter(String.class);
        this.idAdapter = gson.getAdapter(String.class);
        this.tnfAdapter = gson.getAdapter(String.class);
        this.typeAdapter = gson.getAdapter(String.class);
        this.prefixAdapter = gson.getAdapter(String.class);
        this.prefixLengthAdapter = gson.getAdapter(Integer.class);
        this.suffixAdapter = gson.getAdapter(String.class);
        this.suffixLengthAdapter = gson.getAdapter(Integer.class);
        this.payloadAdapter = gson.getAdapter(String.class);
        this.recordsAdapter = gson.getAdapter(new TypeToken<List<Record>>(this) {
        });
    }

    public void write(JsonWriter jsonWriter, Record record) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("name");
        this.nameAdapter.write(jsonWriter, record.name());
        if (record.id() != null) {
            jsonWriter.name("id");
            this.idAdapter.write(jsonWriter, record.id());
        }
        if (record.tnf() != null) {
            jsonWriter.name("tnf");
            this.tnfAdapter.write(jsonWriter, record.tnf());
        }
        if (record.type() != null) {
            jsonWriter.name("type");
            this.typeAdapter.write(jsonWriter, record.type());
        }
        if (record.prefix() != null) {
            jsonWriter.name("prefix");
            this.prefixAdapter.write(jsonWriter, record.prefix());
        }
        jsonWriter.name("prefixLength");
        this.prefixLengthAdapter.write(jsonWriter, Integer.valueOf(record.prefixLength()));
        if (record.suffix() != null) {
            jsonWriter.name("suffix");
            this.suffixAdapter.write(jsonWriter, record.suffix());
        }
        jsonWriter.name("suffixLength");
        this.suffixLengthAdapter.write(jsonWriter, Integer.valueOf(record.suffixLength()));
        if (record.payload() != null) {
            jsonWriter.name("payload");
            this.payloadAdapter.write(jsonWriter, record.payload());
        }
        jsonWriter.name("records");
        this.recordsAdapter.write(jsonWriter, record.records());
        jsonWriter.endObject();
    }

    public Record read(JsonReader jsonReader) throws IOException {
        String str = null;
        jsonReader.beginObject();
        List emptyList = Collections.emptyList();
        int i = 0;
        String str2 = null;
        int i2 = 0;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        String str7 = null;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            if (jsonReader.peek() != JsonToken.NULL) {
                byte b = (byte) -1;
                switch (nextName.hashCode()) {
                    case -980110702:
                        if (nextName.equals("prefix")) {
                            b = (byte) 4;
                            break;
                        }
                        break;
                    case -891422895:
                        if (nextName.equals("suffix")) {
                            b = (byte) 6;
                            break;
                        }
                        break;
                    case -786701938:
                        if (nextName.equals("payload")) {
                            b = (byte) 8;
                            break;
                        }
                        break;
                    case -661348969:
                        if (nextName.equals("suffixLength")) {
                            b = (byte) 7;
                            break;
                        }
                        break;
                    case 3355:
                        if (nextName.equals("id")) {
                            b = (byte) 1;
                            break;
                        }
                        break;
                    case 114988:
                        if (nextName.equals("tnf")) {
                            b = (byte) 2;
                            break;
                        }
                        break;
                    case 3373707:
                        if (nextName.equals("name")) {
                            b = (byte) 0;
                            break;
                        }
                        break;
                    case 3575610:
                        if (nextName.equals("type")) {
                            b = (byte) 3;
                            break;
                        }
                        break;
                    case 243803160:
                        if (nextName.equals("prefixLength")) {
                            b = (byte) 5;
                            break;
                        }
                        break;
                    case 1082596930:
                        if (nextName.equals("records")) {
                            b = (byte) 9;
                            break;
                        }
                        break;
                }
                switch (b) {
                    case (byte) 0:
                        str7 = (String) this.nameAdapter.read(jsonReader);
                        break;
                    case (byte) 1:
                        str6 = (String) this.idAdapter.read(jsonReader);
                        break;
                    case (byte) 2:
                        str5 = (String) this.tnfAdapter.read(jsonReader);
                        break;
                    case (byte) 3:
                        str4 = (String) this.typeAdapter.read(jsonReader);
                        break;
                    case (byte) 4:
                        str3 = (String) this.prefixAdapter.read(jsonReader);
                        break;
                    case (byte) 5:
                        i2 = ((Integer) this.prefixLengthAdapter.read(jsonReader)).intValue();
                        break;
                    case (byte) 6:
                        str2 = (String) this.suffixAdapter.read(jsonReader);
                        break;
                    case (byte) 7:
                        i = ((Integer) this.suffixLengthAdapter.read(jsonReader)).intValue();
                        break;
                    case (byte) 8:
                        str = (String) this.payloadAdapter.read(jsonReader);
                        break;
                    case (byte) 9:
                        emptyList = (List) this.recordsAdapter.read(jsonReader);
                        break;
                    default:
                        jsonReader.skipValue();
                        break;
                }
            }
            jsonReader.skipValue();
        }
        jsonReader.endObject();
        return new C$AutoValue_Record(str7, str6, str5, str4, str3, i2, str2, i, str, emptyList, (byte) 0);
    }
}
