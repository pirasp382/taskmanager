package org.example.services.validation;

import org.example.dto.database.TaskEntity;

import java.lang.reflect.Field;
import java.util.Arrays;

public class TaskValidation {

  public static boolean tableContainsColumn(final String columnName) {
    final Field[] columns = TaskEntity.class.getDeclaredFields();
    return Arrays.stream(columns)
        .filter(item -> !item.getName().startsWith("$$_"))
        .anyMatch(item -> item.getName().equals(columnName));
  }
}
