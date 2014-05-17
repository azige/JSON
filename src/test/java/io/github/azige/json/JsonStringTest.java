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
public class JsonStringTest{
    
    @BeforeClass
    public static void setUpClass(){
    }
    
    @AfterClass
    public static void tearDownClass(){
    }
    
        public JsonStringTest(){
        }
    
    @Before
    public void setUp(){
    }
    
    @After
    public void tearDown(){
    }

    /**
     * Test of toString method, of class JsonString.
     */
    @Test
    public void testToString(){
        System.out.println("toString");
        JsonStringImpl instance = new JsonStringImpl("asd\nasd\\asd\\asd\tasd");
        String expResult = "\"asd\\nasd\\\\asd\\\\asd\\tasd\"";
        String result = instance.toString();
        System.out.println(result);
        assertEquals(expResult, result);
    }
}
