package com.google.commerce.tapandpay.merchantapp.json;

import com.google.android.libraries.commerce.hce.common.SmartTapStatusWord;
import com.google.android.libraries.commerce.hce.iso7816.Iso7816StatusWord;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.commerce.tapandpay.merchantapp.validation.ValidationAutoValueTypeAdapterFactory;
import com.google.common.base.Optional;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Json {

    static class ByteArrayToHexAdapter implements JsonDeserializer<byte[]>, JsonSerializer<byte[]> {
        private ByteArrayToHexAdapter() {
        }

        public JsonElement serialize(byte[] bArr, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(Hex.encodeUpper(bArr));
        }

        public byte[] deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return Hex.decode(jsonElement.getAsString());
        }
    }

    static class ByteToHexAdapter implements JsonDeserializer<Byte>, JsonSerializer<Byte> {
        private ByteToHexAdapter() {
        }

        public JsonElement serialize(Byte b, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(Hex.encodeUpper(b.byteValue()));
        }

        public Byte deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            String asString = jsonElement.getAsString();
            byte[] decode = Hex.decode(asString);
            if (decode.length == 1) {
                return Byte.valueOf(decode[0]);
            }
            throw new IllegalArgumentException(new StringBuilder(String.valueOf(asString).length() + 44).append("Expected JSON string ").append(asString).append(" to be only 1 byte long").toString());
        }
    }

    static class Iso7816StatusWordAdapter implements JsonDeserializer<Iso7816StatusWord>, JsonSerializer<Iso7816StatusWord> {
        private Iso7816StatusWordAdapter() {
        }

        public JsonElement serialize(Iso7816StatusWord iso7816StatusWord, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonElement jsonObject = new JsonObject();
            jsonObject.addProperty("Message", iso7816StatusWord.getMessage());
            jsonObject.add("Status Word", jsonSerializationContext.serialize(iso7816StatusWord.toBytes()));
            return jsonObject;
        }

        public Iso7816StatusWord deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return Iso7816StatusWord.fromBytes((byte[]) jsonDeserializationContext.deserialize(jsonElement.getAsJsonObject().get("Status Word"), byte[].class));
        }
    }

    static class OptionalAdapter<T> implements JsonDeserializer<Optional<T>>, JsonSerializer<Optional<T>> {
        private OptionalAdapter() {
        }

        public JsonElement serialize(Optional<T> optional, Type type, JsonSerializationContext jsonSerializationContext) {
            return jsonSerializationContext.serialize(optional.orNull());
        }

        public Optional<T> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return Optional.fromNullable(jsonDeserializationContext.deserialize(jsonElement, ((ParameterizedType) type).getActualTypeArguments()[0]));
        }
    }

    static class SmartTapStatusWordAdapter implements JsonDeserializer<SmartTapStatusWord>, JsonSerializer<SmartTapStatusWord> {
        private SmartTapStatusWordAdapter() {
        }

        public JsonElement serialize(SmartTapStatusWord smartTapStatusWord, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonElement jsonObject = new JsonObject();
            jsonObject.addProperty("Message", smartTapStatusWord.getCode().getMessage());
            jsonObject.add("Status Word", jsonSerializationContext.serialize(smartTapStatusWord.toBytes()));
            return jsonObject;
        }

        public SmartTapStatusWord deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return SmartTapStatusWord.fromBytes((byte[]) jsonDeserializationContext.deserialize(jsonElement.getAsJsonObject().get("Status Word"), byte[].class));
        }
    }

    private Json() {
    }

    public static GsonBuilder getGsonBuilder() {
        return new GsonBuilder().registerTypeAdapterFactory(ValidationAutoValueTypeAdapterFactory.create()).registerTypeAdapter(byte[].class, new ByteArrayToHexAdapter()).registerTypeAdapter(Byte.class, new ByteToHexAdapter()).registerTypeAdapter(Optional.class, new OptionalAdapter()).registerTypeAdapter(SmartTapStatusWord.class, new SmartTapStatusWordAdapter()).registerTypeAdapter(Iso7816StatusWord.class, new Iso7816StatusWordAdapter()).setPrettyPrinting();
    }
}
