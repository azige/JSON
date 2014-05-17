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

import java.util.Arrays;
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
public class JsonArrayTest{

    @BeforeClass
    public static void setUpClass(){
    }

    @AfterClass
    public static void tearDownClass(){
    }

        public JsonArrayTest(){
        }

    @Before
    public void setUp(){
    }

    @After
    public void tearDown(){
    }

    @Test
    public void testSomething(){
        JsonArray array = new JsonArray(Arrays.asList(new JsonNumber(123), new JsonString("asd"), JsonBoolean.TRUE));
        String result = array.toString();
        System.out.println(result);
        assertEquals("[123,\"asd\",true]", result);
    }
}
