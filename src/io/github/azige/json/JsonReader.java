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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;

/**
 * 用于读入JSON类型对象的字符流。<br/>
 * 尽管它名字有Writer，但它并不是{@link Reader}的子类，它只关心如何将文本解析为JSON类型对象。
 *
 * @author Azige
 */
public class JsonReader{

    private final Reader in;

    /**
     * 包装一个字符输入流以构造对象。
     *
     * @param in 要读入的流
     */
    public JsonReader(Reader in){
        if (in.markSupported()){
            this.in = in;
        }else{
            this.in = new BufferedReader(in);
        }
    }

    /**
     * 读入一个JSON数组或对象。
     *
     * @return 流中的下一个JSON数组或对象
     */
    public JsonType read(){
        try{
            char c = peekOne();
            if (c == OBJECT_START){
                return readArray();
            }else if (c == ARRAY_START){
                return readObject();
            }else{
                throw new UnexpectedCharacterException(c);
            }
        }catch (IOException ex){
            throw new JsonException(ex);
        }
    }

    /**
     * 读入一个JSON对象。
     *
     * @return 流中的下一个JSON对象
     */
    public JsonObject readObject(){
        // <BEFORE> \{ <BEFORE_PAIRS> ( key <AFTER_KEY> : <BEFORE_VALUE> value <AFTER_VALUE> (, <BEFORE_KEY> key <AFTER_KEY> : <BEFORE_VALUE> value <AFTER_VALUE>)* )? \} <AFTER>
        final int BEFORE = 1;
        final int BEFORE_PAIRS = 2;
        final int BEFORE_KEY = 3;
        final int AFTER_KEY = 4;
        final int BEFORE_VALUE = 5;
        final int AFTER_VALUE = 6;
        final int AFTER = 7;
        int state = BEFORE;
        JsonObject jo = new JsonObject();
        String key = null;
        JsonType value = null;
        try{
            while (true){
                if (state == AFTER){
                    return jo;
                }
                boolean unexpected = false;
                in.mark(1);
                char c = readOne();
                if (Character.isWhitespace(c)){
                    continue;
                }
                switch (state){
                    case BEFORE:
                        unexpected = c != OBJECT_START;
                        state = BEFORE_PAIRS;
                        break;
                    case BEFORE_PAIRS:
                        if (c == OBJECT_END){
                            state = AFTER;
                            break;
                        }else{
                            // do BEFORE_KEY
                        }
                    case BEFORE_KEY:
                        in.reset();
                        key = readString().getValue();
                        state = AFTER_KEY;
                        break;
                    case AFTER_KEY:
                        unexpected = c != OBJECT_PAIR_SEPARATOR;
                        state = BEFORE_VALUE;
                        break;
                    case BEFORE_VALUE:
                        in.reset();
                        value = readAny();
                        state = AFTER_VALUE;
                        break;
                    case AFTER_VALUE:
                        jo.put(key, value);
                        if (c == OBJECT_PAIR_SEPARATOR){
                            state = BEFORE_KEY;
                        }else if (c == OBJECT_END){
                            state = AFTER;
                        }else{
                            unexpected = true;
                        }
                        break;
                    default:
                        assert false;
                }
                if (unexpected){
                    throw new UnexpectedCharacterException(c);
                }
            }
        }catch (IOException ex){
            throw new JsonException(ex);
        }
    }

    /**
     * 读入一个JSON数组。
     *
     * @return 流中的下一个JSON数组
     */
    public JsonArray readArray(){
        // <BEFORE> \[ <BEFORE_VALUES> ( value <AFTER_VALUE> (, <BEFORE_VALUE> value <AFTER_VALUE>)* )? \] <AFTER>
        final int BEFORE = 1;
        final int BEFORE_VALUES = 2;
        final int BEFORE_VALUE = 3;
        final int AFTER_VALUE = 4;
        final int AFTER = 5;
        int state = BEFORE;
        JsonArray ja = new JsonArray();
        JsonType value = null;
        try{
            while (true){
                if (state == AFTER){
                    return ja;
                }
                boolean unexpected = false;
                in.mark(1);
                char c = readOne();
                if (Character.isWhitespace(c)){
                    continue;
                }
                switch (state){
                    case BEFORE:
                        unexpected = c != ARRAY_START;
                        state = BEFORE_VALUES;
                        break;
                    case BEFORE_VALUES:
                        if (c == ARRAY_END){
                            state = AFTER;
                            break;
                        }else{
                            // do BEFORE_VALUE
                        }
                    case BEFORE_VALUE:
                        in.reset();
                        value = readAny();
                        state = AFTER_VALUE;
                        break;
                    case AFTER_VALUE:
                        ja.add(value);
                        if (c == ARRAY_VALUE_SEPARATOR){
                            state = BEFORE_VALUE;
                        }else if (c == ARRAY_END){
                            state = AFTER;
                        }else{
                            unexpected = true;
                        }
                        break;
                    default:
                        assert false;
                }
                if (unexpected){
                    throw new UnexpectedCharacterException(c);
                }
            }
        }catch (IOException ex){
            throw new JsonException(ex);
        }
    }

