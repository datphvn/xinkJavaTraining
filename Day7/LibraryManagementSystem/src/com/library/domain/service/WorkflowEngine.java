package com.library.domain.service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Engine for executing workflows.
 * Manages workflow registration and execution with monitoring.
 */
public class WorkflowEngine {
    private final Map<String, Workflow> workflows;
    private final List<WorkflowExecutionListener> listeners;
    private final Map<String, WorkflowExecutionMetrics> metrics;

    public WorkflowEngine() {
        this.workflows = new ConcurrentHashMap<>();
        this.listeners = Collections.synchronizedList(new ArrayList<>());
        this.metrics = new ConcurrentHashMap<>();
    }

    /**
     * Registers a workflow.
     * @param workflow The workflow to register
     */
    public void registerWorkflow(Workflow workflow) {
        Objects.requireNonNull(workflow, "Workflow cannot be null");
        workflows.put(workflow.getName(), workflow);
    }

    /**
     * Unregisters a workflow.
     * @param workflowName The name of the workflow to unregister
     */
    public void unregisterWorkflow(String workflowName) {
        workflows.remove(workflowName);
    }

    /**
     * Executes a workflow.
     * @param workflowName The name of the workflow to execute
     * @param context The workflow context
     * @return The workflow result
     */
    public WorkflowResult executeWorkflow(String workflowName, WorkflowContext context) {
        Workflow workflow = workflows.get(workflowName);
        if (workflow == null) {
            return WorkflowResult.failure("Workflow not found: " + workflowName);
        }

        return executeWorkflow(workflow, context);
    }

    /**
     * Executes a workflow.
     * @param workflow The workflow to execute
     * @param context The workflow context
     * @return The workflow result
     */
    public WorkflowResult executeWorkflow(Workflow workflow, WorkflowContext context) {
        Objects.requireNonNull(workflow, "Workflow cannot be null");
        Objects.requireNonNull(context, "Context cannot be null");

        WorkflowExecutionMetrics executionMetrics = new WorkflowExecutionMetrics(workflow.getName());
        metrics.put(workflow.getName(), executionMetrics);

        try {
            // Validate context
            if (!workflow.validateContext(context)) {
                return WorkflowResult.failure("Invalid workflow context");
            }

            notifyWorkflowStarted(workflow, context);
            executionMetrics.start();

            // Execute workflow
            WorkflowResult result = workflow.execute(context);

            executionMetrics.finish();
            if (result.isSuccess()) {
                notifyWorkflowSucceeded(workflow, context, result);
            } else {
                notifyWorkflowFailed(workflow, context, result);
            }

            return result;
        } catch (Exception e) {
            executionMetrics.recordException(e);
            notifyWorkflowException(workflow, context, e);
            return WorkflowResult.failure("Workflow execution failed: " + e.getMessage(), e);
        }
    }

    /**
     * Gets a registered workflow.
     * @param workflowName The name of the workflow
     * @return The workflow, or empty if not found
     */
    public Optional<Workflow> getWorkflow(String workflowName) {
        return Optional.ofNullable(workflows.get(workflowName));
    }

    /**
     * Gets all registered workflows.
     * @return A collection of all registered workflows
     */
    public Collection<Workflow> getAllWorkflows() {
        return new ArrayList<>(workflows.values());
    }

    /**
     * Gets the execution metrics for a workflow.
     * @param workflowName The name of the workflow
     * @return The execution metrics, or empty if not found
     */
    public Optional<WorkflowExecutionMetrics> getMetrics(String workflowName) {
        return Optional.ofNullable(metrics.get(workflowName));
    }

    /**
     * Adds a listener for workflow execution events.
     * @param listener The listener to add
     */
    public void addListener(WorkflowExecutionListener listener) {
        listeners.add(Objects.requireNonNull(listener, "Listener cannot be null"));
    }

    /**
     * Removes a listener for workflow execution events.
     * @param listener The listener to remove
     */
    public void removeListener(WorkflowExecutionListener listener) {
        listeners.remove(listener);
    }

    private void notifyWorkflowStarted(Workflow workflow, WorkflowContext context) {
        for (WorkflowExecutionListener listener : listeners) {
            try {
                listener.onWorkflowStarted(workflow.getName(), context);
            } catch (Exception e) {
                // Ignore listener errors
            }
        }
    }

    private void notifyWorkflowSucceeded(Workflow workflow, WorkflowContext context, WorkflowResult result) {
        for (WorkflowExecutionListener listener : listeners) {
            try {
                listener.onWorkflowSucceeded(workflow.getName(), context, result);
            } catch (Exception e) {
                // Ignore listener errors
            }
        }
    }

    private void notifyWorkflowFailed(Workflow workflow, WorkflowContext context, WorkflowResult result) {
        for (WorkflowExecutionListener listener : listeners) {
            try {
                listener.onWorkflowFailed(workflow.getName(), context, result);
            } catch (Exception e) {
                // Ignore listener errors
            }
        }
    }

    private void notifyWorkflowException(Workflow workflow, WorkflowContext context, Exception exception) {
        for (WorkflowExecutionListener listener : listeners) {
            try {
                listener.onWorkflowException(workflow.getName(), context, exception);
            } catch (Exception e) {
                // Ignore listener errors
            }
        }
    }

    /**
     * Interface for listening to workflow execution events.
     */
    public interface WorkflowExecutionListener {
        void onWorkflowStarted(String workflowName, WorkflowContext context);
        void onWorkflowSucceeded(String workflowName, WorkflowContext context, WorkflowResult result);
        void onWorkflowFailed(String workflowName, WorkflowContext context, WorkflowResult result);
        void onWorkflowException(String workflowName, WorkflowContext context, Exception exception);
    }

    /**
     * Metrics for workflow execution.
     */
    public static class WorkflowExecutionMetrics {
        private final String workflowName;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Exception exception;
        private int executionCount;

        public WorkflowExecutionMetrics(String workflowName) {
            this.workflowName = workflowName;
            this.executionCount = 0;
        }

        public void start() {
            this.startTime = LocalDateTime.now();
            this.executionCount++;
        }

        public void finish() {
            this.endTime = LocalDateTime.now();
        }

        public void recordException(Exception exception) {
            this.exception = exception;
            this.endTime = LocalDateTime.now();
        }

        public String getWorkflowName() {
            return workflowName;
        }

        public LocalDateTime getStartTime() {
            return startTime;
        }

        public LocalDateTime getEndTime() {
            return endTime;
        }

        public long getDurationMillis() {
            if (startTime == null || endTime == null) {
                return 0;
            }
            return java.time.temporal.ChronoUnit.MILLIS.between(startTime, endTime);
        }

        public Exception getException() {
            return exception;
        }

        public int getExecutionCount() {
            return executionCount;
        }

        public boolean isSuccessful() {
            return exception == null && endTime != null;
        }

        @Override
        public String toString() {
            return "WorkflowExecutionMetrics{" +
                    "workflowName='" + workflowName + '\'' +
                    ", duration=" + getDurationMillis() + "ms" +
                    ", executionCount=" + executionCount +
                    ", successful=" + isSuccessful() +
                    '}';
        }
    }
}
