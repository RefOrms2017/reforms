package com.reforms.orm.select.report.converter;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.reforms.orm.select.SelectedColumn;

/**
 * Контракт на преобразование значения из выборки ResultSet в строковое значение
 * @author evgenie
 */
class FloatColumnValueConverter implements IColumnValueConverter {

    @Override
    public String convertValue(SelectedColumn column, ResultSet rs) throws SQLException {
        float value = rs.getFloat(column.getIndex());
        if (rs.wasNull()) {
            return null;
        }
        return String.valueOf(value);
    }

}
