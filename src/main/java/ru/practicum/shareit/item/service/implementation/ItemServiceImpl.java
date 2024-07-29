package ru.practicum.shareit.item.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.common.Patcher;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public ItemDto addItem(long userId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User " + userId + " not found"));
        item.setOwner(user);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto updateItem(long userId, long itemId, ItemUpdateDto patchDto) {
        Item item = ItemMapper.fromPatchItemDto(patchDto);
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User " + userId + " not found"));
        Item updatedItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item " + itemId + " not found"));
        if (!updatedItem.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Item " + itemId + " is not owner of user " + userId);
        }
        try {
            Patcher.patch(updatedItem, item);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return ItemMapper.toItemDto(itemRepository.save(updatedItem));
    }

    @Override
    public List<ItemCommentNextLastDto> getAllItems(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User " + userId + " not found"));
        List<Item> items = itemRepository.findAllByOwnerId(userId);

        List<Booking> bookings = bookingRepository.findAllByItemInAndStatusNotIn(
                items, List.of(BookingStatus.REJECTED));
        return ItemMapper.toItemCommentLastNextDtos(items, bookings);
    }

    @Override
    public List<ItemDto> getItemsByText(String text) {
        if (text == null || text.isEmpty()) {
            return Collections.emptyList();
        } else {
            return ItemMapper.toItemDtos(itemRepository.searchText(text.toLowerCase()));
        }
    }

    @Override
    public ItemCommentNextLastDto getItemById(long itemId, long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User " + userId + " not found"));
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException("Item " + itemId + " not found"));
        List<Booking> bookings = bookingRepository.findAllByItemIdAndItemOwnerIdAndStatusNotIn(
                itemId, userId, List.of(BookingStatus.REJECTED));
        return ItemMapper.toItemCommentNextLastDto(item, bookings);
    }

    @Override
    @Transactional
    public CommentResponseDto addCommentToItem(long userId, long itemId, CommentAddDto commentAddDto) {
        Comment comment = CommentMapper.toComment(commentAddDto);
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException("Item " + itemId + " not found")
        );
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User " + userId + " not found")
        );
        List<Booking> bookings = bookingRepository.findByItemIdAndBookerIdAndStatusAndEndBefore(
                itemId, userId, BookingStatus.APPROVED, LocalDateTime.now());
        if (CollectionUtils.isEmpty(bookings)) {
            throw new NotAvailableException("User id: " + userId + " not booked item id: " + itemId);
        }
        comment.setItem(item);
        comment.setAuthor(user);
        return CommentMapper.toCommentResponseDto(commentRepository.save(comment));
    }


}
