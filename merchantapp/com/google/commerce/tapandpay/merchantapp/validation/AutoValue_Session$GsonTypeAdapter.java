package com.google.commerce.tapandpay.merchantapp.validation;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;

public final class AutoValue_Session$GsonTypeAdapter extends TypeAdapter<Session> {
    private final TypeAdapter<ArrayList<Apdu>> apdusAdapter;
    private final TypeAdapter<String> nameAdapter;

    public AutoValue_Session$GsonTypeAdapter(Gson gson) {
        this.nameAdapter = gson.getAdapter(String.class);
        this.apdusAdapter = gson.getAdapter(new TypeToken<ArrayList<Apdu>>(this) {
        });
    }

    public void write(JsonWriter jsonWriter, Session session) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("name");
        this.nameAdapter.write(jsonWriter, session.name());
        jsonWriter.name("apdus");
        this.apdusAdapter.write(jsonWriter, session.apdus());
        jsonWriter.endObject();
    }

    public Session read(JsonReader jsonReader) throws IOException {
        ArrayList arrayList = null;
        jsonReader.beginObject();
        String str = null;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.skipValue();
            } else {
                String str2;
                ArrayList arrayList2;
                byte b = (byte) -1;
                switch (nextName.hashCode()) {
                    case 3373707:
                        if (nextName.equals("name")) {
                            b = (byte) 0;
                            break;
                        }
                        break;
                    case 93017971:
                        if (nextName.equals("apdus")) {
                            b = (byte) 1;
                            break;
                        }
                        break;
                }
                switch (b) {
                    case (byte) 0:
                        ArrayList arrayList3 = arrayList;
                        str2 = (String) this.nameAdapter.read(jsonReader);
                        arrayList2 = arrayList3;
                        break;
                    case (byte) 1:
                        arrayList2 = (ArrayList) this.apdusAdapter.read(jsonReader);
                        str2 = str;
                        break;
                    default:
                        jsonReader.skipValue();
                        arrayList2 = arrayList;
                        str2 = str;
                        break;
                }
                str = str2;
                arrayList = arrayList2;
            }
        }
        jsonReader.endObject();
        return new C$AutoValue_Session(str, arrayList, (byte) 0);
    }
}
