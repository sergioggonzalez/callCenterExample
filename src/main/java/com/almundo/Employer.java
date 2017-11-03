/**
 * Created by segonzalez on 10/31/17.
 */
package com.almundo;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Employer {

    public enum EmployerType {
        OPERADOR, SUPERVISOR, DIRECTOR
    };

    public enum State {
        AVAILABLE, ASSIGNED
    };

    private EmployerType employerType;
    private State state;
    private String id;
    private Logger logger = Logger.getLogger(Employer.class.toString());

    public Employer(EmployerType type, String id) {
        this.employerType = type;
        this.state = State.AVAILABLE;
        this.id = id;
    }

    public EmployerType getEmployerType() {
        return employerType;
    }

    public State getState() {
        return state;
    }

    public String getId() {
        return id;
    }

    public boolean isAvailable() {
        return state == State.AVAILABLE;
    }

    public void take(Call call) {
        logger.log(Level.INFO, this.id + " Take call " + call.getId());
        call.takeCallBy(this);
        this.state = State.AVAILABLE;
        logger.log(Level.INFO, this.id + " finish call " + call.getId());
    }

    public Employer assign() {
        this.state = State.ASSIGNED;
        return this;
    }



}
