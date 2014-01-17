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

import java.math.BigDecimal;
import java.util.Objects;

/**
 * JSON数值类型。
 *
 * @author Azige
 */
public class JsonNumber extends JsonType{

    private final BigDecimal value;

    /**
     * 将一个整型数值包装为JSON类型对象。
     *
     * @param value 要包装的整型数值
     * @return 表示此整型数值的JSON类型对象
     */
    public static JsonNumber valueOf(long value){
        return new JsonNumber(value);
    }

    /**
     * 将一个浮点型数值包装为JSON类型对象。
     *
     * @param value 要包装的浮点型数值
     * @return 表示此浮点型数值的JSON类型对象
     */
    public static JsonNumber valueOf(double value){
        return new JsonNumber(value);
    }

    /**
     * 将一个{@link Number}对象包装为JSON类型对象。
     * 如果此对象的类型为{@link Float}、{@link Double}或{@link BigDecimal}，则被作为浮点型处理，否则作为整型。
     *
     * @param value 要包装的对象
     * @return 表示对应数值的JSON类型对象
     * @throws NullPointerException 如果{@code value}为空
     */
    public static JsonNumber valueOf(Number value){
        Class<?> cls = value.getClass();
        if (cls == Float.class || cls == Double.class || cls == BigDecimal.class){
            return valueOf(value.doubleValue());
        }else{
            return valueOf(value.longValue());
        }
    }

    /**
     * 以整型数值构造一个对象。
     * @param value 此对象表示的整型数值
     */
    public JsonNumber(long value){
        this.value = BigDecimal.valueOf(value);
    }

    /**
     * 以浮点型数值构造一个对象。
     * @param value 此对象表示的浮点型数值
     */
    public JsonNumber(double value){
        this.value = BigDecimal.valueOf(value);
    }

    /**
     * 获得此对象的{@code int}型数值表示形式。
     * @return 此对象的{@code int}型数值
     */
    public int getIntValue(){
        return value.intValue();
    }

    /**
     * 获得此对象的{@code long}型数值表示形式。
     * @return 此对象的{@code long}型数值
     */
    public long getLongValue(){
        return value.longValue();
    }

    /**
     * 获得此对象的{@code float}型数值表示形式。
     * @return 此对象的{@code float}型数值
     */
    public float getFloatValue(){
        return value.floatValue();
    }

    /**
     * 获得此对象的{@code double}型数值表示形式。
     * @return 此对象的{@code double}型数值
     */
    public double getDoubleValue(){
        return value.doubleValue();
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
        return value.toString();
    }
}
