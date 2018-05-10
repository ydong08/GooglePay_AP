package com.google.android.libraries.commerce.hce.applet.smarttap.ose;

import com.google.android.libraries.commerce.hce.basictlv.BasicConstructedTlv;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlv;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlvException;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.common.base.Optional;

public abstract class SmartTap2ProprietaryData {
    public abstract Optional<Short> maxVersion();

    public abstract Optional<Short> minVersion();

    public abstract Optional<ByteArrayWrapper> mobileDeviceNonce();

    public abstract boolean supportsSkippingSelect();

    public static SmartTap2ProprietaryData create(Optional<Short> optional, Optional<Short> optional2, Optional<ByteArrayWrapper> optional3, boolean z) {
        return new AutoValue_SmartTap2ProprietaryData(optional, optional2, optional3, z);
    }

    static SmartTap2ProprietaryData parse(BasicTlv basicTlv) throws BasicTlvException {
        BasicConstructedTlv asConstructedTlv = basicTlv.asConstructedTlv();
        Optional absent = Optional.absent();
        Optional absent2 = Optional.absent();
        Optional absent3 = Optional.absent();
        Optional optional = absent2;
        Optional optional2 = absent;
        boolean z = false;
        absent = absent3;
        for (BasicTlv basicTlv2 : asConstructedTlv.getChildren()) {
            boolean z2;
            switch (basicTlv2.getTag()) {
                case 57165:
                    optional = Optional.of(Short.valueOf((short) basicTlv2.asPrimitiveTlv().getShort()));
                    z2 = z;
                    absent2 = absent;
                    absent = optional;
                    optional = optional2;
                    break;
                case 57186:
                    z2 = (((byte) asConstructedTlv.getChild(57186).asPrimitiveTlv().getByte()) & 1) != 0;
                    absent2 = absent;
                    absent = optional;
                    optional = optional2;
                    break;
                case 57197:
                    optional2 = Optional.of(Short.valueOf((short) basicTlv2.asPrimitiveTlv().getShort()));
                    z2 = z;
                    absent2 = absent;
                    absent = optional;
                    optional = optional2;
                    break;
                case 57198:
                    z2 = z;
                    absent2 = Optional.of(new ByteArrayWrapper(basicTlv2.asPrimitiveTlv().getValue()));
                    absent = optional;
                    optional = optional2;
                    break;
                default:
                    z2 = z;
                    absent2 = absent;
                    absent = optional;
                    optional = optional2;
                    break;
            }
            optional2 = optional;
            optional = absent;
            absent = absent2;
            z = z2;
        }
        return create(optional2, optional, absent, z);
    }
}
