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
/**
 * 一个简单的JSON库。<br/>
 * 可以简单的将任何合适的类型包装为JSON类型，例如
 * <pre>
 * JsonObject person = new JsonObject();
 * person.put("name", "bob");
 * person.put("age", 12);
 * person.put("asset", new String[]{"PC", "phone", "TV"});
 * </pre>
 * 那么{@code person.toString()}将生成类似如下的JSON文本
 * <p>
 * {"name":"bob","age":20,"asset":["PC","phone","TV"]}</p>
 * 也可以简单的直接包装JavaBean对象，例如
 * <pre>
 * class Person{
 *     String name;
 *     int age;
 *     String[] asset;
 *
 *     // Constructors, setters and getters.
 * }
 * </pre>
 * 那么{@code JsonType.valueOf(new Person("bob", 12, new String[]{"PC", "phone", "TV"})).toString()}同样将得到与上述类似的JSON文本。
 */
package io.github.azige.json;
