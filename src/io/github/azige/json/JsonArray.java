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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * JSON数组类型。<br/>
 * 此类实现了{@link List}接口，由一个{@link ArrayList}的实例作为存储容器。
 * <b>此类的对象<i>不应当</i>包含其本身而造成递归包含</b>，其大部分方法都会检查这种行为并尽可能抛出异常以避免，
 * 但仅限于<b>直接包含</b>而不限于<b>间接包含</b>，使用者应当自行避免这种情况。
 * @author Azige
 */
public class JsonArray extends JsonType implements List<JsonType>{

    private final List<JsonType> list;

    /**
     * 将一个数组包装为JSON类型对象。此数组中的元素会按{@link JsonType#valueOf(Object)}的方式进行包装。
     * @param array 要包装的数组
     * @return 包含原本数组中的元素的包装为JSON类型的JSON数组对象
     */
    public static JsonArray valueOf(Object[] array){
        if (array instanceof JsonType[]){
            return new JsonArray(Arrays.asList((JsonType[])array));
        }else{
            JsonArray ja = new JsonArray();
            for (Object element : array){
                ja.list.add(JsonType.valueOf(element));
            }
            return ja;
        }
    }

    /**
     * 将一个集合包装为JSON类型对象。此集合中的元素会按{@link JsonType#valueOf(Object)}的方式进行包装。
     * @param collection 要包装的集合
     * @return 包含原本集合中的元素的包装为JSON类型的JSON数组对象
     */
    public static JsonArray valueOf(Collection<?> collection){
        JsonArray ja = new JsonArray();
        for (Object element : collection){
            ja.add(JsonType.valueOf(element));
        }
        return ja;
    }

    /**
     * 构造一个空对象。
     */
    public JsonArray(){
        this.list = new ArrayList<>();
    }

    /**
     * 以一个集合来构造一个对象。此集合的元素都会被添加到新对象。
     * @param collection 要复制的集合
     */
    public JsonArray(Collection<? extends JsonType> collection){
        this.list = new ArrayList<>(collection);
    }

    @Override
    public String toString(){
        Iterator<JsonType> iter = iterator();
        if (!iter.hasNext()){
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        while (true){
            sb.append(iter.next());
            if (iter.hasNext()){
                sb.append(',');
            }else{
                break;
            }
        }
        sb.append(']');
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
    public Iterator<JsonType> iterator(){
        return list.iterator();
    }

    @Override
    public Object[] toArray(){
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a){
        return list.toArray(a);
    }

    /**
     * 添加一个元素到此对象。如果添加此对象自身会导致异常。
     * @param e 要添加的元素
     * @return {@code true}
     * @throws IllegalArgumentException 如果{@code e}为此对象自身
     */
    @Override
    public boolean add(JsonType e){
        if (e == this){
            throw new IllegalArgumentException("Can not add self.");
        }
        return list.add(e);
    }

    @Override
    public boolean remove(Object o){
        return list.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c){
        return list.containsAll(c);
    }

    /**
     * 将一个集合中的元素添加到此列表末尾。如果集合中包含此对象自身会导致异常。
     * @param c 要添加的集合
     * @return 如果此列表由于调用而发生更改，则返回{@code true}
     * @throws IllegalArgumentException 如果{@code c}包含此对象自身
     */
    @Override
    public boolean addAll(Collection<? extends JsonType> c){
        if (c.contains(this)){
            throw new IllegalArgumentException("Can not add self.");
        }
        return list.addAll(c);
    }

    /**
     * 将一个集合中的元素插入到此列表指定索引处。如果集合中包含此对象自身会导致异常。
     * @param index 要插入的第一个元素的位置
     * @param c 要添加的集合
     * @return 如果此列表由于调用而发生更改，则返回{@code true}
     * @throws IllegalArgumentException 如果{@code c}包含此对象自身
     */
    @Override
    public boolean addAll(int index, Collection<? extends JsonType> c){
        if (c.contains(this)){
            throw new IllegalArgumentException("Can not add self.");
        }
        return list.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c){
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c){
        return list.retainAll(c);
    }

    @Override
    public void clear(){
        list.clear();
    }

    @Override
    public boolean equals(Object o){
        return list.equals(o);
    }

    @Override
    public int hashCode(){
        return list.hashCode();
    }

    @Override
    public JsonType get(int index){
        return list.get(index);
    }

    @Override
    public JsonType set(int index, JsonType element){
        return list.set(index, element);
    }

    @Override
    public void add(int index, JsonType element){
        if (element == this){
            throw new IllegalArgumentException();
        }
        list.add(index, element);
    }

    @Override
    public JsonType remove(int index){
        return list.remove(index);
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
    public ListIterator<JsonType> listIterator(){
        return list.listIterator();
    }

    @Override
    public ListIterator<JsonType> listIterator(int index){
        return list.listIterator(index);
    }

    @Override
    public List<JsonType> subList(int fromIndex, int toIndex){
        return list.subList(fromIndex, toIndex);
    }
}
