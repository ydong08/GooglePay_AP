package com.google.commerce.tapandpay.merchantapp.result;

import com.google.commerce.tapandpay.merchantapp.common.CommandType;
import com.google.commerce.tapandpay.merchantapp.result.Result.CommandAndResponse;
import java.util.Arrays;

abstract class C$AutoValue_Result_CommandAndResponse extends CommandAndResponse {
    private final byte[] command;
    private final boolean isResponseError;
    private final byte[] response;
    private final CommandType type;

    C$AutoValue_Result_CommandAndResponse(byte[] bArr, byte[] bArr2, CommandType commandType, boolean z) {
        if (bArr == null) {
            throw new NullPointerException("Null command");
        }
        this.command = bArr;
        if (bArr2 == null) {
            throw new NullPointerException("Null response");
        }
        this.response = bArr2;
        if (commandType == null) {
            throw new NullPointerException("Null type");
        }
        this.type = commandType;
        this.isResponseError = z;
    }

    public byte[] command() {
        return this.command;
    }

    public byte[] response() {
        return this.response;
    }

    public CommandType type() {
        return this.type;
    }

    public boolean isResponseError() {
        return this.isResponseError;
    }

    public String toString() {
        String valueOf = String.valueOf(Arrays.toString(this.command));
        String valueOf2 = String.valueOf(Arrays.toString(this.response));
        String valueOf3 = String.valueOf(this.type);
        return new StringBuilder(((String.valueOf(valueOf).length() + 69) + String.valueOf(valueOf2).length()) + String.valueOf(valueOf3).length()).append("CommandAndResponse{command=").append(valueOf).append(", response=").append(valueOf2).append(", type=").append(valueOf3).append(", isResponseError=").append(this.isResponseError).append("}").toString();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CommandAndResponse)) {
            return false;
        }
        boolean z;
        CommandAndResponse commandAndResponse = (CommandAndResponse) obj;
        if (Arrays.equals(this.command, commandAndResponse instanceof C$AutoValue_Result_CommandAndResponse ? ((C$AutoValue_Result_CommandAndResponse) commandAndResponse).command : commandAndResponse.command())) {
            if (Arrays.equals(this.response, commandAndResponse instanceof C$AutoValue_Result_CommandAndResponse ? ((C$AutoValue_Result_CommandAndResponse) commandAndResponse).response : commandAndResponse.response()) && this.type.equals(commandAndResponse.type()) && this.isResponseError == commandAndResponse.isResponseError()) {
                z = true;
                return z;
            }
        }
        z = false;
        return z;
    }

    public int hashCode() {
        return (this.isResponseError ? 1231 : 1237) ^ ((((((Arrays.hashCode(this.command) ^ 1000003) * 1000003) ^ Arrays.hashCode(this.response)) * 1000003) ^ this.type.hashCode()) * 1000003);
    }
}
