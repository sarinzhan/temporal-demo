package com.example.temporaldemo;

import com.example.temporaldemo.activities.OrderActivityImpl;
import com.example.temporaldemo.workflow.OrderWorkflowImpl;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class WorkerStarter implements CommandLineRunner {

    private final WorkerFactory factory;

    public WorkerStarter(WorkerFactory factory) {
        this.factory = factory;
    }

    @Override
    public void run(String... args) {

        Worker worker = factory.newWorker("ORDER_TASK_QUEUE");

        worker.registerWorkflowImplementationTypes(OrderWorkflowImpl.class);
        worker.registerActivitiesImplementations(new OrderActivityImpl());

//        factory.start();

//        System.out.println("Temporal Worker Started...");
    }
}
