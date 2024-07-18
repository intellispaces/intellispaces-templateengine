package tech.intellispaces.framework.templateengine.template.expression.value;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.intellispaces.framework.templateengine.exception.ResolveTemplateException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link ValueFunctions}.
 */
public class ValueFunctionsTest {

  @Test
  public void testObjectToValue_whenNull() throws Exception {
    // When
    Value value = ValueFunctions.objectToValue(null);

    // Then
    assertThat(value.type()).isEqualTo(ValueTypes.Void);
  }

  @Test
  public void testObjectToValue_whenBoolean() throws Exception {
    // When
    Value value = ValueFunctions.objectToValue(true);

    // Then
    assertThat(value.type()).isEqualTo(ValueTypes.Boolean);
    assertThat(((BooleanValue) value).get()).isTrue();
  }

  @Test
  public void testObjectToValuewhenInteger() throws Exception {
    // When
    Value value = ValueFunctions.objectToValue(1);

    // Then
    assertThat(value.type()).isEqualTo(ValueTypes.Integer);
    assertThat(((IntegerValue) value).get()).isEqualTo(1);
  }

  @Test
  public void testObjectToValue_whenLong() {
    assertThatThrownBy(() -> ValueFunctions.objectToValue(1L))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("Object of type java.lang.Long can't be casted to value");
  }

  @Test
  public void testObjectToValue_whenDouble() throws Exception {
    // When
    Value value = ValueFunctions.objectToValue(1.2);

    // Then
    assertThat(value.type()).isEqualTo(ValueTypes.Real);
    assertThat(((RealValue) value).get()).isEqualTo(1.2);
  }

  @Test
  public void testObjectToValue_whenFloat() {
    assertThatThrownBy(() -> ValueFunctions.objectToValue(1.0f))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("Object of type java.lang.Float can't be casted to value");
  }

  @Test
  public void testObjectToValue_whenString() throws Exception {
    // When
    Value value = ValueFunctions.objectToValue("abc");

    // Then
    assertThat(value.type()).isEqualTo(ValueTypes.String);
    assertThat(((StringValue) value).get()).isEqualTo("abc");
  }

  @Test
  public void testObjectToValue_whenChar() throws Exception {
    // When
    Value value = ValueFunctions.objectToValue('i');

    // Then
    assertThat(value.type()).isEqualTo(ValueTypes.String);
    assertThat(((StringValue) value).get()).isEqualTo("i");
  }

  @Test
  public void testObjectToValue_whenStringList() throws Exception {
    // When
    Value value = ValueFunctions.objectToValue(List.of("a", "b", "c"));

    // Then
    assertThat(value.type()).isEqualTo(ValueTypes.List);
    Assertions.assertThat(((ListValue) value).get()).isEqualTo(List.of(
        StringValues.get("a"), StringValues.get("b"), StringValues.get("c")
    ));
  }

  @Test
  public void testObjectToValue_whenFloatList() {
    assertThatThrownBy(() -> ValueFunctions.objectToValue(List.of(1.0f, 2.0f)))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("Object of type java.lang.Float can't be casted to value");
  }

  @Test
  public void testObjectToValue_whenIntegerToStringMap() throws Exception {
    // When
    Value value = ValueFunctions.objectToValue(Map.of(1, "a", 2, "b"));

    // Then
    assertThat(value.type()).isEqualTo(ValueTypes.Map);
    assertThat(((MapValue) value).get()).isEqualTo(Map.of(
        IntegerValues.get(1), StringValues.get("a"),
        IntegerValues.get(2), StringValues.get("b")
    ));
  }

  @Test
  public void testObjectToValue_whenIntegerToFloatMap() {
    assertThatThrownBy(() -> ValueFunctions.objectToValue(Map.of(1, 1.0f, 2, 2.0f)))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("Object of type java.lang.Float can't be casted to value");
  }

