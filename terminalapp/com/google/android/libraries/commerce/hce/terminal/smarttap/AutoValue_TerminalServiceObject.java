package com.google.android.libraries.commerce.hce.terminal.smarttap;

import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject.Issuer;
import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject.Type;
import com.google.android.libraries.commerce.hce.ndef.Format;
import java.util.Arrays;

final class AutoValue_TerminalServiceObject extends TerminalServiceObject {
    private final byte[] cvc;
    private final Format cvcFormat;
    private final byte[] deviceId;
    private final Format deviceIdFormat;
    private final byte[] expiration;
    private final Format expirationFormat;
    private final Issuer issuer;
    private final Format issuerFormat;
    private final byte[] issuerId;
    private final byte[] pin;
    private final Format pinFormat;
    private final String preferedLanguage;
    private final byte[] serviceId;
    private final byte[] serviceNumberId;
    private final Format serviceNumberIdFormat;
    private final byte[] tapId;
    private final Format tapIdFormat;
    private final Type type;

    private AutoValue_TerminalServiceObject(Type type, byte[] bArr, byte[] bArr2, Format format, byte[] bArr3, Format format2, byte[] bArr4, Issuer issuer, Format format3, byte[] bArr5, Format format4, byte[] bArr6, Format format5, byte[] bArr7, Format format6, byte[] bArr8, Format format7, String str) {
        this.type = type;
        this.serviceId = bArr;
        this.serviceNumberId = bArr2;
        this.serviceNumberIdFormat = format;
        this.deviceId = bArr3;
        this.deviceIdFormat = format2;
        this.issuerId = bArr4;
        this.issuer = issuer;
        this.issuerFormat = format3;
        this.tapId = bArr5;
        this.tapIdFormat = format4;
        this.pin = bArr6;
        this.pinFormat = format5;
        this.cvc = bArr7;
        this.cvcFormat = format6;
        this.expiration = bArr8;
        this.expirationFormat = format7;
        this.preferedLanguage = str;
    }

    public Type type() {
        return this.type;
    }

    public byte[] serviceId() {
        return this.serviceId;
    }

    public byte[] serviceNumberId() {
        return this.serviceNumberId;
    }

    public Format serviceNumberIdFormat() {
        return this.serviceNumberIdFormat;
    }

    public byte[] deviceId() {
        return this.deviceId;
    }

    public Format deviceIdFormat() {
        return this.deviceIdFormat;
    }

    public byte[] issuerId() {
        return this.issuerId;
    }

    public Issuer issuer() {
        return this.issuer;
    }

    public Format issuerFormat() {
        return this.issuerFormat;
    }

    public byte[] tapId() {
        return this.tapId;
    }

    public Format tapIdFormat() {
        return this.tapIdFormat;
    }

    public byte[] pin() {
        return this.pin;
    }

    public Format pinFormat() {
        return this.pinFormat;
    }

    public byte[] cvc() {
        return this.cvc;
    }

    public Format cvcFormat() {
        return this.cvcFormat;
    }

    public byte[] expiration() {
        return this.expiration;
    }

    public Format expirationFormat() {
        return this.expirationFormat;
    }

