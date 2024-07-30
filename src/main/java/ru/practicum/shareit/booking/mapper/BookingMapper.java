package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingShortResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@UtilityClass
public class BookingMapper {
    public Booking toBooking(BookingCreateDto bookingCreateDto) {
        Item item = new Item();
        item.setId(bookingCreateDto.getItemId());
        return Booking.builder()
                .start(bookingCreateDto.getStart())
                .end(bookingCreateDto.getEnd())
                .item(item)
                .build();
    }

    public BookingResponseDto toBookingResponseDto(Booking booking) {
        return BookingResponseDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .build();
    }


    public BookingShortResponseDto toBookingShortResponseDto(Booking booking) {
        ItemShortDto itemShortDto = new ItemShortDto();
        itemShortDto.setId(booking.getItem().getId());
        itemShortDto.setName(booking.getItem().getName());
        return BookingShortResponseDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(itemShortDto)
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .build();
    }

    public List<BookingShortResponseDto> toBookingShortResponseDtos(List<Booking> bookings) {
        return bookings
                .stream()
                .map(BookingMapper::toBookingShortResponseDto)
                .toList();
    }
}
