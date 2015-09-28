package com.ivt.blueftp.bluetooth;

/**
 * Project name: File-explorer
 * Created by zhuminjue on 2015/9/23.
 */
public abstract class BluetoothAction {
    private Object SYNC = new Object();

    public abstract void setParameter(Object... parameter);

    protected boolean block() {
        return true;
    }

    public void execute() {
        doExecute();
        waitIfBlock();
    }

    protected abstract void doExecute();

    private void waitIfBlock() {
        if (!block()) {
            return;
        }
        synchronized (SYNC) {
            try {
                SYNC.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void finish() {
        if (!block()) {
            return;
        }
        synchronized (SYNC) {
            SYNC.notify();
        }
    }

    public void destroy() {

    }
    public abstract Object getResult();
}
