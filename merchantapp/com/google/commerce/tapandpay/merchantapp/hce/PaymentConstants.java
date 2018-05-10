package com.google.commerce.tapandpay.merchantapp.hce;

import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.commerce.tapandpay.merchantapp.paypass.PayPassCredential;
import com.google.commerce.tapandpay.merchantapp.paypass.PayPassCrypto;
import java.security.GeneralSecurityException;

public class PaymentConstants {
    public static final byte[] SELECT_PPSE_RESPONSE = Hex.decode("6F23840E325041592E5359532E4444463031A511BF0C0E610C4F07A00000000410108701019000");

    private PaymentConstants() {
    }

    public static PayPassCredential getPayPassCredentials() {
        return PayPassCredential.newBuilder().fillWithDefaults().setTrack1Data(Hex.decode("42353431333132333435363738343830305E535550504C4945442F4E4F545E31393036313031333330303033333330303032323232323030303131313130")).setPcvc3Track1(Hex.decode("000000380000")).setPunatcTrack1(Hex.decode("00000000E0E0")).setNatcTrack1(Hex.decode("03")[0]).setTrack2Data(Hex.decode("5413123456784800D19061019000990000000F")).setPcvc3Track2(Hex.decode("000E")).setPunatcTrack2(Hex.decode("0E70")).setNatcTrack2(Hex.decode("03")[0]).setPayPassCrypto(getPaypassCrypto()).build();
    }

    private static PayPassCrypto getPaypassCrypto() {
        try {
            return PayPassCrypto.makeWithIvs(Hex.decode("A3D0BC420A766611F7BC19E7F2E221AF"), Hex.decode("7780"), Hex.decode("99D3"));
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException();
        }
    }
}
