package org.bytesync.hotelmanagement.model.enums;

import lombok.Getter;

@Getter
public enum Floor {
    FOURTH("4th"), FIFTH("5th"), SEVENTH("7th"), EIGHTH("8th");

    private final String value;

    Floor(String value) {
        this.value = value;
    }

}
