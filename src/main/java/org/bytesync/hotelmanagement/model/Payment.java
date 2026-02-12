package org.bytesync.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.*;
import org.bytesync.hotelmanagement.enums.PaymentMethod;
import org.bytesync.hotelmanagement.enums.IncomeType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "payments")
public class Payment extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;

    @ManyToOne
    private Guest guest;

    @ElementCollection
    @CollectionTable(
            name = "payment_amount",
            joinColumns = @JoinColumn(name = "payment_id")
    )
    @MapKeyColumn(name = "payment_method")
    @Column(name = "payment_amount")
    @Enumerated(EnumType.STRING)
    private Map<PaymentMethod, Integer> paymentAmountMap = new HashMap<>();

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    private Reservation reservation;

    @Enumerated(value = EnumType.STRING)
    private IncomeType type;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.PERSIST)
    private List<Voucher> vouchers = new ArrayList<>();

    public void setReservation(Reservation resv) {
        this.reservation = resv;
        resv.addPayment(this);
    }

    public void addVoucher(Voucher voucher) {
        this.vouchers.add(voucher);
        voucher.setPayment(this);
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
        guest.addPayment(this);
    }

    public void addPaymentAmount(PaymentMethod paymentMethod, Integer amount) {
        this.paymentAmountMap.put(paymentMethod, amount);
    }

    public int getPaidAmount() {
        return this.paymentAmountMap.values().stream().reduce(Integer::sum).orElse(0);
    }

    public int getAmountByPaymentMethod(PaymentMethod paymentMethod) {
        return this.paymentAmountMap.getOrDefault(paymentMethod, 0);
    }

    public int deductFromMethod(PaymentMethod method, int amountToDeduct) {
        if (amountToDeduct <= 0) {
            return 0;
        }

        Integer currentAmount = this.getPaymentAmountMap().get(method);
        if (currentAmount == null || currentAmount <= 0) {
            return amountToDeduct;
        }

        if (currentAmount >= amountToDeduct) {
            this.getPaymentAmountMap().put(method, currentAmount - amountToDeduct);
            return 0;
        } else {
            this.getPaymentAmountMap().put(method, 0);
            return amountToDeduct - currentAmount;
        }
    }

}
