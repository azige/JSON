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
import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

/**
 *
 * @author Azige
 */
public class JsonArrayBuilderImpl implements JsonArrayBuilder{

    List<JsonValue> list = new ArrayList<>();

    @Override
    public JsonArrayBuilder add(JsonValue value){
        list.add(value);
        return this;
    }

    @Override
    public JsonArrayBuilder add(String value){
        return add(new JsonStringImpl(value));
    }

    @Override
    public JsonArrayBuilder add(BigDecimal value){
        return add(new JsonNumberImpl(value));
    }

    @Override
    public JsonArrayBuilder add(BigInteger value){
        return add(new JsonNumberImpl(value));
    }

    @Override
    public JsonArrayBuilder add(int value){
        return add(new JsonNumberImpl(value));
    }

    @Override
    public JsonArrayBuilder add(long value){
        return add(new JsonNumberImpl(value));
    }

    @Override
    public JsonArrayBuilder add(double value){
        return add(new JsonNumberImpl(value));
    }

    @Override
    public JsonArrayBuilder add(boolean value){
        return add(value ? JsonValue.TRUE : JsonValue.FALSE);
    }

    @Override
    public JsonArrayBuilder addNull(){
        return add(JsonValue.NULL);
    }

    @Override
    public JsonArrayBuilder add(JsonObjectBuilder builder){
        return add(builder.build());
    }

    @Override
    public JsonArrayBuilder add(JsonArrayBuilder builder){
        return add(builder.build());
    }

    @Override
    public JsonArray build(){
        return new JsonArrayImpl(list);
    }
}
