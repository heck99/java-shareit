package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {

    private final BookingService service;

    private static final String userHeader = "X-Sharer-User-Id";

    @Autowired
    public BookingController(BookingService service) {
        this.service = service;
    }

    @PostMapping
    public BookingPostDto createData(@Valid @RequestBody BookingPostDto element, @RequestHeader(name = userHeader) long userId) {
        log.info("/POST: " + element.toString());
        return service.create(element, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingGetDto answerBooking(@PathVariable long bookingId, @RequestHeader(name = userHeader) long userId,
                              @RequestParam boolean approved) {
        return service.answerBooking(bookingId, userId, approved);
    }

    @GetMapping("/{id}")
    public BookingGetDto get(@PathVariable Long id, @RequestHeader(name = userHeader) long userId) {
        log.info("/GET /{}", id);
        return service.getById(id, userId);
    }

    @GetMapping()
    public Collection<BookingGetDto> getAllByUser(@RequestParam(defaultValue = "ALL") StateStatus state,
                                                  @RequestHeader(name = userHeader) long userId) {
        log.info("/GET");
        return service.getAllByUser(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingGetDto> getAllByItemOwner(@RequestParam(defaultValue = "ALL") StateStatus state,
                                                       @RequestHeader(name = userHeader) long userId) {
        log.info("/GET");
        return service.getAllByItemOwner(userId, state);
    }
}
