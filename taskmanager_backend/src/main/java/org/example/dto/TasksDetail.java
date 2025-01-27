package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TasksDetail {
    private String title;
    private String color;
    private Long value;
}
