package com.reforms.orm.dao.filter.param;

import java.util.*;

import static com.reforms.orm.dao.column.ColumnAliasType.*;

/**
 * Фабрика установщиков параметров в ResultSet
 * @author evgenie
 */
public class ParamSetterFactory {

    private Map<String, ParamSetter> baseParamSetters;

    private Map<String, ParamSetter> customParamSetters;

    private Set<String> prefixes = null;

    public ParamSetterFactory configure() {
        baseParamSetters = new HashMap<>();
        baseParamSetters.put(CAT_Z_BOOLEAN.getMarker(), new BooleanParamSetter());
        baseParamSetters.put(CAT_Y_BYTE.getMarker(), new ByteParamSetter());
        baseParamSetters.put(CAT_X_SHORT.getMarker(), new ShortParamSetter());
        baseParamSetters.put(CAT_C_CHARACTER.getMarker(), new CharParamSetter());
        baseParamSetters.put(CAT_I_INT.getMarker(), new IntParamSetter());
        baseParamSetters.put(CAT_F_FLOAT.getMarker(), new FloatParamSetter());
        baseParamSetters.put(CAT_W_DOUBLE.getMarker(), new DoubleParamSetter());
        baseParamSetters.put(CAT_L_LONG.getMarker(), new LongParamSetter());
        baseParamSetters.put(CAT_E_ENUM.getMarker(), new EnumParamSetter(this));
        baseParamSetters.put(CAT_S_STRING.getMarker(), new StringParamSetter());
        baseParamSetters.put(CAT_N_BIGDECIMAL.getMarker(), new BigDecimalParamSetter());
        baseParamSetters.put(CAT_I_BIGINTEGER.getMarker(), new BigDecimalParamSetter());
        baseParamSetters.put(CAT_D_DATE.getMarker(), new DateParamSetter());
        baseParamSetters.put(CAT_T_TIMESTAMP.getMarker(), new TimestampParamSetter());
        baseParamSetters.put(CAT_V_TIME.getMarker(), new TimeParamSetter());
        baseParamSetters.put(CAT_O_OBJECT_TYPE.getMarker(), new ObjectParamSetter());
        return this;
    }

    public ParamSetterFactory addCustomParamSetter(String key, ParamSetter converter) {
        if (customParamSetters == null) {
            customParamSetters = new HashMap<>();
        }
        customParamSetters.put(key, converter);
        return this;
    }

    public ParamSetterFactory sealed() {
        if (baseParamSetters == null) {
            baseParamSetters = new HashMap<>();
        }
        baseParamSetters = Collections.unmodifiableMap(baseParamSetters);
        prefixes = new TreeSet<>(new ParamSetterMarkerComparator());
        prefixes.addAll(baseParamSetters.keySet());
        return this;
    }

    public ParamSetterFactory sealedCustom() {
        if (customParamSetters == null) {
            customParamSetters = new HashMap<>();
            customParamSetters = Collections.unmodifiableMap(customParamSetters);
            prefixes.addAll(customParamSetters.keySet());
        }
        return this;
    }

    public ParamSetter getParamSetter(String key) {
        if (customParamSetters != null) {
            ParamSetter customConverter = customParamSetters.get(key);
            if (customConverter != null) {
                return customConverter;
            }
        }
        if (baseParamSetters == null) {
            throw new IllegalStateException("Необходимо сконфигурировать 'ParamSetterFactory'");
        }
        return baseParamSetters.get(key);
    }

    public String findParamSetterMarker(Object value) {
        for (String marker : prefixes) {
            ParamSetter paramSetter = getParamSetter(marker);
            if (paramSetter.acceptValue(value)) {
                return marker;
            }
        }
        return null;
    }

    public ParamSetter findParamSetter(Object value) {
        for (String marker : prefixes) {
            ParamSetter paramSetter = getParamSetter(marker);
            if (paramSetter.acceptValue(value)) {
                return paramSetter;
            }
        }
        return null;
    }
}