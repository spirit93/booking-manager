package com.vits.booking.seat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "seats")
public class SeatEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String label;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected SeatEntity() {
    }

    public SeatEntity(UUID id, String label, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.label = label;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }
}
