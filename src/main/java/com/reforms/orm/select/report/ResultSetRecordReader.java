package com.reforms.orm.select.report;

import java.sql.ResultSet;
import java.util.List;

import com.reforms.orm.OrmContext;
import com.reforms.orm.select.ColumnAlias;
import com.reforms.orm.select.SelectedColumn;
import com.reforms.orm.select.report.converter.ColumnValueConverterFactory;
import com.reforms.orm.select.report.converter.IColumnValueConverter;
import com.reforms.orm.select.report.model.ReportRecord;

/**
 *
 * @author evgenie
 */
public class ResultSetRecordReader {

    private List<SelectedColumn> columns;
    private ColumnValueConverterFactory converterFactory;
    private IColumnToRecordNameConverter columnToRecordNameConverter;

    public ResultSetRecordReader(List<SelectedColumn> columns, OrmContext rCtx) {
        this.columns = columns;
        this.converterFactory = rCtx.getColumnValueConverterFactory();
        this.columnToRecordNameConverter = rCtx.getColumnToRecordNameConverter();
    }

    public ReportRecord read(ResultSet rs) throws Exception {
        if (!rs.next()) {
            return null;
        }
        ReportRecord reportRecord = new ReportRecord();
        for (SelectedColumn column : columns) {
            ColumnAlias cAlias = column.getColumnAlias();
            String aliasPrefix = cAlias.getAliasPrefix();
            IColumnValueConverter converter = converterFactory.getConverter(aliasPrefix);
            if (converter == null) {
                throw new IllegalStateException("Не определен преобразователь данных для типа '" + aliasPrefix + "'");
            }
            String value = converter.convertValue(column, rs);
            String key = columnToRecordNameConverter.getRecordName(column);
            reportRecord.put(key, value);
        }
        return reportRecord;
    }

}
