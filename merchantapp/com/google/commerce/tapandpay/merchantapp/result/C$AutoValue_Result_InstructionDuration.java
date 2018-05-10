package com.google.commerce.tapandpay.merchantapp.result;

import com.google.commerce.tapandpay.merchantapp.common.CommandType;
import com.google.commerce.tapandpay.merchantapp.result.Result.InstructionDuration;

abstract class C$AutoValue_Result_InstructionDuration extends InstructionDuration {
    private final long duration;
    private final CommandType type;

    C$AutoValue_Result_InstructionDuration(CommandType commandType, long j) {
        if (commandType == null) {
            throw new NullPointerException("Null type");
        }
        this.type = commandType;
        this.duration = j;
    }

    public CommandType type() {
        return this.type;
    }

    public long duration() {
        return this.duration;
    }

    public String toString() {
        String valueOf = String.valueOf(this.type);
        return new StringBuilder(String.valueOf(valueOf).length() + 57).append("InstructionDuration{type=").append(valueOf).append(", duration=").append(this.duration).append("}").toString();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof InstructionDuration)) {
            return false;
        }
        InstructionDuration instructionDuration = (InstructionDuration) obj;
        if (this.type.equals(instructionDuration.type()) && this.duration == instructionDuration.duration()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (int) (((long) ((this.type.hashCode() ^ 1000003) * 1000003)) ^ ((this.duration >>> 32) ^ this.duration));
    }
}
