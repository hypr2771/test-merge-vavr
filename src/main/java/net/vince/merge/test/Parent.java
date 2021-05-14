package net.vince.merge.test;

import com.fasterxml.jackson.annotation.JsonMerge;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class Parent {

  @JsonMerge List<String> list;
  @JsonMerge Map<String, String> map;
  @JsonMerge Map<String, Map<String, String>> deepMap;

}
