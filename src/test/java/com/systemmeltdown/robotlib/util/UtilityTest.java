package com.systemmeltdown.robotlib.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class UtilityTest{
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