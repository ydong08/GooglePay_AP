package com.google.commerce.tapandpay.merchantapp.sharing;

import android.content.Context;
import com.google.android.apps.common.inject.annotation.ApplicationContext;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObjectConverter;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.SessionRequest;
import com.google.android.libraries.commerce.hce.basictlv.BasicPrimitiveTlv;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlv;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlvException;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.commerce.tapandpay.merchantapp.common.ByteHelper;
import com.google.commerce.tapandpay.merchantapp.common.ByteHelper.ByteHelperException;
import com.google.commerce.tapandpay.merchantapp.common.CommandType;
import com.google.commerce.tapandpay.merchantapp.json.Json;
import com.google.commerce.tapandpay.merchantapp.result.Result;
import com.google.commerce.tapandpay.merchantapp.result.Result.Builder;
import com.google.commerce.tapandpay.merchantapp.result.Result.CommandAndResponse;
import com.google.commerce.tapandpay.merchantapp.result.Result.InstructionDuration;
import com.google.commerce.tapandpay.merchantapp.result.Result.Status;
import com.google.commerce.tapandpay.merchantapp.smarttap.v2.MerchantValuable;
import com.google.commerce.tapandpay.merchantapp.smarttap.v2.MerchantValuableTypeAdapterFactory;
import com.google.commerce.tapandpay.merchantapp.validation.ValidationResults;
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;

public class JsonConverter {
    private final Context context;
    private final Gson gson = Json.getGsonBuilder().registerTypeAdapterFactory(MerchantValuableTypeAdapterFactory.create()).registerTypeHierarchyAdapter(BasicTlv.class, new BasicTlvAdapter()).registerTypeHierarchyAdapter(InstructionDuration.class, new InstructionDurationAdapter()).registerTypeHierarchyAdapter(Result.class, new ResultAdapter()).create();
    private final ServiceObjectConverter serviceObjectConverter;

    class BasicTlvAdapter implements JsonDeserializer<BasicTlv>, JsonSerializer<BasicTlv> {
        private BasicTlvAdapter() {
        }

        public BasicTlv deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            try {
                return BasicPrimitiveTlv.getInstance(Hex.decode(jsonElement.getAsJsonObject().get("Tag").getAsString())[0], Hex.decode(jsonElement.getAsJsonObject().get("Value").getAsString()));
            } catch (IllegalArgumentException e) {
                return null;
            } catch (BasicTlvException e2) {
                return null;
            }
        }

