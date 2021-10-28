package datamining.package_reseaux.other;

import java.io.*;

import datamining.package_reseaux.Interface.Reponse;



public class ReponseSUM implements Reponse, Serializable {
    public static int EMAIL_OK = 201;
    public static int EMAIL_NOT_FOUND = 501;
    public static int KEY_GENERATED = 202;
    public static int WRONG_PASSWORD = 401;

    private int codeRetour;
    private String chargeUtile;

    public ReponseSUM(int c, String chu) {
        codeRetour = c; setChargeUtile(chu);
    }

    public int getCode() { return codeRetour; }
    public String getChargeUtile() { return chargeUtile; }
    public void setChargeUtile(String chargeU) { chargeUtile = chargeU; }
}
