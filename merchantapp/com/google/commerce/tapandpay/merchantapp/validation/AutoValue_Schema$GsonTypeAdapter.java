package com.google.commerce.tapandpay.merchantapp.validation;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;

public final class AutoValue_Schema$GsonTypeAdapter extends TypeAdapter<Schema> {
    private final TypeAdapter<String> nameAdapter;
    private final TypeAdapter<ArrayList<Session>> sessionsAdapter;
    private final TypeAdapter<Type> typeAdapter;

    public AutoValue_Schema$GsonTypeAdapter(Gson gson) {
        this.nameAdapter = gson.getAdapter(String.class);
        this.typeAdapter = gson.getAdapter(Type.class);
        this.sessionsAdapter = gson.getAdapter(new TypeToken<ArrayList<Session>>(this) {
        });
    }

    public void write(JsonWriter jsonWriter, Schema schema) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("name");
        this.nameAdapter.write(jsonWriter, schema.name());
        jsonWriter.name("type");
        this.typeAdapter.write(jsonWriter, schema.type());
        jsonWriter.name("sessions");
        this.sessionsAdapter.write(jsonWriter, schema.sessions());
        jsonWriter.endObject();
    }

    public Schema read(JsonReader jsonReader) throws IOException {
        ArrayList arrayList = null;
        jsonReader.beginObject();
        Type type = null;
        String str = null;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.skipValue();
            } else {
                Type type2;
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
                    case 3575610:
                        if (nextName.equals("type")) {
                            b = (byte) 1;
                            break;
                        }
                        break;
                    case 1405079709:
                        if (nextName.equals("sessions")) {
                            b = (byte) 2;
                            break;
                        }
                        break;
                }
                switch (b) {
                    case (byte) 0:
                        ArrayList arrayList3 = arrayList;
                        type2 = type;
                        str2 = (String) this.nameAdapter.read(jsonReader);
                        arrayList2 = arrayList3;
                        break;
                    case (byte) 1:
                        str2 = str;
                        arrayList2 = arrayList;
                        type2 = (Type) this.typeAdapter.read(jsonReader);
                        break;
                    case (byte) 2:
                        arrayList2 = (ArrayList) this.sessionsAdapter.read(jsonReader);
                        type2 = type;
                        str2 = str;
                        break;
                    default:
                        jsonReader.skipValue();
                        arrayList2 = arrayList;
                        type2 = type;
                        str2 = str;
                        break;
                }
                str = str2;
                type = type2;
                arrayList = arrayList2;
            }
        }
        jsonReader.endObject();
        return new C$AutoValue_Schema(str, type, arrayList, (byte) 0);
    }
}
