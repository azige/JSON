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
public class JsonArrayTestPerformance{

    public JsonArrayTestPerformance(){
    }

    @BeforeClass
    public static void setUpClass(){
    }

    @AfterClass
    public static void tearDownClass(){
    }

    @Before
    public void setUp(){
    }

    @After
    public void tearDown(){
    }

    @Test
    public void testAddNull(){
        System.out.println("testAddNull");
        long start = System.nanoTime();
        JsonArrayImpl array = new JsonArrayImpl();
        for (int i = 0; i < 10_000_000; i++){
            array.addObj(null);
        }
        long timeElapsed = System.nanoTime() - start;
        System.out.println("time elapsed: " + timeElapsed);
    }

    @Test
    public void testAddJsonType_NULL(){
        System.out.println("testAddJsonType_NULL");
        long start = System.nanoTime();
        JsonArrayImpl array = new JsonArrayImpl();
        for (int i = 0; i < 10_000_000; i++){
            array.addObj(JsonValueType.NULL);
        }
        long timeElapsed = System.nanoTime() - start;
        System.out.println("time elapsed: " + timeElapsed);
    }
}
