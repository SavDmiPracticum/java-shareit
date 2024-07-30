package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemCommentNextLastDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@UtilityClass
public class ItemMapper {
    public ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequest())
                .build();
    }

    public Item toItem(ItemDto dto) {
        return Item.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .available(dto.getAvailable())
                .build();
    }

    public List<ItemDto> toItemDtos(List<Item> items) {
        return items
                .stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    public Item fromPatchItemDto(ItemUpdateDto dto) {
        return Item.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .available(dto.getAvailable())
                .build();
    }

    public ItemCommentNextLastDto toItemCommentNextLastDto(Item item, List<Booking> bookings) {
        ItemCommentNextLastDto dto = ItemCommentNextLastDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .owner(item.getOwner())
                .available(item.getAvailable())
                .request(item.getRequest())
                .build();
        List<Comment> comments = new ArrayList<>(item.getComments());
        if (!CollectionUtils.isEmpty(comments)) {
            dto.setComments(comments.stream().map(CommentMapper::toCommentResponseDto).toList());
        } else {
            dto.setComments(Collections.emptyList());
        }
        if (!CollectionUtils.isEmpty(bookings)) {
            dto.setLastBooking(getLast(bookings));
            dto.setNextBooking(getNext(bookings));
        }
        return dto;
    }

    public static List<ItemCommentNextLastDto> toItemCommentLastNextDtos(List<Item> items, List<Booking> bookings) {
        List<ItemCommentNextLastDto> dtos = new ArrayList<>();
        for (Item item : items) {
            ItemCommentNextLastDto dto = ItemCommentNextLastDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .owner(item.getOwner())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .request(item.getRequest())
                    .build();
            List<Comment> comments = new ArrayList<>(item.getComments());
            if (!CollectionUtils.isEmpty(comments)) {
                dto.setComments(comments.stream().map(CommentMapper::toCommentResponseDto).toList());
            } else {
                dto.setComments(Collections.emptyList());
            }
            List<Booking> bookingsItem = bookings.stream()
                    .filter(booking -> booking.getItem().getId().equals(item.getId()))
                    .toList();
            dto.setLastBooking(getLast(bookingsItem));
            dto.setNextBooking(getNext(bookingsItem));
            dtos.add(dto);
        }
        return dtos;
    }

    private static ItemCommentNextLastDto.LastNextBooking getLast(List<Booking> bookings) {
        return bookings.stream()
                .filter(b -> b.getStart().isBefore(LocalDateTime.now()))
                .max(Comparator.comparing(Booking::getEnd))
                .map(lb -> new ItemCommentNextLastDto.LastNextBooking(lb.getId(), lb.getBooker().getId()))
                .orElse(null);
    }

    private static ItemCommentNextLastDto.LastNextBooking getNext(List<Booking> bookings) {
        return bookings.stream()
                .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(Booking::getStart))
                .map(nb -> new ItemCommentNextLastDto.LastNextBooking(nb.getId(), nb.getBooker().getId()))
                .orElse(null);
    }
}
