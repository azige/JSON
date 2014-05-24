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

import java.io.StringReader;

import javax.json.stream.JsonParser.Event;

/**
 *
 * @author Azige
 */
public class JsonParserImplTest{

    public JsonParserImplTest(){
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
    public void testSomeMethod(){
        String text = "{"
            + "   \"firstName\": \"John\", \"lastName\": \"Smith\", \"age\": 25,\n"
            + "   \"phoneNumber\": [\n"
            + "       { \"type\": \"home\", \"number\": \"212 555-1234\" },\n"
            + "       { \"type\": \"fax\", \"number\": \"646 555-4567\" }\n"
            + "    ]\n"
            + " }";
        JsonParserImpl jpi = new JsonParserImpl(new StringReader(text));

        assertEquals(Event.START_OBJECT, jpi.next());

        assertEquals(Event.KEY_NAME, jpi.next());
        assertEquals("firstName", jpi.getString());
        assertEquals(Event.VALUE_STRING, jpi.next());
        assertEquals("John", jpi.getString());

        assertEquals(Event.KEY_NAME, jpi.next());
        assertEquals("lastName", jpi.getString());
        assertEquals(Event.VALUE_STRING, jpi.next());
        assertEquals("Smith", jpi.getString());

        assertEquals(Event.KEY_NAME, jpi.next());
        assertEquals("age", jpi.getString());
        assertEquals(Event.VALUE_NUMBER, jpi.next());
        assertEquals(25, jpi.getInt());

        assertEquals(Event.KEY_NAME, jpi.next());
        assertEquals("phoneNumber", jpi.getString());
        assertEquals(Event.START_ARRAY, jpi.next());

        assertEquals(Event.START_OBJECT, jpi.next());

        assertEquals(Event.KEY_NAME, jpi.next());
        assertEquals("type", jpi.getString());
        assertEquals(Event.VALUE_STRING, jpi.next());
        assertEquals("home", jpi.getString());

        assertEquals(Event.KEY_NAME, jpi.next());
        assertEquals("number", jpi.getString());
        assertEquals(Event.VALUE_STRING, jpi.next());
        assertEquals("212 555-1234", jpi.getString());

        assertEquals(Event.END_OBJECT, jpi.next());

        assertEquals(Event.START_OBJECT, jpi.next());

        assertEquals(Event.KEY_NAME, jpi.next());
        assertEquals("type", jpi.getString());
        assertEquals(Event.VALUE_STRING, jpi.next());
        assertEquals("fax", jpi.getString());

        assertEquals(Event.KEY_NAME, jpi.next());
        assertEquals("number", jpi.getString());
        assertEquals(Event.VALUE_STRING, jpi.next());
        assertEquals("646 555-4567", jpi.getString());

        assertEquals(Event.END_OBJECT, jpi.next());

        assertEquals(Event.END_ARRAY, jpi.next());

        assertEquals(Event.END_OBJECT, jpi.next());
    }

}
