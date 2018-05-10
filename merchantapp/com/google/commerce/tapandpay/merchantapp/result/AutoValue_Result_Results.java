package com.google.commerce.tapandpay.merchantapp.result;

import com.google.commerce.tapandpay.merchantapp.result.Result.Results;
import com.google.common.collect.ImmutableList;

final class AutoValue_Result_Results extends Results {
    private final Result expected;
    private final ImmutableList<Result> userResults;

    AutoValue_Result_Results(ImmutableList<Result> immutableList, Result result) {
        this.userResults = immutableList;
        this.expected = result;
    }

    public ImmutableList<Result> userResults() {
        return this.userResults;
    }

    public Result expected() {
        return this.expected;
    }

    public String toString() {
        String valueOf = String.valueOf(this.userResults);
        String valueOf2 = String.valueOf(this.expected);
        return new StringBuilder((String.valueOf(valueOf).length() + 32) + String.valueOf(valueOf2).length()).append("Results{userResults=").append(valueOf).append(", expected=").append(valueOf2).append("}").toString();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r5) {
        /*
        r4 = this;
        r0 = 1;
        r1 = 0;
        if (r5 != r4) goto L_0x0005;
    L_0x0004:
        return r0;
    L_0x0005:
        r2 = r5 instanceof com.google.commerce.tapandpay.merchantapp.result.Result.Results;
        if (r2 == 0) goto L_0x003b;
    L_0x0009:
        r5 = (com.google.commerce.tapandpay.merchantapp.result.Result.Results) r5;
        r2 = r4.userResults;
        if (r2 != 0) goto L_0x0021;
    L_0x000f:
        r2 = r5.userResults();
        if (r2 != 0) goto L_0x001f;
    L_0x0015:
        r2 = r4.expected;
        if (r2 != 0) goto L_0x002e;
    L_0x0019:
        r2 = r5.expected();
        if (r2 == 0) goto L_0x0004;
    L_0x001f:
        r0 = r1;
        goto L_0x0004;
    L_0x0021:
        r2 = r4.userResults;
        r3 = r5.userResults();
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x001f;
    L_0x002d:
        goto L_0x0015;
    L_0x002e:
        r2 = r4.expected;
        r3 = r5.expected();
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x001f;
    L_0x003a:
        goto L_0x0004;
    L_0x003b:
        r0 = r1;
        goto L_0x0004;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.commerce.tapandpay.merchantapp.result.AutoValue_Result_Results.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((this.userResults == null ? 0 : this.userResults.hashCode()) ^ 1000003) * 1000003;
        if (this.expected != null) {
            i = this.expected.hashCode();
        }
        return hashCode ^ i;
    }
}
