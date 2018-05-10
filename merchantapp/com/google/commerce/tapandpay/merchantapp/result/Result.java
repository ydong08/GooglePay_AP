package com.google.commerce.tapandpay.merchantapp.result;

import android.content.Context;
import com.google.commerce.tapandpay.merchantapp.common.ByteHelper;
import com.google.commerce.tapandpay.merchantapp.common.ByteHelper.ByteHelperException;
import com.google.commerce.tapandpay.merchantapp.common.CommandType;
import com.google.commerce.tapandpay.merchantapp.smarttap.v2.MerchantValuable;
import com.google.commerce.tapandpay.merchantapp.validation.ValidationResults;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class Result implements Serializable {

    public static abstract class CommandAndResponse implements Serializable {
        public abstract byte[] command();

        public abstract boolean isResponseError();

        public abstract byte[] response();

        public abstract CommandType type();

        public static CommandAndResponse create(byte[] bArr, byte[] bArr2, CommandType commandType, boolean z) {
            return new AutoValue_Result_CommandAndResponse(bArr, bArr2, commandType, z);
        }
    }

    public static abstract class InstructionDuration implements Serializable {
        public abstract long duration();

        public abstract CommandType type();

        public static InstructionDuration create(CommandType commandType, long j) {
            return new AutoValue_Result_InstructionDuration(commandType, j);
        }
    }

    public static abstract class Results implements Serializable {
        public abstract Result expected();

        public abstract ImmutableList<Result> userResults();

        public static Results create(List<Result> list, Result result) {
            return new AutoValue_Result_Results(ImmutableList.copyOf((Collection) list), result);
        }
    }

    public static abstract class Builder {
        private ImmutableList<CommandAndResponse> commandAndResponses;
        private ImmutableSet<MerchantValuable> encryptedValuables;
        private Long id;
        private ImmutableList<InstructionDuration> instructionDurations;
        private Status paymentStatus;
        private Status smarttapStatus;
        private Long testCaseId;
        private String timestamp;
        private ValidationResults validationResults;

        Builder(byte b) {
            this();
        }

        public Builder setSmarttapStatus(Status status) {
            this.smarttapStatus = status;
            return this;
        }

        public Builder setPaymentStatus(Status status) {
            this.paymentStatus = status;
            return this;
        }

        public Builder setValidationResults(ValidationResults validationResults) {
            this.validationResults = validationResults;
            return this;
        }

        public Builder setTimestamp(String str) {
            this.timestamp = str;
            return this;
        }

        public Builder setCommandAndResponses(List<CommandAndResponse> list) {
            this.commandAndResponses = ImmutableList.copyOf((Collection) list);
            return this;
        }

        public Builder setInstructionDurations(List<InstructionDuration> list) {
            this.instructionDurations = ImmutableList.copyOf((Collection) list);
            return this;
        }

        public Builder setEncryptedValuables(Set<MerchantValuable> set) {
            this.encryptedValuables = set == null ? null : ImmutableSet.copyOf((Collection) set);
            return this;
        }

        public Builder setTestCaseId(long j) {
            this.testCaseId = Long.valueOf(j);
            return this;
        }

        public Builder setId(long j) {
            this.id = Long.valueOf(j);
            return this;
        }

        public Result build() {
            String str = "";
            if (this.smarttapStatus == null) {
                str = String.valueOf(str).concat(" smarttapStatus");
            }
            if (this.paymentStatus == null) {
                str = String.valueOf(str).concat(" paymentStatus");
            }
            if (this.validationResults == null) {
                str = String.valueOf(str).concat(" validationResults");
            }
            if (this.timestamp == null) {
                str = String.valueOf(str).concat(" timestamp");
            }
            if (this.commandAndResponses == null) {
                str = String.valueOf(str).concat(" commandAndResponses");
            }
            if (this.instructionDurations == null) {
                str = String.valueOf(str).concat(" instructionDurations");
            }
            if (this.testCaseId == null) {
                str = String.valueOf(str).concat(" testCaseId");
            }
            if (this.id == null) {
                str = String.valueOf(str).concat(" id");
            }
            if (str.isEmpty()) {
                return new AutoValue_Result(this.smarttapStatus, this.paymentStatus, this.validationResults, this.timestamp, this.commandAndResponses, this.instructionDurations, this.encryptedValuables, this.testCaseId.longValue(), this.id.longValue());
            }
            String str2 = "Missing required properties:";
            str = String.valueOf(str);
            throw new IllegalStateException(str.length() != 0 ? str2.concat(str) : new String(str2));
        }

        private Builder(Result result) {
            this();
            this.smarttapStatus = result.smarttapStatus();
            this.paymentStatus = result.paymentStatus();
            this.validationResults = result.validationResults();
            this.timestamp = result.timestamp();
            this.commandAndResponses = result.commandAndResponses();
            this.instructionDurations = result.instructionDurations();
            this.encryptedValuables = result.encryptedValuables();
            this.testCaseId = Long.valueOf(result.testCaseId());
            this.id = Long.valueOf(result.id());
        }
    }

    public enum Status {
        STATUS_SUCCESS(R.string.status_success),
        STATUS_ERROR(R.string.status_error),
        STATUS_DEACTIVATED_LINK_LOSS(R.string.status_deactivated_link_loss),
        STATUS_DEACTIVATED_DESELECTED(R.string.status_deactivated_deselected);
        
        private final int stringResId;

        private Status(int i) {
            this.stringResId = i;
        }

        public int stringResId() {
            return this.stringResId;
        }

        public static Status fromString(String str, Context context) {
            for (Status status : values()) {
                if (context.getString(status.stringResId()).equals(str)) {
                    return status;
                }
            }
            return null;
        }
    }

    public abstract ImmutableList<CommandAndResponse> commandAndResponses();

    public abstract ImmutableSet<MerchantValuable> encryptedValuables();

    public abstract long id();

    public abstract ImmutableList<InstructionDuration> instructionDurations();

    public abstract Status paymentStatus();

    public abstract Status smarttapStatus();

    public abstract long testCaseId();

    public abstract String timestamp();

    public abstract Builder toBuilder();

    public abstract ValidationResults validationResults();

    public static Builder builder() {
        return new Builder((byte) 0).setValidationResults(ValidationResults.create(com.google.commerce.tapandpay.merchantapp.validation.ValidationResults.Status.NO_SCHEMA)).setId(-1);
    }

    public boolean commandsEquals(Result result) {
        return smarttapStatus().equals(result.smarttapStatus()) && paymentStatus().equals(result.paymentStatus()) && smartTapCommandsEqual(result.smartTapCommands()) && paymentCommandsEqual(result.paymentCommands());
    }

    private boolean paymentCommandsEqual(List<CommandAndResponse> list) {
        List paymentCommands = paymentCommands();
        if (paymentCommands.size() != list.size()) {
            return false;
        }
        for (int i = 0; i < paymentCommands.size(); i++) {
            CommandAndResponse commandAndResponse = (CommandAndResponse) paymentCommands.get(i);
            CommandAndResponse commandAndResponse2 = (CommandAndResponse) list.get(i);
            if (commandAndResponse.type() != commandAndResponse2.type()) {
                return false;
            }
            if (commandAndResponse.type() != CommandType.COMPUTE_CRYPTOGRAPHIC_CHECKSUM && !commandAndResponse.equals(commandAndResponse2)) {
                return false;
            }
        }
        return true;
    }

    private boolean smartTapCommandsEqual(List<CommandAndResponse> list) {
        List smartTapCommands = smartTapCommands();
        if (smartTapCommands.size() != list.size()) {
            return false;
        }
        for (int i = 0; i < smartTapCommands.size(); i++) {
            CommandAndResponse commandAndResponse = (CommandAndResponse) smartTapCommands.get(i);
            CommandAndResponse commandAndResponse2 = (CommandAndResponse) list.get(i);
            if (commandAndResponse.type() != commandAndResponse2.type()) {
                return false;
            }
            if (commandAndResponse.type() != CommandType.GET_SMARTTAP_DATA && !commandAndResponse.equals(commandAndResponse2)) {
                return false;
            }
            if (commandAndResponse.type() == CommandType.GET_SMARTTAP_DATA) {
                try {
                    Set hashSet = new HashSet(ByteHelper.getCommandTlvs(commandAndResponse.command()));
                    HashSet hashSet2 = new HashSet(ByteHelper.getCommandTlvs(commandAndResponse2.command()));
                    Set hashSet3 = new HashSet(ByteHelper.getResponseTlvs(commandAndResponse.response()));
                    HashSet hashSet4 = new HashSet(ByteHelper.getResponseTlvs(commandAndResponse2.response()));
                    if (!hashSet.equals(hashSet2) || !hashSet3.equals(hashSet4)) {
                        return false;
                    }
                } catch (ByteHelperException e) {
                    return false;
                }
            }
        }
        return true;
    }

    private List<CommandAndResponse> smartTapCommands() {
        List<CommandAndResponse> arrayList = new ArrayList();
        Iterator it = commandAndResponses().iterator();
        while (it.hasNext()) {
            CommandAndResponse commandAndResponse = (CommandAndResponse) it.next();
            if (commandAndResponse.type().isSmartTap()) {
                arrayList.add(commandAndResponse);
            }
        }
        return arrayList;
    }

    private List<CommandAndResponse> paymentCommands() {
        List<CommandAndResponse> arrayList = new ArrayList();
        Iterator it = commandAndResponses().iterator();
        while (it.hasNext()) {
            CommandAndResponse commandAndResponse = (CommandAndResponse) it.next();
            if (commandAndResponse.type().isPayment()) {
                arrayList.add(commandAndResponse);
            }
        }
        return arrayList;
    }
}
