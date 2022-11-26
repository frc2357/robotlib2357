#ifndef JSONELEMENT_H
#define JSONELEMENT_H

#include <Arduino.h>

#define JSON_TYPE_NONE      0
#define JSON_TYPE_BOOLEAN   1
#define JSON_TYPE_INT       2
#define JSON_TYPE_FLOAT     3
#define JSON_TYPE_STRING    4
#define JSON_TYPE_ARRAY     5
#define JSON_TYPE_OBJECT    6

// Switch the comments on the macro to enable debugging
//#define JSON_LOG_ERROR(...) JsonElement::logError(__VA_ARGS__)
#define JSON_LOG_ERROR(...) do {} while(0)

class JsonElement {
  public:
    static JsonElement NotFound;

    JsonElement();
    JsonElement(const JsonElement &element);
    ~JsonElement();

    JsonElement &operator[](const size_t index) const;
    JsonElement &operator[](const char *key) const;
    void operator=(JsonElement &element);
    void operator=(bool value);
    void operator=(int value);
    void operator=(long value);
    void operator=(float value);
    void operator=(double value);
    void operator=(const char *value);

    const char *type() const;
    const char *key() const;
    size_t length() const;
    bool hasChanged() const;
    void clearChanged();

    bool asBoolean() const;
    int asInt() const;
    long asLong() const;
    float asFloat() const;
    double asDouble() const;
    const char *asString() const;

    void printJson(int indent, Print &out) const;
    void printJson(int indent, Print &out, bool onlyChanged) const;

  protected:
    static Print &m_errorLog;

    static void logError(const char *format, ...);

    union JsonElementValue {
      bool booleanValue;
      long longValue;
      double doubleValue;
      char *stringValue;
      JsonElement *arrayValue;
    };

    const char *m_key;
    char m_type;
    JsonElementValue m_value;
    size_t m_length;
    bool m_hasChanged;

    friend class Json;
    friend class JsonState;
    friend class JsonUtils;

    JsonElement(const char *key, char type, JsonElementValue value, size_t length);
    void printJsonBoolean(int indent, Print &out) const;
    void printJsonInt(int indent, Print &out) const;
    void printJsonFloat(int indent, Print &out) const;
    void printJsonString(int indent, Print &out) const;
    void printJsonArray(int indent, Print &out, bool onlyChanged) const;
    void printJsonObject(int indent, Print &out, bool onlyChanged) const;

    JsonElement &findByKey(const char *key, size_t length) const;

    size_t updateFromJson(const char *json, size_t length, size_t &elementsUpdated);
    size_t updateFromJsonBoolean(const char *json, size_t length, size_t &elementsUpdated);
    size_t updateFromJsonInt(const char *json, size_t length, size_t &elementsUpdated);
    size_t updateFromJsonFloat(const char *json, size_t length, size_t &elementsUpdated);
    size_t updateFromJsonString(const char *json, size_t length, size_t &elementsUpdated);
    size_t updateFromJsonArray(const char *json, size_t length, size_t &elementsUpdated);
    size_t updateFromJsonObject(const char *json, size_t length, size_t &elementsUpdated);
};

class Json {
  public:
    static JsonElement Boolean(bool value) {
      return Boolean("", value);
    }

    static JsonElement Boolean(const char *key, bool value) {
      JsonElement::JsonElementValue elementValue;
      elementValue.booleanValue = value;
      return JsonElement(key, JSON_TYPE_BOOLEAN, elementValue, -1);
    }

    static JsonElement Int(long value) {
      return Int("", value);
    }

    static JsonElement Int(const char *key, long value) {
      JsonElement::JsonElementValue elementValue;
      elementValue.longValue = value;
      return JsonElement(key, JSON_TYPE_INT, elementValue, -1);
    }

    static JsonElement Float(double value) {
      return Float("", value);
    }

    static JsonElement Float(const char *key, double value) {
      JsonElement::JsonElementValue elementValue;
      elementValue.doubleValue = value;
      return JsonElement(key, JSON_TYPE_FLOAT, elementValue, -1);
    }

    static JsonElement String(const char *value) {
      return String("", value);
    }

    static JsonElement String(const char *value, size_t length) {
      return String("", value, length);
    }

    static JsonElement String(const char *key, const char *value) {
      return String(key, value, strlen(value) + 1);
    }

    static JsonElement String(const char *key, const char *value, size_t length) {
      size_t stringLength = length + 1; // for null-terminated strings

      JsonElement::JsonElementValue elementValue;
      elementValue.stringValue = new char[stringLength];
      strlcpy(elementValue.stringValue, value, stringLength);
      return JsonElement(key, JSON_TYPE_STRING, elementValue, stringLength);
    }

    template<size_t S>
    static JsonElement Array(JsonElement (&array)[S]) {
      return Array("", array);
    }

    template<size_t S>
    static JsonElement Array(const char *key, JsonElement (&array)[S]) {
      JsonElement::JsonElementValue elementValue;
      elementValue.arrayValue = array;
      return JsonElement(key, JSON_TYPE_ARRAY, elementValue, S);
    }

    template<size_t S>
    static JsonElement Object(JsonElement (&fields)[S]) {
      return Object("", fields);
    }

    template<size_t S>
    static JsonElement Object(const char *key, JsonElement (&fields)[S]) {
      JsonElement::JsonElementValue elementValue;
      elementValue.arrayValue = fields;
      return JsonElement(key, JSON_TYPE_OBJECT, elementValue, S);
    }
};

class JsonState {
  public:
    JsonState(JsonElement &root);
    ~JsonState();

    JsonElement &root() const;
    void printJson(Print &out) const;
    void printJson(Print &out, bool onlyChanged) const;
    void printJson(bool pretty, Print &out) const;
    void printJson(bool pretty, Print &out, bool onlyChanged) const;
    bool hasChanged();
    void clearChanged();
    bool updateFromJson(String &json);
    bool updateFromJson(const char *json);
    size_t updateFromJson(const char *json, size_t length, size_t &elementsUpdated);

  private:
    JsonElement &m_root;
};

class JsonUtils {
  public:
    static void indentNewline(int times, Print &out);
    static bool isWhitespace(char ch);
    static size_t countWhitespace(const char* str, size_t length);
    static size_t getNumberStringLength(const char* str, size_t length, bool isFloat);
    static size_t getQuoteStringLength(const char* str, size_t length);
    static size_t getObjectKeyStringLength(const char* str, size_t length);
    static JsonElement &findElementByJsonKey(JsonElement &jsonObject, const char* str, size_t length);
};

#endif /* JSONELEMENT_H */
