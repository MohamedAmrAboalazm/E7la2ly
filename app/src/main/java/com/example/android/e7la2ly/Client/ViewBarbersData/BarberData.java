package com.example.android.e7la2ly.Client.ViewBarbersData;

public class BarberData {
    private String salonname;
    private String barbername;
    private String barberimagelink;

    public BarberData() {
    }

    public BarberData(String salonname, String barbername, String barberimagelink) {
        this.salonname = salonname;
        this.barbername = barbername;
        this.barberimagelink = barberimagelink;
    }

    public String getSalonname() {
        return salonname;
    }


    public String getBarbername() {
        return barbername;
    }


    public String getBarberimagelink() {
        return barberimagelink;
    }
}
