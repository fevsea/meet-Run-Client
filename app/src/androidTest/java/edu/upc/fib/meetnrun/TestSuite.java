package edu.upc.fib.meetnrun;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        MeetingListTest.class,
        FriendListTest.class,
        UserProfileTest.class,
        CreateMeetingTest.class
})

public class TestSuite {}
