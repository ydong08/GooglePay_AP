package com.google.android.gms.common.api;

public abstract class PendingResult<R extends Result> {
    public abstract void cancel();

    public abstract void setResultCallback(ResultCallback<? super R> resultCallback);

    public Integer zzasd() {
        throw new UnsupportedOperationException();
    }
}
