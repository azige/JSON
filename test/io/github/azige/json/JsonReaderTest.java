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

/**
 *
 * @author Azige
 */
public class JsonReaderTest{

    public JsonReaderTest(){
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
    public void testReadTrue(){
        String str = "true";
        JsonReader reader = new JsonReader(new StringReader(str));
        JsonType json = reader.readBoolean();
        assertEquals(JsonBoolean.TRUE, json);
    }

    @Test
    public void testReadFalse(){
        String str = "false";
        JsonReader reader = new JsonReader(new StringReader(str));
        JsonType json = reader.readBoolean();
        assertEquals(JsonBoolean.FALSE, json);
    }

    @Test
    public void testReadNull(){
        String str = "null";
        JsonReader reader = new JsonReader(new StringReader(str));
        JsonType json = reader.readAny();
        assertEquals(JsonType.NULL, json);
    }

    @Test
    public void testReadString(){
        String str = "\"物\\t\\u54c1\"";
        JsonReader reader = new JsonReader(new StringReader(str));
        JsonString json = reader.readString();
        assertEquals("物\t品", json.getValue());
        assertEquals(str, json.toString());
    }

    @Test(expected = JsonException.class)
    public void testException(){
        System.out.println("testException");
        String str = "asd";
        JsonReader reader = new JsonReader(new StringReader(str));
        try{
            reader.readAny();
        }catch (JsonException ex){
            System.out.println(ex.getMessage());
            throw ex;
        }
    }

    @Test
    public void testReadNumber(){
        String str = "-124.567E-123";
        JsonReader reader = new JsonReader(new StringReader(str));
        JsonNumber number = reader.readNumber();
        assertEquals(-124.567E-123, number.getDoubleValue(), 0.0);
    }

    @Test(expected = JsonException.class)
    public void testReadNumberException(){
        System.out.println("testReadNumberException");
        String str = "-124.567E-";
        JsonReader reader = new JsonReader(new StringReader(str));
        try{
            reader.readNumber();
        }catch (JsonException ex){
            System.out.println(ex.getMessage());
            throw ex;
        }
    }

    @Test
    public void testReadArray(){
        String str = "[null, true, false, -124.567E-123, \"物\\t\\u54c1\"]";
        JsonReader reader = new JsonReader(new StringReader(str));
        JsonArray array = reader.readArray();
        assertEquals(JsonType.NULL, array.get(0));
        assertEquals(JsonBoolean.TRUE, array.get(1));
        assertEquals(JsonBoolean.FALSE, array.get(2));
        assertEquals(-124.567E-123, ((JsonNumber)array.get(3)).getDoubleValue(), 0.0);
        assertEquals("物\t品", ((JsonString)array.get(4)).getValue());
    }

    @Test
    public void testFullParse(){
        String str = "{\n"
            + "  \"root\": {\n"
            + "    \"id\": \"dqfzmvuo\",\n"
            + "    \"text\": \"JSON\\n\",\n"
            + "    \"layout\": \"map\",\n"
            + "    \"children\": [\n"
            + "      {\n"
            + "        \"id\": \"yddmsdln\",\n"
            + "        \"text\": \"JSON Type\\n\",\n"
            + "        \"side\": \"right\",\n"
            + "        \"children\": [\n"
            + "          {\n"
            + "            \"id\": \"aoqzmflg\",\n"
            + "            \"text\": \"JSON Value\\n\",\n"
            + "            \"children\": [\n"
            + "              {\n"
            + "                \"id\": \"dvtdufuq\",\n"
            + "                \"text\": \"true\\n\"\n"
            + "              },\n"
            + "              {\n"
            + "                \"id\": \"plrosevm\",\n"
            + "                \"text\": \"false\"\n"
            + "              },\n"
            + "              {\n"
            + "                \"id\": \"wygorhwb\",\n"
            + "                \"text\": \"null\"\n"
            + "              },\n"
            + "              {\n"
            + "                \"id\": \"mqpiyjyp\",\n"
            + "                \"text\": \"JSON String\\n\"\n"
            + "              },\n"
            + "              {\n"
            + "                \"id\": \"avsznway\",\n"
            + "                \"text\": \"JSON Number\\n\"\n"
            + "              }\n"
            + "            ]\n"
            + "          },\n"
            + "          {\n"
            + "            \"id\": \"yvmcrzdx\",\n"
            + "            \"text\": \"JSON Object\\n\"\n"
            + "          },\n"
            + "          {\n"
            + "            \"id\": \"qosjgocm\",\n"
            + "            \"text\": \"JSON Array\\n\"\n"
            + "          }\n"
            + "        ]\n"
            + "      }\n"
            + "    ]\n"
            + "  },\n"
            + "  \"id\": \"ldhwcvrb\"\n"
            + "}";
        JsonObject jsonObject = new JsonReader(new StringReader(str)).readObject();
    }
}
