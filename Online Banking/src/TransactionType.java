public enum TransactionType {
    DEPOSIT('D'),
    WITHDRAW('W');

    char shorthand;

    TransactionType(char shorthand){
        this.shorthand = shorthand;
    }

    @Override
    public String toString() {
        return shorthand+"";
    }

    public static TransactionType getTransactionType(char shorthand){
        switch(shorthand){
            case 'D':
                return DEPOSIT;
            case 'W':
                return WITHDRAW;
            default:
                return null;
        }
    }
}