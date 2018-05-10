package com.google.commerce.tapandpay.merchantapp.smarttap.v2;

import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject.Issuer;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject.Type;
import com.google.android.libraries.commerce.hce.ndef.Format;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

final class AutoValue_MerchantValuable extends C$AutoValue_MerchantValuable {

    public static final class GsonTypeAdapter extends TypeAdapter<MerchantValuable> {
        private final TypeAdapter<byte[]> cvcAdapter;
        private final TypeAdapter<Format> cvcFormatAdapter;
        private final TypeAdapter<byte[]> deviceIdAdapter;
        private final TypeAdapter<Format> deviceIdFormatAdapter;
        private final TypeAdapter<byte[]> expirationAdapter;
        private final TypeAdapter<Format> expirationFormatAdapter;
        private final TypeAdapter<Issuer> issuerAdapter;
        private final TypeAdapter<Format> issuerFormatAdapter;
        private final TypeAdapter<byte[]> issuerIdAdapter;
        private final TypeAdapter<byte[]> pinAdapter;
        private final TypeAdapter<Format> pinFormatAdapter;
        private final TypeAdapter<String> preferedLanguageAdapter;
        private final TypeAdapter<byte[]> serviceIdAdapter;
        private final TypeAdapter<byte[]> serviceNumberIdAdapter;
        private final TypeAdapter<Format> serviceNumberIdFormatAdapter;
        private final TypeAdapter<byte[]> tapIdAdapter;
        private final TypeAdapter<Format> tapIdFormatAdapter;
        private final TypeAdapter<Type> typeAdapter;

        public GsonTypeAdapter(Gson gson) {
            this.typeAdapter = gson.getAdapter(Type.class);
            this.serviceIdAdapter = gson.getAdapter(byte[].class);
            this.serviceNumberIdAdapter = gson.getAdapter(byte[].class);
            this.serviceNumberIdFormatAdapter = gson.getAdapter(Format.class);
            this.deviceIdAdapter = gson.getAdapter(byte[].class);
            this.deviceIdFormatAdapter = gson.getAdapter(Format.class);
            this.issuerIdAdapter = gson.getAdapter(byte[].class);
            this.issuerAdapter = gson.getAdapter(Issuer.class);
            this.issuerFormatAdapter = gson.getAdapter(Format.class);
            this.tapIdAdapter = gson.getAdapter(byte[].class);
            this.tapIdFormatAdapter = gson.getAdapter(Format.class);
            this.pinAdapter = gson.getAdapter(byte[].class);
            this.pinFormatAdapter = gson.getAdapter(Format.class);
            this.cvcAdapter = gson.getAdapter(byte[].class);
            this.cvcFormatAdapter = gson.getAdapter(Format.class);
            this.expirationAdapter = gson.getAdapter(byte[].class);
            this.expirationFormatAdapter = gson.getAdapter(Format.class);
            this.preferedLanguageAdapter = gson.getAdapter(String.class);
        }

