package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @Valid @RequestBody ItemDto itemDto) {
        log.info("Adding item: {}", itemDto);
        return itemService.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @Valid @RequestBody ItemUpdateDto patchDto,
                              @PathVariable int itemId) {
        log.info("Updating by id: {} item: {}", itemId, patchDto);
        return itemService.updateItem(userId, itemId, patchDto);
    }

    @GetMapping
    public List<ItemCommentNextLastDto> getAllItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Getting all items by user: {}", userId);
        return itemService.getAllItems(userId);
    }

    @GetMapping("/{itemId}")
    public ItemCommentNextLastDto getItemById(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @PathVariable long itemId) {
        return itemService.getItemById(itemId, userId);

    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        log.info("Searching by text: {}", text);
        return itemService.getItemsByText(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addCommentToItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                               @PathVariable long itemId,
                                               @Valid @RequestBody CommentAddDto commentAddDto) {
        log.info("Adding comment to item: {}", itemId);
        return itemService.addCommentToItem(userId, itemId, commentAddDto);
    }
}
