package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b from Booking b where b.item.id = ?1 and b.end < ?2 and b.start > ?3")
    Optional<Booking> findBookingItemByDate(long itemId, LocalDate start, LocalDate end);

    Collection<Booking> findAllByBooker_IdOrderByStart(long userId);

    Collection<Booking> findAllByItem_Owner_IdOrderByStart(long userId);

    Collection<Booking> findAllByBooker_IdAndItem_Id(long userId, long itemId);
}
