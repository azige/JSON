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
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.spi.JsonProvider;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;

/**
 *
 * @author Azige
 */
public class JsonProviderImpl extends JsonProvider{

    private static final JsonParserFactory parserFactory = new JsonParserFactoryImpl();
    private static final JsonGeneratorFactory generatorFactory = new JsonGeneratorFactoryImpl();
    private static final JsonReaderFactory readerFactory = new JsonReaderFactoryImpl();
    private static final JsonWriterFactory writerFactory = new JsonWriterFactoryImpl();
    private static final JsonBuilderFactory builderFactory = new JsonBuilderFactoryImpl();

    @Override
    public JsonParser createParser(Reader reader){
        return parserFactory.createParser(reader);
    }

    @Override
    public JsonParser createParser(InputStream in){
        return parserFactory.createParser(in);
    }

    @Override
    public JsonParserFactory createParserFactory(Map<String, ?> config){
        return new JsonParserFactoryImpl(config);
    }

    @Override
    public JsonGenerator createGenerator(Writer writer){
        return generatorFactory.createGenerator(writer);
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out){
        return generatorFactory.createGenerator(out);
    }

    @Override
    public JsonGeneratorFactory createGeneratorFactory(Map<String, ?> config){
        return new JsonGeneratorFactoryImpl(config);
    }

    @Override
    public JsonReader createReader(Reader reader){
        return readerFactory.createReader(reader);
    }

    @Override
    public JsonReader createReader(InputStream in){
        return readerFactory.createReader(in);
    }

    @Override
    public JsonWriter createWriter(Writer writer){
        return writerFactory.createWriter(writer);
    }

    @Override
    public JsonWriter createWriter(OutputStream out){
        return writerFactory.createWriter(out);
    }

    @Override
    public JsonWriterFactory createWriterFactory(Map<String, ?> config){
        return new JsonWriterFactoryImpl(config);
    }

    @Override
    public JsonReaderFactory createReaderFactory(Map<String, ?> config){
        return new JsonReaderFactoryImpl(config);
    }

    @Override
    public JsonObjectBuilder createObjectBuilder(){
        return builderFactory.createObjectBuilder();
    }

    @Override
    public JsonArrayBuilder createArrayBuilder(){
        return builderFactory.createArrayBuilder();
    }

    @Override
    public JsonBuilderFactory createBuilderFactory(Map<String, ?> config){
        return new JsonBuilderFactoryImpl(config);
    }

}
