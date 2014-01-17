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

/**
 * JSON布尔类型。
 *
 * @author Azige
 */
public class JsonBoolean extends JsonType{

    /**
     * 表示{@code true}的对象。
     */
    public static final JsonBoolean TRUE = new JsonBoolean(true);
    /**
     * 表示{@code false}的对象。
     */
    public static final JsonBoolean FALSE = new JsonBoolean(false);
    private final boolean value;

    /**
     * 将一个布尔值包装为JSON类型对象。
     * @param value 要包装的布尔值
     * @return 表示此布尔值的JSON类型对象
     */
    public static JsonBoolean valueOf(boolean value){
        return value ? TRUE : FALSE;
    }

    /**
     * 以一个布尔值构造一个对象。
     * @param value 此对象表示的布尔值
     */
    public JsonBoolean(boolean value){
        this.value = value;
    }

    /**
     * 获得此对象表示的布尔值。
     * @return 此对象表示的布尔值
     */
    public boolean getValue(){
        return value;
    }

    /**
     * “标准化”此对象为{@link #TRUE}或{@link #FALSE}。
     * @return 此对象的“标准化”对象
     */
    public JsonBoolean normalize(){
        return valueOf(value);
    }

    @Override
    public int hashCode(){
        return value ? 1 : 0;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null){
            return false;
        }
        if (getClass() != obj.getClass()){
            return false;
        }
        final JsonBoolean other = (JsonBoolean)obj;
        if (this.value != other.value){
            return false;
        }
        return true;
    }

    @Override
    public String toString(){
        return value ? "true" : "false";
    }
}
