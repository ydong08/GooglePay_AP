package com.google.commerce.tapandpay.merchantapp.validation;

import com.google.commerce.tapandpay.merchantapp.validation.ValidationResults.Status;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;

final class AutoValue_ValidationResults extends C$AutoValue_ValidationResults {

    public static final class GsonTypeAdapter extends TypeAdapter<ValidationResults> {
        private final TypeAdapter<String> messageAdapter;
        private final TypeAdapter<ArrayList<ValidationResults>> nestedResultsAdapter;
        private final TypeAdapter<Status> statusAdapter;
        private final TypeAdapter<Throwable> throwableAdapter;

        public GsonTypeAdapter(Gson gson) {
            this.statusAdapter = gson.getAdapter(Status.class);
            this.messageAdapter = gson.getAdapter(String.class);
            this.throwableAdapter = gson.getAdapter(Throwable.class);
            this.nestedResultsAdapter = gson.getAdapter(new TypeToken<ArrayList<ValidationResults>>(this) {
            });
        }

        public void write(JsonWriter jsonWriter, ValidationResults validationResults) throws IOException {
            jsonWriter.beginObject();
            jsonWriter.name("status");
            this.statusAdapter.write(jsonWriter, validationResults.status());
            if (validationResults.message() != null) {
                jsonWriter.name("message");
                this.messageAdapter.write(jsonWriter, validationResults.message());
            }
            if (validationResults.throwable() != null) {
                jsonWriter.name("throwable");
                this.throwableAdapter.write(jsonWriter, validationResults.throwable());
            }
            jsonWriter.name("nestedResults");
            this.nestedResultsAdapter.write(jsonWriter, validationResults.nestedResults());
            jsonWriter.endObject();
        }

        public ValidationResults read(JsonReader jsonReader) throws IOException {
            ArrayList arrayList = null;
            jsonReader.beginObject();
            Throwable th = null;
            String str = null;
            Status status = null;
            while (jsonReader.hasNext()) {
                String nextName = jsonReader.nextName();
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.skipValue();
                } else {
                    Throwable th2;
                    String str2;
                    Status status2;
                    ArrayList arrayList2;
                    Object obj = -1;
                    switch (nextName.hashCode()) {
                        case -1255712448:
                            if (nextName.equals("throwable")) {
                                obj = 2;
                                break;
                            }
                            break;
                        case -892481550:
                            if (nextName.equals("status")) {
                                obj = null;
                                break;
                            }
                            break;
                        case -788545793:
                            if (nextName.equals("nestedResults")) {
                                obj = 3;
                                break;
                            }
                            break;
                        case 954925063:
                            if (nextName.equals("message")) {
                                obj = 1;
                                break;
                            }
                            break;
                    }
                    ArrayList arrayList3;
                    switch (obj) {
                        case null:
                            arrayList3 = arrayList;
                            th2 = th;
                            str2 = str;
                            status2 = (Status) this.statusAdapter.read(jsonReader);
                            arrayList2 = arrayList3;
                            break;
                        case 1:
                            status2 = status;
                            Throwable th3 = th;
                            str2 = (String) this.messageAdapter.read(jsonReader);
                            arrayList2 = arrayList;
                            th2 = th3;
                            break;
                        case 2:
                            str2 = str;
                            status2 = status;
                            arrayList3 = arrayList;
                            th2 = (Throwable) this.throwableAdapter.read(jsonReader);
                            arrayList2 = arrayList3;
                            break;
                        case 3:
                            arrayList2 = (ArrayList) this.nestedResultsAdapter.read(jsonReader);
                            th2 = th;
                            str2 = str;
                            status2 = status;
                            break;
                        default:
                            jsonReader.skipValue();
                            arrayList2 = arrayList;
                            th2 = th;
                            str2 = str;
                            status2 = status;
                            break;
                    }
                    status = status2;
                    str = str2;
                    th = th2;
                    arrayList = arrayList2;
                }
            }
            jsonReader.endObject();
            return new AutoValue_ValidationResults(status, str, th, arrayList);
        }
    }

    AutoValue_ValidationResults(Status status, String str, Throwable th, ArrayList<ValidationResults> arrayList) {
        super(status, str, th, arrayList);
    }
}
