package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingShortResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto addBooking(BookingCreateDto bookingDto, long userId);

    BookingResponseDto bookingApproved(long userId, long bookingId, boolean approved);

    BookingResponseDto getBooking(long userId, long bookingId);

    List<BookingShortResponseDto> getBookings(long userId, String state);

    List<BookingShortResponseDto> getBookingsOwner(long userId, String state);
}
