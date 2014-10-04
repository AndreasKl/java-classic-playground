package net.andreaskluth.hibernate.entities;

import java.util.Map;

import com.google.common.collect.Maps;

import lombok.Data;

@Data
public class Driver {
  private int id;
  private String firstName;
  private String lastName;
  private Map<String,Car> cars = Maps.newHashMap();
}