    /**
     * 读入一个任何可能的JSON类型。
     */
    JsonType readAny(){
        try{
            char c = peekOne();
            if (c == OBJECT_START){
                return readArray();
            }else if (c == ARRAY_START){
                return readObject();
            }else if (c == STRING_START){
                return readString();
            }else if (isNumberStart(c)){
                return readNumber();
            }else if (isBooleanStart(c)){
                return readBoolean();
            }else if (c == 'n'){
                in.read();
                if ((c = readOne()) != 'u' || (c = readOne()) != 'l' || (c = readOne()) != 'l'){
                    throw new UnexpectedCharacterException(c);
                }
                return JsonType.NULL;
            }else{
                throw new UnexpectedCharacterException(c);
            }
        }catch (IOException ex){
            throw new JsonException(ex);
        }
    }

    /**
     * 读入一个JSON字符串。
     */
    JsonString readString(){
        // <BEFORE>"<BEFORE_CHARACTER>(\\<ESCAPE>(["\\/bfnrt]|u[0-9a-fA-F]{4})|[^\\"])*"<AFTER>
        final int BEFORE = 1;
        final int BEFORE_CHARACTER = 2;
        final int ESCAPE = 3;
        final int AFTER = 4;
        int state = BEFORE;
        StringBuilder value = new StringBuilder();
        StringBuilder text = new StringBuilder();
        try{
            while (true){
                if (state == AFTER){
                    return new JsonString(value.toString(), text.toString());
                }
                boolean unexpected = false;
                char c = readOne();
                text.append(c);
                switch (state){
                    case BEFORE:
                        unexpected = c != STRING_START;
                        state = BEFORE_CHARACTER;
                        break;
                    case BEFORE_CHARACTER:
                        if (c == STRING_END){
                            state = AFTER;
                        }else if (c == '\\'){
                            state = ESCAPE;
                        }else{
                            value.append(c);
                        }
                        break;
                    case ESCAPE:
                        if (c == '\"'){
                            value.append('\"');
                        }else if (c == '\\'){
                            value.append('\\');
                        }else if (c == '/'){
                            value.append('/');
                        }else if (c == 'b'){
                            value.append('\b');
                        }else if (c == 'f'){
                            value.append('\f');
                        }else if (c == 'n'){
                            value.append('\n');
                        }else if (c == 'r'){
                            value.append('\r');
                        }else if (c == 't'){
                            value.append('\t');
                        }else if (c == 'u'){
                            for (int i = 0; i < 4; i++){
                                c = readOne();
                                text.append(c);
                                if (!isHexDigit(c)){
                                    throw new UnexpectedCharacterException(c);
                                }
                            }
                            value.append(Character.toChars(Integer.parseInt(text.substring(text.length() - 4), 16))[0]);
                        }else{
                            throw new UnexpectedCharacterException(c);
                        }
                        state = BEFORE_CHARACTER;
                        break;
                    default:
                        assert false;
                }
                if (unexpected){
                    throw new UnexpectedCharacterException(c);
                }
            }
        }catch (IOException ex){
            throw new JsonException(ex);
        }
    }