        public JsonElement serialize(BasicTlv basicTlv, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonElement jsonObject = new JsonObject();
            jsonObject.addProperty("Tag", basicTlv.getTagAsString());
            jsonObject.addProperty("Length", Integer.valueOf(basicTlv.getLength()));
            jsonObject.addProperty("Value", ByteHelper.getTlvStringValue(JsonConverter.this.context, basicTlv));
            return jsonObject;
        }
    }

    class InstructionDurationAdapter implements JsonDeserializer<InstructionDuration>, JsonSerializer<InstructionDuration> {
        private InstructionDurationAdapter() {
        }

        public InstructionDuration deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            Matcher matcher = Pattern.compile("(.+)\\s:\\s(\\d+)ms").matcher(jsonElement.getAsString());
            if (matcher.find()) {
                return InstructionDuration.create(CommandType.fromTitle(matcher.group(1), JsonConverter.this.context), Long.parseLong(matcher.group(2)));
            }
            return null;
        }

        public JsonElement serialize(InstructionDuration instructionDuration, Type type, JsonSerializationContext jsonSerializationContext) {
            String valueOf = String.valueOf(JsonConverter.this.context.getString(instructionDuration.type().titleResId()));
            return new JsonPrimitive(new StringBuilder(String.valueOf(valueOf).length() + 25).append(valueOf).append(" : ").append(instructionDuration.duration()).append("ms").toString());
        }
    }

    class ResultAdapter implements JsonDeserializer<Result>, JsonSerializer<Result> {
        private ResultAdapter() {
        }

        public JsonElement serialize(Result result, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonElement jsonObject = new JsonObject();
            jsonObject.addProperty("SmartTap Status", JsonConverter.this.context.getString(result.smarttapStatus().stringResId()));
            jsonObject.addProperty("Payment Status", JsonConverter.this.context.getString(result.paymentStatus().stringResId()));
            jsonObject.addProperty("Validation Results", result.validationResults().toJson());
            jsonObject.addProperty("Timestamp", result.timestamp());
            jsonObject.add("Durations (ms until phone responded)", jsonSerializationContext.serialize(result.instructionDurations()));
            jsonObject.add("Apdus", JsonConverter.this.toApdus(result.commandAndResponses(), jsonSerializationContext));
            jsonObject.add("Merchant Valuables (Pre-Encryption)", jsonSerializationContext.serialize(result.encryptedValuables()));
            jsonObject.addProperty("Test Case Id", Long.valueOf(result.testCaseId()));
            return jsonObject;
        }

        public Result deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            Builder builder = Result.builder();
            builder.setSmarttapStatus(Status.fromString(asJsonObject.get("SmartTap Status").getAsString(), JsonConverter.this.context));
            builder.setPaymentStatus(Status.fromString(asJsonObject.get("Payment Status").getAsString(), JsonConverter.this.context));
            builder.setValidationResults(ValidationResults.fromString(asJsonObject.get("Validation Results").getAsString()));
            builder.setTimestamp(asJsonObject.get("Timestamp").getAsString());
            builder.setInstructionDurations((List) jsonDeserializationContext.deserialize(asJsonObject.get("Durations (ms until phone responded)"), new TypeToken<List<InstructionDuration>>(this) {
            }.getType()));
            builder.setCommandAndResponses(JsonConverter.this.fromApdus(asJsonObject.get("Apdus").getAsJsonArray(), jsonDeserializationContext));
            Set set = (Set) jsonDeserializationContext.deserialize(asJsonObject.getAsJsonArray("Merchant Valuables (Pre-Encryption)"), new TypeToken<Set<MerchantValuable>>(this) {
            }.getType());
            if (!(set == null || set.isEmpty())) {
                builder.setEncryptedValuables(set);
            }
            builder.setTestCaseId(asJsonObject.get("Test Case Id").getAsLong());
            return builder.build();
        }
    }

    static class V2Command {
        private final JsonElement json;
        private final SessionRequest sessionRequest;

        V2Command(SessionRequest sessionRequest, JsonElement jsonElement) {
            this.sessionRequest = sessionRequest;
            this.json = jsonElement;
        }

        SessionRequest getSessionRequest() {
            return this.sessionRequest;
        }

        JsonElement getJson() {
            return this.json;
        }
    }

    @Inject
    public JsonConverter(@ApplicationContext Context context, ServiceObjectConverter serviceObjectConverter) {
        this.context = context;
        this.serviceObjectConverter = serviceObjectConverter;
    }

    public String toJsonArray(List<Result> list) {
        return this.gson.toJson((Object) list);
    }

    public List<Result> fromJsonArray(String str) {
        return (List) this.gson.fromJson(str, new TypeToken<List<Result>>(this) {
        }.getType());
    }

    private JsonArray toApdus(List<CommandAndResponse> list, JsonSerializationContext jsonSerializationContext) {
        CommandType commandType = CommandType.SELECT_SMARTTAP_V1_3;
        JsonArray jsonArray = new JsonArray();
        CommandType commandType2 = commandType;
        for (CommandAndResponse commandAndResponse : list) {
            JsonElement json;
            JsonElement v2Response;
            JsonElement jsonObject = new JsonObject();
            jsonObject.addProperty("Description", commandAndResponse.type().commandTitle(this.context));
            jsonObject.add("Data", jsonSerializationContext.serialize(commandAndResponse.command()));
            JsonElement jsonObject2 = new JsonObject();
            jsonObject2.addProperty("Description", commandAndResponse.type().responseTitle(this.context, commandAndResponse.isResponseError()));
            jsonObject2.add("Data", jsonSerializationContext.serialize(commandAndResponse.response()));
            if (commandAndResponse.type() == CommandType.SELECT_SMARTTAP_V2_0) {
                commandType2 = CommandType.SELECT_SMARTTAP_V2_0;
                try {
                    jsonObject2.add("Handset Nonce", jsonSerializationContext.serialize(ByteHelper.getHandsetNonce(commandAndResponse.response())));
                } catch (ByteHelperException e) {
                    jsonObject2.addProperty("Handset Nonce", e.getMessage());
                }
            }
            if (commandType2 == CommandType.SELECT_SMARTTAP_V2_0) {
                V2Command v2Command = getV2Command(commandAndResponse);
                json = v2Command.getJson();
                SessionRequest sessionRequest = v2Command.getSessionRequest();
                v2Response = sessionRequest == null ? null : getV2Response(commandAndResponse, sessionRequest.getVersion());
            } else {
                json = getV1Command(commandAndResponse);
                v2Response = getV1Response(commandAndResponse);
            }
            if (json != null) {
                jsonObject.add("Command", json);
            }
            if (v2Response != null) {
                jsonObject2.add("Response", v2Response);
            }
            jsonArray.add(jsonObject);
            jsonArray.add(jsonObject2);
        }
        return jsonArray;
    }

    private List<CommandAndResponse> fromApdus(JsonArray jsonArray, JsonDeserializationContext jsonDeserializationContext) {
        if (jsonArray.size() % 2 != 0) {
            return ImmutableList.of();
        }
        List<CommandAndResponse> arrayList = new ArrayList();
        byte[] bArr = new byte[0];
        CommandType commandType = CommandType.ERROR;
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject asJsonObject = jsonArray.get(i).getAsJsonObject();
            CommandType fromTitle = CommandType.fromTitle(asJsonObject.get("Description").getAsString(), this.context);
            byte[] bArr2 = (byte[]) jsonDeserializationContext.deserialize(asJsonObject.get("Data"), byte[].class);
            if (i % 2 == 0) {
                bArr = bArr2;
                commandType = fromTitle;
            } else {
                arrayList.add(CommandAndResponse.create(bArr, bArr2, commandType, fromTitle == CommandType.ERROR));
            }
        }
        return arrayList;
    }

    private V2Command getV2Command(CommandAndResponse commandAndResponse) {
        try {
            SessionRequest negotiateSmartTapRequest;
            switch (commandAndResponse.type()) {
                case NEGOTIATE_SMARTTAP:
                    negotiateSmartTapRequest = ByteHelper.negotiateSmartTapRequest(commandAndResponse.command());
                    break;
                case GET_SMARTTAP_DATA:
                    negotiateSmartTapRequest = ByteHelper.getSmartTapDataRequest(commandAndResponse.command());
                    break;
                case GET_ADDITIONAL_SMARTTAP_DATA:
                    negotiateSmartTapRequest = ByteHelper.getAdditionalSmartTapDataRequest(commandAndResponse.command());
                    break;
                case PUSH_SMARTTAP_DATA:
                    negotiateSmartTapRequest = ByteHelper.pushSmartTapDataRequest(commandAndResponse.command());
                    break;
                default:
                    return new V2Command(null, null);
            }
            return new V2Command(negotiateSmartTapRequest, this.gson.toJsonTree(negotiateSmartTapRequest));
        } catch (ByteHelperException e) {
            String str = "Error parsing V2 response: ";
            String valueOf = String.valueOf(e.getMessage());
            if (valueOf.length() != 0) {
                valueOf = str.concat(valueOf);
            } else {
                valueOf = new String(str);
            }
            return new V2Command(null, new JsonPrimitive(valueOf));
        }
    }

    private JsonElement getV2Response(CommandAndResponse commandAndResponse, short s) {
        try {
            switch (commandAndResponse.type()) {
                case NEGOTIATE_SMARTTAP:
                    return this.gson.toJsonTree(ByteHelper.negotiateSmartTapResponse(commandAndResponse.response(), s));
                case GET_SMARTTAP_DATA:
                case GET_ADDITIONAL_SMARTTAP_DATA:
                    return this.gson.toJsonTree(ByteHelper.getSmartTapDataResponse(commandAndResponse.response(), this.serviceObjectConverter, s));
                case PUSH_SMARTTAP_DATA:
                    return this.gson.toJsonTree(ByteHelper.sessionResponse(commandAndResponse.response(), SmartTap2Values.PUSH_SERVICE_RESPONSE_NDEF_TYPE, s));
                default:
                    return null;
            }
        } catch (ByteHelperException e) {
            String str = "Error parsing V2 response: ";
            String valueOf = String.valueOf(e.getMessage());
            return new JsonPrimitive(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
        }
    }

    private JsonElement getV1Command(CommandAndResponse commandAndResponse) {
        try {
            switch (commandAndResponse.type()) {
                case GET_SMARTTAP_DATA:
                    return this.gson.toJsonTree(ByteHelper.getCommandTlvs(commandAndResponse.command()));
                default:
                    return null;
            }
        } catch (ByteHelperException e) {
            String str = "Error parsing V1 command: ";
            String valueOf = String.valueOf(e.getMessage());
            return new JsonPrimitive(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
        }
    }

    private JsonElement getV1Response(CommandAndResponse commandAndResponse) {
        try {
            switch (commandAndResponse.type()) {
                case GET_SMARTTAP_DATA:
                case GET_ADDITIONAL_SMARTTAP_DATA:
                    return this.gson.toJsonTree(ByteHelper.getResponseTlvs(commandAndResponse.response()));
                default:
                    return null;
            }
        } catch (ByteHelperException e) {
            String str = "Error parsing V1 response: ";
            String valueOf = String.valueOf(e.getMessage());
            return new JsonPrimitive(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
        }
    }
}
