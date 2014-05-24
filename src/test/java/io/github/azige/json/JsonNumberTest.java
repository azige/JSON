/*
 * Copyright 2014 Azige.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.azige.json;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Azige
 */
public class JsonNumberTest{

    @BeforeClass
    public static void setUpClass(){
    }

    @AfterClass
    public static void tearDownClass(){
    }

        public JsonNumberTest(){
        }

    @Before
    public void setUp(){
    }

    @After
    public void tearDown(){
    }

    /**
     * Test of getIntValue method, of class JsonNumber.
     */
    @Test
    public void testGetIntValue(){
        System.out.println("getIntValue");
        JsonNumberImpl instance = new JsonNumberImpl(12);
        int expResult = 12;
        int result = instance.getIntValue();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLongValue method, of class JsonNumber.
     */
    @Test
    public void testGetLongValue(){
        System.out.println("getLongValue");
        JsonNumberImpl instance = new JsonNumberImpl(12L);
        long expResult = 12L;
        long result = instance.getLongValue();
        assertEquals(expResult, result);
    }

    /**
     * Test of getFloatValue method, of class JsonNumber.
     */
    @Test
    public void testGetFloatValue(){
        System.out.println("getFloatValue");
        JsonNumberImpl instance = new JsonNumberImpl(1.2F);
        float expResult = 1.2F;
        float result = instance.getFloatValue();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getDoubleValue method, of class JsonNumber.
     */
    @Test
    public void testGetDoubleValue(){
        System.out.println("getDoubleValue");
        JsonNumberImpl instance = new JsonNumberImpl(1.2);
        double expResult = 1.2;
        double result = instance.getDoubleValue();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of toString method, of class JsonNumber.
     */
    @Test
    public void testToString(){
        System.out.println("toString");
        JsonNumberImpl instance = new JsonNumberImpl(12.0);
        String expResult = "12.0";
        String result = instance.toString();
        System.out.println(result);
        assertEquals(expResult, result);
    }

//    /**
//     * Test of valueOf method, of class JsonNumber.
//     */
//    @Test
//    public void testValueOf_String(){
//        System.out.println("valueOf");
//        String value = "-124.567E-123";
//        JsonNumberImpl number = JsonNumberImpl.valueOf(value);
//        assertEquals(-124.567E-123, number.getDoubleValue(), 0.0);
//    }
}
