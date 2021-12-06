package com.example.termproject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GymClass {
	public String _id;

    public String instructorName;

    public ClassType classType;
    public String difficultyLevel;
    public String day;
    public String time;
    public int maximumCapacity;
    public int remainingCapacity;

    public static List<Account> usersEnrolled = new ArrayList<Account>();

    public GymClass(String _id, String instructorName, ClassType classType, String difficultyLevel, String day, String time, int maximumCapacity) {
        this._id = _id;
        this.instructorName = instructorName;
        this.classType = classType;
        this.difficultyLevel = difficultyLevel;
        this.day = day;
        this.time = time;
        this.maximumCapacity = maximumCapacity;
        this.remainingCapacity = maximumCapacity;
    }

    //no argument constructor for firebase queries
    public GymClass() {}

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public ClassType getClassType() {
        return classType;
    }

    public void setClassType(ClassType classType) {
        this.classType = classType;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMaximumCapacity() {
        return maximumCapacity;
    }

    public int getRemainingCapacity() {
        return remainingCapacity;
    }

    public void setMaximumCapacity(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }

    public List<Account> getUsersEnrolled() {
        return usersEnrolled;
    }

    public void setUsersEnrolled(ArrayList<Account> usersEnrolled) {
        this.usersEnrolled = usersEnrolled;
    }

    public boolean userIsEnrolled(String id) {
        if(this.usersEnrolled.size()>0) {
            for(Account a : this.usersEnrolled) {
                if(a.get_id().equals(id)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void addUser(Account account) {
        this.usersEnrolled.add(account);
        remainingCapacity--;
    }

    public void removeUser(String id) {
        int index=-1;
        for(int i=0; i<this.usersEnrolled.size();i++) {
            if(this.usersEnrolled.get(i).get_id().equals(id)) {
                index=i;
            }
        }

        if(index!=-1) {
            usersEnrolled.remove(index);
            remainingCapacity++;
        }
    }
}
