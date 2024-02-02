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

    public static Gender getGender(char shorthand){
        if(shorthand == 'M') return MALE;
        if(shorthand == 'F') return FEMALE;
        return null;
    }
}