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

import java.io.IOException;
import java.io.Writer;

/**
 * 用于输出JSON类型对象的字符流。<br>
 * 尽管它名字有Writer，但它并不是{@link Writer}的子类，它只关心如何输出JSON类型对象的文本。
 * @author Azige
 */
public class JsonWriter implements AutoCloseable{

    private final Writer out;

    /**
     * 包装一个字符输出流以构造对象。
     * @param out 要输出的流
     */
    public JsonWriter(Writer out){
        this.out = out;
    }

    /**
     * 向字符流写入一个JSON对象。
     * @param value 要写入的对象
     * @throws JsonException 如果底层流发生IO异常
     */
    public void write(JsonType value){
        try{
            out.write(value.toString());
        }catch (IOException ex){
            throw new JsonException(ex);
        }
    }

    /**
     * 刷新底层流的缓存。
     * @throws JsonException 如果底层流发生IO异常
     */
    public void flush(){
        try{
            out.flush();
        }catch (IOException ex){
            throw new JsonException(ex);
        }
    }

    /**
     * 关闭底层流。
     * @throws JsonException 如果底层流发生IO异常
     */
    @Override
    public void close(){
        try{
            out.close();
        }catch (IOException ex){
            throw new JsonException(ex);
        }
    }
}
