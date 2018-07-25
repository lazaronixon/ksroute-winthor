package com.heuristica.ksroutewinthor.dozer.converters;

import org.dozer.DozerConverter;

public class StringBooleanConverter extends DozerConverter<String, Boolean> {

    public StringBooleanConverter() {
        super(String.class, Boolean.class);
    }

    @Override
    public Boolean convertTo(String source, Boolean destination) {
        if ("S".equals(source)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    public String convertFrom(Boolean source, String destination) {
        if (Boolean.TRUE.equals(source)) {
            return "S";
        } else {
            return "N";
        }
    }

}
