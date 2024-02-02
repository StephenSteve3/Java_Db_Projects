public enum Standard {
    PRE_K_G,
    L_K_G,
    U_K_G,
    I,
    II,
    III,
    IV,
    V,
    VI,
    VII,
    VIII,
    IX,
    X,
    XI,
    XII;

    Standard(){
        DBHelper.storeData(this);
    }
    
}