  @Test
  public void testValueToObject_whenVoid() {
    assertThat(ValueFunctions.valueToObject(VoidValues.get())).isNull();
  }

  @Test
  public void testValueToObject_whenBoolean() {
    assertThat(ValueFunctions.valueToObject(BooleanValues.get(true))).isEqualTo(true);
    assertThat(ValueFunctions.valueToObject(BooleanValues.get(false))).isEqualTo(false);
  }

  @Test
  public void testValueToObject_whenInteger() {
    assertThat(ValueFunctions.valueToObject(IntegerValues.get(1))).isEqualTo(1);
    assertThat(ValueFunctions.valueToObject(IntegerValues.get(0))).isEqualTo(0);
    assertThat(ValueFunctions.valueToObject(IntegerValues.get(-1))).isEqualTo(-1);
  }

  @Test
  public void testValueToObject_whenReal() {
    assertThat(ValueFunctions.valueToObject(RealValues.get(1))).isEqualTo(1.0);
    assertThat(ValueFunctions.valueToObject(RealValues.get(0))).isEqualTo(0.0);
    assertThat(ValueFunctions.valueToObject(RealValues.get(-1))).isEqualTo(-1.0);
  }

  @Test
  public void testValueToObject_whenString() {
    assertThat(ValueFunctions.valueToObject(StringValues.get(""))).isEqualTo("");
    assertThat(ValueFunctions.valueToObject(StringValues.get("abc"))).isEqualTo("abc");
    assertThat(ValueFunctions.valueToObject(StringValues.get('a'))).isEqualTo("a");
  }

  @Test
  public void testValueToObject_whenList() {
    assertThat(ValueFunctions.valueToObject(ListValues.empty())).isEqualTo(List.of());
    assertThat(ValueFunctions.valueToObject(ListValues.get("a", "b", "c"))).isEqualTo(List.of("a", "b", "c"));
  }

  @Test
  public void testValueToObject_whenMap() throws Exception {
    assertThat(ValueFunctions.valueToObject(MapValues.empty())).isEqualTo(Map.of());
    assertThat(ValueFunctions.valueToObject(MapValues.get(1, "a", 2, "b"))).isEqualTo(Map.of(1, "a", 2, "b"));
  }

  @Test
  public void testCastToBoolean() throws Exception {
    // When void
    assertThatThrownBy(() -> ValueFunctions.castToBoolean(VoidValues.get()))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("Value of type void can't be casted to boolean primitive");

    // When boolean
    assertThat(ValueFunctions.castToBoolean(BooleanValues.get(true))).isTrue();
    assertThat(ValueFunctions.castToBoolean(BooleanValues.get(false))).isFalse();

    // When integer
    assertThat(ValueFunctions.castToBoolean(IntegerValues.get(0))).isFalse();
    assertThat(ValueFunctions.castToBoolean(IntegerValues.get(1))).isTrue();
    assertThatThrownBy(() -> ValueFunctions.castToBoolean(IntegerValues.get(2)))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("Integer value 2 can't be casted to boolean primitive");

    // When real
    assertThat(ValueFunctions.castToBoolean(RealValues.get(0.0))).isFalse();
    assertThat(ValueFunctions.castToBoolean(RealValues.get(1.0))).isTrue();
    assertThatThrownBy(() -> ValueFunctions.castToBoolean(RealValues.get(1.1)))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("Real value 1.1 can't be casted to boolean primitive");

    // When string
    assertThat(ValueFunctions.castToBoolean(StringValues.get("true"))).isTrue();
    assertThat(ValueFunctions.castToBoolean(StringValues.get("false"))).isFalse();
    assertThatThrownBy(() -> ValueFunctions.castToBoolean(StringValues.get("abc")))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("String value abc can't be casted to boolean primitive");

    // When list
    assertThatThrownBy(() -> ValueFunctions.castToBoolean(ListValues.get(1, 2, 3)))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("Value of type list can't be casted to boolean primitive");

    // When map
    assertThatThrownBy(() -> ValueFunctions.castToBoolean(MapValues.get(1, "a", 2, "b")))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("Value of type map can't be casted to boolean primitive");
  }

