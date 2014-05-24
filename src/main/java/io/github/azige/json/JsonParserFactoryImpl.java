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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;

/**
 *
 * @author Azige
 */
public class JsonParserFactoryImpl implements JsonParserFactory{

    private Map<String, ?> config;

    public JsonParserFactoryImpl(){
        this(Collections.<String, Object>emptyMap());
    }

    public JsonParserFactoryImpl(Map<String, ?> config){
        this.config = Collections.unmodifiableMap(new HashMap<>(config));
    }

    @Override
    public JsonParser createParser(Reader reader){
        return new JsonParserImpl(reader);
    }

    @Override
    public JsonParser createParser(InputStream in){
        return createParser(in, Charset.forName(Constant.DEFAULT_CHARSET));
    }

    @Override
    public JsonParser createParser(InputStream in, Charset charset){
        return createParser(new InputStreamReader(in, charset));
    }

    @Override
    public JsonParser createParser(JsonObject obj){
        return createParser(new StringReader(obj.toString()));
    }

    @Override
    public JsonParser createParser(JsonArray array){
        return createParser(new StringReader(array.toString()));
    }

    @Override
    public Map<String, ?> getConfigInUse(){
        return config;
    }

}
