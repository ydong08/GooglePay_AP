package com.google.android.libraries.commerce.hce.terminal.smarttap;

import android.nfc.NdefRecord;
import com.google.common.collect.ImmutableList;
import java.util.Arrays;

final class AutoValue_Version2_CommandParameters extends CommandParameters {
    private final byte[] ndefType;
    private final byte[] prefix;
    private final ImmutableList<NdefRecord> recordsWithoutSession;
    private final byte[] sessionId;
    private final short version;

    private AutoValue_Version2_CommandParameters(byte[] bArr, byte[] bArr2, ImmutableList<NdefRecord> immutableList, byte[] bArr3, short s) {
        this.ndefType = bArr;
        this.prefix = bArr2;
        this.recordsWithoutSession = immutableList;
        this.sessionId = bArr3;
        this.version = s;
    }

    byte[] ndefType() {
        return this.ndefType;
    }

    byte[] prefix() {
        return this.prefix;
    }

    ImmutableList<NdefRecord> recordsWithoutSession() {
        return this.recordsWithoutSession;
    }

    byte[] sessionId() {
        return this.sessionId;
    }

    short version() {
        return this.version;
    }

    public String toString() {
        String valueOf = String.valueOf(Arrays.toString(this.ndefType));
        String valueOf2 = String.valueOf(Arrays.toString(this.prefix));
        String valueOf3 = String.valueOf(this.recordsWithoutSession);
        String valueOf4 = String.valueOf(Arrays.toString(this.sessionId));
        return new StringBuilder((((String.valueOf(valueOf).length() + 89) + String.valueOf(valueOf2).length()) + String.valueOf(valueOf3).length()) + String.valueOf(valueOf4).length()).append("CommandParameters{ndefType=").append(valueOf).append(", prefix=").append(valueOf2).append(", recordsWithoutSession=").append(valueOf3).append(", sessionId=").append(valueOf4).append(", version=").append(this.version).append("}").toString();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CommandParameters)) {
            return false;
        }
        boolean z;
        CommandParameters commandParameters = (CommandParameters) obj;
        if (Arrays.equals(this.ndefType, commandParameters instanceof AutoValue_Version2_CommandParameters ? ((AutoValue_Version2_CommandParameters) commandParameters).ndefType : commandParameters.ndefType())) {
            if (Arrays.equals(this.prefix, commandParameters instanceof AutoValue_Version2_CommandParameters ? ((AutoValue_Version2_CommandParameters) commandParameters).prefix : commandParameters.prefix()) && this.recordsWithoutSession.equals(commandParameters.recordsWithoutSession())) {
                if (Arrays.equals(this.sessionId, commandParameters instanceof AutoValue_Version2_CommandParameters ? ((AutoValue_Version2_CommandParameters) commandParameters).sessionId : commandParameters.sessionId()) && this.version == commandParameters.version()) {
                    z = true;
                    return z;
                }
            }
        }
        z = false;
        return z;
    }

    public int hashCode() {
        return ((((((((Arrays.hashCode(this.ndefType) ^ 1000003) * 1000003) ^ Arrays.hashCode(this.prefix)) * 1000003) ^ this.recordsWithoutSession.hashCode()) * 1000003) ^ Arrays.hashCode(this.sessionId)) * 1000003) ^ this.version;
    }
}
