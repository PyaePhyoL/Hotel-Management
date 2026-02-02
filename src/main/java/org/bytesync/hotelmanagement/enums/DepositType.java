package org.bytesync.hotelmanagement.enums;

public enum DepositType {
    CASH, KPAY;

    public static PaymentMethod convertToPaymentMethod(DepositType type) {
        return switch (type) {
            case CASH -> PaymentMethod.CASH;
            case KPAY -> PaymentMethod.KPAY;
        };
    }
}
