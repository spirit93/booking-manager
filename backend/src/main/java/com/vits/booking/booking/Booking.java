package com.vits.booking.booking;

import com.vits.booking.seat.Seat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "bookings")
public class Booking {
    public enum Status {
        ACTIVE
    }

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Column(nullable = false)
    private LocalDate bookingDay;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private Instant createdAt;

    protected Booking() {
    }

    public Booking(String id, Seat seat, LocalDate bookingDay, Status status, Instant createdAt) {
        this.id = id;
        this.seat = seat;
        this.bookingDay = bookingDay;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public Seat getSeat() {
        return seat;
    }

    public LocalDate getBookingDay() {
        return bookingDay;
    }

    public Status getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
