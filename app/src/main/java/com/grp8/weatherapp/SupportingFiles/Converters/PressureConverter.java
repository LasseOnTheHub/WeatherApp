package com.grp8.weatherapp.SupportingFiles.Converters;

/**
 * Created by Henrik on 09-01-2017.
 */
public class PressureConverter {

    public double toBAR(double hectopascal) {
    hectopascal = hectopascal/1000;
        return hectopascal;
    }

    public double toPSI(double hectopascal) {
    hectopascal = hectopascal/68.94757293168;
        return hectopascal;
    }

    public double toATM(double hectopascal) {
    hectopascal = hectopascal/1013.25;
        return hectopascal;
    }
}

