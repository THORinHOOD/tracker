package com.lada.tracker.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.util.PGobject;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@Converter
public final class PgJsonbToMapConverter implements AttributeConverter<Map<String, ?>, PGobject> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public PGobject convertToDatabaseColumn(Map<String, ? extends Object> map) {
        PGobject po = new PGobject();
        po.setType("jsonb");

        try {
            po.setValue(map == null ? null : MAPPER.writeValueAsString(map));
        } catch (SQLException | JsonProcessingException ex) {
            throw new IllegalStateException(ex);
        }
        return po;
    }

    @Override
    public Map<String, ?> convertToEntityAttribute(PGobject dbData) {
        if (dbData == null || dbData.getValue() == null) {
            return null;
        }
        try {
            return MAPPER.readValue(dbData.getValue(), new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException ex) {
            return null;
        }
    }

}
