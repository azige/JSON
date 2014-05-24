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

import java.io.StringReader;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonNumber;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.json.JsonObject;
import javax.json.JsonString;



/**
 *
 * @author Azige
 */
public class JsonReaderImplTest{

    public JsonReaderImplTest(){
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
    public void testReadObject(){
        String text = "{\"name\":\"bob\", \"age\":22, \"phones\":[\"123456\", \"234567\"]}";

        JsonObject object = new JsonReaderImpl(new StringReader(text)).readObject();
        assertEquals("bob", object.getString("name"));
        assertEquals(22, object.getInt("age"));
        JsonArray phoneList = object.getJsonArray("phones");
        assertEquals("123456", phoneList.getString(0));
        assertEquals("234567", phoneList.getString(1));
    }

}
