package model;

import java.time.LocalDate;

/**
 * Model class for an Employee.
 */
public class Employee {
    private String id;
    private String name;
    private LocalDate hireDate;
    private double salaryPerMonth;

    public Employee(String id, String name, LocalDate hireDate, double salaryPerMonth) {
        this.id = id;
        this.name = name;
        this.hireDate = hireDate;
        this.salaryPerMonth = salaryPerMonth;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public double getSalaryPerMonth() {
        return salaryPerMonth;
    }

    public void setSalaryPerMonth(double salaryPerMonth) {
        this.salaryPerMonth = salaryPerMonth;
    }
}
