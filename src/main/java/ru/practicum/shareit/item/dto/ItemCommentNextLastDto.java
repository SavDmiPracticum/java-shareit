package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ItemCommentNextLastDto {
    Long id;
    String name;
    String description;
    User owner;
    Boolean available;
    ItemRequest request;
    LastNextBooking lastBooking;
    LastNextBooking nextBooking;
    List<CommentResponseDto> comments;

    public record LastNextBooking(Long id, Long bookerId) {
    }
}
