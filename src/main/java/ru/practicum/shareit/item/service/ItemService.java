package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {
    ItemDto addItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, long itemId, ItemUpdateDto patchDto);

    List<ItemCommentNextLastDto> getAllItems(long userId);

    List<ItemDto> getItemsByText(String text);

    ItemCommentNextLastDto getItemById(long itemId, long userId);

    CommentResponseDto addCommentToItem(long userId, long itemId, CommentAddDto commentAddDto);
}