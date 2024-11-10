import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class PaymentData {
    private enum PERIODIC {
        MONTHLY,
        QUATERLY,
        YEARLY

    }

    BigDecimal periodicPayment;
    BigDecimal employerContribution;
    PERIODIC periodicContribution;

    public PaymentData() {
    }

    public PaymentData(BigDecimal periodicPayment, BigDecimal employerContribution, PERIODIC periodicContribution) {
        this.periodicPayment = periodicPayment;
        this.employerContribution = employerContribution;
        this.periodicContribution = periodicContribution;
    }

    public BigDecimal getPeriodicPayment() {
        return periodicPayment;
    }

    public BigDecimal getEmployerContribution() {
        return employerContribution;
    }

    public PERIODIC getPeriodicContribution() {
        return periodicContribution;
    }

    static Predicate<PaymentData> isPeriodicPayment() {
        return paymentData -> Optional.ofNullable(paymentData)
                .map(PaymentData::getPeriodicPayment)
                .isPresent();
    }

    static Predicate<PaymentData> isEmployerContribution() {
        return paymentData -> Optional.ofNullable(paymentData)
                .map(PaymentData::getEmployerContribution)
                .isPresent();
    }

    static Predicate<PaymentData> isMonthlyContribution() {
        return paymentData -> Optional.ofNullable(paymentData)
                .map(PaymentData::getPeriodicContribution)
                .map(periodicContribution -> periodicContribution == PERIODIC.MONTHLY)
                .orElse(false);
    }

    static Optional<BigDecimal> getPaymentContribution(PaymentData paymentData) {
        System.out.println("getPaymentContribution: " + paymentData);
        return Optional.ofNullable(paymentData)
                .filter(isPeriodicPayment().and(isEmployerContribution()).and(isMonthlyContribution()))
                .map(subtract(PaymentData::getPeriodicPayment, PaymentData::getEmployerContribution));
    }

    private static Function<PaymentData, BigDecimal> subtract(Function<PaymentData, BigDecimal> subtractFrom,
                                                              Function<PaymentData, BigDecimal> secondAmount) {

        return pd -> getAmountOrZero(pd, subtractFrom)
                .subtract(getAmountOrZero(pd, secondAmount));
    }

    private static BigDecimal getAmountOrZero(PaymentData pd, Function<PaymentData, BigDecimal> amountGetter) {
        return Optional.ofNullable(amountGetter.apply(pd)).orElse(BigDecimal.ZERO);
    }

    @Override
    public String toString() {
        return "PaymentData{" +
                "periodicPayment=" + periodicPayment +
                ", employerContribution=" + employerContribution +
                ", periodicContribution=" + periodicContribution +
                '}';
    }

    public static void main(String[] args) {

        PaymentData paymentData = new PaymentData();
        System.out.println("paymentData: " + paymentData);
        DecimalFormat df = new DecimalFormat("#,###.00");
        Optional<BigDecimal> paymemtContribution = PaymentData.getPaymentContribution(paymentData);
        paymemtContribution.ifPresent(
                (p) -> System.out.printf("Contribution:  %s", df.format(p)));
        paymemtContribution.orElseThrow(()-> new RuntimeException("Geen paymemtContribution beschikbaar "));
    }
}

