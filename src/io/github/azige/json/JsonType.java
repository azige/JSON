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
 * 所有JSON类型的超类。<br>
 * 此类定义使用{@link #toString()}方法来转换对象为JSON文本，
 * 此外提供了将普通的Java对象包装为JSON类型对象的方法。
 *
 * @author Azige
 */
public abstract class JsonType{


    /**
     * 将普通的Java类型的对象包装为JSON类型的对象。{@code null}值会被转换为{@link JsonValueType#NULL}。<br>
     * 各种类型的转换关系如下。
     * <ul>
     * <li>Boolean - JsonBoolean</li>
     * <li>Number - JsonNumber</li>
     * <li>String - JsonString</li>
     * <li>非基本类型数组 - JsonArray</li>
     * <li>其他类型 - JsonObject</li>
     * </ul>
     *
     * @param obj 要包装的对象
     * @return 包装过的JSON类型的对象，如果{@code obj}是{@code JsonType}类型则返回它本身
     * @throws IllegalArgumentException 如果{@code obj}为基本类型数组
     * @see JsonBoolean#valueOf(boolean)
     * @see JsonNumber#valueOf(Number)
     * @see JsonString#valueOf(String)
     * @see JsonArray#valueOf(Object[])
     * @see JsonObject#valueOf(Object)
     */
    public static JsonType valueOf(Object obj){
        if (obj == null){
            return JsonValueType.NULL;
        }else if (obj instanceof JsonType){
            return (JsonType)obj;
        }else if (obj instanceof Boolean){
            return JsonBoolean.valueOf(((Boolean)obj).booleanValue());
        }else if (obj instanceof Number){
            return JsonNumber.valueOf(((Number)obj));
        }else if (obj instanceof String){
            return JsonString.valueOf((String)obj);
        }else if (obj.getClass().isArray()){
            if (obj.getClass().getComponentType().isPrimitive()){
                throw new IllegalArgumentException("Primitive type array are not accepted.");
            }
            return JsonArray.valueOf((Object[])obj);
        }else{
            return JsonObject.valueOf(obj);
        }
    }

    /**
     * 返回此对象的JSON文本表示形式。
     *
     * @return 此对象的JSON文本
     */
    @Override
    public abstract String toString();
}
