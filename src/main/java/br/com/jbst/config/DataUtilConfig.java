package br.com.jbst.config;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;



public class DataUtilConfig {

    private static final ZoneId FUSO_HORARIO = ZoneId.of("America/Sao_Paulo");
    private static final DateTimeFormatter FORMATADOR_EXTENSO =
        DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("pt", "BR"));

    public static String formatarData(Date date) {
        if (date == null) return "";
        Instant instant = date.toInstant();
        LocalDate localDate = instant.atZone(FUSO_HORARIO).toLocalDate();
        return FORMATADOR_EXTENSO.format(localDate);
    }

    public static String formatarData(LocalDate localDate) {
        if (localDate == null) return "";
        return FORMATADOR_EXTENSO.format(localDate);
    }

    public static String formatarData(Instant instant) {
        if (instant == null) return "";
        return FORMATADOR_EXTENSO.format(instant.atZone(FUSO_HORARIO).toLocalDate());
    }

    public static String formatarData(String isoDateStr) {
        if (isoDateStr == null || isoDateStr.isBlank()) return "";
        LocalDate data = LocalDate.parse(isoDateStr); // espera formato yyyy-MM-dd
        return FORMATADOR_EXTENSO.format(data);
    }
}
