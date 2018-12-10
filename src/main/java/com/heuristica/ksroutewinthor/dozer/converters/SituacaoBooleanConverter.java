package com.heuristica.ksroutewinthor.dozer.converters;

import com.github.dozermapper.core.DozerConverter;

public class SituacaoBooleanConverter extends DozerConverter<String, Boolean> {

    public SituacaoBooleanConverter() {
        super(String.class, Boolean.class);
    }

    @Override
    public Boolean convertTo(String source, Boolean destination) {
        if ("A".equals(source)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    public String convertFrom(Boolean source, String destination) {
        if (Boolean.TRUE.equals(source)) {
            return "A";
        } else {
            return "I";
        }
    }

}
