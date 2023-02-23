package kz.attractor.java.lesson44;

public class SuccessfulRegistrationDataModel {
    private SuccessfulRegistration successfulRegistration;

    public SuccessfulRegistrationDataModel() {
        this.successfulRegistration = new SuccessfulRegistration("SUCCESSFUL REGISTRATION", "Welcome to our World Mr....");
    }

    public SuccessfulRegistration getSuccessfulRegistration() {
        return successfulRegistration;
    }

    public void setSuccessfulRegistration(SuccessfulRegistration successfulRegistration) {
        this.successfulRegistration = successfulRegistration;
    }
}