        public void write(JsonWriter jsonWriter, MerchantValuable merchantValuable) throws IOException {
            jsonWriter.beginObject();
            jsonWriter.name("type");
            this.typeAdapter.write(jsonWriter, merchantValuable.type());
            jsonWriter.name("serviceId");
            this.serviceIdAdapter.write(jsonWriter, merchantValuable.serviceId());
            jsonWriter.name("serviceNumberId");
            this.serviceNumberIdAdapter.write(jsonWriter, merchantValuable.serviceNumberId());
            jsonWriter.name("serviceNumberIdFormat");
            this.serviceNumberIdFormatAdapter.write(jsonWriter, merchantValuable.serviceNumberIdFormat());
            if (merchantValuable.deviceId() != null) {
                jsonWriter.name("deviceId");
                this.deviceIdAdapter.write(jsonWriter, merchantValuable.deviceId());
            }
            if (merchantValuable.deviceIdFormat() != null) {
                jsonWriter.name("deviceIdFormat");
                this.deviceIdFormatAdapter.write(jsonWriter, merchantValuable.deviceIdFormat());
            }
            if (merchantValuable.issuerId() != null) {
                jsonWriter.name("issuerId");
                this.issuerIdAdapter.write(jsonWriter, merchantValuable.issuerId());
            }
            if (merchantValuable.issuer() != null) {
                jsonWriter.name("issuer");
                this.issuerAdapter.write(jsonWriter, merchantValuable.issuer());
            }
            if (merchantValuable.issuerFormat() != null) {
                jsonWriter.name("issuerFormat");
                this.issuerFormatAdapter.write(jsonWriter, merchantValuable.issuerFormat());
            }
            if (merchantValuable.tapId() != null) {
                jsonWriter.name("tapId");
                this.tapIdAdapter.write(jsonWriter, merchantValuable.tapId());
            }
            if (merchantValuable.tapIdFormat() != null) {
                jsonWriter.name("tapIdFormat");
                this.tapIdFormatAdapter.write(jsonWriter, merchantValuable.tapIdFormat());
            }
            if (merchantValuable.pin() != null) {
                jsonWriter.name("pin");
                this.pinAdapter.write(jsonWriter, merchantValuable.pin());
            }
            if (merchantValuable.pinFormat() != null) {
                jsonWriter.name("pinFormat");
                this.pinFormatAdapter.write(jsonWriter, merchantValuable.pinFormat());
            }
            if (merchantValuable.cvc() != null) {
                jsonWriter.name("cvc");
                this.cvcAdapter.write(jsonWriter, merchantValuable.cvc());
            }
            if (merchantValuable.cvcFormat() != null) {
                jsonWriter.name("cvcFormat");
                this.cvcFormatAdapter.write(jsonWriter, merchantValuable.cvcFormat());
            }
            if (merchantValuable.expiration() != null) {
                jsonWriter.name("expiration");
                this.expirationAdapter.write(jsonWriter, merchantValuable.expiration());
            }
            if (merchantValuable.expirationFormat() != null) {
                jsonWriter.name("expirationFormat");
                this.expirationFormatAdapter.write(jsonWriter, merchantValuable.expirationFormat());
            }
            if (merchantValuable.preferedLanguage() != null) {
                jsonWriter.name("preferedLanguage");
                this.preferedLanguageAdapter.write(jsonWriter, merchantValuable.preferedLanguage());
            }
            jsonWriter.endObject();
        }

