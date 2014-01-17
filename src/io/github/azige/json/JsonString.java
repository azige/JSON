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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Objects;

/**
 * JSON字符串类型。
 * @author Azige
 */
public class JsonString extends JsonType{

    private final String value;
    private final String jsonText;

    /**
     * 将一个字符串包装为JSON类型对象。
     * @param value 要包装的字符串
     * @return 表示对应的字符串的JSON类型对象
     * @throws NullPointerException 如果{@code value}为空
     */
    public static JsonString valueOf(String value){
        return new JsonString(value);
    }

    static String convertToJsonText(String string){
        StringBuilder sb = new StringBuilder();
        sb.append('\"');
        try (BufferedReader reader = new BufferedReader(new StringReader(string))){
            for (int i; (i = reader.read()) != -1;){
                char c = (char)i;
                switch (c){
                    case '\"':
                        sb.append("\\\"");
                        break;
                    case '\\':
                        sb.append("\\\\");
                        break;
                    case '\b':
                        sb.append("\\b");
                        break;
                    case '\f':
                        sb.append("\\f");
                        break;
                    case '\n':
                        sb.append("\\n");
                        break;
                    case '\r':
                        sb.append("\\r");
                        break;
                    case '\t':
                        sb.append("\\t");
                        break;
                    default:
                        sb.append(c);
                }
            }
        }catch (IOException ex){
            assert false;
        }
        sb.append('\"');
        return sb.toString();
    }

    static String convertToString(String jsonText){
        // TODO: implement it.
        throw new UnsupportedOperationException();
    }

    /**
     * 以一个字符串构造一个对象。
     * @param value 此对象所表示的字符串
     * @throws NullPointerException 如果{@code value}为空
     */
    public JsonString(String value){
        if (value == null){
            throw new NullPointerException();
        }
        this.value = value;
        this.jsonText = convertToJsonText(value);
    }

    /**
     * 获得此对象所表示的字符串。
     * @return 此对象所表示的字符串
     */
    public String getValue(){
        return value;
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(this.value);
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null){
            return false;
        }
        if (getClass() != obj.getClass()){
            return false;
        }
        final JsonString other = (JsonString)obj;
        if (!Objects.equals(this.value, other.value)){
            return false;
        }
        return true;
    }

    @Override
    public String toString(){
        return jsonText;
    }
}
