 /**
 * Created by segonzalez on 10/31/17.
 */

package com.almundo;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Call {


    private static int MIN_DURATION = 5000;
    private static int MAX_DURATION = 10000;
    Random random = new Random();
    private String id;
    private int duration;
    private State state;
    private Employer operador;
    private boolean holdFlag;
    private Logger logger = Logger.getLogger(Call.class.toString());

    public enum State {
        NEW, WAITING, ATTENDED, FINISHED
    }

    public Call(String string) {
        id = string;
        duration = setDuration();
        state = State.NEW;
        holdFlag = false;
    }

    public int getDuration() {
        return duration;
    }

    private int setDuration() {
        return random.nextInt(MAX_DURATION - MIN_DURATION) + MIN_DURATION;
    }

    public String getId() {
        return id;
    }

    public State getState() {
        return state;
    }

    public Employer getOperador() {
        return operador;
    }

    public void takeCallBy(Employer employer) {
        operador = employer;
        state = State.ATTENDED;


        try {
            TimeUnit.MILLISECONDS.sleep(duration);
            logger.log(Level.INFO, "Start call " + this.id + ", duration: " + this.getDuration());

        } catch (InterruptedException e) {
            logger.log(Level.WARNING, "Error in call:  " + e.toString());
        }
        state = State.FINISHED;
        logger.log(Level.INFO, "Finish call " + this.id + " de duracin " + this.getDuration());
    }

    public void putOnHold() {
        state = State.WAITING;
        holdFlag = true;
    }

    public boolean getHoldFlag() {
        return holdFlag;
    }

}
