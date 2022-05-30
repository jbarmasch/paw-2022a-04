package ar.edu.itba.paw.service;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class TransactionUtil {
//    TransactionUtil.executeAfterTransaction(() -> );

    public static void executeAfterTransaction(Runnable task) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void suspend() {}

            @Override
            public void resume() {}

            @Override
            public void flush() {}

            @Override
            public void beforeCommit(boolean b) {}

            @Override
            public void beforeCompletion() {}

            public void afterCommit() {
                task.run();
            }

            @Override
            public void afterCompletion(int i) {}
        });
    }
}
