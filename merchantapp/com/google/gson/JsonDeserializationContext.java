package com.google.gson;

import java.lang.reflect.Type;

public class JsonDeserializationContext {
    final /* synthetic */ Gson this$0;

    JsonDeserializationContext(Gson gson) {
        this.this$0 = gson;
    }

    public <T> T deserialize(JsonElement jsonElement, Type type) throws JsonParseException {
        return this.this$0.fromJson(jsonElement, type);
    }
}
