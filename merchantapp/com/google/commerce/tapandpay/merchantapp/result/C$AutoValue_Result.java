package com.google.commerce.tapandpay.merchantapp.result;

import com.google.commerce.tapandpay.merchantapp.result.Result.Builder;
import com.google.commerce.tapandpay.merchantapp.result.Result.CommandAndResponse;
import com.google.commerce.tapandpay.merchantapp.result.Result.InstructionDuration;
import com.google.commerce.tapandpay.merchantapp.result.Result.Status;
import com.google.commerce.tapandpay.merchantapp.smarttap.v2.MerchantValuable;
import com.google.commerce.tapandpay.merchantapp.validation.ValidationResults;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

abstract class C$AutoValue_Result extends Result {
    private final ImmutableList<CommandAndResponse> commandAndResponses;
    private final ImmutableSet<MerchantValuable> encryptedValuables;
    private final long id;
    private final ImmutableList<InstructionDuration> instructionDurations;
    private final Status paymentStatus;
    private final Status smarttapStatus;
    private final long testCaseId;
    private final String timestamp;
    private final ValidationResults validationResults;

    C$AutoValue_Result(Status status, Status status2, ValidationResults validationResults, String str, ImmutableList<CommandAndResponse> immutableList, ImmutableList<InstructionDuration> immutableList2, ImmutableSet<MerchantValuable> immutableSet, long j, long j2) {
        if (status == null) {
            throw new NullPointerException("Null smarttapStatus");
        }
        this.smarttapStatus = status;
        if (status2 == null) {
            throw new NullPointerException("Null paymentStatus");
        }
        this.paymentStatus = status2;
        if (validationResults == null) {
            throw new NullPointerException("Null validationResults");
        }
        this.validationResults = validationResults;
        if (str == null) {
            throw new NullPointerException("Null timestamp");
        }
        this.timestamp = str;
        if (immutableList == null) {
            throw new NullPointerException("Null commandAndResponses");
        }
        this.commandAndResponses = immutableList;
        if (immutableList2 == null) {
            throw new NullPointerException("Null instructionDurations");
        }
        this.instructionDurations = immutableList2;
        this.encryptedValuables = immutableSet;
        this.testCaseId = j;
        this.id = j2;
    }

    public Status smarttapStatus() {
        return this.smarttapStatus;
    }

    public Status paymentStatus() {
        return this.paymentStatus;
    }

    public ValidationResults validationResults() {
        return this.validationResults;
    }

    public String timestamp() {
        return this.timestamp;
    }

    public ImmutableList<CommandAndResponse> commandAndResponses() {
        return this.commandAndResponses;
    }

    public ImmutableList<InstructionDuration> instructionDurations() {
        return this.instructionDurations;
    }

    public ImmutableSet<MerchantValuable> encryptedValuables() {
        return this.encryptedValuables;
    }

    public long testCaseId() {
        return this.testCaseId;
    }

    public long id() {
        return this.id;
    }

    public String toString() {
        String valueOf = String.valueOf(this.smarttapStatus);
        String valueOf2 = String.valueOf(this.paymentStatus);
        String valueOf3 = String.valueOf(this.validationResults);
        String str = this.timestamp;
        String valueOf4 = String.valueOf(this.commandAndResponses);
        String valueOf5 = String.valueOf(this.instructionDurations);
        String valueOf6 = String.valueOf(this.encryptedValuables);
        long j = this.testCaseId;
        return new StringBuilder(((((((String.valueOf(valueOf).length() + 195) + String.valueOf(valueOf2).length()) + String.valueOf(valueOf3).length()) + String.valueOf(str).length()) + String.valueOf(valueOf4).length()) + String.valueOf(valueOf5).length()) + String.valueOf(valueOf6).length()).append("Result{smarttapStatus=").append(valueOf).append(", paymentStatus=").append(valueOf2).append(", validationResults=").append(valueOf3).append(", timestamp=").append(str).append(", commandAndResponses=").append(valueOf4).append(", instructionDurations=").append(valueOf5).append(", encryptedValuables=").append(valueOf6).append(", testCaseId=").append(j).append(", id=").append(this.id).append("}").toString();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r7) {
        /*
        r6 = this;
        r0 = 1;
        r1 = 0;
        if (r7 != r6) goto L_0x0005;
    L_0x0004:
        return r0;
    L_0x0005:
        r2 = r7 instanceof com.google.commerce.tapandpay.merchantapp.result.Result;
        if (r2 == 0) goto L_0x0080;
    L_0x0009:
        r7 = (com.google.commerce.tapandpay.merchantapp.result.Result) r7;
        r2 = r6.smarttapStatus;
        r3 = r7.smarttapStatus();
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0071;
    L_0x0017:
        r2 = r6.paymentStatus;
        r3 = r7.paymentStatus();
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0071;
    L_0x0023:
        r2 = r6.validationResults;
        r3 = r7.validationResults();
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0071;
    L_0x002f:
        r2 = r6.timestamp;
        r3 = r7.timestamp();
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0071;
    L_0x003b:
        r2 = r6.commandAndResponses;
        r3 = r7.commandAndResponses();
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0071;
    L_0x0047:
        r2 = r6.instructionDurations;
        r3 = r7.instructionDurations();
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0071;
    L_0x0053:
        r2 = r6.encryptedValuables;
        if (r2 != 0) goto L_0x0073;
    L_0x0057:
        r2 = r7.encryptedValuables();
        if (r2 != 0) goto L_0x0071;
    L_0x005d:
        r2 = r6.testCaseId;
        r4 = r7.testCaseId();
        r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r2 != 0) goto L_0x0071;
    L_0x0067:
        r2 = r6.id;
        r4 = r7.id();
        r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r2 == 0) goto L_0x0004;
    L_0x0071:
        r0 = r1;
        goto L_0x0004;
    L_0x0073:
        r2 = r6.encryptedValuables;
        r3 = r7.encryptedValuables();
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0071;
    L_0x007f:
        goto L_0x005d;
    L_0x0080:
        r0 = r1;
        goto L_0x0004;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.commerce.tapandpay.merchantapp.result.$AutoValue_Result.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        return (int) (((long) (((int) (((long) (((this.encryptedValuables == null ? 0 : this.encryptedValuables.hashCode()) ^ ((((((((((((this.smarttapStatus.hashCode() ^ 1000003) * 1000003) ^ this.paymentStatus.hashCode()) * 1000003) ^ this.validationResults.hashCode()) * 1000003) ^ this.timestamp.hashCode()) * 1000003) ^ this.commandAndResponses.hashCode()) * 1000003) ^ this.instructionDurations.hashCode()) * 1000003)) * 1000003)) ^ ((this.testCaseId >>> 32) ^ this.testCaseId))) * 1000003)) ^ ((this.id >>> 32) ^ this.id));
    }

    public Builder toBuilder() {
        return new Builder();
    }
}
