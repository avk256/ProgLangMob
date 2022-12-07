package com.example.lab6;

import android.os.CountDownTimer;

public abstract class MyCountDownTimer {

    private CountDownTimer cdt;
    private long millisInFuture;
    private long countDownInterval;

    public MyCountDownTimer(long millisInFuture, long countDownInterval) {
        this.millisInFuture = millisInFuture;
        this.countDownInterval = countDownInterval;

        recreateCounter(millisInFuture, countDownInterval);
    }
    public long getMillisInFuture(){
        return millisInFuture;
    }
    public long getCountDownInterval(){
        return countDownInterval;
    }

    public abstract void onFinish();
    public void pause(){cdt.cancel();};
    public void resume(){recreateCounter(millisInFuture, countDownInterval);};
    public void resume(long millisInFuture, long countDownInterval){recreateCounter(millisInFuture, countDownInterval);};


    public abstract void onTick(long millisUntilFinished);

    public void start(){
        cdt.start();
    }

    private void setMillisInFuture(long millisInFuture){
        this.millisInFuture = millisInFuture;
    }

    public void onIncrement(long millis){
        millisInFuture += millis;
        recreateCounter(millisInFuture, countDownInterval);
    }

    private void recreateCounter(long millisInFuture, long countDownInterval){
        if(cdt != null){
            try {
                cdt.cancel();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }

        cdt = new CountDownTimer(millisInFuture, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                setMillisInFuture(millisUntilFinished);
                MyCountDownTimer.this.onTick(millisUntilFinished);
            }


            @Override
            public void onFinish() {
                MyCountDownTimer.this.onFinish();
            }
        };
        cdt.start();
    }
}
