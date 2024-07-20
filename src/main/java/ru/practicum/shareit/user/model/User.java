package ru.practicum.shareit.user.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class User {
    Long id;
    String name;
    String email;
}
