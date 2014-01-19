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
 * 用于此包在处理过程中产生的各种异常。
 * @author Azige
 */
public class JsonException extends RuntimeException{

    public JsonException(){
    }

    public JsonException(String message){
        super(message);
    }

    public JsonException(String message, Throwable cause){
        super(message, cause);
    }

    public JsonException(Throwable cause){
        super(cause);
    }
}
