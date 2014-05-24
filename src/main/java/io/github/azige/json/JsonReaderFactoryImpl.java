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
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;

import javax.json.JsonReader;
import javax.json.JsonReaderFactory;

/**
 *
 * @author Azige
 */
public class JsonReaderFactoryImpl implements JsonReaderFactory{

    private final Map<String, ?> config;

    public JsonReaderFactoryImpl(){
        this(Collections.<String, Object>emptyMap());
    }

    public JsonReaderFactoryImpl(Map<String, ?> config){
        this.config = Collections.unmodifiableMap(config);

    }

    @Override
    public JsonReader createReader(Reader reader){
        return new JsonReaderImpl(reader);
    }

    @Override
    public JsonReader createReader(InputStream in){
        return createReader(in, Charset.forName(Constant.DEFAULT_CHARSET));
    }

    @Override
    public JsonReader createReader(InputStream in, Charset charset){
        return createReader(new InputStreamReader(in, charset));
    }

    @Override
    public Map<String, ?> getConfigInUse(){
        return config;
    }

}
