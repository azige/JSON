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
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Deque;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import javax.json.JsonException;
import javax.json.stream.JsonLocation;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParsingException;

/**
 *
 * @author Azige
 */
public class JsonParserImpl implements JsonParser{

    private final BufferedReader in;
    private BufferedReader line;
    private Event event;
    private String value;
    private BigDecimal number;
    private final StateMachine sm = new StateMachine();
    private long lineCount = -1;
    private long columnCount = -1;
    private long offset = -1;
    private boolean peekCharFlag = false;
    private boolean peekEventFlag = false;
    private char peekedChar;
    private StringBuilder buffer;

    public JsonParserImpl(Reader in){
        if (in instanceof BufferedReader){
            this.in = (BufferedReader)in;
        }else{
            this.in = new BufferedReader(in);
        }
    }

    @Override
    public boolean hasNext(){
        if (!peekEventFlag){
            peekEventFlag = true;
            try{
                sm.parseNext();
                return true;
            }catch (NoSuchElementException ex){
                return false;
            }
        }else{
            return true;
        }
    }

    @Override
    public Event next(){
        if (!peekEventFlag){
            sm.parseNext();
        }else{
            peekEventFlag = false;
        }

        event = sm.event;
        value = sm.value;
        number = sm.number;

        return event;
    }

    @Override
    public String getString(){
        if (event == Event.KEY_NAME || event == Event.VALUE_STRING || event == Event.VALUE_NUMBER){
            return value;
        }else{
            throw new IllegalStateException();
        }
    }

    @Override
    public boolean isIntegralNumber(){
        if (event == Event.VALUE_NUMBER){
            return number.scale() <= 0;
        }else{
            throw new IllegalStateException();
        }
    }

    @Override
    public int getInt(){
        if (event == Event.VALUE_NUMBER){
            return number.intValue();
        }else{
            throw new IllegalStateException();
        }
    }

    @Override
    public long getLong(){
        if (event == Event.VALUE_NUMBER){
            return number.longValue();
        }else{
            throw new IllegalStateException();
        }
    }

    @Override
    public BigDecimal getBigDecimal(){
        if (event == Event.VALUE_NUMBER){
            return number;
        }else{
            throw new IllegalStateException();
        }
    }

    @Override
    public JsonLocation getLocation(){
        final long lineCount = this.lineCount;
        final long columnCount = this.columnCount;
        final long offset = this.offset;
        return new JsonLocation(){

            @Override
            public long getLineNumber(){
                return lineCount;
            }

            @Override
            public long getColumnNumber(){
                return columnCount;
            }

            @Override
            public long getStreamOffset(){
                return offset;
            }
        };
    }

    @Override
    public void close(){
        try{
            in.close();
        }catch (IOException ex){
            throw new JsonException(ex.getLocalizedMessage(), ex);
        }
    }

    private static boolean isDigit(char c, boolean exceptZero){
        char start = exceptZero ? '1' : '0';
        return c >= start && c <= '9';
    }

    private static boolean isHexDigit(char c){
        return c >= '0' && c <= '9' || c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F';
    }

    private static boolean isWhitespace(char c){
        return c == 0x9 || c == 0xa || c == 0xd || c == 0x20;
    }

    private char pollChar(){
        if (peekCharFlag){
            peekCharFlag = false;
            return peekedChar;
        }

        int c;
        try{
            columnCount++;
            offset++;
            while (line == null || (c = line.read()) == -1){
                String lineString = in.readLine();
                if (lineString == null){
                    throw new JsonException("Unexpected EOF.");
                }
                lineCount++;
                offset++;
                columnCount = 1;
                line = new BufferedReader(new StringReader(lineString));
            }
        }catch (IOException ex){
            throw new JsonException(ex.getLocalizedMessage(), ex);
        }

        return (char)c;
    }

    private char peekChar(){
        if (!peekCharFlag){
            try{
                peekedChar = pollChar();
            }catch (JsonException ex){
                return (char)-1;
            }
            peekCharFlag = true;
        }

        return peekedChar;
    }

    private char pollCharUntillNotWhitespace(){
        char c = pollChar();
        while (isWhitespace(c)){
            c = pollChar();
        }
        return c;
    }

    private char peekCharUntillNotWhitespace(){
        char c = peekChar();
        while (isWhitespace(c)){
            pollChar();
            c = peekChar();
        }
        return c;
    }

    private void unexpectedCharacter(char c){
        throw new JsonParsingException(
            String.format(
                "Unexpected character %c at line %d, colum %d",
                c,
                lineCount,
                columnCount
            ),
            getLocation());
    }

    /*
     * State Machines
     */
    private interface State{

