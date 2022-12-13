#include "JsonElement.h"

// ---------
// JsonState
// ---------
JsonState::JsonState(JsonElement& root) : m_root(root) {}

JsonState::~JsonState() {
  JSON_LOG_ERROR("[WARNING: JsonState must be created in the root level sketch (.ino) file!]");
}

JsonElement &JsonState::root() const {
  return m_root;
}

void JsonState::printJson(Print &out) const {
  printJson(false, out, false);
}

void JsonState::printJson(Print &out, bool onlyChanged) const {
  printJson(false, out, onlyChanged);
}

void JsonState::printJson(bool pretty, Print &out) const {
  printJson(pretty, out, false);
}

void JsonState::printJson(bool pretty, Print &out, bool onlyChanged) const {
  m_root.printJson(pretty ? 0 : -1, out, onlyChanged);
}

bool JsonState::hasChanged() {
  return m_root.hasChanged();
}

void JsonState::clearChanged() {
  m_root.clearChanged();
}

bool JsonState::updateFromJson(String &json) {
  return updateFromJson(json.c_str());
}

bool JsonState::updateFromJson(const char* json) {
  size_t elementsUpdated = 0;
  size_t length = strlen(json);
  size_t charsRead = updateFromJson(json, length, elementsUpdated);

  if (charsRead == -1) {
    return false;
  }
  return true;
}

size_t JsonState::updateFromJson(const char* json, size_t length, size_t &elementsUpdated) {
  size_t whitespaceLength = JsonUtils::countWhitespace(json, length);
  json += whitespaceLength;
  length -= whitespaceLength;

  return m_root.updateFromJson(json, length, elementsUpdated);
}

// -----------
// JsonElement
// -----------
JsonElement JsonElement::NotFound;

Print &JsonElement::m_errorLog = Serial;

void JsonElement::logError(const char *format, ...) {
  va_list args;
  char message[64];
  va_start(args, format);
  vsprintf(message, format, args);
  va_end(args);

  m_errorLog.print("[JsonState ERROR: ");
  m_errorLog.print(message);
  m_errorLog.println("]");
}

JsonElement::JsonElement() {
  m_key = "";
  m_type = JSON_TYPE_NONE;
  m_value.arrayValue = NULL;
  m_length = -1;
  m_hasChanged = false;
}

JsonElement::JsonElement(const char *key, char type, JsonElementValue value, size_t length) {
  m_key = key;
  m_type = type;
  m_value = value;
  m_length = length;
  m_hasChanged = false;
}

JsonElement::JsonElement(const JsonElement& element) {
  m_key = element.m_key;
  m_type = element.m_type;
  m_value = element.m_value;
  m_length = element.m_length;
  m_hasChanged = element.m_hasChanged;
}

JsonElement::~JsonElement() {
  // Note: Strings do allocate via `new`, but they
  // should never be destructed because they should
  // be in the root .ino scope
}

JsonElement& JsonElement::operator[](const size_t index) const {
  if (m_type != JSON_TYPE_ARRAY && m_type != JSON_TYPE_OBJECT) {
    JSON_LOG_ERROR("Cannot access subelement of type %s", type());
    return NotFound;
  }

  if (index > m_length) {
    JSON_LOG_ERROR("Array index '%d' out of bounds.", index);
    return NotFound;
  }
  return m_value.arrayValue[index];
}

JsonElement& JsonElement::operator[](const char* key) const {
  return findByKey(key, strlen(key));
}

void JsonElement::operator=(const JsonElement& element) {
  m_key = element.m_key;
  m_type = element.m_type;
  m_value = element.m_value;
  m_length = element.m_length;
  m_hasChanged = element.m_hasChanged;
}

void JsonElement::operator=(bool value) {
  if (m_type == JSON_TYPE_BOOLEAN) {
    m_value.booleanValue = value;
    m_hasChanged = true;
  } else {
    JSON_LOG_ERROR("Cannot assign boolean value to type %s", type());
  }
}

void JsonElement::operator=(int value) {
  return operator=((long)value);
}

void JsonElement::operator=(long value) {
  if (m_type == JSON_TYPE_INT) {
    m_value.longValue = value;
    m_hasChanged = true;
  } else if (m_type == JSON_TYPE_FLOAT) {
    m_value.doubleValue = value;
    m_hasChanged = true;
  } else {
    JSON_LOG_ERROR("Cannot assign int value to type %s", type());
  }
}

