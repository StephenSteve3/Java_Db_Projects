public enum Gender {
    MALE('M'),
    FEMALE('F');

    private char shorthand;

    private Gender(char shorthand) {
        this.shorthand = shorthand;
    }

    @Override
    public String toString() {
        return shorthand+"";
    }
}