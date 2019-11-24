package com.systemmeltdown.robotlib.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class UtilityTest{

    //double clamp tests

    @Test
    public void testDoubleClampReturnsThreeAndTwoTenthsWhenMinIsNegativeFiveAndAHalfAndMaxIsFiveAndAHalfAndInputIsThreeAndTwoTenths(){
        assertEquals(Utility.clamp(3.2,-5.5,5.5), 3.2, 0.0);
    }

    @Test
    public void testDoubleClampReturnsNegativeFiveAndAHalfWhenMinIsNegativeFiveAndAHalfAndMaxIsFiveAndAHalfAndInputIsNegativeSevenAndAHalf(){
        assertEquals(Utility.clamp(-7.5,-5.5,5.5), -5.5, 0.0);
    }

    @Test
    public void testDoubleClampReturnsFiveAndAHalfWhenMinIsNegativeFiveAndAHalfAndMaxIsFiveAndAHalfAndInputIsEightAndTwoTenths(){
        assertEquals(Utility.clamp(8.2,-5.5,5.5), 5.5, 0.0);
    }

    //int clamp tests

    @Test
    public void testIntClampReturnsThreeWhenMinIsNegativeFiveAndMaxIsFiveAndInputIsThree(){
        assertEquals(Utility.clamp(3,-5,5), 3, 0);
    }

    @Test
    public void testIntClampReturnsNegativeFiveWhenMinIsNegativeFiveAndMaxIsFiveAndInputIsNegativeSeven(){
        assertEquals(Utility.clamp(-7,-5,5), -5, 0);
    }

    @Test
    public void testIntClampReturnsFiveWhenMinIsNegativeFiveAndMaxIsFiveAndInputIsEight(){
        assertEquals(Utility.clamp(8,-5,5), 5, 0);
    }

    //getAverage tests
    @Test
    public void testGetAverageReturnsFiveWithTenElementArrayFromOneToTen(){
        int[] samples = {1,2,3,4,5,6,7,8,9,10};

        assertEquals(Utility.getAverage(samples), 5, 0);
    }

    //Deadband Tests

    @Test
    public void testDeadbandReturnsOneWhenInputIsOneAndDeadbandIsOneHalf(){
        assertEquals(Utility.deadband(1.0,0.5), 0.9, 1.0);
    }

    @Test 
    public void testDeadbandReturnsZeroWhenInputIsOneHalfAndDeadbandIsOne(){
        assertEquals(Utility.deadband(0.5,1.0), 0.0, 0.0);
    }

    @Test
    public void testDeadbandReturnsNegativeOneWhenInputIsNegativeOneAndDeadbandIsOneHalf(){
        assertEquals(Utility.deadband(-1.0,0.5), -1.0, -0.9);
    }
}