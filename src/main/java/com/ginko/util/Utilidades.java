package com.ginko.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class Utilidades {

    private Utilidades() {
    }

    public static LocalDateTime parseFecha(String valor, boolean endOfDay) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("La fecha no debe estar vacía");
        }

        try {
            if (valor.length() <= 10) {
                LocalDate fecha = LocalDate.parse(valor, DateTimeFormatter.ISO_LOCAL_DATE);
                return endOfDay ? fecha.atTime(LocalTime.MAX) : fecha.atStartOfDay();
            }
            return LocalDateTime.parse(valor, DateTimeFormatter.ISO_DATE_TIME);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use yyyy-MM-dd o yyyy-MM-dd'T'HH:mm:ss");
        }
    }    

    public static LocalDateTime calcularFechaVencimiento(LocalDateTime fechaCreacion, int diasHabiles) {
        LocalDate fecha = fechaCreacion.toLocalDate();
        int diasSumados = 0;

        while (diasSumados < diasHabiles) {
            fecha = fecha.plusDays(1);
            if (esDiaHabil(fecha)) {
                diasSumados++;
            }
        }

        return fecha.atTime(LocalTime.MAX);
    }

    public static LocalDateTime calcularFechaFinHabil(LocalDate fechaActual,Integer diasHabiles) {
        LocalDate resultado = fechaActual;
        int diasHabil = 0;

        while (diasHabil < diasHabiles) {
            resultado = resultado.plusDays(1);
            if (esDiaHabil(resultado)) {
                diasHabil++;
            }
        }

        return resultado.atTime(LocalTime.MAX);
    }

    public static boolean esDiaHabil(LocalDate fecha) {
        DayOfWeek dia = fecha.getDayOfWeek();
        return dia != DayOfWeek.SATURDAY && dia != DayOfWeek.SUNDAY;
    }
}
