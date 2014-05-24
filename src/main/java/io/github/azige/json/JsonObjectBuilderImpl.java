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
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

/**
 *
 * @author Azige
 */
public class JsonObjectBuilderImpl implements JsonObjectBuilder{

    Map<String, JsonValue> map = new LinkedHashMap<>();

    @Override
    public JsonObjectBuilder add(String name, JsonValue value){
        map.put(name, value);
        return this;
    }

    @Override
    public JsonObjectBuilder add(String name, String value){
        return add(name, new JsonStringImpl(value));
    }

    @Override
    public JsonObjectBuilder add(String name, BigInteger value){
        return add(name, new JsonNumberImpl(value));
    }

    @Override
    public JsonObjectBuilder add(String name, BigDecimal value){
        return add(name, new JsonNumberImpl(value));
    }

    @Override
    public JsonObjectBuilder add(String name, int value){
        return add(name, new JsonNumberImpl(value));
    }

    @Override
    public JsonObjectBuilder add(String name, long value){
        return add(name, new JsonNumberImpl(value));
    }

    @Override
    public JsonObjectBuilder add(String name, double value){
        return add(name, new JsonNumberImpl(value));
    }

    @Override
    public JsonObjectBuilder add(String name, boolean value){
        return add(name, value?JsonValue.TRUE:JsonValue.FALSE);
    }

    @Override
    public JsonObjectBuilder addNull(String name){
        return add(name, JsonValue.NULL);
    }

    @Override
    public JsonObjectBuilder add(String name, JsonObjectBuilder builder){
        return add(name, builder.build());
    }

    @Override
    public JsonObjectBuilder add(String name, JsonArrayBuilder builder){
        return add(name, builder.build());
    }

    @Override
    public JsonObject build(){
        return new JsonObjectImpl(map);
    }

}
