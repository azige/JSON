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

import static javax.management.Query.value;

import java.io.Writer;
import java.util.Map.Entry;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import javax.json.JsonWriter;
import javax.json.stream.JsonGenerator;

/**
 * 用于输出JSON类型对象的字符流。<br>
 * 尽管它名字有Writer，但它并不是{@link Writer}的子类，它只关心如何输出JSON类型对象的文本。
 *
 * @author Azige
 */
public class JsonWriterImpl implements JsonWriter{

    private final JsonGenerator generator;
    private boolean closed;

    /**
     * 包装一个字符输出流以构造对象。
     *
     * @param out 要输出的流
     */
    public JsonWriterImpl(Writer out){
        this(out, false);
    }

    public JsonWriterImpl(Writer out, boolean prettyPrint){
        this.generator = new JsonGeneratorImpl(out, prettyPrint);
    }

    @Override
    public void writeArray(JsonArray array){
        if (closed){
            throw new IllegalStateException();
        }
        closed = true;

        generator.writeStartArray();
        for (JsonValue value : array){
            generator.write(value);
        }
        generator.writeEnd();
    }

    @Override
    public void writeObject(JsonObject object){
        if (closed){
            throw new IllegalStateException();
        }
        closed = true;

        generator.writeStartObject();
        for (Entry<String, JsonValue> e : object.entrySet()){
            generator.write(e.getKey(), e.getValue());
        }
        generator.writeEnd();
    }

    @Override
    public void write(JsonStructure value){
        if (value.getValueType() == ValueType.OBJECT){
            writeObject((JsonObject)value);
        }else{
            writeArray((JsonArray)value);
        }
    }

    @Override
    public void close(){
        closed = true;
        generator.close();
    }
}
