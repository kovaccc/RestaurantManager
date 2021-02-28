package com.example.restaurantmanager.Model;

public class Tables {
    private String Number, Persons;
    private String State;

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }


    public Tables() {
    }

    public Tables(String number, String persons, String state) {
        Number = number;
        Persons = persons;
        State = state;
    }


    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getPersons() {
        return Persons;
    }

    public void setPersons(String persons) {
        Persons = persons;
    }

    
}
