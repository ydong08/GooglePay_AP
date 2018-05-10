package com.google.android.libraries.commerce.hce.terminal.smarttap;

import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage;
import java.util.Arrays;

final class AutoValue_Version2_CommandResponse extends CommandResponse {
    private final byte[] finalCommand;
    private final byte nextSequenceNumber;
    private final NfcMessage nfcResponse;

    AutoValue_Version2_CommandResponse(byte[] bArr, NfcMessage nfcMessage, byte b) {
        if (bArr == null) {
            throw new NullPointerException("Null finalCommand");
        }
        this.finalCommand = bArr;
        if (nfcMessage == null) {
            throw new NullPointerException("Null nfcResponse");
        }
        this.nfcResponse = nfcMessage;
        this.nextSequenceNumber = b;
    }

    byte[] finalCommand() {
        return this.finalCommand;
    }

    NfcMessage nfcResponse() {
        return this.nfcResponse;
    }

    byte nextSequenceNumber() {
        return this.nextSequenceNumber;
    }

    public String toString() {
        String valueOf = String.valueOf(Arrays.toString(this.finalCommand));
        String valueOf2 = String.valueOf(this.nfcResponse);
        return new StringBuilder((String.valueOf(valueOf).length() + 69) + String.valueOf(valueOf2).length()).append("CommandResponse{finalCommand=").append(valueOf).append(", nfcResponse=").append(valueOf2).append(", nextSequenceNumber=").append(this.nextSequenceNumber).append("}").toString();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CommandResponse)) {
            return false;
        }
        boolean z;
        CommandResponse commandResponse = (CommandResponse) obj;
        if (Arrays.equals(this.finalCommand, commandResponse instanceof AutoValue_Version2_CommandResponse ? ((AutoValue_Version2_CommandResponse) commandResponse).finalCommand : commandResponse.finalCommand()) && this.nfcResponse.equals(commandResponse.nfcResponse()) && this.nextSequenceNumber == commandResponse.nextSequenceNumber()) {
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return ((((Arrays.hashCode(this.finalCommand) ^ 1000003) * 1000003) ^ this.nfcResponse.hashCode()) * 1000003) ^ this.nextSequenceNumber;
    }
}
