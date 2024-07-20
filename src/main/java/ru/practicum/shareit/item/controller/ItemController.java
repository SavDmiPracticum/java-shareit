package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody ItemDto itemDto) {
        Item newItem = ItemMapper.toItem(itemDto);
        log.info("Adding item: {}", newItem);
        return ItemMapper.toItemDto(itemService.addItem(userId, newItem));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @Valid @RequestBody ItemUpdateDto patchDto, @PathVariable int itemId) {
        Item itemToUpdate = ItemMapper.fromPatchItemDto(patchDto);
        log.info("Updating by id: {} item: {}", itemId, itemToUpdate);
        return ItemMapper.toItemDto(itemService.updateItem(userId, itemId, itemToUpdate));
    }

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Getting all items by user: {}", userId);
        return ItemMapper.toItemDtos(itemService.getAllItems(userId));
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        return ItemMapper.toItemDto(itemService.getItemById(itemId, userId));
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        log.info("Searching by text: {}", text);
        return ItemMapper.toItemDtos(itemService.getItemsByText(text));
    }
}
