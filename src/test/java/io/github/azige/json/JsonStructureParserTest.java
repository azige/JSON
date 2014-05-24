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

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.json.JsonObject;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

/**
 *
 * @author Azige
 */
public class JsonStructureParserTest{

    public JsonStructureParserTest(){
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
        JsonObject object = new JsonObjectBuilderImpl()
            .add("firstName", "John")
            .add("lastName", "Smith")
            .add("age", 25)
            .add("phoneNumber", new JsonArrayBuilderImpl()
                .add(new JsonObjectBuilderImpl()
                    .add("type", "home")
                    .add("number", "212 555-1234")
                )
                .add(new JsonObjectBuilderImpl()
                    .add("type", "fax")
                    .add("number", "646 555-4567")
                )
            )
            .build();

        JsonParser parser = new JsonStructureParser(object);

        assertEquals(Event.START_OBJECT, parser.next());

        assertEquals(Event.KEY_NAME, parser.next());
        assertEquals("firstName", parser.getString());
        assertEquals(Event.VALUE_STRING, parser.next());
        assertEquals("John", parser.getString());

        assertEquals(Event.KEY_NAME, parser.next());
        assertEquals("lastName", parser.getString());
        assertEquals(Event.VALUE_STRING, parser.next());
        assertEquals("Smith", parser.getString());

        assertEquals(Event.KEY_NAME, parser.next());
        assertEquals("age", parser.getString());
        assertEquals(Event.VALUE_NUMBER, parser.next());
        assertEquals(25, parser.getInt());

        assertEquals(Event.KEY_NAME, parser.next());
        assertEquals("phoneNumber", parser.getString());
        assertEquals(Event.START_ARRAY, parser.next());

        assertEquals(Event.START_OBJECT, parser.next());

        assertEquals(Event.KEY_NAME, parser.next());
        assertEquals("type", parser.getString());
        assertEquals(Event.VALUE_STRING, parser.next());
        assertEquals("home", parser.getString());

        assertEquals(Event.KEY_NAME, parser.next());
        assertEquals("number", parser.getString());
        assertEquals(Event.VALUE_STRING, parser.next());
        assertEquals("212 555-1234", parser.getString());

        assertEquals(Event.END_OBJECT, parser.next());

        assertEquals(Event.START_OBJECT, parser.next());

        assertEquals(Event.KEY_NAME, parser.next());
        assertEquals("type", parser.getString());
        assertEquals(Event.VALUE_STRING, parser.next());
        assertEquals("fax", parser.getString());

        assertEquals(Event.KEY_NAME, parser.next());
        assertEquals("number", parser.getString());
        assertEquals(Event.VALUE_STRING, parser.next());
        assertEquals("646 555-4567", parser.getString());

        assertEquals(Event.END_OBJECT, parser.next());

        assertEquals(Event.END_ARRAY, parser.next());

        assertEquals(Event.END_OBJECT, parser.next());

    }

}
