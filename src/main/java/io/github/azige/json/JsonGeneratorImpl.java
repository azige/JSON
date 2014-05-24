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

import static io.github.azige.json.Constant.*;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map.Entry;

import javax.json.JsonArray;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import javax.json.stream.JsonGenerationException;
import javax.json.stream.JsonGenerator;

/**
 *
 * @author Azige
 */
public class JsonGeneratorImpl implements JsonGenerator{

    private static final String INDENT_STRING = "    ";
    private final Writer out;
    private final Deque<Context> contextStack = new LinkedList<>();
    private final boolean prettyPrint;
    private boolean started = false;

    enum ContextType{

        OBJECT(OBJECT_PAIR_SEPARATOR, OBJECT_END),
        ARRAY(ARRAY_VALUE_SEPARATOR, ARRAY_END);

        private final char separator;
        private final char end;

        private ContextType(char separator, char end){
            this.separator = separator;
            this.end = end;
        }
    }

    static class Context{

        ContextType type;
        private boolean separatorFlag = false;

        public Context(ContextType type){
            this.type = type;
        }

        static Context newObject(){
            return new Context(ContextType.OBJECT);
        }

        static Context newArray(){
            return new Context(ContextType.ARRAY);
        }
    }

    public JsonGeneratorImpl(Writer out){
        this(out, false);
    }

    public JsonGeneratorImpl(Writer out, boolean prettyPrint){
        this.out = out;
        this.prettyPrint = prettyPrint;
    }

    private void checkStructureStartContext(){
        if (contextStack.isEmpty()){
            if (started){
                throw new JsonGenerationException("Can not start a structure in no context more than once.");
            }
        }else{
            checkArrayContext();
        }
    }

    private void checkObjectContext(){
        if (contextStack.peek() == null || contextStack.peek().type != ContextType.OBJECT){
            throw new JsonGenerationException("Can not write a pair outside a object context.");
        }
    }

    private void checkArrayContext(){
        if (contextStack.peek() == null || contextStack.peek().type != ContextType.ARRAY){
            throw new JsonGenerationException("Can not write a single value outside a array context.");
        }
    }

    private void printIntent() throws IOException{
        for (int i = 0; i < contextStack.size(); i++){
            out.append(INDENT_STRING);
        }
    }

    private void printSeparator() throws IOException{
        if (contextStack.isEmpty()){
            return;
        }
        if (contextStack.peek().separatorFlag){
            out.append(contextStack.peek().type.separator);
            if (prettyPrint){
                out.append('\n');
            }
        }else{
            contextStack.peek().separatorFlag = true;
        }
    }

    @Override
    public JsonGenerator writeStartObject(){
        checkStructureStartContext();

        try{
            printSeparator();
            if (prettyPrint){
                printIntent();
                out.append(OBJECT_START)
                    .append('\n');
            }else{
                out.append(OBJECT_START);
            }
        }catch (IOException ex){
            throw new JsonException(ex.getLocalizedMessage(), ex);
        }

        contextStack.push(Context.newObject());
        started = true;
        return this;
    }

    @Override
    public JsonGenerator writeStartObject(String name){
        checkObjectContext();

        try{
            printSeparator();
            if (prettyPrint){
                printIntent();
                out.append(new JsonStringImpl(name).toString())
                    .append(OBJECT_KEY_VALUE_SEPARATOR)
                    .append(' ')
                    .append(OBJECT_START)
                    .append('\n');
            }else{
                out.append(new JsonStringImpl(name).toString())
                    .append(OBJECT_KEY_VALUE_SEPARATOR)
                    .append(OBJECT_START);
            }
        }catch (IOException ex){
            throw new JsonException(ex.getLocalizedMessage(), ex);
        }

        contextStack.push(Context.newObject());
        return this;
    }

    @Override
    public JsonGenerator writeStartArray(){
        checkStructureStartContext();

        try{
            printSeparator();
            if (prettyPrint){
                printIntent();
                out.append(ARRAY_START)
                    .append('\n');
            }else{
                out.append(ARRAY_START);
            }
        }catch (IOException ex){
            throw new JsonException(ex.getLocalizedMessage(), ex);
        }

        contextStack.push(Context.newArray());
        started = true;
        return this;
    }

    @Override
    public JsonGenerator writeStartArray(String name){
        checkObjectContext();

        try{
            printSeparator();
            if (prettyPrint){
                printIntent();
                out.append(new JsonStringImpl(name).toString())
                    .append(OBJECT_KEY_VALUE_SEPARATOR)
                    .append(' ')
                    .append(ARRAY_START)
                    .append('\n');
            }else{
                out.append(new JsonStringImpl(name).toString())
                    .append(OBJECT_KEY_VALUE_SEPARATOR)
                    .append(ARRAY_START);
            }
        }catch (IOException ex){
            throw new JsonException(ex.getLocalizedMessage(), ex);
        }

        contextStack.push(Context.newArray());
        return this;
    }

