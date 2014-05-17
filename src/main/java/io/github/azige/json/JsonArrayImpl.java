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

import java.util.*;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;

/**
 * JSON数组类型。<br>
 * 此类实现了{@link List}接口，由一个{@link ArrayList}的实例作为存储容器。
 * 此列表<i>不允许</i>包含{@code null}元素，应使用{@link JsonValueType#NULL}进行包装，可以调用{@link #addObj(Object)}自动包装。
 * <b>此类的对象<i>不应当</i>包含其本身而造成递归包含</b>，其大部分方法都会检查这种行为并尽可能抛出异常以避免，
 * 但仅限于<b>直接包含</b>而不限于<b>间接包含</b>，使用者应当自行避免这种情况。
 *
 * @author Azige
 */
public class JsonArrayImpl extends JsonType implements JsonArray{

    private final List<JsonValue> list;

//    /**
//     * 将一个数组包装为JSON类型对象。此数组中的元素会按{@link JsonValue#valueOf(Object)}的方式进行包装。
//     *
//     * @param array 要包装的数组
//     * @return 包含原本数组中的元素的包装为JSON类型的JSON数组对象
//     */
//    public static JsonArray valueOf(Object[] array){
//        JsonArray ja = new JsonArray();
//        for (Object element : array){
//            ja.list.add(JsonValue.valueOf(element));
//        }
//        return ja;
//    }
//
//    /**
//     * 将一个集合包装为JSON类型对象。此集合中的元素会按{@link JsonValue#valueOf(Object)}的方式进行包装。
//     *
//     * @param collection 要包装的集合
//     * @return 包含原本集合中的元素的包装为JSON类型的JSON数组对象
//     */
//    public static JsonArray valueOf(Collection<?> collection){
//        JsonArray ja = new JsonArray();
//        for (Object element : collection){
//            ja.list.add(JsonValue.valueOf(element));
//        }
//        return ja;
//    }
//
//    /**
//     * 构造一个空对象。
//     */
//    public JsonArray(){
//        this.list = new ArrayList<>();
//    }
    /**
     * 以一个集合来构造一个对象。此集合的元素都会被添加到新对象，集合中不要包含null元素。
     *
     * @param collection 要复制的集合
     */
    JsonArrayImpl(Collection<? extends JsonValue> collection){
        this.list = Collections.unmodifiableList(new ArrayList<>(collection));
    }

    @Override
    public String toString(){
        Iterator<JsonValue> iter = iterator();
        if (!iter.hasNext()){
            return "" + ARRAY_START + ARRAY_END;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(ARRAY_START);
        while (true){
            sb.append(JsonType.valueOf(iter.next()));
            if (iter.hasNext()){
                sb.append(ARRAY_VALUE_SEPARATOR);
            }else{
                break;
            }
        }
        sb.append(ARRAY_END);
        return sb.toString();
    }

    @Override
    public int size(){
        return list.size();
    }

    @Override
    public boolean isEmpty(){
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o){
        return list.contains(o);
    }

    @Override
    public Iterator<JsonValue> iterator(){
        return list.iterator();
    }

    @Override
    public ValueType getValueType(){
        return ValueType.ARRAY;
    }

    @Override
    public JsonObject getJsonObject(int index){
        return (JsonObject)list.get(index);
    }

    @Override
    public JsonArray getJsonArray(int index){
        return (JsonArray)list.get(index);
    }

    @Override
    public JsonNumber getJsonNumber(int index){
        return (JsonNumber)list.get(index);
    }

    @Override
    public JsonString getJsonString(int index){
        return (JsonString)list.get(index);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends JsonValue> List<T> getValuesAs(Class<T> clazz){
        return (List<T>)list;
    }

    @Override
    public String getString(int index){
        return getJsonString(index).getString();
    }

    @Override
    public String getString(int index, String defaultValue){
        if (index >= 0 && index < list.size() && list.get(index) instanceof JsonString){
            return getString(index);
        }else{
            return defaultValue;
        }
    }

    @Override
    public int getInt(int index){
        return getJsonNumber(index).intValue();
    }

    @Override
    public int getInt(int index, int defaultValue){
        if (index >= 0 && index < list.size() && list.get(index) instanceof JsonNumber){
            return getInt(index);
        }else{
            return defaultValue;
        }
    }

    @Override
    public boolean getBoolean(int index){
        JsonValue value = list.get(index);
        if (value == TRUE){
            return true;
        }else if (value == FALSE){
            return false;
        }else{
            throw new ClassCastException();
        }
    }

    @Override
    public boolean getBoolean(int index, boolean defaultValue){
        try{
            return getBoolean(index);
        }catch (ArrayIndexOutOfBoundsException | ClassCastException ex){
            return defaultValue;
        }
    }

    @Override
    public boolean isNull(int index){
        return list.get(index) == NULL;
    }

    @Override
    public Object[] toArray(){
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a){
        return list.toArray(a);
    }

    @Override
    public boolean add(JsonValue e){
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o){
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c){
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends JsonValue> c){
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends JsonValue> c){
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c){
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c){
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear(){
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonValue get(int index){
        return list.get(index);
    }

    @Override
    public JsonValue set(int index, JsonValue element){
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, JsonValue element){
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonValue remove(int index){
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o){
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o){
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<JsonValue> listIterator(){
        return list.listIterator();
    }

    @Override
    public ListIterator<JsonValue> listIterator(int index){
        return list.listIterator(index);
    }

    @Override
    public List<JsonValue> subList(int fromIndex, int toIndex){
        return list.subList(fromIndex, toIndex);
    }
}
