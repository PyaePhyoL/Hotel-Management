package org.bytesync.hotelmanagement.enums;

public enum VoucherType {
    SECTION,
    DAILY,
    CASH_DOWN,
    EXTEND,
    FOOD_BEVERAGE,
    SERVICE;

    public static VoucherType getVoucherTypeFromStayType(StayType stayType) {
        return switch (stayType) {
            case SECTION -> VoucherType.SECTION;
            case NORMAL -> VoucherType.CASH_DOWN;
            case LONG -> VoucherType.DAILY;
        };
    }
}
