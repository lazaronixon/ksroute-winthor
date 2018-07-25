package com.heuristica.ksroutewinthor.util;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.config.PropertyNamingStrategy;

public class JavaUtil {
    
    public static Jsonb defaultJsonb() {
        String prop = PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES;
        JsonbConfig config = new JsonbConfig().withPropertyNamingStrategy(prop);
        return JsonbBuilder.create(config);
    }

}
