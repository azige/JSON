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

import java.io.Reader;

import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import javax.json.stream.JsonParsingException;

/**
 * 用于读入JSON类型对象的字符流。<br>
 * 尽管它名字有Writer，但它并不是{@link Reader}的子类，它只关心如何将文本解析为JSON类型对象。
 *
 * @author Azige
 */
public class JsonReaderImpl implements JsonReader{

    private final JsonParser parser;
    private boolean closed = false;

    /**
     * 包装一个字符输入流以构造对象。
     *
     * @param in 要读入的流
     */
    public JsonReaderImpl(Reader in){
        parser = new JsonParserImpl(in);
    }

    @Override
    public JsonStructure read(){
        if (closed){
            throw new IllegalStateException();
        }
        closed = true;

        JsonStructure structure;
        Event event = parser.next();
        if (event == Event.START_OBJECT){
            structure = buildObject();
        }else if (event == Event.START_ARRAY){
            structure = buildArray();
        }else{
            throw new JsonParsingException("No structure type available.", parser.getLocation());
        }
        return structure;
    }

    @Override
    public JsonObject readObject(){
        if (closed){
            throw new IllegalStateException();
        }
        closed = true;

        JsonObject object;
        Event event = parser.next();
        if (event == Event.START_OBJECT){
            object = buildObject();
        }else{
            throw new JsonParsingException("No object available.", parser.getLocation());
        }
        return object;
    }

    @Override
    public JsonArray readArray(){
        if (closed){
            throw new IllegalStateException();
        }
        closed = true;

        JsonArray array;
        Event event = parser.next();
        if (event == Event.START_OBJECT){
            array = buildArray();
        }else{
            throw new JsonParsingException("No object available.", parser.getLocation());
        }
        return array;
    }

    @Override
    public void close(){
        closed = true;

        parser.close();
    }

    private JsonObject buildObject(){
        JsonObjectBuilder builder = new JsonObjectBuilderImpl();
        String key = null;
        while (true){
            Event event = parser.next();
            if (event == Event.KEY_NAME){
                key = parser.getString();
            }else if (event == Event.VALUE_STRING){
                builder.add(key, parser.getString());
            }else if (event == Event.VALUE_NUMBER){
                builder.add(key, parser.getBigDecimal());
            }else if (event == Event.VALUE_TRUE){
                builder.add(key, JsonValue.TRUE);
            }else if (event == Event.VALUE_FALSE){
                builder.add(key, JsonValue.FALSE);
            }else if (event == Event.VALUE_NULL){
                builder.addNull(key);
            }else if (event == Event.START_OBJECT){
                builder.add(key, buildObject());
            }else if (event == Event.START_ARRAY){
                builder.add(key, buildArray());
            }else if (event == Event.END_OBJECT){
                return builder.build();
            }else{
                assert false;
            }
        }
    }

    private JsonArray buildArray(){
        JsonArrayBuilder builder = new JsonArrayBuilderImpl();
        String key = null;
        while (true){
            Event event = parser.next();
            if (event == Event.VALUE_STRING){
                builder.add(parser.getString());
            }else if (event == Event.VALUE_NUMBER){
                builder.add(parser.getBigDecimal());
            }else if (event == Event.VALUE_TRUE){
                builder.add(JsonValue.TRUE);
            }else if (event == Event.VALUE_FALSE){
                builder.add(JsonValue.FALSE);
            }else if (event == Event.VALUE_NULL){
                builder.addNull();
            }else if (event == Event.START_OBJECT){
                builder.add(buildObject());
            }else if (event == Event.START_ARRAY){
                builder.add(buildArray());
            }else if (event == Event.END_ARRAY){
                return builder.build();
            }else{
                assert false;
            }
        }
    }
}
