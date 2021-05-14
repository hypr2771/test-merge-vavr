package net.vince.merge.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.jackson.datatype.VavrModule;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParentTest {

  @Test
  @SneakyThrows
  void test(){
    var mapper = new ObjectMapper().registerModule(new VavrModule());

    var parent = Parent.builder()
                       .list(List.of("first", "second"))
                       .map(HashMap.of("first_key", "first_value",
                                       "second_key", "second_value"))
                       .deepMap(HashMap.of("first_key", HashMap.of("first_nested_key", "first_nested_value"),
                                           "second_key", HashMap.of("second_nested_key", "second_nested_value")))
                       .build();

    var toMerge = Parent.builder()
                       .list(List.of("first", "third"))
                       .map(HashMap.of("first_key", "first_value",
                                       "third_key", "third_value"))
                       .deepMap(HashMap.of("first_key", HashMap.of("first_nested_key", "first_nested_value"),
                                           "third_key", HashMap.of("third_nested_key", "third_nested_value")))
                       .build();

    var expected = Parent.builder()
                        .list(List.of("first", "third"))
                        .map(HashMap.of("first_key", "first_value",
                                        "second_key", "second_value",
                                        "third_key", "third_value"))
                        .deepMap(HashMap.of("first_key", HashMap.of("first_nested_key", "first_nested_value"),
                                            "second_key", HashMap.of("second_nested_key", "second_nested_value"),
                                            "third_key", HashMap.of("third_nested_key", "third_nested_value")))
                        .build();

    var originCopyAsString = mapper.writerFor(Parent.class).writeValueAsString(toMerge);

    var parentAsString = mapper.writerFor(Parent.class).writeValueAsString(parent);
    var parentCopy     = mapper.readerFor(Parent.class).readValue(parentAsString);

    var clone = mapper.readerForUpdating(parentCopy).readValue(originCopyAsString);

    Assertions.assertEquals(clone, expected);

  }

}