    /**
     * 读入一个JSON数值。
     */
    JsonNumber readNumber(){
        // <BEFORE>(-<AFTER_SIGN>)?(0<AFTER_ZERO>|[1-9]<INTEGER_NUMBER>\d*)(\.<AFTER_POINT>(\d<DECIMAL_NUMBER>)+)?([eE]<AFTER_E>([+-]<AFTER_E_SIGN>)?(\d<E_NUMBER>)+)<AFTER>
        final int BEFORE = 1;
        final int AFTER_SIGN = 2;
        final int AFTER_ZERO = 3;
        final int INTEGER_NUMBER = 4;
        final int AFTER_POINT = 5;
        final int DECIMAL_NUMBER = 6;
        final int AFTER_E = 7;
        final int AFTER_E_SIGN = 8;
        final int E_NUMBER = 9;
        final int AFTER = 10;
        int state = BEFORE;
        StringBuilder text = new StringBuilder();
        try{
            while (true){
                boolean unexpected = false;
                in.mark(1);
                char c = 0;
                try{
                    // catch EOF
                    c = readOne();
                }catch (JsonException ex){
                    switch (state){
                        case INTEGER_NUMBER:
                        case AFTER_ZERO:
                        case DECIMAL_NUMBER:
                        case E_NUMBER:
                            state = AFTER;
                            break;
                        default:
                            throw ex;
                    }
                }
                switch (state){
                    case BEFORE:
                        if (c == '-'){
                            state = AFTER_SIGN;
                        }else if (c == '0'){
                            state = AFTER_ZERO;
                        }else if ('1' <= c && c <= '9'){
                            state = INTEGER_NUMBER;
                        }else{
                            unexpected = true;
                        }
                        break;
                    case AFTER_SIGN:
                        if (c == '0'){
                            state = AFTER_ZERO;
                        }else if ('1' <= c && c <= '9'){
                            state = INTEGER_NUMBER;
                        }else{
                            unexpected = true;
                        }
                        break;
                    case AFTER_ZERO:
                        if (c == '.'){
                            state = AFTER_POINT;
                        }else{
                            state = AFTER;
                        }
                        break;
                    case INTEGER_NUMBER:
                        if ('0' <= c && c <= '9'){
                            // don't change the state
                        }else if (c == '.'){
                            state = AFTER_POINT;
                        }else if (c == 'e' || c == 'E'){
                            state = AFTER_E;
                        }else{
                            state = AFTER;
                        }
                        break;
                    case AFTER_POINT:
                        if ('0' <= c && c <= '9'){
                            state = DECIMAL_NUMBER;
                        }else{
                            unexpected = true;
                        }
                        break;
                    case DECIMAL_NUMBER:
                        if ('0' <= c && c <= '9'){
                            // don't change the state
                        }else if (c == 'e' || c == 'E'){
                            state = AFTER_E;
                        }else{
                            state = AFTER;
                        }
                        break;
                    case AFTER_E:
                        if (c == '+' || c == '-'){
                            state = AFTER_E_SIGN;
                        }else if ('0' <= c && c <= '9'){
                            state = E_NUMBER;
                        }else{
                            unexpected = true;
                        }
                        break;
                    case AFTER_E_SIGN:
                        if ('0' <= c && c <= '9'){
                            state = E_NUMBER;
                        }else{
                            unexpected = true;
                        }
                        break;
                    case E_NUMBER:
                        if ('0' <= c && c <= '9'){
                            state = E_NUMBER;
                        }else{
                            state = AFTER;
                        }
                        break;
                    case AFTER:
                        break;
                    default:
                        assert false;
                }
                if (state == AFTER){
                    in.reset();
                    String str = text.toString();
                    // JSON数值的表示形式与BigDecimal定义的是一致的
                    return new JsonNumber(new BigDecimal(str), str);
                }
                if (unexpected){
                    throw new UnexpectedCharacterException(c);
                }
                text.append(c);
            }
        }catch (IOException ex){
            throw new JsonException(ex);
        }
    }

    /**
     * 读入一个JSON布尔值。
     */
    JsonBoolean readBoolean(){
        try{
            char c = readOne();
            if (c == 't'){
                if ((c = readOne()) != 'r' || (c = readOne()) != 'u' || (c = readOne()) != 'e'){
                    throw new UnexpectedCharacterException(c);
                }
                return JsonBoolean.TRUE;
            }else if (c == 'f'){
                if ((c = readOne()) != 'a' || (c = readOne()) != 'l' || (c = readOne()) != 's' || (c = readOne()) != 'e'){
                    throw new UnexpectedCharacterException(c);
                }
                return JsonBoolean.FALSE;
            }else{
                throw new UnexpectedCharacterException(c);
            }
        }catch (IOException ex){
            throw new JsonException(ex);
        }
    }

    /**
     * 读入一个字符，如果遇到EOF会抛出异常。
     */
    private char readOne() throws IOException{

        int read = in.read();
        if (read == -1){
            throw new JsonException("Unexpected EOF.");
        }
        return (char)read;
    }

    /**
     * 标记并读入一个字符，之后重置in。
     */
    private char peekOne() throws IOException{
        in.mark(1);
        char c = readOne();
        in.reset();
        return c;
    }

    /**
     * 检查c是否为一个数字的开始。
     */
    private static boolean isNumberStart(char c){
        return c == '-' || c >= '0' && c <= '9';
    }

    /**
     * 检查c是否为一个布尔值的开始。
     */
    private static boolean isBooleanStart(char c){
        return c == 't' || c == 'f';
    }

    /**
     * 检查c是否为一个十六进制数字。
     */
    private static boolean isHexDigit(char c){
        return c >= '0' && c <= '9' || c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F';
    }
}
