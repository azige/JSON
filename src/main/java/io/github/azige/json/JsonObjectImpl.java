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

import static io.github.azige.json.Constant.*;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;

/**
 * JSON“对象”类型。<br>
 * 此类实现了{@link Map}接口，由一个{@link HashMap}的实例作为存储容器。
 * 此Map<i>不允许</i>空键与空串{@code ""}键，<i>不允许</i>空值，"null"值应使用{@link JsonValueType#NULL}包装。
 * <b>此类的对象<i>不应当</i>包含其本身而造成递归包含</b>，其大部分方法都会检查这种行为并尽可能抛出异常以避免，
 * 但仅限于<b>直接包含</b>而不限于<b>间接包含</b>，使用者应当自行避免这种情况。
 *
 * @author Azige
 */
public class JsonObjectImpl extends JsonType implements JsonObject{

    private static final Logger LOG = Logger.getLogger(JsonObjectImpl.class.getName());

    private final Map<String, JsonValue> map;

//    /**
//     * 将一个JavaBean对象包装为JSON类型对象。JavaBean的属性会成为键，对应的包装过的JSON类型对象成为值。
//     * JavaBean对象不应当含有循环引用。
//     *
//     * @param bean 要包装的JavaBean对象
//     * @return 表示对应的JavaBean对象的JSON类型对象
//     */
//    public static JsonObjectImpl valueOf(Object bean){
//        JsonObjectImpl jo = new JsonObjectImpl();
//        try{
//            BeanInfo info = Introspector.getBeanInfo(bean.getClass(), Object.class);
//            for (PropertyDescriptor prop : info.getPropertyDescriptors()){
//                Method setter = prop.getReadMethod();
//                if (setter != null){
//                    Object value = setter.invoke(bean);
//                    if (value == bean){
//                        throw new IllegalArgumentException("The bean has circularity of reference.");
//                    }
//                    jo.map.put(prop.getName(), JsonType.valueOf(value));
//                }
//            }
//        }catch (IntrospectionException | IllegalAccessException | InvocationTargetException ex){
//            assert false;
//            LOG.log(Level.FINER, "", ex);
//        }
//        return jo;
//    }

    /**
     * 以一个Map构造一个对象，Map中的所有键值对都会被添加到新对象。
     *
     * @param map 要复制的Map
     */
    JsonObjectImpl(Map<String, ? extends JsonValue> map){
        this.map = Collections.unmodifiableMap(new HashMap<>(map));
    }

    @Override
    public String toString(){
//        Iterator<Entry<String, JsonValue>> iter = entrySet().iterator();
//        if (!iter.hasNext()){
//            return "" + OBJECT_START + OBJECT_END;
//        }
//
//        StringBuilder sb = new StringBuilder();
//        sb.append(OBJECT_START);
//        while (true){
//            Entry<String, JsonValue> entry = iter.next();
//            sb.append(JsonStringImpl.valueOf(entry.getKey()).toString())
//                .append(OBJECT_KEY_VALUE_SEPARATOR)
//                .append(entry.getValue().toString());
//            if (iter.hasNext()){
//                sb.append(OBJECT_PAIR_SEPARATOR);
//            }else{
//                break;
//            }
//        }
//        sb.append(OBJECT_END);
//        return sb.toString();
        StringWriter sw = new StringWriter();
        JsonWriterImpl writer = new JsonWriterImpl(sw);
        writer.writeObject(this);
        writer.close();
        return sw.toString();
    }

    @Override
    public int size(){
        return map.size();
    }

    @Override
    public boolean isEmpty(){
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key){
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value){
        return map.containsValue(value);
    }

    @Override
    public JsonValue get(Object key){
        return map.get(key);
    }

    @Override
    public JsonValue put(String key, JsonValue value){
        throw new UnsupportedOperationException();
    }

    public JsonValue put(String key, Object value){
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonValue remove(Object key){
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends String, ? extends JsonValue> m){
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear(){
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> keySet(){
        return map.keySet();
    }

    @Override
    public Collection<JsonValue> values(){
        return map.values();
    }

    @Override
    public Set<Entry<String, JsonValue>> entrySet(){
        return map.entrySet();
    }

    @Override
    public boolean equals(Object o){
        return map.equals(o);
    }

    @Override
    public int hashCode(){
        return map.hashCode();
    }

    @Override
    public ValueType getValueType(){
        return ValueType.OBJECT;
    }

    @Override
    public JsonArray getJsonArray(String name){
        return (JsonArray)map.get(name);
    }

    @Override
    public JsonObject getJsonObject(String name){
        return (JsonObject)map.get(name);
    }

    @Override
    public JsonNumber getJsonNumber(String name){
        return (JsonNumber)map.get(name);
    }

    @Override
    public JsonString getJsonString(String name){
        return (JsonString)map.get(name);
    }

    @Override
    public String getString(String name){
        return getJsonString(name).getString();
    }

    @Override
    public String getString(String name, String defaultValue){
        JsonValue value = map.get(name);
        if (value != null && value instanceof JsonString){
            return ((JsonString)value).getString();
        }else{
            return defaultValue;
        }
    }

    @Override
    public int getInt(String name){
        return getJsonNumber(name).intValue();
    }

    @Override
    public int getInt(String name, int defaultValue){
        JsonValue value = map.get(name);
        if (value != null && value instanceof JsonNumber){
            return ((JsonNumber)value).intValue();
        }else{
            return defaultValue;
        }
    }

    @Override
    public boolean getBoolean(String name){
        JsonValue value = map.get(name);
        if (value == null){
            throw new NullPointerException();
        }else if (value == TRUE){
            return true;
        }else if (value == FALSE){
            return false;
        }else{
            throw new ClassCastException();
        }
    }

    @Override
    public boolean getBoolean(String name, boolean defaultValue){
        try{
            return getBoolean(name);
        }catch(NullPointerException | ClassCastException ex){
            return defaultValue;
        }
    }

    @Override
    public boolean isNull(String name){
        JsonValue value = map.get(name);
        if (value == null){
            throw new NullPointerException();
        }
        return value == NULL;
    }
}
