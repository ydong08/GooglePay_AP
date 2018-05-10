package com.google.gson;

public class JsonSerializationContext {
    final /* synthetic */ Gson this$0;

    JsonSerializationContext(Gson gson) {
        this.this$0 = gson;
    }

    public JsonElement serialize(Object obj) {
        return this.this$0.toJsonTree(obj);
    }
}