    public String preferedLanguage() {
        return this.preferedLanguage;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r5) {
        /*
        r4 = this;
        r1 = 1;
        r2 = 0;
        if (r5 != r4) goto L_0x0005;
    L_0x0004:
        return r1;
    L_0x0005:
        r0 = r5 instanceof com.google.android.libraries.commerce.hce.terminal.smarttap.TerminalServiceObject;
        if (r0 == 0) goto L_0x019f;
    L_0x0009:
        r5 = (com.google.android.libraries.commerce.hce.terminal.smarttap.TerminalServiceObject) r5;
        r0 = r4.type;
        r3 = r5.type();
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x011d;
    L_0x0017:
        r3 = r4.serviceId;
        r0 = r5 instanceof com.google.android.libraries.commerce.hce.terminal.smarttap.AutoValue_TerminalServiceObject;
        if (r0 == 0) goto L_0x00ff;
    L_0x001d:
        r0 = r5;
        r0 = (com.google.android.libraries.commerce.hce.terminal.smarttap.AutoValue_TerminalServiceObject) r0;
        r0 = r0.serviceId;
    L_0x0022:
        r0 = java.util.Arrays.equals(r3, r0);
        if (r0 == 0) goto L_0x011d;
    L_0x0028:
        r3 = r4.serviceNumberId;
        r0 = r5 instanceof com.google.android.libraries.commerce.hce.terminal.smarttap.AutoValue_TerminalServiceObject;
        if (r0 == 0) goto L_0x0105;
    L_0x002e:
        r0 = r5;
        r0 = (com.google.android.libraries.commerce.hce.terminal.smarttap.AutoValue_TerminalServiceObject) r0;
        r0 = r0.serviceNumberId;
    L_0x0033:
        r0 = java.util.Arrays.equals(r3, r0);
        if (r0 == 0) goto L_0x011d;
    L_0x0039:
        r0 = r4.serviceNumberIdFormat;
        r3 = r5.serviceNumberIdFormat();
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x011d;
    L_0x0045:
        r3 = r4.deviceId;
        r0 = r5 instanceof com.google.android.libraries.commerce.hce.terminal.smarttap.AutoValue_TerminalServiceObject;
        if (r0 == 0) goto L_0x010b;
    L_0x004b:
        r0 = r5;
        r0 = (com.google.android.libraries.commerce.hce.terminal.smarttap.AutoValue_TerminalServiceObject) r0;
        r0 = r0.deviceId;
    L_0x0050:
        r0 = java.util.Arrays.equals(r3, r0);
        if (r0 == 0) goto L_0x011d;
    L_0x0056:
        r0 = r4.deviceIdFormat;
        if (r0 != 0) goto L_0x0111;
    L_0x005a:
        r0 = r5.deviceIdFormat();
        if (r0 != 0) goto L_0x011d;
    L_0x0060:
        r3 = r4.issuerId;
        r0 = r5 instanceof com.google.android.libraries.commerce.hce.terminal.smarttap.AutoValue_TerminalServiceObject;
        if (r0 == 0) goto L_0x011f;
    L_0x0066:
        r0 = r5;
        r0 = (com.google.android.libraries.commerce.hce.terminal.smarttap.AutoValue_TerminalServiceObject) r0;
        r0 = r0.issuerId;
    L_0x006b:
        r0 = java.util.Arrays.equals(r3, r0);
        if (r0 == 0) goto L_0x011d;
    L_0x0071:
        r0 = r4.issuer;
        if (r0 != 0) goto L_0x0125;
    L_0x0075:
        r0 = r5.issuer();
        if (r0 != 0) goto L_0x011d;
    L_0x007b:
        r0 = r4.issuerFormat;
        if (r0 != 0) goto L_0x0133;
    L_0x007f:
        r0 = r5.issuerFormat();
        if (r0 != 0) goto L_0x011d;
    L_0x0085:
        r3 = r4.tapId;
        r0 = r5 instanceof com.google.android.libraries.commerce.hce.terminal.smarttap.AutoValue_TerminalServiceObject;
        if (r0 == 0) goto L_0x0141;
    L_0x008b:
        r0 = r5;
        r0 = (com.google.android.libraries.commerce.hce.terminal.smarttap.AutoValue_TerminalServiceObject) r0;
        r0 = r0.tapId;
    L_0x0090:
        r0 = java.util.Arrays.equals(r3, r0);
        if (r0 == 0) goto L_0x011d;
    L_0x0096:
        r0 = r4.tapIdFormat;
        if (r0 != 0) goto L_0x0147;
    L_0x009a:
        r0 = r5.tapIdFormat();
        if (r0 != 0) goto L_0x011d;
    L_0x00a0:
        r3 = r4.pin;
        r0 = r5 instanceof com.google.android.libraries.commerce.hce.terminal.smarttap.AutoValue_TerminalServiceObject;
        if (r0 == 0) goto L_0x0155;
    L_0x00a6:
        r0 = r5;
        r0 = (com.google.android.libraries.commerce.hce.terminal.smarttap.AutoValue_TerminalServiceObject) r0;
        r0 = r0.pin;
    L_0x00ab:
        r0 = java.util.Arrays.equals(r3, r0);
        if (r0 == 0) goto L_0x011d;
    L_0x00b1:
        r0 = r4.pinFormat;
        if (r0 != 0) goto L_0x015b;
    L_0x00b5:
        r0 = r5.pinFormat();
        if (r0 != 0) goto L_0x011d;
    L_0x00bb:
        r3 = r4.cvc;
        r0 = r5 instanceof com.google.android.libraries.commerce.hce.terminal.smarttap.AutoValue_TerminalServiceObject;
        if (r0 == 0) goto L_0x0169;
    L_0x00c1:
        r0 = r5;
        r0 = (com.google.android.libraries.commerce.hce.terminal.smarttap.AutoValue_TerminalServiceObject) r0;
        r0 = r0.cvc;
    L_0x00c6:
        r0 = java.util.Arrays.equals(r3, r0);
        if (r0 == 0) goto L_0x011d;
    L_0x00cc:
        r0 = r4.cvcFormat;
        if (r0 != 0) goto L_0x016f;
    L_0x00d0:
        r0 = r5.cvcFormat();
        if (r0 != 0) goto L_0x011d;
    L_0x00d6:
        r3 = r4.expiration;
        r0 = r5 instanceof com.google.android.libraries.commerce.hce.terminal.smarttap.AutoValue_TerminalServiceObject;
        if (r0 == 0) goto L_0x017d;
    L_0x00dc:
        r0 = r5;
        r0 = (com.google.android.libraries.commerce.hce.terminal.smarttap.AutoValue_TerminalServiceObject) r0;
        r0 = r0.expiration;
    L_0x00e1:
        r0 = java.util.Arrays.equals(r3, r0);
        if (r0 == 0) goto L_0x011d;
    L_0x00e7:
        r0 = r4.expirationFormat;
        if (r0 != 0) goto L_0x0183;
    L_0x00eb:
        r0 = r5.expirationFormat();
        if (r0 != 0) goto L_0x011d;
    L_0x00f1:
        r0 = r4.preferedLanguage;
        if (r0 != 0) goto L_0x0191;
    L_0x00f5:
        r0 = r5.preferedLanguage();
        if (r0 != 0) goto L_0x011d;
    L_0x00fb:
        r0 = r1;
    L_0x00fc:
        r1 = r0;
        goto L_0x0004;
    L_0x00ff:
        r0 = r5.serviceId();
        goto L_0x0022;
    L_0x0105:
        r0 = r5.serviceNumberId();
        goto L_0x0033;
    L_0x010b:
        r0 = r5.deviceId();
        goto L_0x0050;
    L_0x0111:
        r0 = r4.deviceIdFormat;
        r3 = r5.deviceIdFormat();
        r0 = r0.equals(r3);
        if (r0 != 0) goto L_0x0060;
    L_0x011d:
        r0 = r2;
        goto L_0x00fc;
    L_0x011f:
        r0 = r5.issuerId();
        goto L_0x006b;
    L_0x0125:
        r0 = r4.issuer;
        r3 = r5.issuer();
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x011d;
    L_0x0131:
        goto L_0x007b;
    L_0x0133:
        r0 = r4.issuerFormat;
        r3 = r5.issuerFormat();
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x011d;
    L_0x013f:
        goto L_0x0085;
    L_0x0141:
        r0 = r5.tapId();
        goto L_0x0090;
    L_0x0147:
        r0 = r4.tapIdFormat;
        r3 = r5.tapIdFormat();
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x011d;
    L_0x0153:
        goto L_0x00a0;
    L_0x0155:
        r0 = r5.pin();
        goto L_0x00ab;
    L_0x015b:
        r0 = r4.pinFormat;
        r3 = r5.pinFormat();
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x011d;
    L_0x0167:
        goto L_0x00bb;
    L_0x0169:
        r0 = r5.cvc();
        goto L_0x00c6;
    L_0x016f:
        r0 = r4.cvcFormat;
        r3 = r5.cvcFormat();
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x011d;
    L_0x017b:
        goto L_0x00d6;
    L_0x017d:
        r0 = r5.expiration();
        goto L_0x00e1;
    L_0x0183:
        r0 = r4.expirationFormat;
        r3 = r5.expirationFormat();
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x011d;
    L_0x018f:
        goto L_0x00f1;
    L_0x0191:
        r0 = r4.preferedLanguage;
        r3 = r5.preferedLanguage();
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x011d;
    L_0x019d:
        goto L_0x00fb;
    L_0x019f:
        r1 = r2;
        goto L_0x0004;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.libraries.commerce.hce.terminal.smarttap.AutoValue_TerminalServiceObject.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((this.expirationFormat == null ? 0 : this.expirationFormat.hashCode()) ^ (((((this.cvcFormat == null ? 0 : this.cvcFormat.hashCode()) ^ (((((this.pinFormat == null ? 0 : this.pinFormat.hashCode()) ^ (((((this.tapIdFormat == null ? 0 : this.tapIdFormat.hashCode()) ^ (((((this.issuerFormat == null ? 0 : this.issuerFormat.hashCode()) ^ (((this.issuer == null ? 0 : this.issuer.hashCode()) ^ (((((this.deviceIdFormat == null ? 0 : this.deviceIdFormat.hashCode()) ^ ((((((((((this.type.hashCode() ^ 1000003) * 1000003) ^ Arrays.hashCode(this.serviceId)) * 1000003) ^ Arrays.hashCode(this.serviceNumberId)) * 1000003) ^ this.serviceNumberIdFormat.hashCode()) * 1000003) ^ Arrays.hashCode(this.deviceId)) * 1000003)) * 1000003) ^ Arrays.hashCode(this.issuerId)) * 1000003)) * 1000003)) * 1000003) ^ Arrays.hashCode(this.tapId)) * 1000003)) * 1000003) ^ Arrays.hashCode(this.pin)) * 1000003)) * 1000003) ^ Arrays.hashCode(this.cvc)) * 1000003)) * 1000003) ^ Arrays.hashCode(this.expiration)) * 1000003)) * 1000003;
        if (this.preferedLanguage != null) {
            i = this.preferedLanguage.hashCode();
        }
        return hashCode ^ i;
    }
}
