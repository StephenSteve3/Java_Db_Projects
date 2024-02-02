public enum Gender {
    MALE('M'),
    FEMALE('F');

    char shorthand;

    Gender(char shorthand){
        this.shorthand = shorthand;
    }

    @Override
    public String toString() {
        return shorthand+"";
    }
}