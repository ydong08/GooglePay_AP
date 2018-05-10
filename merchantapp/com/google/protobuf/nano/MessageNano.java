package com.google.protobuf.nano;

public abstract class MessageNano {
    protected volatile int cachedSize = -1;

    public String toString() {
        return MessageNanoPrinter.print(this);
    }

    public MessageNano clone() throws CloneNotSupportedException {
        return (MessageNano) super.clone();
    }
}
