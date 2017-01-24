package com.example.vick.astrontest;

import static com.example.vick.astrontest.Person.AgeGroup.RETIRED;
import static com.example.vick.astrontest.Person.AgeGroup.STUDENT;
import static com.example.vick.astrontest.Person.AgeGroup.WORKER;

/**
 * Created by Vick on 1/22/17.
 */

public class Person {
    public enum AgeGroup {
        STUDENT(0,R.mipmap.ic_school_black_24dp),
        WORKER(1,R.mipmap.ic_person_black_24dp),
        RETIRED(2,R.mipmap.ic_spa_black_24dp);

        private int value;
        private int iconID;

        private AgeGroup(int value, int iconID){
            this.value = value;
            this.iconID = iconID;
        }

        public AgeGroup fromInt(int value) {
            for (AgeGroup a : AgeGroup.values()) {
                if (a.value == value) {
                    return a;
                }
            }
            return WORKER;
        }

        public int getIconID() {
            return iconID;
        }

        public int getValue() {
            return value;
        }
    }

    private int id;
    private String firstName;
    private String lastName;
    private int gender;
    private int age;
    private AgeGroup ageGroup;

    private int maleIconID;
    private int femaleIconID;

    public Person(int id, String firstName, String lastName, int gender, int age){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;

        this.ageGroup = age > 60 ? RETIRED : (age < 21 ? STUDENT : WORKER);

        this.femaleIconID = gender == 2 ? ageGroup.getIconID() : android.R.color.transparent;
        this.maleIconID = gender == 1 ? ageGroup.getIconID() : android.R.color.transparent;
    }

    public AgeGroup getAgeGroup () {
        return ageGroup;
    }

    public String getFirstName() { return firstName;}

    public String getLastName() { return lastName; }

    public int getGender() { return gender; }

    public int getMaleIconID() { return maleIconID; }

    public int getFemaleIconID() { return femaleIconID; }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