        State next();
    }

    private class StateMachine{

        private Event event;
        private String value;
        private BigDecimal number;
        private final Deque<State> stateStack = new LinkedList<>();

        private void parseNext(){
            if (stateStack.isEmpty()){
                stateStack.push(valueBegin);
            }

            State state = stateStack.pop();
            while (state != null){
                state = state.next();
            }
        }

        /*
         * Value
         */
        private final State valueBegin = new State(){

            @Override
            public State next(){
                buffer = new StringBuilder();
                char c = pollCharUntillNotWhitespace();
                if (c == NUMBER_MINUS_SIGN){
                    event = Event.VALUE_NUMBER;
                    buffer.append(c);
                    return numberAfterSign;
                }else if (c == '0'){
                    event = Event.VALUE_NUMBER;
                    buffer.append(c);
                    return numberAfterZero;
                }else if (c >= '1' && c <= '9'){
                    event = Event.VALUE_NUMBER;
                    buffer.append(c);
                    return numberIntegerNumber;
                }else if (c == STRING_START){
                    event = Event.VALUE_STRING;
                    return stringBeforeCharacter;
                }else if (c == ARRAY_START){
                    event = Event.START_ARRAY;
                    stateStack.push(arrayBeforeValues);
                    return null;
                }else if (c == OBJECT_START){
                    event = Event.START_OBJECT;
                    stateStack.push(objectBeforePairs);
                    return null;
                }else if (c == 't'){
                    if ((c = pollChar()) == 'r'
                        && (c = pollChar()) == 'u'
                        && (c = pollChar()) == 'e'){
                        event = Event.VALUE_TRUE;
                        return null;
                    }else{
                        unexpectedCharacter(c);
                    }
                }else if (c == 'f'){
                    if ((c = pollChar()) == 'a'
                        && (c = pollChar()) == 'l'
                        && (c = pollChar()) == 's'
                        && (c = pollChar()) == 'e'){
                        event = Event.VALUE_FALSE;
                        return null;
                    }else{
                        unexpectedCharacter(c);
                    }
                }else if (c == 'n'){
                    if ((c = pollChar()) == 'u'
                        && (c = pollChar()) == 'l'
                        && (c = pollChar()) == 'l'){
                        event = Event.VALUE_NULL;
                        return null;
                    }else{
                        unexpectedCharacter(c);
                    }
                }else{
                    throw new NoSuchElementException();
                }
                return null;
            }
        };

        /*
         * Number
         */
        private final State numberAfterSign = new State(){

            @Override
            public State next(){
                char c = pollChar();
                if (c == '0'){
                    return numberAfterZero;
                }else if (isDigit(c, true)){
                    return numberIntegerNumber;
                }else{
                    unexpectedCharacter(c);
                }
                return null;
            }
        };

        private final State numberAfterZero = new State(){

            @Override
            public State next(){
                char c = peekChar();
                if (c == NUMBER_DECIMAL_POINT){
                    pollChar();
                    buffer.append(c);
                    return numberAfterPoint;
                }else{
                    return numberAfter;
                }
            }
        };

        private final State numberIntegerNumber = new State(){

            @Override
            public State next(){
                char c = peekChar();
                if (c == NUMBER_DECIMAL_POINT){
                    pollChar();
                    buffer.append(c);
                    return numberAfterPoint;
                }else if (c == 'E' || c == 'e'){
                    pollChar();
                    buffer.append(c);
                    return numberAfterE;
                }else if (isDigit(c, false)){
                    pollChar();
                    buffer.append(c);
                    return this;
                }else{
                    return numberAfter;
                }
            }
        };

        private final State numberAfterPoint = new State(){

            @Override
            public State next(){
                char c = pollChar();
                if (isDigit(c, false)){
                    buffer.append(c);
                    return numberDecimalNumber;
                }else{
                    unexpectedCharacter(c);
                }
                return null;
            }
        };

        private final State numberDecimalNumber = new State(){

            @Override
            public State next(){
                char c = peekChar();
                if (c == 'E' || c == 'e'){
                    pollChar();
                    buffer.append(c);
                    return numberAfterE;
                }else if (isDigit(c, false)){
                    pollChar();
                    buffer.append(c);
                    return this;
                }else{
                    return numberAfter;
                }
            }
        };

        private final State numberAfterE = new State(){

            @Override
            public State next(){
                char c = pollChar();
                if (c == '+' || c == '-'){
                    buffer.append(c);
                    return numberAfterESign;
                }else if (isDigit(c, false)){
                    buffer.append(c);
                    return numberENumber;
                }else{
                    unexpectedCharacter(c);
                }
                return null;
            }
        };
        private final State numberAfterESign = new State(){

            @Override
            public State next(){
                char c = pollChar();
                if (isDigit(c, false)){
                    buffer.append(c);
                    return numberENumber;
                }else{
                    unexpectedCharacter(c);
                }
                return null;
            }
        };