void JsonElement::operator=(float value) {
  return operator=((double)value);
}

void JsonElement::operator=(double value) {
  if (m_type == JSON_TYPE_FLOAT) {
    m_value.doubleValue = value;
    m_hasChanged = true;
  } else {
    JSON_LOG_ERROR("Cannot assign double value to type %s", type());
  }
}

void JsonElement::operator=(const char *value) {
  if (m_type == JSON_TYPE_STRING) {
    if (m_length < strlen(value) + 1) {
      JSON_LOG_ERROR("Concatenating incoming string: %s to %d chars", value, m_length - 1);
    }
    strlcpy(m_value.stringValue, value, m_length);
    m_hasChanged = true;
  } else {
    JSON_LOG_ERROR("Cannot assign string value to type %s", type());
  }
}

const char* JsonElement::type() const {
  switch(m_type) {
    case JSON_TYPE_BOOLEAN:
      return "boolean";
    case JSON_TYPE_INT:
      return "int";
    case JSON_TYPE_FLOAT:
      return "float";
    case JSON_TYPE_STRING:
      return "string";
    case JSON_TYPE_ARRAY:
      return "array";
    case JSON_TYPE_OBJECT:
      return "object";
    default:
      return "(unknown)";
  }
}

const char* JsonElement::key() const {
  if (m_key[0] == '\0') {
    return NotFound.m_key;
  }
  return m_key;
}

size_t JsonElement::length() const {
  return m_length;
}

bool JsonElement::hasChanged() const {
  switch(m_type) {
    case JSON_TYPE_ARRAY:
    case JSON_TYPE_OBJECT:
      for (int i = 0; i < m_length; i++) {
        JsonElement& el = (*this)[i];
        if (el.hasChanged()) {
          return true;
        }
      }
      return false;
    default:
      return m_hasChanged;
  }
}

void JsonElement::clearChanged() {
  switch(m_type) {
    case JSON_TYPE_ARRAY:
    case JSON_TYPE_OBJECT:
      for (int i = 0; i < m_length; i++) {
        JsonElement& el = (*this)[i];
        el.clearChanged();
      }
    default:
      m_hasChanged = false;
  }
}

bool JsonElement::asBoolean() const {
  if (m_type == JSON_TYPE_BOOLEAN) {
    return m_value.booleanValue;
  }
  JSON_LOG_ERROR("Cannot access type %s as boolean", type());
  return false;
}

int JsonElement::asInt() const {
  if (m_type == JSON_TYPE_INT) {
    return m_value.longValue;
  }
  JSON_LOG_ERROR("Cannot access type %s as int", type());
  return -1;
}

long JsonElement::asLong() const {
  if (m_type == JSON_TYPE_INT) {
    return m_value.longValue;
  }
  JSON_LOG_ERROR("Cannot access type %s as int", type());
  return -1;
}

float JsonElement::asFloat() const {
  if (m_type == JSON_TYPE_FLOAT) {
    return (float) m_value.doubleValue;
  }
  JSON_LOG_ERROR("Cannot access type %s as float", type());
  return NAN;
}

double JsonElement::asDouble() const {
  if (m_type == JSON_TYPE_FLOAT) {
    return m_value.doubleValue;
  }
  JSON_LOG_ERROR("Cannot access type %s as double", type());
  return NAN;
}

const char* JsonElement::asString() const {
  if (m_type == JSON_TYPE_STRING) {
    return m_value.stringValue;
  }
  JSON_LOG_ERROR("Cannot access type %s as string", type());
  return "";
}

JsonElement &JsonElement::findByKey(const char *key, size_t length) const {
  if (m_type != JSON_TYPE_OBJECT) {
    JSON_LOG_ERROR("Cannot access key of type %s", type());
    return NotFound;
  }

  for (size_t i = 0; i < m_length; i++) {
    JsonElement& el = m_value.arrayValue[i];
    if (strncmp(el.key(), key, length) == 0) {
      return el;
    }
  }
  JSON_LOG_ERROR("Key '%s' not found.", key);
  return NotFound;
}

