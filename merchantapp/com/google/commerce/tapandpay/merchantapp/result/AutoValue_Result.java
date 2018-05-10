package com.google.commerce.tapandpay.merchantapp.result;

import com.google.commerce.tapandpay.merchantapp.result.Result.CommandAndResponse;
import com.google.commerce.tapandpay.merchantapp.result.Result.InstructionDuration;
import com.google.commerce.tapandpay.merchantapp.result.Result.Status;
import com.google.commerce.tapandpay.merchantapp.smarttap.v2.MerchantValuable;
import com.google.commerce.tapandpay.merchantapp.validation.ValidationResults;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

final class AutoValue_Result extends C$AutoValue_Result {
    AutoValue_Result(Status status, Status status2, ValidationResults validationResults, String str, ImmutableList<CommandAndResponse> immutableList, ImmutableList<InstructionDuration> immutableList2, ImmutableSet<MerchantValuable> immutableSet, long j, long j2) {
        super(status, status2, validationResults, str, immutableList, immutableList2, immutableSet, j, j2);
    }
}
