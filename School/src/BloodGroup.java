public enum BloodGroup {
    O_POSITIVE("O+ve"),
    O_NEGATIVE("O-ve"),
    A_POSITIVE("A+ve"),
    A_NEGATIVE("A-ve"),
    B_POSITIVE("B+ve"),
    B_NEGATIVE("B-ve"),
    AB_POSITIVE("AB+ve"),
    AB_NEGATIVE("AB-ve");

    private String shorthand;

    private BloodGroup(String shorthand) {
        this.shorthand = shorthand;
    }

    @Override
    public String toString() {
        return shorthand;
    }
}