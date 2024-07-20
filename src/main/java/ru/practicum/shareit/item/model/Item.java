package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Item {
    Long id;
    String name;
    String description;
    User owner;
    Boolean available;
    ItemRequest request;
}
