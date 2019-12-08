package com.ihc.smartparking;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Registro {
    // registro de entrada e saida do estacionamento.
    public String tipo;
    public long time;
    public double valor = 0.0;
    public String localTime;

    Registro(String tipo) {
        this.tipo = tipo;
        time = System.currentTimeMillis() / 1000;
        Calendar cal = Calendar.getInstance();
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
        localTime = date.format(currentLocalTime);
    }
}
