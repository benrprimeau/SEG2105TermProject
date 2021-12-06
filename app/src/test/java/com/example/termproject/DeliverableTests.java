package com.example.termproject;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DeliverableTests {
    public Account account1 = new Account("ac1", "Account1", "password");
    public Account account2 = new Account("ac2", "Account2","password");
    public Account account3 = new Account("ac3","Account3","password");
    public ClassType classType = new ClassType("1","Name","Description");
    public GymClass gymClass = new GymClass("1","Instructor",classType, "Easy", "Monday","9-10",2);

    @Test
    public void testGymClassEnrolledUsers() {
        GymClass gymClass1 = new GymClass();
        gymClass.addUser(account1);
        gymClass.addUser(account2);
        gymClass.addUser(account3);

        assertEquals(3, gymClass.getUsersEnrolled().size());
        assertEquals(account1, gymClass.getUsersEnrolled().get(0));
    }

    @Test
    public void testClassCapacity() {
        GymClass gymClass2 = new GymClass("1","Instructor",classType, "Easy", "Monday","9-10",2);
        gymClass2.addUser(account1);
        gymClass2.addUser(account2);

        assertEquals(0, gymClass2.getRemainingCapacity());
    }

    @Test
    public void testAccountClasses() {
        Account account4 = new Account("Ac4","Account 4","password");

        account4.addClass(gymClass.get_id());

        assertEquals(gymClass.get_id(), account4.getClasses().get(0));

        account4.removeClass(gymClass.get_id());
    }

    @Test
    public void testIsEnrolled() {
        GymClass gymClass3 = new GymClass();
        gymClass3.addUser(account2);
        assertEquals(true, gymClass3.userIsEnrolled(account2.get_id()));
    }

    @Test
    public void testEnrolledInClass() {
        account3.addClass(gymClass.get_id());
        assertEquals(true, account3.enrolledInClass(gymClass.get_id()));
    }
}
