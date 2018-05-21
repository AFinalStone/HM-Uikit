package com.hm.iou.uikit.handler;

import android.os.Handler;

import java.lang.ref.WeakReference;

public class WeakReferenceHandler<T> extends Handler {

    protected final WeakReference<T> mWeakReferenceObject;

    public WeakReferenceHandler(T object) {
        this.mWeakReferenceObject = new WeakReference<T>(object);
    }
}