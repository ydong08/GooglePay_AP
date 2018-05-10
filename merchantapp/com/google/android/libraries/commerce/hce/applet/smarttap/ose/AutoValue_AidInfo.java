package com.google.android.libraries.commerce.hce.applet.smarttap.ose;

import com.google.android.libraries.commerce.hce.iso7816.Aid;
import com.google.common.base.Optional;

final class AutoValue_AidInfo extends AidInfo {
    private final Aid aid;
    private final int priority;
    private final Optional<SmartTap2ProprietaryData> proprietaryDataOptional;

    AutoValue_AidInfo(Aid aid, int i, Optional<SmartTap2ProprietaryData> optional) {
        if (aid == null) {
            throw new NullPointerException("Null aid");
        }
        this.aid = aid;
        this.priority = i;
        if (optional == null) {
            throw new NullPointerException("Null proprietaryDataOptional");
        }
        this.proprietaryDataOptional = optional;
    }

    public Aid aid() {
        return this.aid;
    }

    public int priority() {
        return this.priority;
    }

    public Optional<SmartTap2ProprietaryData> proprietaryDataOptional() {
        return this.proprietaryDataOptional;
    }

    public String toString() {
        String valueOf = String.valueOf(this.aid);
        int i = this.priority;
        String valueOf2 = String.valueOf(this.proprietaryDataOptional);
        return new StringBuilder((String.valueOf(valueOf).length() + 61) + String.valueOf(valueOf2).length()).append("AidInfo{aid=").append(valueOf).append(", priority=").append(i).append(", proprietaryDataOptional=").append(valueOf2).append("}").toString();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AidInfo)) {
            return false;
        }
        AidInfo aidInfo = (AidInfo) obj;
        if (this.aid.equals(aidInfo.aid()) && this.priority == aidInfo.priority() && this.proprietaryDataOptional.equals(aidInfo.proprietaryDataOptional())) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return ((((this.aid.hashCode() ^ 1000003) * 1000003) ^ this.priority) * 1000003) ^ this.proprietaryDataOptional.hashCode();
    }
}
