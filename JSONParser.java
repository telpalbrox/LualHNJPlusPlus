import java.util.Hashtable;
import java.util.Vector;
import java.lang.RuntimeException;

public class JSONParser {
    private int pos;
    private String json;

    public Object parse(String json) {
        this.json = json;
        this.pos = 0;
        return readValue();
    }

    private Object readValue() {
        skipWhitespace();
        char ch = json.charAt(pos);
        if (ch == '{') {
            return readObject();
        } else if (ch == '[') {
            return readArray();
        } else if (ch == '\"') {
            return readString();
        } else if (ch == 't' || ch == 'f') {
            return readBoolean();
        } else if (ch == 'n') {
            return readNull();
        } else if ((ch >= '0' && ch <= '9') || ch == '-') {
            return readNumber();
        } else {
            throw new RuntimeException("Unexpected character: " + ch);
        }
    }

    private Hashtable readObject() {
        Hashtable object = new Hashtable();
        pos++; // skip '{'
        while (true) {
            skipWhitespace();
            if (json.charAt(pos) == '}') {
                pos++; // skip '}'
                break;
            }
            String key = readString();
            skipWhitespace();
            if (json.charAt(pos) != ':') {
                throw new RuntimeException("Expected ':' after key");
            }
            pos++; // skip ':'
            skipWhitespace();
            Object value = readValue();
			if (value != null) {
				object.put(key, value);
			}
            skipWhitespace();
            if (json.charAt(pos) == ',') {
                pos++; // skip ','
            } else if (json.charAt(pos) == '}') {
                pos++; // skip '}'
                break;
            } else {
                throw new RuntimeException("Expected ',' or '}'");
            }
        }
        return object;
    }

    private Vector readArray() {
        Vector array = new Vector();
        pos++; // skip '['
        while (true) {
            skipWhitespace();
            if (json.charAt(pos) == ']') {
                pos++; // skip ']'
                break;
            }
            Object value = readValue();
            array.addElement(value);
            skipWhitespace();
            if (json.charAt(pos) == ',') {
                pos++; // skip ','
            } else if (json.charAt(pos) == ']') {
                pos++; // skip ']'
                break;
            } else {
                throw new RuntimeException("Expected ',' or ']'");
            }
        }
        return array;
    }

    private String readString() {
        StringBuffer sb = new StringBuffer();
        pos++; // skip '\"'
        while (true) {
            char ch = json.charAt(pos);
            if (ch == '\"') {
                pos++; // skip '\"'
                break;
            } else if (ch == '\\') {
                pos++; // skip '\\'
                ch = json.charAt(pos);
                if (ch == '\"' || ch == '\\' || ch == '/' || ch == 'b' || ch == 'f' || ch == 'n' || ch == 'r' || ch == 't') {
                    sb.append(ch);
                } else {
                    throw new RuntimeException("Invalid escape character: " + ch);
                }
            } else {
                sb.append(ch);
            }
            pos++;
        }
        return sb.toString();
    }

    private Boolean readBoolean() {
        if (json.startsWith("true", pos)) {
            pos += 4;
            return Boolean.TRUE;
        } else if (json.startsWith("false", pos)) {
            pos += 5;
            return Boolean.FALSE;
        } else {
            throw new RuntimeException("Expected 'true' or 'false'");
        }
    }

    private Object readNull() {
        if (json.startsWith("null", pos)) {
            pos += 4;
            return null;
        } else {
            throw new RuntimeException("Expected 'null'");
        }
    }

    private Number readNumber() {
        int start = pos;
        if (json.charAt(pos) == '-') {
            pos++;
        }
        while (pos < json.length() && Character.isDigit(json.charAt(pos))) {
            pos++;
        }
        if (pos < json.length() && json.charAt(pos) == '.') {
            pos++;
            while (pos < json.length() && Character.isDigit(json.charAt(pos))) {
                pos++;
            }
        }
        if (pos < json.length() && (json.charAt(pos) == 'e' || json.charAt(pos) == 'E')) {
            pos++;
            if (pos < json.length() && (json.charAt(pos) == '+' || json.charAt(pos) == '-')) {
                pos++;
            }
            while (pos < json.length() && Character.isDigit(json.charAt(pos))) {
                pos++;
            }
        }
        String numberStr = json.substring(start, pos);
        if (numberStr.indexOf('.') >= 0 || numberStr.indexOf('e') >= 0 || numberStr.indexOf('E') >= 0) {
            return Double.valueOf(numberStr);
        } else {
            return Integer.valueOf(numberStr);
        }
    }

    private void skipWhitespace() {
        while (pos < json.length() && Character.isWhitespace(json.charAt(pos))) {
            pos++;
        }
    }
	
	public static Vector parseArray(String json) {
		return (Vector) new JSONParser().parse(json);
	}
	
	public static Hashtable parseObject(String json) {
		return (Hashtable) new JSONParser().parse(json);
	}

    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
        String jsonString = "{\"name\": \"John\", \"age\": 30, \"isStudent\": false, \"courses\": [\"Math\", \"Science\"]}";
        try {
            Hashtable json = (Hashtable) parser.parse(jsonString);
            System.out.println("Parsed JSON: " + json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
