package ru.practicum.shareit.booking.service.implementation;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingShortResponseDto;
import ru.practicum.shareit.booking.enums.BookingState;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public BookingResponseDto addBooking(BookingCreateDto bookingDto, long userId) {
        Booking booking = BookingMapper.toBooking(bookingDto);
        User booker = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(
                "User with id " + userId + " not found"));
        Item bookingItem = itemRepository.findById(booking.getItem().getId()).orElseThrow(
                () -> new NotFoundException("Item with id " + booking.getItem().getId() + " not found"));
        if (!bookingItem.getAvailable()) {
            throw new NotAvailableException("Booking is not available");
        }
        if (booker.getId().equals(bookingItem.getOwner().getId())) {
            throw new NotFoundException("Not allowed to book your item");
        }
        booking.setBooker(booker);
        booking.setItem(bookingItem);
        booking.setStatus(BookingStatus.WAITING);
        return BookingMapper.toBookingResponseDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingResponseDto bookingApproved(long userId, long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new NotFoundException("Booking with id " + bookingId + " not found"));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new NotFoundException("You do not own this item in booking");
        }
        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new NotAvailableException("Booking is already approved");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return BookingMapper.toBookingResponseDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto getBooking(long userId, long bookingId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(
                "User with id " + userId + " not found"));
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new NotFoundException("Booking with id " + bookingId + " not found"));
        if (booking.getItem().getOwner().getId() != userId && booking.getBooker().getId() != userId) {
            throw new NotFoundException("You do not own this booking or item in booking");
        }
        return BookingMapper.toBookingResponseDto(booking);
    }

    @Override
    public List<BookingShortResponseDto> getBookings(long userId, String state) {
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unknown state: " + state);
        }
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(
                "User with id " + userId + " not found"));
        List<Booking> bookings = switch (bookingState) {
            case ALL -> bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
            case CURRENT -> bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                    userId, LocalDateTime.now(), LocalDateTime.now());
            case PAST -> bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(
                    userId, LocalDateTime.now());
            case FUTURE -> bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(
                    userId, LocalDateTime.now());
            case WAITING -> bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(
                    userId, BookingStatus.WAITING);
            case REJECTED -> bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(
                    userId, BookingStatus.REJECTED);
        };
        return BookingMapper.toBookingShortResponseDtos(bookings);
    }

    @Override
    public List<BookingShortResponseDto> getBookingsOwner(long userId, String state) {
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unknown state: " + state);
        }
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(
                "Owner with id " + userId + " not found"));
        List<Booking> bookings = switch (bookingState) {
            case ALL -> bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);
            case CURRENT -> bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                    userId, LocalDateTime.now(), LocalDateTime.now());
            case PAST -> bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(
                    userId, LocalDateTime.now());
            case FUTURE -> bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(
                    userId, LocalDateTime.now());
            case WAITING -> bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(
                    userId, BookingStatus.WAITING);
            case REJECTED -> bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(
                    userId, BookingStatus.REJECTED);
        };
        return BookingMapper.toBookingShortResponseDtos(bookings);
    }
}
