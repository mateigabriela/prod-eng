package ro.unibuc.prodeng.model;

public enum AppointmentReason {
    ENGINE_DIAGNOSTIC(60),
    OIL_CHANGE(45),
    BRAKE_INSPECTION(50),
    TIRE_CHANGE(30),
    ELECTRICAL_CHECK(70),
    AC_SERVICE(75),
    GENERAL_REVISION(90);

    private final int estimatedMinutes;

    AppointmentReason(int estimatedMinutes) {
        this.estimatedMinutes = estimatedMinutes;
    }

    public int estimatedMinutes() {
        return estimatedMinutes;
    }
}