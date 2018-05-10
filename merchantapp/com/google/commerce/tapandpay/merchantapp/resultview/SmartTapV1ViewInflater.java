package com.google.commerce.tapandpay.merchantapp.resultview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlv;
import com.google.commerce.tapandpay.merchantapp.common.ByteHelper;
import com.google.commerce.tapandpay.merchantapp.common.ByteHelper.ByteHelperException;
import com.google.commerce.tapandpay.merchantapp.common.CommandType;
import com.google.commerce.tapandpay.merchantapp.result.Result;
import com.google.commerce.tapandpay.merchantapp.result.Result.CommandAndResponse;
import java.util.List;

public class SmartTapV1ViewInflater extends SmartTapViewInflater {
    public SmartTapV1ViewInflater(Context context, Result result) {
        super(context, result);
    }

    public void renderCommandAndResponse(CommandAndResponse commandAndResponse, ViewGroup viewGroup) {
        addHexView(commandAndResponse.type().commandTitle(getContext()), commandAndResponse.command(), viewGroup);
        if (commandAndResponse.type() == CommandType.GET_SMARTTAP_DATA) {
            try {
                addTlvTable(ByteHelper.getCommandTlvs(commandAndResponse.command()), viewGroup);
            } catch (ByteHelperException e) {
                addTlvError(e, viewGroup);
            }
        }
        addHexView(commandAndResponse.type().responseTitle(getContext(), commandAndResponse.isResponseError()), commandAndResponse.response(), viewGroup);
        if (!commandAndResponse.isResponseError()) {
            if (commandAndResponse.type() == CommandType.GET_SMARTTAP_DATA || commandAndResponse.type() == CommandType.GET_ADDITIONAL_SMARTTAP_DATA) {
                try {
                    addTlvTable(ByteHelper.getResponseTlvs(commandAndResponse.response()), viewGroup);
                } catch (ByteHelperException e2) {
                    addTlvError(e2, viewGroup);
                }
            }
        }
    }

    private void addTlvTable(List<BasicTlv> list, ViewGroup viewGroup) {
        View inflate = inflate(R.layout.result_tlv_table, viewGroup);
        TableLayout tableLayout = (TableLayout) inflate.findViewById(R.id.table);
        for (BasicTlv basicTlv : list) {
            View inflate2 = inflate(R.layout.result_tlv_row, tableLayout);
            setText(inflate2, R.id.tag, basicTlv.getTagAsString());
            setText(inflate2, R.id.length, String.valueOf(basicTlv.getLength()));
            setText(inflate2, R.id.value, ByteHelper.getTlvStringValue(getContext(), basicTlv));
            tableLayout.addView(inflate2);
        }
        viewGroup.addView(inflate);
    }

    private void addTlvError(ByteHelperException byteHelperException, ViewGroup viewGroup) {
        View inflate = inflate(R.layout.error_text_view, viewGroup);
        setText(inflate, R.id.title, R.string.tlvs);
        setText(inflate, R.id.error_summary, R.string.tlv_error);
        setText(inflate, R.id.error_message, byteHelperException.getMessage());
        viewGroup.addView(inflate);
    }
}
