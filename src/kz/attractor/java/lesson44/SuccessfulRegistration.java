package kz.attractor.java.lesson44;

public class SuccessfulRegistration {
    private String goodResult;
    private String welcome;

    public SuccessfulRegistration(String goodResult, String welcome) {
        this.goodResult = goodResult;
        this.welcome = welcome;
    }

    public String getGoodResult() {
        return goodResult;
    }

    public void setGoodResult(String goodResult) {
        this.goodResult = goodResult;
    }

    public String getWelcome() {
        return welcome;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }
}
