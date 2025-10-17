package com.library.domain.service;

/**
 * Interface for implementing workflows.
 * Workflows define a sequence of steps to be executed.
 */
public interface Workflow {
    /**
     * Executes the workflow with the given context.
     * @param context The workflow context containing input data
     * @return The workflow result
     */
    WorkflowResult execute(WorkflowContext context);

    /**
     * Gets the name of this workflow.
     * @return The workflow name
     */
    String getName();

    /**
     * Gets a description of this workflow.
     * @return A human-readable description
     */
    String getDescription();

    /**
     * Validates the workflow context before execution.
     * @param context The workflow context to validate
     * @return true if the context is valid, false otherwise
     */
    boolean validateContext(WorkflowContext context);
}
