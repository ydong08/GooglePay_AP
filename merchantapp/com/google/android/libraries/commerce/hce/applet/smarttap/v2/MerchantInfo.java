package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import com.google.android.libraries.commerce.hce.applet.smarttap.v2.ServiceObject.Request;
import com.google.android.libraries.commerce.hce.ndef.Primitive;
import com.google.android.libraries.commerce.hce.ndef.Text;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.util.Set;

public class MerchantInfo {
    private MerchantCategory category;
    private Optional<Primitive> locationIdOptional;
    private long merchantId;
    private Optional<Text> nameOptional;
    private Set<Request> requestedServiceObjects;
    private Optional<Primitive> terminalIdOptional;

    public MerchantInfo(long j, Primitive primitive, Primitive primitive2, Text text, MerchantCategory merchantCategory, Set<Request> set) {
        this.merchantId = j;
        this.locationIdOptional = Optional.fromNullable(primitive);
        this.terminalIdOptional = Optional.fromNullable(primitive2);
        this.nameOptional = Optional.fromNullable(text);
        this.category = merchantCategory;
        this.requestedServiceObjects = (Set) Preconditions.checkNotNull(set);
    }

    public long getMerchantId() {
        return this.merchantId;
    }

    public Optional<Primitive> getLocationId() {
        return this.locationIdOptional;
    }

    public Optional<Primitive> getTerminalId() {
        return this.terminalIdOptional;
    }

    public Optional<Text> getName() {
        return this.nameOptional;
    }

    public MerchantCategory getCategory() {
        return this.category;
    }

    public Set<Request> getRequestedServiceObjects() {
        return this.requestedServiceObjects;
    }
}
