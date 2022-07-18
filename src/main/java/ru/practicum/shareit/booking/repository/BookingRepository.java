package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Collection<Booking> findAllByBooker_IdOrderByStartDesc(long userId);

    Collection<Booking> findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(long userId, LocalDateTime now);

    Collection<Booking> findAllByBooker_IdAndStartIsAfterOrderByStartDesc(long userId, LocalDateTime now);

    Collection<Booking> findAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
            long userId, LocalDateTime now, LocalDateTime now2
    );

    Collection<Booking> findAllByBooker_IdAndStatusOrderByStartDesc(long userId, BookingStatus status);

    Collection<Booking> findAllByItem_Owner_IdOrderByStartDesc(long userId);

    Collection<Booking> findAllByItem_Owner_IdAndEndIsBeforeOrderByStartDesc(long userId, LocalDateTime now);

    Collection<Booking> findAllByItem_Owner_IdAndStartIsAfterOrderByStartDesc(long userId, LocalDateTime now);

    Collection<Booking> findAllByItem_Owner_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
            long userId, LocalDateTime now, LocalDateTime now2
    );

    Collection<Booking> findAllByItem_Owner_IdAndStatusOrderByStartDesc(long userId, BookingStatus now);


    @Query("select b from Booking b where b.booker.id = ?1 and b.item.id = ?2 and b.status = 'APPROVED'")
    Collection<Booking> findItemBookingFromUser(long userId, long itemId);


    Optional<Booking> findFirstByItem_Owner_IdAndEndBeforeAndStatusOrderByEndDesc(long id, LocalDateTime now,
                                                                                  BookingStatus bookingStatus);

    Optional<Booking> findFirstByItem_Owner_IdAndStartAfterAndStatusOrderByEndAsc(long id, LocalDateTime now,
                                                                                  BookingStatus bookingStatus);

}