  @Test
  public void testCastToInteger() throws Exception {
    // When void
    assertThatThrownBy(() -> ValueFunctions.castToInteger(VoidValues.get()))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("Value of type void can't be casted to integer");

    // When boolean
    assertThat(ValueFunctions.castToInteger(BooleanValues.get(true))).isEqualTo(1);
    assertThat(ValueFunctions.castToInteger(BooleanValues.get(false))).isEqualTo(0);

    // When integer
    assertThat(ValueFunctions.castToInteger(IntegerValues.get(1))).isEqualTo(1);
    assertThat(ValueFunctions.castToInteger(IntegerValues.get(-1))).isEqualTo(-1);

    // When real
    assertThat(ValueFunctions.castToInteger(RealValues.get(1.0))).isEqualTo(1);
    assertThat(ValueFunctions.castToInteger(RealValues.get(2.0))).isEqualTo(2);
    assertThat(ValueFunctions.castToInteger(RealValues.get(-2.0))).isEqualTo(-2);
    assertThatThrownBy(() -> ValueFunctions.castToInteger(RealValues.get(1.1)))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("Real 1.1 can't be casted to integer");

    // When string
    assertThat(ValueFunctions.castToInteger(StringValues.get("123"))).isEqualTo(123);
    assertThatThrownBy(() -> ValueFunctions.castToInteger(StringValues.get("1a")))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("String 1a can't be casted to integer");

    // When list
    assertThatThrownBy(() -> ValueFunctions.castToInteger(ListValues.get(1, 2, 3)))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("Value of type list can't be casted to integer");

    // When map
    assertThatThrownBy(() -> ValueFunctions.castToInteger(MapValues.get(1, "a", 2, "b")))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("Value of type map can't be casted to integer");
  }

  @Test
  public void testCastToReal() throws Exception {
    // When void
    assertThatThrownBy(() -> ValueFunctions.castToReal(VoidValues.get()))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("Value of type void can't be casted to real");

    // When boolean
    assertThat(ValueFunctions.castToReal(BooleanValues.get(true))).isEqualTo(1.0);
    assertThat(ValueFunctions.castToReal(BooleanValues.get(false))).isEqualTo(0.0);

    // When integer
    assertThat(ValueFunctions.castToReal(IntegerValues.get(1))).isEqualTo(1.0);
    assertThat(ValueFunctions.castToReal(IntegerValues.get(-1))).isEqualTo(-1.0);

    // When real
    assertThat(ValueFunctions.castToReal(RealValues.get(1))).isEqualTo(1.0);
    assertThat(ValueFunctions.castToReal(RealValues.get(-1))).isEqualTo(-1.0);

    // When string
    assertThat(ValueFunctions.castToReal(StringValues.get("1"))).isEqualTo(1.0);
    assertThat(ValueFunctions.castToReal(StringValues.get("2.0"))).isEqualTo(2.0);
    assertThat(ValueFunctions.castToReal(StringValues.get("3.14"))).isEqualTo(3.14);
    assertThatThrownBy(() -> ValueFunctions.castToReal(StringValues.get("3p14")))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("String 3p14 can't be casted to real");

    // When list
    assertThatThrownBy(() -> ValueFunctions.castToReal(ListValues.get(1, 2, 3)))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("Value of type list can't be casted to real");

    // When map
    assertThatThrownBy(() -> ValueFunctions.castToReal(MapValues.get(1, "a", 2, "b")))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("Value of type map can't be casted to real");
  }

