package com.pm.patientservice.lib;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MultiFormatLocalDateDeserializer extends JsonDeserializer<LocalDate> {
    private static final DateTimeFormatter[] F = new DateTimeFormatter[]{
            DateTimeFormatter.ISO_LOCAL_DATE,                    // 1999-08-02
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),           // 02-08-1999
            DateTimeFormatter.ofPattern("MM-dd-yyyy"),           // 08-02-1999
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),           // 02/08/1999
            DateTimeFormatter.ofPattern("MM/dd/yyyy")            // 08/02/1999
    };

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        String s = p.getValueAsString();

        if (s != null) s = s.trim();

        for (var fmt : F) {
            try {
                return LocalDate.parse(s, fmt);
            } catch (DateTimeParseException ignore) {
            }
        }
        throw new InvalidFormatException(
                p,
                "Invalid date format. Supported: yyyy-MM-dd, dd-MM-yyyy, MM-dd-yyyy, dd/MM/yyyy, MM/dd/yyyy",
                s,
                LocalDate.class);
    }
}
