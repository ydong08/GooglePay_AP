package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import com.google.android.libraries.commerce.hce.crypto.ValuablesCryptoException;

public interface GetData {
    SmartTapResponse getDataResponse(byte b) throws SmartTapV2Exception, ValuablesCryptoException;

    SmartTapResponse getMoreDataResponse(byte b) throws SmartTapV2Exception, ValuablesCryptoException;

    boolean hasMoreData();
}
