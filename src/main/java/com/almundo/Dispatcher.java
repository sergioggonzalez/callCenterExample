 /**
 * Created by segonzalez on 10/26/17.
 */
package com.almundo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.almundo.Employer;

public class Dispatcher {
    private static int MAX_CONCURRENT_CALLS = 10;
    private ExecutorService service = Executors.newFixedThreadPool(MAX_CONCURRENT_CALLS);
    private BlockingQueue<Employer> employers = new LinkedBlockingQueue();
    private Queue<Call> pendingCalls = new ConcurrentLinkedQueue();
    private List<Call> attendedCalls = new ArrayList();
    private int activeLines = 0;

    private Logger logger = Logger.getLogger(Dispatcher.class.toString());

    public void dispatchCall(Call call) {
        if (isRadyToDistpatch()) {
            distpatchCall(call);
        } else {
            callOnHold(call);
        }
    }

    private boolean isRadyToDistpatch() {
        return availableLines() && availableEmployers();
    }

    private boolean availableLines() {
        return activeLines < MAX_CONCURRENT_CALLS;
    }

    private void distpatchCall(Call call) {
        activeLines++;
        final Employer employer = asignNewEmployer();
        logger.log(Level.INFO, employer.getId() + " Assign to " + call.getId());
        service.execute(() -> take(employer, call));
    }

    private void take(Employer employer, Call call) {
        employer.take(call);
        distpatchPendingCall();
        attendedCalls.add(call);
        activeLines--;
    }

    private void distpatchPendingCall() {
        if (!pendingCalls.isEmpty()) {
            Call call = pendingCalls.poll();
            logger.log(Level.INFO, "Distpatch pending call " + call.getId());
            distpatchCall(call);
        }
        else {
            finish();
        }
    }


    private boolean availableEmployers() {
        return employers.stream().anyMatch(e -> e.isAvailable());
    }

    private synchronized Employer asignNewEmployer() {

        Optional<Employer> employer = filterBy(employers, Employer.EmployerType.OPERADOR);

        if (!employer.isPresent()) {
            employer = filterBy(employers, Employer.EmployerType.SUPERVISOR);
        }

        if (!employer.isPresent()) {
            employer = filterBy(employers, Employer.EmployerType.DIRECTOR);
        }
        return employer.get();
    }

    private Optional<Employer> filterBy(BlockingQueue<Employer> employers, Employer.EmployerType type) {
        return employers.stream().filter(e -> e.isAvailable() && e.getEmployerType().equals(type))
                .map(e -> e.assign())
                .findFirst();
    }

    public void finish() {
        service.shutdown();
    }

    public void waitForAllCalls() {
        try {
            service.awaitTermination(120, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, "Erron in calls distpatch.");
        }
    }

    private void callOnHold(Call call) {
        call.putOnHold();
        pendingCalls.add(call);
    }


    public Collection<Call> getAttendedCalls() {
        return attendedCalls;
    }

    public Collection<Call> getPendingCalls() {
        return pendingCalls;
    }

    public void addEmployer(Employer e) {
        employers.add(e);
    }




}
