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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JSON“对象”类型。<br/>
 * 此类实现了{@link Map}接口，由一个{@link HashMap}的实例作为存储容器。
 * 此Map<i>不允许</i>空键与空串{@code ""}键，<i>不允许</i>空值，"null"值应使用{@link JsonType#NULL}包装。
 * <b>此类的对象<i>不应当</i>包含其本身而造成递归包含</b>，其大部分方法都会检查这种行为并尽可能抛出异常以避免，
 * 但仅限于<b>直接包含</b>而不限于<b>间接包含</b>，使用者应当自行避免这种情况。
 *
 * @author Azige
 */
public class JsonObject extends JsonType implements Map<String, JsonType>{

    private static final Logger LOG = Logger.getLogger(JsonObject.class.getName());

    private final Map<String, JsonType> map;

    /**
     * 将一个JavaBean对象包装为JSON类型对象。JavaBean的属性会成为键，对应的包装过的JSON类型对象成为值。
     * JavaBean对象不应当含有循环引用。
     *
     * @param bean 要包装的JavaBean对象
     * @return 表示对应的JavaBean对象的JSON类型对象
     */
    public static JsonObject valueOf(Object bean){
        JsonObject jo = new JsonObject();
        try{
            BeanInfo info = Introspector.getBeanInfo(bean.getClass(), Object.class);
            for (PropertyDescriptor prop : info.getPropertyDescriptors()){
                Method setter = prop.getReadMethod();
                if (setter != null){
                    Object value = setter.invoke(bean);
                    if (value == bean){
                        throw new IllegalArgumentException("The bean has circularity of reference.");
                    }
                    jo.map.put(prop.getName(), JsonType.valueOf(value));
                }
            }
        }catch (IntrospectionException | IllegalAccessException | InvocationTargetException ex){
            assert false;
            LOG.log(Level.FINER, "", ex);
        }
        return jo;
    }

    /**
     * 构造一个空对象。
     */
    public JsonObject(){
        this.map = new HashMap<>();
    }

    /**
     * 以一个Map构造一个对象，Map中的所有键值对都会被添加到新对象。
     *
     * @param map 要复制的Map
     * @throws IllegalArgumentException 如果要复制的Map包含不合法的键或值
     */
    public JsonObject(Map<String, ? extends JsonType> map){
        if (map.containsKey(null)){
            throw new IllegalArgumentException("Null key.");
        }else if (map.containsKey("")){
            throw new IllegalArgumentException("Null string \"\" key.");
        }else if (map.containsValue(null)){
            throw new IllegalArgumentException("Null value.");
        }
        this.map = new HashMap<>(map);
    }

    @Override
    public String toString(){
        Iterator<Entry<String, JsonType>> iter = entrySet().iterator();
        if (!iter.hasNext()){
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('{');
        while (true){
            Entry<String, JsonType> entry = iter.next();
            sb.append(JsonString.valueOf(entry.getKey()).toString())
                .append(':')
                .append(entry.getValue().toString());
            if (iter.hasNext()){
                sb.append(',');
            }else{
                break;
            }
        }
        sb.append('}');
        return sb.toString();
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
    public JsonType get(Object key){
        return map.get(key);
    }

    /**
     * 添加或更新一个键值对。如果值是此对象本身会导致异常。
     *
     * @param key 键
     * @param value 值
     * @return 以前与{@code key}关联的值
     * @throws IllegalArgumentException
     * 如果{@code value}是此对象本身，或{@code key}为{@code ""}
     * @throws NullPointerException 如果{@code key}或{@code value}为{@code null}
     */
    @Override
    public JsonType put(String key, JsonType value){
        if (value == this){
            throw new IllegalArgumentException("Can not add self.");
        }else if (value == null){
            throw new NullPointerException("Null value.");
        }else if (key == null){
            throw new NullPointerException("Null key.");
        }else if (key.equals("")){
            throw new IllegalArgumentException("Null string \"\" key.");
        }
        return map.put(key, value);
    }

    /**
     * 添加或更新一个键值对。值使用{@link JsonType#valueOf(Object)}进行包装。如果值是此对象本身会导致异常。
     *
     * @param key 键
     * @param value 值
     * @return 以前与{@code key}关联的值
     * @throws IllegalArgumentException
     * 如果{@code value}是此对象本身，或{@code key}为{@code ""}
     * @throws NullPointerException 如果{@code key}为{@code null}
     */
    public JsonType put(String key, Object value){
        return put(key, JsonType.valueOf(value));
    }

    @Override
    public JsonType remove(Object key){
        return map.remove(key);
    }

    /**
     * 从指定Map中将所有键值对复制到此对象中。如果指定Map中的值集包含此对象本身会导致异常。
     *
     * @param m 要添加键值对到此对象的Map
     * @throws IllegalArgumentException 如果指定{@code m}的值集包含此对象本身或包含不合法的键或值
     */
    @Override
    public void putAll(Map<? extends String, ? extends JsonType> m){
        if (m.containsValue(this)){
            throw new IllegalArgumentException("Can not add self.");
        }else if (m.containsValue(null)){
            throw new IllegalArgumentException("Null value.");
        }else if (m.containsKey(null)){
            throw new IllegalArgumentException("Null key.");
        }else if (m.containsKey("")){
            throw new IllegalArgumentException("Null string \"\" key.");
        }
        map.putAll(m);
    }

    @Override
    public void clear(){
        map.clear();
    }

    @Override
    public Set<String> keySet(){
        return map.keySet();
    }

    @Override
    public Collection<JsonType> values(){
        return map.values();
    }

    @Override
    public Set<Entry<String, JsonType>> entrySet(){
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
}
