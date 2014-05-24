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

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;

/**
 *
 * @author Azige
 */
public class JsonWriterFactoryImpl implements JsonWriterFactory{

    private final Map<String, ?> config;

    public JsonWriterFactoryImpl(){
        this(Collections.<String, Object>emptyMap());
    }

    public JsonWriterFactoryImpl(Map<String, ?> config){
        this.config = Collections.unmodifiableMap(new HashMap<>(config));
    }

    @Override
    public JsonWriter createWriter(Writer writer){
        return new JsonWriterImpl(writer);
    }

    @Override
    public JsonWriter createWriter(OutputStream out){
        return createWriter(out, Charset.forName(Constant.DEFAULT_CHARSET));
    }

    @Override
    public JsonWriter createWriter(OutputStream out, Charset charset){
        return createWriter(new OutputStreamWriter(out, charset));
    }

    @Override
    public Map<String, ?> getConfigInUse(){
        return config;
    }

}
