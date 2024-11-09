import config.PaymentDetailsException;

import java.util.Optional;

class CarService {

    public static Car getCar(int id) {

        PaymentDetails paymentDetails = new PaymentDetails(4, true, 100.50);
        LeaseContract leaseContract = new LeaseContract(paymentDetails);
        Driver driver = new Driver(leaseContract);

        return new Car(driver);
    }
}

class Car {
    Driver driver;

    public Car(Driver driver) {
        this.driver = driver;
    }

    public Driver getCurrentDriver() {
        return driver;
    }

}

class Driver {

    LeaseContract leaseContract;

    public Driver(LeaseContract leaseContract) {
        this.leaseContract = leaseContract;
    }

    public LeaseContract getLeaseContract() {
        return leaseContract;
    }

}

class LeaseContract {
    PaymentDetails paymentDetails;

    public LeaseContract(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }
}

class PaymentDetails {
    private int leasePeriod;
    private boolean payFor;
    private double borg;

    public PaymentDetails(int leasePeriod, boolean payFor, double borg) {
        this.leasePeriod = leasePeriod;
        this.payFor = payFor;
        this.borg = borg;
    }

    public int getLeasePeriod() {
        return leasePeriod;
    }

    public boolean isPayFor() {
        return payFor;
    }

    public double getBorg() {
        return borg;
    }
}

public class OptionalPaymentDetails {

    public static void main(String[] args) {
        Optional<PaymentDetails> paymentDetails = OptionalPaymentDetails.getMyPaymentDetailsById(null);
        paymentDetails.orElseThrow(() -> new PaymentDetailsException("nada"));
        System.out.printf("borg: %f\n", paymentDetails.get().getBorg());
        System.out.printf("LeasePeriod: %d\n", paymentDetails.get().getLeasePeriod());
        System.out.printf("Betaald: %s\n", paymentDetails.get().isPayFor());
    }

    static Optional<PaymentDetails> getMyPaymentDetailsById(Integer id) {
        return Optional.ofNullable(id)
                .map(CarService::getCar)
                .map(Car::getCurrentDriver)
                .map(Driver::getLeaseContract)
                .map(LeaseContract::getPaymentDetails);
    }

}
