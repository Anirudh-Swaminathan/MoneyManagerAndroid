package com.anirudh.anirudhswami.personalassistant.model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Anirudh Swami on 08-07-2016 for the project PersonalAssistant.
 */
public class BudgetRow {
    @DatabaseField(id = true)
    private String roll;

    @DatabaseField(canBeNull = false, defaultValue = "false")
    private Boolean subscribed;

    @DatabaseField(canBeNull = false)
    private int month;

    @DatabaseField(canBeNull = false)
    private int year;

    @DatabaseField(canBeNull = false)
    private int budget;

    @DatabaseField(canBeNull = false, defaultValue = "0")
    private Integer cost;

    @DatabaseField(canBeNull = false, defaultValue = "0")
    private Integer numDays;

    public BudgetRow() {

    }

    public BudgetRow(int budget, Integer cost, int month, Integer numDays, String roll, Boolean subscribed, int year) {
        this.budget = budget;
        this.cost = cost;
        this.month = month;
        this.numDays = numDays;
        this.roll = roll;
        this.subscribed = subscribed;
        this.year = year;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public Integer getNumDays() {
        return numDays;
    }

    public void setNumDays(Integer numDays) {
        this.numDays = numDays;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public Boolean getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(Boolean subscribed) {
        this.subscribed = subscribed;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "BudgetRow[ roll = " + roll + "Month = " + month + "Year = " + year + "Budget = " + budget;
        //return super.toString();
    }
}
