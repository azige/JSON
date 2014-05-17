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

import java.io.StringReader;
import java.util.Objects;

/**
 * JSON数值类型。
 *
 * @author Azige
 */
public class JsonNumber extends JsonType{

    private final Number value;
    private final String jsonText;

    /**
     * 将一个字符串包装为JSON数值类型对象。
     *
     * @param value 要包装的字符串
     * @return 表示此字符串所表示的数值的JSON类型对象
     * @throws NumberFormatException 如果字符串的格式不正确
     */
    public static JsonNumber valueOf(String value){
        return new JsonReader(new StringReader(value)).readNumber();
    }

    /**
     * 将一个{@link Number}对象包装为JSON类型对象。
     *
     * @param value 要包装的对象
     * @return 表示对应数值的JSON类型对象
     * @throws NullPointerException 如果{@code value}为空
     */
    public static JsonNumber valueOf(Number value){
        return new JsonNumber(value);
    }

    /**
     * 以整型数值构造一个对象。
     *
     * @param value 此对象表示的整型数值
     */
    public JsonNumber(long value){
        this(Long.valueOf(value));
    }

    /**
     * 以浮点型数值构造一个对象。
     *
     * @param value 此对象表示的浮点型数值
     */
    public JsonNumber(double value){
        this(Double.valueOf(value));
    }

    /**
     * 以{@code Number}对象构造一个对象。
     *
     * @param value 此对象表示的数值
     * @throws NullPointerException 如果{@code value}为空
     */
    public JsonNumber(Number value){
        // JSON数值的表示形式与Java中定义的数值的表示形式是一致的
        this(value, value.toString());
    }

    /**
     * 用指定的value与jsonText构造一个对象。
     */
    JsonNumber(Number value, String jsonText) throws NullPointerException{
        if (value == null || jsonText == null){
            throw new NullPointerException();
        }
        this.value = value;
        this.jsonText = jsonText;
    }

    /**
     * 获得此对象的{@code int}型数值表示形式。
     *
     * @return 此对象的{@code int}型数值
     */
    public int getIntValue(){
        return value.intValue();
    }

    /**
     * 获得此对象的{@code long}型数值表示形式。
     *
     * @return 此对象的{@code long}型数值
     */
    public long getLongValue(){
        return value.longValue();
    }

    /**
     * 获得此对象的{@code float}型数值表示形式。
     *
     * @return 此对象的{@code float}型数值
     */
    public float getFloatValue(){
        return value.floatValue();
    }

    /**
     * 获得此对象的{@code double}型数值表示形式。
     *
     * @return 此对象的{@code double}型数值
     */
    public double getDoubleValue(){
        return value.doubleValue();
    }

    /**
     * 获得此对象表示的数值。
     *
     * @return 此对象表示的数值
     */
    public Number getValue(){
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
        final JsonNumber other = (JsonNumber)obj;
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
