package net.vince.merge.test;

import com.fasterxml.jackson.annotation.JsonMerge;
import com.fasterxml.jackson.annotation.OptBoolean;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ParentVavr {

  @JsonMerge(OptBoolean.FALSE)
  List<String> list;

  @JsonMerge
  Map<String, String> map;

  @JsonMerge
  Map<String, Map<String, String>> deepMap;

  @JsonMerge
  Child child;

  @Getter
  @Builder
  @ToString
  @EqualsAndHashCode
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Child {

    String name;
    String description;

  }

}
