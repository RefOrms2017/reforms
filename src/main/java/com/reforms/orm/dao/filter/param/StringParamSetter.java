package com.reforms.orm.dao.filter.param;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.reforms.ann.ThreadSafe;

/**
 * Установить параметр типа String
 * @author evgenie
 */
@ThreadSafe
public class StringParamSetter implements ParamSetter {

    @Override
    public void setValue(Object value, int index, PreparedStatement ps) throws SQLException {
        ps.setString(index, convertValue(value));
    }

    @Override
    public boolean acceptValue(Object value) {
        return convertValue(value) != null;
    }

    protected String convertValue(Object value) {
        if (value instanceof String) {
            return (String) value;
        }
        return null;
    }
}
