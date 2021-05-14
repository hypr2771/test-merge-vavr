package net.vince.merge.test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.vavr.jackson.datatype.VavrModule;
import io.vavr.jackson.datatype.VavrModule.Settings;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParentJavaTest {

  @Test
  @SneakyThrows
  void test() {
    var mapper = new ObjectMapper().configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
                                   .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                                   .setDefaultPropertyInclusion(Include.NON_EMPTY)
                                   .registerModule(new VavrModule(new Settings().deserializeNullAsEmptyCollection(true)));

    var parent = """
        {
          "list": [
            "first",
            "second"
          ],
          "map": {
            "first_key": "first_value",
            "second_key": "second_value"
          },
          "deepMap": {
            "first_key": {
              "first_nested_key": "first_nested_value"
            },
            "second_key": {
              "second_nested_key": "second_nested_value"
            }
          },
          "child":{
            "name": "original_name",
            "description": "original_description"
          }
        }
        """;

    var toMerge = """
        {
          "list": [
            "first",
            "third"
          ],
          "map": {
            "first_key": "first_overridden_value",
            "third_key": "third_value"
          },
          "deepMap": {
            "first_key": {
              "first_overridden_nested_key": "first_overridden_nested_value"
            },
            "third_key": {
              "third_nested_key": "third_nested_value"
            }
          },
          "child":{
            "name": "overridden_name"
          }
        }
        """;

    var expected = """
        {
          "list": [
            "first",
            "third"
          ],
          "map": {
            "first_key": "first_overridden_value",
            "second_key": "second_value",
            "third_key": "third_value"
          },
          "deepMap": {
            "first_key": {
              "first_nested_key": "first_nested_value",
              "first_overridden_nested_key": "first_overridden_nested_value"
            },
            "second_key": {
              "second_nested_key": "second_nested_value"
            },
            "third_key": {
              "third_nested_key": "third_nested_value"
            }
          },
          "child":{
            "name": "overridden_name",
            "description": "original_description"
          }
        }
        """;

    var parentObject = mapper.readerFor(ParentJava.class).readValue(parent);

    var updated = mapper.readerForUpdating(parentObject).readValue(toMerge);

    var expectedObject = mapper.readerFor(ParentJava.class).readValue(expected);

    Assertions.assertEquals(updated, expectedObject);

  }

}
