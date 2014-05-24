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
 * 包含常量的类。
 * @author Azige
 */
final class Constant{

    static final String DEFAULT_CHARSET = "UTF-8";
    
    static final char OBJECT_START = '{';
    static final char OBJECT_KEY_VALUE_SEPARATOR = ':';
    static final char OBJECT_PAIR_SEPARATOR = ',';
    static final char OBJECT_END = '}';
    static final char ARRAY_START = '[';
    static final char ARRAY_VALUE_SEPARATOR = ',';
    static final char ARRAY_END = ']';
    static final char STRING_START = '\"';
    static final char STRING_END = '\"';
    static final char STRING_ESCAPE = '\\';
    static final char NUMBER_MINUS_SIGN = '-';
    static final char NUMBER_DECIMAL_POINT = '.';

    private Constant(){
    }
}
