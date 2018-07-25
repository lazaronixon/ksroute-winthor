package com.heuristica.ksroutewinthor.util;

import javax.json.bind.JsonbConfig;
import javax.json.bind.config.PropertyNamingStrategy;

public class JavaUtil {

    public static JsonbConfig defaultJsonConfig() {
        return new JsonbConfig().withPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES);
    }

}
