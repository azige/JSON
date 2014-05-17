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

import org.junit.*;
import static org.junit.Assert.*;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Azige
 */
public class JsonObjectTest{

    @BeforeClass
    public static void setUpClass(){
        Logger logger = Logger.getLogger("io.github.azige.json");
        ConsoleHandler handler = new ConsoleHandler();
        logger.setLevel(Level.ALL);
        handler.setLevel(Level.ALL);
        logger.addHandler(handler);
    }

    @AfterClass
    public static void tearDownClass(){
    }

    public JsonObjectTest(){
    }

    @Before
    public void setUp(){
    }

    @After
    public void tearDown(){
    }

    @Test
    public void testSomething(){
        System.out.println("testSomething");
        JsonObject obj = new JsonObject();
        obj.put("name", "bob");
        obj.put("age", 20);
        obj.put("asset", new String[]{"PC", "phone", "TV"});
        System.out.println(obj);
    }

    @Test
    public void testWrapBean(){
        System.out.println("testWrapBean");
        Bean bean = new Bean("asd", 12);
        JsonObject jo = JsonObject.valueOf(bean);
        System.out.println(jo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrapBeanException(){
        System.out.println("testWrapBean");
        Bean bean = new Bean("asd", 12);
        bean.setO(bean);
        JsonObject jo = JsonObject.valueOf(bean);
    }

    class Bean{

        String name;
        int age;
        Object o;

        public Bean(){
        }

        public Bean(String name, int age){
            this.name = name;
            this.age = age;
        }

        public String getName(){
            return name;
        }

        public void setName(String name){
            this.name = name;
        }

        public int getAge(){
            return age;
        }

        public void setAge(int age){
            this.age = age;
        }

        public Object getO(){
            return o;
        }

        public void setO(Object o){
            this.o = o;
        }
    }
}
