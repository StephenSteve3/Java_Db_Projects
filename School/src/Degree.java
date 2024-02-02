public enum Degree {
    B_A("B.A"),
    B_B_A("B.B.A"),
    B_ED("B.Ed"),
    B_E("B.E"),
    B_TECH("B.Tech"),
    B_SC("B.Sc"),
    B_C_A("B.C.A"),
    M_A("M.A"),
    M_B_A("M.B.A"),
    M_ED("M.Ed"),
    M_E("M.E"),
    M_TECH("M.Tech"),
    M_SC("M.Sc"),
    M_C_A("M.C.A"),
    PH_D("Ph.D");

    private String shorthand;

    private Degree(String shorthand) {
        this.shorthand = shorthand;
    }

    @Override
    public String toString() {
        return shorthand;
    }

}