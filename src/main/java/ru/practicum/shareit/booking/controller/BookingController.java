package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingShortResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @Valid @RequestBody BookingCreateDto bookingDto) {
        log.info("Adding new booking: {}", bookingDto);
        return bookingService.addBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approveBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long bookingId,
                                             @RequestParam boolean approved) {
        log.info("Approving booking: {} by owner {}", bookingId, userId);
        return bookingService.bookingApproved(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @PathVariable long bookingId) {
        log.info("Getting booking {} by requester or owner: {}", bookingId, userId);
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingShortResponseDto> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                                     @RequestParam(required = false, defaultValue = "ALL")
                                                String state) {
        log.info("Getting bookings: {} of user {}", state, userId);
        return bookingService.getBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingShortResponseDto> getBookingsOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                     @RequestParam(required = false, defaultValue = "ALL")
                                                     String state) {
        log.info("Getting bookings: {} of owner {}", state, userId);
        return bookingService.getBookingsOwner(userId, state);
    }
}