    @Override
    public JsonGenerator write(String name, JsonValue value){
        checkObjectContext();

        try{
            printSeparator();
            if (prettyPrint){
                printIntent();
                out.append(new JsonStringImpl(name).toString())
                    .append(OBJECT_KEY_VALUE_SEPARATOR)
                    .append(' ');
            }else{
                out.append(new JsonStringImpl(name).toString())
                    .append(OBJECT_KEY_VALUE_SEPARATOR);
            }
            if (value instanceof JsonStructure){
                if (value.getValueType() == ValueType.OBJECT){
                    writeStartObject(name);
                    for (Entry<String, JsonValue> e : ((JsonObject)value).entrySet()){
                        write(e.getKey(), e.getValue());
                    }
                    writeEnd();
                }else{ // Must be JSON array
                    writeStartArray(name);
                    for (JsonValue v : ((JsonArray)value)){
                        write(v);
                    }
                    writeEnd();
                }
            }else{
                out.append(value.toString());
            }
        }catch (IOException ex){
            throw new JsonGenerationException(ex.getLocalizedMessage(), ex);
        }

        return this;
    }

    @Override
    public JsonGenerator write(String name, String value){
        return write(name, new JsonStringImpl(value));
    }

    @Override
    public JsonGenerator write(String name, BigInteger value){
        return write(name, new JsonNumberImpl(value));
    }

    @Override
    public JsonGenerator write(String name, BigDecimal value){
        return write(name, new JsonNumberImpl(value));
    }

    @Override
    public JsonGenerator write(String name, int value){
        return write(name, new JsonNumberImpl(value));
    }

    @Override
    public JsonGenerator write(String name, long value){
        return write(name, new JsonNumberImpl(value));
    }

    @Override
    public JsonGenerator write(String name, double value){
        return write(name, new JsonNumberImpl(value));
    }

    @Override
    public JsonGenerator write(String name, boolean value){
        return write(name, value ? JsonValue.TRUE : JsonValue.FALSE);
    }

    @Override
    public JsonGenerator writeNull(String name){
        return write(name, JsonValue.NULL);
    }

    @Override
    public JsonGenerator writeEnd(){
        if (contextStack.isEmpty()){
            throw new JsonGenerationException("No context to end.");
        }

        char end = contextStack.pop().type.end;
        try{
            if (prettyPrint){
                out.append('\n');
                printIntent();
            }
            out.append(end);
        }catch (IOException ex){
            throw new JsonGenerationException(ex.getLocalizedMessage(), ex);
        }

        return this;
    }

    @Override
    public JsonGenerator write(JsonValue value){
        checkArrayContext();

        try{
            printSeparator();
            if (prettyPrint){
                printIntent();
            }
            if (value instanceof JsonStructure){
                if (value.getValueType() == ValueType.OBJECT){
                    writeStartObject();
                    for (Entry<String, JsonValue> e : ((JsonObject)value).entrySet()){
                        write(e.getKey(), e.getValue());
                    }
                    writeEnd();
                }else{ // Must be JSON array
                    writeStartArray();
                    for (JsonValue v : ((JsonArray)value)){
                        write(v);
                    }
                    writeEnd();
                }
            }else{
                out.append(value.toString());
            }
        }catch (IOException ex){
            throw new JsonGenerationException(ex.getLocalizedMessage(), ex);
        }

        return this;
    }

    @Override
    public JsonGenerator write(String value){
        return write(new JsonStringImpl(value));
    }

    @Override
    public JsonGenerator write(BigDecimal value){
        return write(new JsonNumberImpl(value));
    }

    @Override
    public JsonGenerator write(BigInteger value){
        return write(new JsonNumberImpl(value));
    }

    @Override
    public JsonGenerator write(int value){
        return write(new JsonNumberImpl(value));
    }

    @Override
    public JsonGenerator write(long value){
        return write(new JsonNumberImpl(value));
    }

    @Override
    public JsonGenerator write(double value){
        return write(new JsonNumberImpl(value));
    }

    @Override
    public JsonGenerator write(boolean value){
        return write(value ? JsonValue.TRUE : JsonValue.FALSE);
    }

    @Override
    public JsonGenerator writeNull(){
        return write(JsonValue.NULL);
    }

    @Override
    public void close(){
        try{
            out.close();
        }catch (IOException ex){
            throw new JsonException(ex.getLocalizedMessage(), ex);
        }
    }

    @Override
    public void flush(){
        try{
            out.flush();
        }catch (IOException ex){
            throw new JsonException(ex.getLocalizedMessage(), ex);
        }
    }
}
