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
import java.util.Map;

import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;

/**
 *
 * @author Azige
 */
public class JsonGeneratorFactoryImpl implements JsonGeneratorFactory{

    private final Map<String, ?> config;
    private final boolean prettyPrint;

    public JsonGeneratorFactoryImpl(){
        this(Collections.<String, Object>emptyMap());
    }

    public JsonGeneratorFactoryImpl(Map<String, ?> config){
        this.config = Collections.unmodifiableMap(config);
        prettyPrint = Boolean.TRUE.equals(config.get(JsonGenerator.PRETTY_PRINTING));
    }

    @Override
    public JsonGenerator createGenerator(Writer writer){
        return new JsonGeneratorImpl(writer, prettyPrint);
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out){
        return createGenerator(out, Charset.forName(Constant.DEFAULT_CHARSET));
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out, Charset charset){
        return createGenerator(new OutputStreamWriter(out, charset));
    }

    @Override
    public Map<String, ?> getConfigInUse(){
        return config;
    }

}
