package me.memegodmidas.dominioncore.loader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class MiniJsonParser {
    private final String text;
    private int index;

    private MiniJsonParser(String text) {
        this.text = text;
    }

    static Object parse(String json) {
        MiniJsonParser parser = new MiniJsonParser(json);
        Object value = parser.parseValue();
        parser.skipWhitespace();
        if (parser.index != parser.text.length()) {
            throw new IllegalArgumentException("Unexpected trailing content at index " + parser.index);
        }
        return value;
    }

    private Object parseValue() {
        skipWhitespace();
        if (index >= text.length()) {
            throw new IllegalArgumentException("Unexpected end of json");
        }
        char c = text.charAt(index);
        return switch (c) {
            case '{' -> parseObject();
            case '[' -> parseArray();
            case '"' -> parseString();
            case 't' -> parseLiteral("true", true);
            case 'f' -> parseLiteral("false", false);
            case 'n' -> parseLiteral("null", null);
            default -> parseNumber();
        };
    }

    private Map<String, Object> parseObject() {
        expect('{');
        Map<String, Object> object = new LinkedHashMap<>();
        skipWhitespace();
        if (peek('}')) {
            expect('}');
            return object;
        }

        while (true) {
            String key = parseString();
            skipWhitespace();
            expect(':');
            Object value = parseValue();
            object.put(key, value);
            skipWhitespace();
            if (peek('}')) {
                expect('}');
                return object;
            }
            expect(',');
            skipWhitespace();
        }
    }

    private List<Object> parseArray() {
        expect('[');
        List<Object> array = new ArrayList<>();
        skipWhitespace();
        if (peek(']')) {
            expect(']');
            return array;
        }

        while (true) {
            array.add(parseValue());
            skipWhitespace();
            if (peek(']')) {
                expect(']');
                return array;
            }
            expect(',');
        }
    }

    private String parseString() {
        expect('"');
        StringBuilder out = new StringBuilder();
        while (index < text.length()) {
            char c = text.charAt(index++);
            if (c == '"') {
                return out.toString();
            }
            if (c == '\\') {
                if (index >= text.length()) {
                    throw new IllegalArgumentException("Invalid escape sequence");
                }
                char escaped = text.charAt(index++);
                out.append(switch (escaped) {
                    case '"' -> '"';
                    case '\\' -> '\\';
                    case '/' -> '/';
                    case 'b' -> '\b';
                    case 'f' -> '\f';
                    case 'n' -> '\n';
                    case 'r' -> '\r';
                    case 't' -> '\t';
                    default -> throw new IllegalArgumentException("Unsupported escape: \\" + escaped);
                });
            } else {
                out.append(c);
            }
        }
        throw new IllegalArgumentException("Unterminated string");
    }

    private Object parseNumber() {
        int start = index;
        while (index < text.length()) {
            char c = text.charAt(index);
            if ((c >= '0' && c <= '9') || c == '-' || c == '+' || c == '.' || c == 'e' || c == 'E') {
                index++;
            } else {
                break;
            }
        }
        String raw = text.substring(start, index);
        if (raw.contains(".") || raw.contains("e") || raw.contains("E")) {
            return Double.parseDouble(raw);
        }
        return Long.parseLong(raw);
    }

    private Object parseLiteral(String literal, Object value) {
        if (!text.startsWith(literal, index)) {
            throw new IllegalArgumentException("Unexpected token at index " + index);
        }
        index += literal.length();
        return value;
    }

    private void expect(char expected) {
        skipWhitespace();
        if (index >= text.length() || text.charAt(index) != expected) {
            throw new IllegalArgumentException("Expected '" + expected + "' at index " + index);
        }
        index++;
    }

    private boolean peek(char value) {
        return index < text.length() && text.charAt(index) == value;
    }

    private void skipWhitespace() {
        while (index < text.length() && Character.isWhitespace(text.charAt(index))) {
            index++;
        }
    }
}