void JsonElement::printJson(int indent, Print& out) const {
  printJson(indent, out, false);
}

void JsonElement::printJson(int indent, Print& out, bool onlyChanged) const {

  switch (m_type) {
    case JSON_TYPE_BOOLEAN:
      return printJsonBoolean(indent, out);
    case JSON_TYPE_INT:
      return printJsonInt(indent, out);
    case JSON_TYPE_FLOAT:
      return printJsonFloat(indent, out);
    case JSON_TYPE_STRING:
      return printJsonString(indent, out);
    case JSON_TYPE_ARRAY:
      return printJsonArray(indent, out, onlyChanged);
    case JSON_TYPE_OBJECT:
      return printJsonObject(indent, out, onlyChanged);
    default:
      out.print("\"unknown type\"");
  }
}

void JsonElement::printJsonBoolean(int indent, Print& out) const {
  out.print(m_value.booleanValue ? "true" : "false");
}

void JsonElement::printJsonInt(int indent, Print& out) const {
  out.print(m_value.longValue);
}

void JsonElement::printJsonFloat(int indent, Print& out) const {
  out.print(m_value.doubleValue);
}

void JsonElement::printJsonString(int indent, Print& out) const {
  out.print("\"");
  out.print(m_value.stringValue);
  out.print("\"");
}

void JsonElement::printJsonArray(int indent, Print& out, bool onlyChanged) const {
  if (onlyChanged && !hasChanged()) {
    return;
  }

  int childIndent = indent >= 0 ? indent + 1 : -1;

  out.print("[");
  for (size_t i = 0; i < m_length; i++) {
    JsonElement& element = m_value.arrayValue[i];
    JsonUtils::indentNewline(childIndent, out);
    element.printJson(childIndent, out, onlyChanged);
    if (i < m_length - 1) {
      out.print(',');
    }
  }
  JsonUtils::indentNewline(indent, out);
  out.print("]");
}

void JsonElement::printJsonObject(int indent, Print& out, bool onlyChanged) const {
  if (onlyChanged && !hasChanged()) {
    return;
  }

  int childIndent = indent >= 0 ? indent + 1 : -1;
  int elementsWritten = 0;

  out.print("{");
  for (size_t i = 0; i < m_length; i++) {
    JsonElement& element = m_value.arrayValue[i];

    if (onlyChanged && !element.hasChanged()) {
      continue;
    }
    if (strlen(element.key()) == 0) {
      continue;
    }

    if (elementsWritten > 0) {
      out.print(',');
    }

    JsonUtils::indentNewline(childIndent, out);
    out.print("\"");
    out.print(element.key());
    out.print("\":");
    if (indent >= 0) {
      out.print(" ");
    }
    element.printJson(childIndent, out, onlyChanged);
    elementsWritten++;
  }
  JsonUtils::indentNewline(indent, out);
  out.print("}");
}

size_t JsonElement::updateFromJson(const char* json, size_t length, size_t &elementsUpdated) {
  switch(m_type) {
    case JSON_TYPE_BOOLEAN:
      return updateFromJsonBoolean(json, length, elementsUpdated);
    case JSON_TYPE_INT:
      return updateFromJsonInt(json, length, elementsUpdated);
    case JSON_TYPE_FLOAT:
      return updateFromJsonFloat(json, length, elementsUpdated);
    case JSON_TYPE_STRING:
      return updateFromJsonString(json, length, elementsUpdated);
    case JSON_TYPE_ARRAY:
      return updateFromJsonArray(json, length, elementsUpdated);
    case JSON_TYPE_OBJECT:
      return updateFromJsonObject(json, length, elementsUpdated);
    default:
      return 0;
  }
}

size_t JsonElement::updateFromJsonBoolean(const char *json, size_t length, size_t &elementsUpdated) {
  if (strncasecmp(json, "true", 4) == 0) {
    operator=(true);
    elementsUpdated++;
    return 4;
  }
  if (strncasecmp(json, "false", 5) == 0) {
    operator=(false);
    elementsUpdated++;
    return 5;
  }

  JSON_LOG_ERROR("Expected 'true' or 'false': %s", json);
  return -1;
}

