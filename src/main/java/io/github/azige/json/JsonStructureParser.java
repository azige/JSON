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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import javax.json.stream.JsonLocation;
import javax.json.stream.JsonParser;

/**
 *
 * @author Azige
 */
public class JsonStructureParser implements JsonParser{

    private Iterator<Token> iterator;
    private Event event;
    private Object value;

    static class Token{

        final Event event;
        final Object value;

        public Token(Event event, Object value){
            this.event = event;
            this.value = value;
        }
    }

    public JsonStructureParser(JsonStructure structure){
        if (structure.getValueType() == ValueType.OBJECT){
            init((JsonObject)structure);
        }else{
            init((JsonArray)structure);
        }
    }

    public JsonStructureParser(JsonArray array){
        init(array);
    }

    public JsonStructureParser(JsonObject object){
        init(object);
    }

    private void init(JsonArray array){
        List<Token> list = new ArrayList<>();
        list.add(new Token(Event.START_ARRAY, null));
        for (JsonValue v : array){
            if (v instanceof JsonStructure){
                JsonStructureParser parser = new JsonStructureParser((JsonStructure)v);
                Iterator<Token> it = parser.iterator;
                while (it.hasNext()){
                    list.add(it.next());
                }
            }else{
                list.add(new Token(toEvent(v), v));
            }
        }
        list.add(new Token(Event.END_ARRAY, null));
        iterator = list.iterator();
    }

    private void init(JsonObject object){
        List<Token> list = new ArrayList<>();
        list.add(new Token(Event.START_OBJECT, null));
        for (Entry<String, JsonValue> e : object.entrySet()){
            list.add(new Token(Event.KEY_NAME, e.getKey()));
            if (e.getValue() instanceof JsonStructure){
                JsonStructureParser parser = new JsonStructureParser((JsonStructure)e.getValue());
                Iterator<Token> it = parser.iterator;
                while (it.hasNext()){
                    list.add(it.next());
                }
            }else{
                list.add(new Token(toEvent(e.getValue()), e.getValue()));
            }
        }
        list.add(new Token(Event.END_OBJECT, null));
        iterator = list.iterator();
    }

    private static Event toEvent(JsonValue value){
        switch (value.getValueType()){
            case STRING:
                return Event.VALUE_STRING;
            case NUMBER:
                return Event.VALUE_NUMBER;
            case TRUE:
                return Event.VALUE_TRUE;
            case FALSE:
                return Event.VALUE_FALSE;
            case NULL:
                return Event.VALUE_NULL;
            default:
                assert false;
                return null;
        }
    }

    @Override
    public boolean hasNext(){
        return iterator.hasNext();
    }

    @Override
    public Event next(){
        Token token = iterator.next();
        event = token.event;
        value = token.value;
        return event;
    }

    @Override
    public String getString(){
        if (event == Event.VALUE_STRING){
            return ((JsonString)value).getString();
        }else if (event == Event.VALUE_NUMBER || event == Event.KEY_NAME){
            return value.toString();
        }else{
            throw new IllegalStateException();
        }
    }

    @Override
    public boolean isIntegralNumber(){
        if (event == Event.VALUE_NUMBER){
            return ((JsonNumber)value).isIntegral();
        }else{
            throw new IllegalStateException();
        }
    }

    @Override
    public int getInt(){
        if (event == Event.VALUE_NUMBER){
            return ((JsonNumber)value).intValue();
        }else{
            throw new IllegalStateException();
        }
    }

    @Override
    public long getLong(){
        if (event == Event.VALUE_NUMBER){
            return ((JsonNumber)value).longValue();
        }else{
            throw new IllegalStateException();
        }
    }

    @Override
    public BigDecimal getBigDecimal(){
        if (event == Event.VALUE_NUMBER){
            return ((JsonNumber)value).bigDecimalValue();
        }else{
            throw new IllegalStateException();
        }
    }

    @Override
    public JsonLocation getLocation(){
        return new JsonLocation(){

            @Override
            public long getLineNumber(){
                return -1;
            }

            @Override
            public long getColumnNumber(){
                return -1;
            }

            @Override
            public long getStreamOffset(){
                return -1;
            }
        };
    }

    @Override
    public void close(){
    }

}