        private final State numberENumber = new State(){

            @Override
            public State next(){
                char c = peekChar();
                if (isDigit(c, false)){
                    pollChar();
                    buffer.append(c);
                    return this;
                }else{
                    return numberAfter;
                }
            }
        };

        private final State numberAfter = new State(){

            @Override
            public State next(){
                value = buffer.toString();
                number = new BigDecimal(value);
                return null;
            }
        };

        /*
         * String
         */
        private final State stringBeforeCharacter = new State(){

            @Override
            public State next(){
                char c = pollChar();
                if (c == STRING_END){
                    value = buffer.toString();
                    return null;
                }else if (c == STRING_ESCAPE){
                    return stringEscape;
                }else{
                    buffer.append(c);
                    return this;
                }
            }
        };

        private final State stringEscape = new State(){

            @Override
            public State next(){
                char c = pollChar();
                if (c == '\"'){
                    buffer.append('\"');
                }else if (c == '\\'){
                    buffer.append('\\');
                }else if (c == '/'){
                    buffer.append('/');
                }else if (c == 'b'){
                    buffer.append('\b');
                }else if (c == 'f'){
                    buffer.append('\f');
                }else if (c == 'n'){
                    buffer.append('\n');
                }else if (c == 'r'){
                    buffer.append('\r');
                }else if (c == 't'){
                    buffer.append('\t');
                }else if (c == 'u'){
                    StringBuilder hexBuffer = new StringBuilder();
                    for (int i = 0; i < 4; i++){
                        c = pollChar();
                        if (!isHexDigit(c)){
                            unexpectedCharacter(c);
                        }
                        hexBuffer.append(c);
                    }
                    c = (char)Integer.parseInt(hexBuffer.toString(), 16);
                    buffer.append(c);
                }else{
                    unexpectedCharacter(c);
                }
                return stringBeforeCharacter;
            }
        };

        /*
         * Array
         */
        private final State arrayBeforeValues = new State(){

            @Override
            public State next(){
                char c = peekCharUntillNotWhitespace();
                if (c == ARRAY_END){
                    pollChar();
                    event = Event.END_ARRAY;
                    return null;
                }else{
                    stateStack.push(arrayAfterValue);
                    return valueBegin;
                }
            }
        };

        private final State arrayAfterValue = new State(){

            @Override
            public State next(){
                char c = pollCharUntillNotWhitespace();
                if (c == ARRAY_END){
                    event = Event.END_ARRAY;
                    return null;
                }else if (c == ARRAY_VALUE_SEPARATOR){
                    stateStack.push(this);
                    return valueBegin;
                }else{
                    unexpectedCharacter(c);
                }
                return null;
            }
        };

        /*
         * Object
         */
        private final State objectBeforePairs = new State(){

            @Override
            public State next(){
                char c = pollCharUntillNotWhitespace();
                if (c == STRING_START){
                    buffer = new StringBuilder();
                    event = Event.KEY_NAME;
                    stateStack.push(objectAfterKey);
                    return stringBeforeCharacter;
                }else if (c == OBJECT_END){
                    event = Event.END_OBJECT;
                    return null;
                }else{
                    unexpectedCharacter(c);
                }
                return null;
            }
        };

        private final State objectAfterKey = new State(){

            @Override
            public State next(){
                char c = pollCharUntillNotWhitespace();
                if (c == OBJECT_KEY_VALUE_SEPARATOR){
                    stateStack.push(objectAfterValue);
                    return valueBegin;
                }else{
                    unexpectedCharacter(c);
                }
                return null;
            }
        };

        private final State objectAfterValue = new State(){

            @Override
            public State next(){
                char c = pollCharUntillNotWhitespace();
                if (c == OBJECT_END){
                    event = Event.END_OBJECT;
                    return null;
                }else if (c == OBJECT_PAIR_SEPARATOR){
                    return objectBeforeKey;
                }else{
                    unexpectedCharacter(c);
                }
                return null;
            }
        };

        private final State objectBeforeKey = new State(){

            @Override
            public State next(){
                char c = pollCharUntillNotWhitespace();
                if (c == STRING_START){
                    event = Event.KEY_NAME;
                    buffer = new StringBuilder();
                    stateStack.push(objectAfterKey);
                    return stringBeforeCharacter;
                }else{
                    unexpectedCharacter(c);
                }
                return null;
            }
        };
    }
}