size_t JsonElement::updateFromJsonInt(const char *json, size_t length, size_t &elementsUpdated) {
  size_t intLength = JsonUtils::getNumberStringLength(json, length, false);

  if (intLength == -1) {
    return -1;
  }

  operator=(atol(json));
  elementsUpdated++;
  return intLength;
}

size_t JsonElement::updateFromJsonFloat(const char *json, size_t length, size_t &elementsUpdated) {
  size_t floatLength = JsonUtils::getNumberStringLength(json, length, true);

  if (floatLength == -1) {
    return -1;
  }

  operator=(atof(json));
  elementsUpdated++;
  return floatLength;
}

size_t JsonElement::updateFromJsonString(const char *json, size_t length, size_t &elementsUpdated) {
  size_t stringLength = JsonUtils::getQuoteStringLength(json, length);

  if (stringLength == -1) {
    return -1;
  }

  strlcpy(m_value.stringValue, &json[1], stringLength -1);
  m_hasChanged = true;
  elementsUpdated++;
  return stringLength;
}

size_t JsonElement::updateFromJsonArray(const char *json, size_t length, size_t &elementsUpdated) {
  if (json[0] != '[') {
    JSON_LOG_ERROR("Expected array to start with '[': %s", json);
    return -1;
  }

  const char *arrayJson = json + 1;
  size_t remainingLength = length - 1;
  size_t arrayIndex = 0;

  while (remainingLength > 0 && arrayJson[0] != '\0') {
    // Advance past whitespace before element
    size_t whitespaceLength = JsonUtils::countWhitespace(arrayJson, remainingLength);
    arrayJson += whitespaceLength;
    remainingLength -= whitespaceLength;

    // Update the element
    JsonElement &element = (*this)[arrayIndex];
    size_t elementLength = element.updateFromJson(arrayJson, remainingLength, elementsUpdated);
    if (elementLength == -1) {
      return -1;
    }
    arrayJson += elementLength;
    remainingLength -= elementLength;

    // Advance past whitespace after value
    whitespaceLength = JsonUtils::countWhitespace(arrayJson, remainingLength);
    arrayJson += whitespaceLength;
    remainingLength -= whitespaceLength;

    if (arrayJson[0] == ']') {
      // We've reached the end of this array.
      if (this->m_length != arrayIndex + 1) {
        JSON_LOG_ERROR("Expected array of size %d instead of %d", this->m_length, arrayIndex + 1);
        return -1;
      }
      return (length - remainingLength) + 1;
    }
    if (arrayJson[0] == ',') {
      // Another element is next, advance past comma and iterate
      arrayIndex++;
      arrayJson++;
      remainingLength--;
      continue;
    }

    // We didn't find the end of the object or a comma, so this is not a well-formed object.
    JSON_LOG_ERROR("Expected either ',' or ']': ", arrayJson);
    return -1;
  }

  // If we didn't return from the ']' inside the loop, it's an error.
  JSON_LOG_ERROR("Expected array end ']': ", arrayJson);
  return -1;
}

size_t JsonElement::updateFromJsonObject(const char *json, size_t length, size_t &elementsUpdated) {
  if (json[0] != '{') {
    JSON_LOG_ERROR("Expected object to start with '{': %s", json);
    return -1;
  }

  // Advance past open { brace
  const char *objectJson = json + 1;
  size_t remainingLength = length - 1;

  while (remainingLength > 0 && objectJson[0] != '\0') {
    // Advance past whitespace before key
    size_t whitespaceLength = JsonUtils::countWhitespace(objectJson, remainingLength);
    objectJson += whitespaceLength;
    remainingLength -= whitespaceLength;

    size_t colonIndex = strcspn(objectJson, ":");
    size_t keyLength = JsonUtils::getObjectKeyStringLength(objectJson, colonIndex);
    if (keyLength == -1) {
      return -1;
    }

    JsonElement &element = JsonUtils::findElementByJsonKey(*this, objectJson, keyLength);
    if (&element == &JsonElement::NotFound) {
      JSON_LOG_ERROR("Failed to find element by key: %s", objectJson);
      return -1;
    }

    // Advance past key and :
    objectJson += colonIndex + 1;
    remainingLength -= colonIndex + 1;

    // Advance past whitespace before value
    whitespaceLength = JsonUtils::countWhitespace(objectJson, remainingLength);
    objectJson += whitespaceLength;
    remainingLength -= whitespaceLength;

    // Update the field element
    size_t elementLength = element.updateFromJson(objectJson, remainingLength, elementsUpdated);
    if (elementLength == -1) {
      return -1;
    }

    objectJson += elementLength;
    remainingLength -= elementLength;

    // Advance past whitespace after value
    whitespaceLength = JsonUtils::countWhitespace(objectJson, remainingLength);
    objectJson += whitespaceLength;
    remainingLength -= whitespaceLength;

    if (objectJson[0] == '}') {
      // We've reached the end of this object.
      return length - remainingLength;
    }
    if (objectJson[0] == ',') {
      // Another field is next, advance past comma and iterate
      objectJson++;
      remainingLength--;
      continue;
    }

    // We didn't find the end of the object or a comma, so this is not a well-formed object.
    JSON_LOG_ERROR("Expected either ',' or '}': ", objectJson);
    return -1;
  }

  // If we didn't return from the '}' inside the loop, it's an error.
  JSON_LOG_ERROR("Expected object end '}': ", objectJson);
  return -1;
}


