package com.vits.booking.seat;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SeatRepository extends JpaRepository<SeatEntity, UUID> {

    @Query("""
            select s.id as id, s.label as label,
                   case when exists (
                       select 1
                       from BookingEntity b
                       where b.seat = s and b.status = com.vits.booking.booking.BookingStatus.ACTIVE
                   ) then true else false end as occupied
            from SeatEntity s
            order by s.label
            """)
    List<SeatAvailabilityProjection> findAvailability();
}
