package com.example.termproject;

public class ClassType {
	public String _id;
	public String name;
	public String description;

	public ClassType(String _id, String name, String description) {
		this._id = _id;
		this.name = name;
		this.description = description;
	}

	public ClassType(String name, String description) {
		this.name = name;
		this.description = description;
	}

	//no argument constructor for firebase queries
	public ClassType() {}

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return this._id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setDescription() {
    	this.description = description;
    }

    public String getDescription() {
    	return this.description;
    }
}
