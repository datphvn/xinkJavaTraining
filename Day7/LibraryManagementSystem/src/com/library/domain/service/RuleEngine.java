package com.library.domain.service;

import com.library.domain.exception.LibraryException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Rule engine for executing business rules.
 * Manages and executes business rules for different entity types.
 * Pure OOP implementation without any framework dependencies.
 */
public class RuleEngine {
    private final Map<Class<?>, List<BusinessRule<?>>> rules;
    private final List<RuleExecutionListener> listeners;

    public RuleEngine() {
        this.rules = new ConcurrentHashMap<>();
        this.listeners = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Registers a business rule for a specific entity type.
     * @param entityType The type of entity this rule applies to
     * @param rule The business rule to register
     */
    public <T> void registerRule(Class<T> entityType, BusinessRule<T> rule) {
        Objects.requireNonNull(entityType, "Entity type cannot be null");
        Objects.requireNonNull(rule, "Rule cannot be null");

        rules.computeIfAbsent(entityType, k -> new java.util.ArrayList<>())
             .add(rule);
    }

    /**
     * Unregisters a business rule.
     * @param entityType The type of entity
     * @param ruleName The name of the rule to unregister
     */
    public <T> void unregisterRule(Class<T> entityType, String ruleName) {
        List<BusinessRule<?>> entityRules = rules.get(entityType);
        if (entityRules != null) {
            entityRules.removeIf(rule -> rule.getName().equals(ruleName));
        }
    }

    /**
     * Executes all applicable rules for an entity.
     * @param entity The entity to apply rules to
     * @throws LibraryException if any rule is violated
     */
    @SuppressWarnings("unchecked")
    public <T> void executeRules(T entity) throws LibraryException {
        Objects.requireNonNull(entity, "Entity cannot be null");

        Class<?> entityType = entity.getClass();
        List<BusinessRule<?>> applicableRules = rules.getOrDefault(entityType, new ArrayList<>());

        // Sort rules by priority (higher priority first)
        List<BusinessRule<T>> sortedRules = applicableRules.stream()
            .map(rule -> (BusinessRule<T>) rule)
            .filter(rule -> rule.applies(entity))
            .sorted((r1, r2) -> Integer.compare(r2.getPriority(), r1.getPriority()))
            .collect(Collectors.toList());

        // Execute rules
        for (BusinessRule<T> rule : sortedRules) {
            try {
                notifyRuleExecutionStarted(rule, entity);
                rule.execute(entity);
                notifyRuleExecutionSucceeded(rule, entity);
            } catch (LibraryException e) {
                notifyRuleExecutionFailed(rule, entity, e);
                throw new LibraryException("RULE_VIOLATION",
                    "Business rule failed: " + rule.getDescription(), e);
            } catch (Exception e) {
                notifyRuleExecutionFailed(rule, entity, e);
                throw new LibraryException("RULE_EXECUTION_ERROR",
                    "Unexpected error executing rule: " + rule.getName(), e);
            }
        }
    }

    /**
     * Executes rules for a specific entity type.
     * @param entityType The type of entity
     * @param entity The entity to apply rules to
     * @throws LibraryException if any rule is violated
     */
    public <T> void executeRulesForType(Class<T> entityType, T entity) throws LibraryException {
        Objects.requireNonNull(entityType, "Entity type cannot be null");
        executeRules(entity);
    }

    /**
     * Gets all registered rules for an entity type.
     * @param entityType The type of entity
     * @return A list of registered rules
     */
    public <T> List<BusinessRule<T>> getRulesForType(Class<T> entityType) {
        @SuppressWarnings("unchecked")
        List<BusinessRule<T>> entityRules = (List<BusinessRule<T>>) (List<?>) 
            rules.getOrDefault(entityType, new ArrayList<>());
        return new ArrayList<>(entityRules);
    }

    /**
     * Gets the number of rules registered for an entity type.
     * @param entityType The type of entity
     * @return The number of rules
     */
    public <T> int getRuleCount(Class<T> entityType) {
        return rules.getOrDefault(entityType, new ArrayList<>()).size();
    }

    /**
     * Clears all registered rules.
     */
    public void clearAllRules() {
        rules.clear();
    }

    /**
     * Clears all rules for a specific entity type.
     * @param entityType The type of entity
     */
    public <T> void clearRulesForType(Class<T> entityType) {
        rules.remove(entityType);
    }

    /**
     * Adds a listener for rule execution events.
     * @param listener The listener to add
     */
    public void addListener(RuleExecutionListener listener) {
        listeners.add(Objects.requireNonNull(listener, "Listener cannot be null"));
    }

    /**
     * Removes a listener for rule execution events.
     * @param listener The listener to remove
     */
    public void removeListener(RuleExecutionListener listener) {
        listeners.remove(listener);
    }

    private <T> void notifyRuleExecutionStarted(BusinessRule<T> rule, T entity) {
        for (RuleExecutionListener listener : listeners) {
            try {
                listener.onRuleExecutionStarted(rule.getName(), entity);
            } catch (Exception e) {
                // Ignore listener errors
            }
        }
    }

    private <T> void notifyRuleExecutionSucceeded(BusinessRule<T> rule, T entity) {
        for (RuleExecutionListener listener : listeners) {
            try {
                listener.onRuleExecutionSucceeded(rule.getName(), entity);
            } catch (Exception e) {
                // Ignore listener errors
            }
        }
    }

    private <T> void notifyRuleExecutionFailed(BusinessRule<T> rule, T entity, Exception exception) {
        for (RuleExecutionListener listener : listeners) {
            try {
                listener.onRuleExecutionFailed(rule.getName(), entity, exception);
            } catch (Exception e) {
                // Ignore listener errors
            }
        }
    }

    /**
     * Interface for listening to rule execution events.
     */
    public interface RuleExecutionListener {
        <T> void onRuleExecutionStarted(String ruleName, T entity);
        <T> void onRuleExecutionSucceeded(String ruleName, T entity);
        <T> void onRuleExecutionFailed(String ruleName, T entity, Exception exception);
    }
}
