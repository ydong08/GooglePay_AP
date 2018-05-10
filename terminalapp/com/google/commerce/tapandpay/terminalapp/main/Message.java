package com.google.commerce.tapandpay.terminalapp.main;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.libraries.commerce.hce.terminal.nfc.NfcMessage;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import java.io.Serializable;
import java.util.Objects;

class Message implements Parcelable {
    public static final Creator<Message> CREATOR = new Creator<Message>() {
        public Message createFromParcel(Parcel parcel) {
            return new Message(parcel);
        }

        public Message[] newArray(int i) {
            return new Message[i];
        }
    };
    private final Optional<Exception> exceptionOptional;
    private final Optional<NfcMessage> nfcMessageOptional;

    public Message(NfcMessage nfcMessage) {
        Preconditions.checkNotNull(nfcMessage);
        this.nfcMessageOptional = Optional.of(nfcMessage);
        this.exceptionOptional = Optional.absent();
    }

    public Message(Exception exception) {
        Preconditions.checkNotNull(exception);
        this.nfcMessageOptional = Optional.absent();
        this.exceptionOptional = Optional.of(exception);
    }

    public Optional<NfcMessage> getNfcMessage() {
        return this.nfcMessageOptional;
    }

    public Optional<Exception> getException() {
        return this.exceptionOptional;
    }

    private Message(Parcel parcel) {
        Bundle readBundle = parcel.readBundle();
        if (readBundle != null) {
            this.nfcMessageOptional = Optional.of(NfcMessage.fromBundle(readBundle));
            this.exceptionOptional = Optional.absent();
            Preconditions.checkArgument(parcel.readSerializable() == null);
            return;
        }
        this.nfcMessageOptional = Optional.absent();
        this.exceptionOptional = Optional.of((Exception) parcel.readSerializable());
        Preconditions.checkNotNull((Exception) this.exceptionOptional.get());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        Serializable serializable = null;
        parcel.writeBundle(!this.nfcMessageOptional.isPresent() ? null : ((NfcMessage) this.nfcMessageOptional.get()).toBundle());
        if (this.exceptionOptional.isPresent()) {
            serializable = (Serializable) this.exceptionOptional.get();
        }
        parcel.writeSerializable(serializable);
    }

    public String toString() {
        if (this.nfcMessageOptional.isPresent()) {
            return ((NfcMessage) this.nfcMessageOptional.get()).toString();
        }
        return Throwables.getStackTraceAsString((Throwable) this.exceptionOptional.get());
    }

    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        Message message = (Message) obj;
        if (Objects.equals(this.nfcMessageOptional, message.nfcMessageOptional) && exceptionsAreEqual(this.exceptionOptional, message.exceptionOptional)) {
            return true;
        }
        return false;
    }

    private static boolean exceptionsAreEqual(Optional<Exception> optional, Optional<Exception> optional2) {
        if (!optional.isPresent() && !optional2.isPresent()) {
            return true;
        }
        if (optional.isPresent() && optional2.isPresent() && ((Exception) optional.get()).getClass() == ((Exception) optional2.get()).getClass()) {
            return Throwables.getStackTraceAsString((Throwable) optional.get()).equals(Throwables.getStackTraceAsString((Throwable) optional2.get()));
        }
        return false;
    }

    public int hashCode() {
        return com.google.common.base.Objects.hashCode(this.nfcMessageOptional, this.exceptionOptional);
    }
}
