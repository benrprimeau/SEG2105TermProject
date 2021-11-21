package com.example.termproject;

public class GymClass {
	public String _id;

    public String instructorName;

    public ClassType classType;
    public String difficultyLevel;
    public String day;
    public String time;
    public int maximumCapacity;

    public GymClass(String _id, String instructorName, ClassType classType, String difficultyLevel, String day, String time, int maximumCapacity) {
        this._id = _id;
        this.instructorName = instructorName;
        this.classType = classType;
        this.difficultyLevel = difficultyLevel;
        this.day = day;
        this.time = time;
        this.maximumCapacity = maximumCapacity;
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

    public void setMaximumCapacity(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }
}