  @Test
  public void testCastToString() throws Exception {
    // When void
    assertThatThrownBy(() -> ValueFunctions.castToString(VoidValues.get()))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("Value of type void can't be casted to string");

    // When boolean
    assertThat(ValueFunctions.castToString(BooleanValues.get(true))).isEqualTo("true");
    assertThat(ValueFunctions.castToString(BooleanValues.get(false))).isEqualTo("false");

    // When integer
    assertThat(ValueFunctions.castToString(IntegerValues.get(1))).isEqualTo("1");
    assertThat(ValueFunctions.castToString(IntegerValues.get(-1))).isEqualTo("-1");

    // When real
    assertThat(ValueFunctions.castToString(RealValues.get(1))).isEqualTo("1.0");
    assertThat(ValueFunctions.castToString(RealValues.get(-1))).isEqualTo("-1.0");

    // When string
    assertThat(ValueFunctions.castToString(StringValues.get("abc"))).isEqualTo("abc");

    // When list
    assertThatThrownBy(() -> ValueFunctions.castToString(ListValues.get(1, 2, 3)))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("Value of type list can't be casted to string");

    // When map
    assertThatThrownBy(() -> ValueFunctions.castToString(MapValues.get(1, "a", 2, "b")))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("Value of type map can't be casted to string");
  }

  @Test
  public void testCastToList() throws Exception {
    // When void
    assertThatThrownBy(() -> ValueFunctions.castToList(VoidValues.get()))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("Value of type void can't be casted to list");

    // When boolean
    assertThat(ValueFunctions.castToList(BooleanValues.get(true))).isEqualTo(List.of(BooleanValues.get(true)));
    assertThat(ValueFunctions.castToList(BooleanValues.get(false))).isEqualTo(List.of(BooleanValues.get(false)));

    // When integer
    assertThat(ValueFunctions.castToList(IntegerValues.get(1))).isEqualTo(List.of(IntegerValues.get(1)));
    assertThat(ValueFunctions.castToList(IntegerValues.get(-1))).isEqualTo(List.of(IntegerValues.get(-1)));

    // When real
    assertThat(ValueFunctions.castToList(RealValues.get(1))).isEqualTo(List.of(RealValues.get(1)));
    assertThat(ValueFunctions.castToList(RealValues.get(-1))).isEqualTo(List.of(RealValues.get(-1)));

    // When string
    assertThat(ValueFunctions.castToList(StringValues.get("abc"))).isEqualTo(List.of(StringValues.get("abc")));

    // When list
    assertThat(ValueFunctions.castToList(ListValues.get(1, 2))).isEqualTo(List.of(IntegerValues.get(1), IntegerValues.get(2)));

    // When map
    assertThat(ValueFunctions.castToList(MapValues.get(1, "a", 2, "b"))).isEqualTo(List.of(
        MapValues.get(1, "a", 2, "b")));
  }

  @Test
  public void testCastToMap() throws Exception {
    // When void
    assertThatThrownBy(() -> ValueFunctions.castToMap(VoidValues.get()))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("Value of type void can't be casted to map");

    // When boolean
    assertThatThrownBy(() -> ValueFunctions.castToMap(BooleanValues.get(true)))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("Value of type boolean can't be casted to map");

    // When integer
    assertThatThrownBy(() -> ValueFunctions.castToMap(IntegerValues.get(1)))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("Value of type integer can't be casted to map");

    // When real
    assertThatThrownBy(() -> ValueFunctions.castToMap(RealValues.get(1.0)))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("Value of type real can't be casted to map");

    // When string
    assertThatThrownBy(() -> ValueFunctions.castToMap(StringValues.get("abc")))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("Value of type string can't be casted to map");

    // When list
    assertThatThrownBy(() -> ValueFunctions.castToMap(ListValues.get(1, 2, 3)))
        .isExactlyInstanceOf(ResolveTemplateException.class)
        .hasMessage("Value of type list can't be casted to map");

    // When map
    assertThat(ValueFunctions.castToMap(MapValues.get(1, "a", 2, "b"))).isEqualTo(Map.of(
        IntegerValues.get(1), StringValues.get("a"),
        IntegerValues.get(2), StringValues.get("b")));
  }
}
