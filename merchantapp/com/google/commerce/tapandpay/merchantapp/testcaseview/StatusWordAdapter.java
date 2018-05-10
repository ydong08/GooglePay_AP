package com.google.commerce.tapandpay.merchantapp.testcaseview;

import android.content.Context;
import com.google.commerce.tapandpay.merchantapp.testcase.TestCase.GetSmartTapDataResponse;
import java.util.Arrays;

public class StatusWordAdapter extends ObjectStringAdapter<GetSmartTapDataResponse> {
    public StatusWordAdapter(Context context) {
        super(context, Arrays.asList(GetSmartTapDataResponse.values()));
    }

    public String getTitle(GetSmartTapDataResponse getSmartTapDataResponse) {
        return getSmartTapDataResponse.getMessage(getContext());
    }
}