        public MerchantValuable read(JsonReader jsonReader) throws IOException {
            jsonReader.beginObject();
            Type type = null;
            byte[] bArr = null;
            byte[] bArr2 = null;
            Format format = null;
            byte[] bArr3 = null;
            Format format2 = null;
            byte[] bArr4 = null;
            Issuer issuer = null;
            Format format3 = null;
            byte[] bArr5 = null;
            Format format4 = null;
            byte[] bArr6 = null;
            Format format5 = null;
            byte[] bArr7 = null;
            Format format6 = null;
            byte[] bArr8 = null;
            Format format7 = null;
            String str = null;
            while (jsonReader.hasNext()) {
                String nextName = jsonReader.nextName();
                if (jsonReader.peek() != JsonToken.NULL) {
                    Object obj = -1;
                    switch (nextName.hashCode()) {
                        case -1828739056:
                            if (nextName.equals("issuerFormat")) {
                                obj = 8;
                                break;
                            }
                            break;
                        case -1404091979:
                            if (nextName.equals("tapIdFormat")) {
                                obj = 10;
                                break;
                            }
                            break;
                        case -1336656536:
                            if (nextName.equals("deviceIdFormat")) {
                                obj = 5;
                                break;
                            }
                            break;
                        case -1308732948:
                            if (nextName.equals("pinFormat")) {
                                obj = 12;
                                break;
                            }
                            break;
                        case -1179159879:
                            if (nextName.equals("issuer")) {
                                obj = 7;
                                break;
                            }
                            break;
                        case -837465425:
                            if (nextName.equals("expiration")) {
                                obj = 15;
                                break;
                            }
                            break;
                        case -655762407:
                            if (nextName.equals("serviceNumberId")) {
                                obj = 2;
                                break;
                            }
                            break;
                        case -194185552:
                            if (nextName.equals("serviceId")) {
                                obj = 1;
                                break;
                            }
                            break;
                        case 98896:
                            if (nextName.equals("cvc")) {
                                obj = 13;
                                break;
                            }
                            break;
                        case 110997:
                            if (nextName.equals("pin")) {
                                obj = 11;
                                break;
                            }
                            break;
                        case 3575610:
                            if (nextName.equals("type")) {
                                obj = null;
                                break;
                            }
                            break;
                        case 110128158:
                            if (nextName.equals("tapId")) {
                                obj = 9;
                                break;
                            }
                            break;
                        case 512447271:
                            if (nextName.equals("preferedLanguage")) {
                                obj = 17;
                                break;
                            }
                            break;
                        case 698724788:
                            if (nextName.equals("issuerId")) {
                                obj = 6;
                                break;
                            }
                            break;
                        case 722430567:
                            if (nextName.equals("cvcFormat")) {
                                obj = 14;
                                break;
                            }
                            break;
                        case 1109191185:
                            if (nextName.equals("deviceId")) {
                                obj = 4;
                                break;
                            }
                            break;
                        case 1230342512:
                            if (nextName.equals("serviceNumberIdFormat")) {
                                obj = 3;
                                break;
                            }
                            break;
                        case 1587359622:
                            if (nextName.equals("expirationFormat")) {
                                obj = 16;
                                break;
                            }
                            break;
                    }
                    switch (obj) {
                        case null:
                            type = (Type) this.typeAdapter.read(jsonReader);
                            break;
                        case 1:
                            bArr = (byte[]) this.serviceIdAdapter.read(jsonReader);
                            break;
                        case 2:
                            bArr2 = (byte[]) this.serviceNumberIdAdapter.read(jsonReader);
                            break;
                        case 3:
                            format = (Format) this.serviceNumberIdFormatAdapter.read(jsonReader);
                            break;
                        case 4:
                            bArr3 = (byte[]) this.deviceIdAdapter.read(jsonReader);
                            break;
                        case 5:
                            format2 = (Format) this.deviceIdFormatAdapter.read(jsonReader);
                            break;
                        case 6:
                            bArr4 = (byte[]) this.issuerIdAdapter.read(jsonReader);
                            break;
                        case 7:
                            issuer = (Issuer) this.issuerAdapter.read(jsonReader);
                            break;
                        case 8:
                            format3 = (Format) this.issuerFormatAdapter.read(jsonReader);
                            break;
                        case 9:
                            bArr5 = (byte[]) this.tapIdAdapter.read(jsonReader);
                            break;
                        case 10:
                            format4 = (Format) this.tapIdFormatAdapter.read(jsonReader);
                            break;
                        case 11:
                            bArr6 = (byte[]) this.pinAdapter.read(jsonReader);
                            break;
                        case 12:
                            format5 = (Format) this.pinFormatAdapter.read(jsonReader);
                            break;
                        case 13:
                            bArr7 = (byte[]) this.cvcAdapter.read(jsonReader);
                            break;
                        case 14:
                            format6 = (Format) this.cvcFormatAdapter.read(jsonReader);
                            break;
                        case 15:
                            bArr8 = (byte[]) this.expirationAdapter.read(jsonReader);
                            break;
                        case 16:
                            format7 = (Format) this.expirationFormatAdapter.read(jsonReader);
                            break;
                        case 17:
                            str = (String) this.preferedLanguageAdapter.read(jsonReader);
                            break;
                        default:
                            jsonReader.skipValue();
                            break;
                    }
                }
                jsonReader.skipValue();
            }
            jsonReader.endObject();
            return new AutoValue_MerchantValuable(type, bArr, bArr2, format, bArr3, format2, bArr4, issuer, format3, bArr5, format4, bArr6, format5, bArr7, format6, bArr8, format7, str);
        }
    }

    AutoValue_MerchantValuable(Type type, byte[] bArr, byte[] bArr2, Format format, byte[] bArr3, Format format2, byte[] bArr4, Issuer issuer, Format format3, byte[] bArr5, Format format4, byte[] bArr6, Format format5, byte[] bArr7, Format format6, byte[] bArr8, Format format7, String str) {
        super(type, bArr, bArr2, format, bArr3, format2, bArr4, issuer, format3, bArr5, format4, bArr6, format5, bArr7, format6, bArr8, format7, str);
    }
}
