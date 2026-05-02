package com.vits.booking.seat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "seats")
public class Seat {
    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String label;

    private String rowLabel;

    private Integer position;

    @Column(nullable = false)
    private boolean enabled;

    protected Seat() {
    }

    public Seat(String id, String label, String rowLabel, Integer position, boolean enabled) {
        this.id = id;
        this.label = label;
        this.rowLabel = rowLabel;
        this.position = position;
        this.enabled = enabled;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getRowLabel() {
        return rowLabel;
    }

    public Integer getPosition() {
        return position;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