// ---------
// JsonUtils
// ---------
void JsonUtils::indentNewline(int times, Print &out) {
  if (times < 0) {
    return;
  }

  out.println();

  for (int i = 0; i < times; i++) {
    out.print("  ");
  }
}

bool JsonUtils::isWhitespace(char ch) {
  return ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r';
}

size_t JsonUtils::countWhitespace(const char* str, size_t length) {
  size_t i = 0;

  while (isWhitespace(str[i]) && i < length) {
    i++;
  }
  return i;
}

size_t JsonUtils::getNumberStringLength(const char* str, size_t length, bool isFloat) {
  int i = 0;
  bool hasDigit = false;
  bool isDecimal = false;
  bool isExponential = false;

  if (str[0] == '-') {
    // It's a negative number, advance.
    i = 1;
  }

  for (; i < length && str[i] != '\0'; i++) {
    int ch = str[i];
    if (isDigit(ch)) {
      hasDigit = true;
      continue;
    }
    if (isFloat && ch == '.' && !isDecimal) {
      isDecimal = true;
      continue;
    }
    if (isFloat && ch == 'e' && !isExponential && isDecimal) {
      isExponential = true;
      continue;
    }
    if (isWhitespace(ch) || ch == ',' || ch == ']' || ch == '}') {
      if (hasDigit) {
        // We reached the end of the number
        return i;
      }
    }
    // None of the above are true, so this is not a JSON-compatible number.
    JSON_LOG_ERROR("Expected number: %s", str);
    return -1;
  }
  // We reached the end of the string
  return i;
}

size_t JsonUtils::getQuoteStringLength(const char* str, size_t length) {
  char quoteChar = str[0];

  for (size_t i = 1; i < length && str[i] != '\0'; i++) {
    if (str[i] == '\\') {
      // Escape next character
      i++;
      continue;
    }
    if (str[i] == quoteChar) {
      return i + 1;
    }
  }

  JSON_LOG_ERROR("Expected quote '%c': %s", quoteChar, str);
  return -1;
}

size_t JsonUtils::getObjectKeyStringLength(const char* str, size_t length) {
  if (str[0] == '"' || str[0] == '\'') {
    return getQuoteStringLength(str, length);
  }

  for (size_t i = 0; str[i] != '\0'; i++) {
    if (str[i] == ':' || isWhitespace(str[i])) {
      return i;
    }
  }
  JSON_LOG_ERROR("Expected colon ':': %s", str);
  return -1;
}

JsonElement &JsonUtils::findElementByJsonKey(JsonElement &jsonObject, const char* str, size_t length) {
  char keyEndChar = ':';
  size_t keyLength = 0;

  if (str[0] == '"' || str[0] == '\'') {
    keyEndChar = str[0];
    str++;
    length--;
  }

  while (str[keyLength] != keyEndChar) {
    if (str[keyLength] == '\0' || keyLength == length) {
      JSON_LOG_ERROR("Expected key end '%c': %s", keyEndChar, str);
      return JsonElement::NotFound;
    }
    keyLength++;
  }

  return jsonObject.findByKey(str, keyLength);
}
