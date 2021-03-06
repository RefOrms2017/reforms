package com.reforms.orm.dao;

import com.reforms.orm.dao.report.model.ReportRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportIterator implements AutoCloseable {

    private PreparedStatement ps;
    private ResultSet rs;

    private IResultSetObjectReader reader;
    private ReportRecord currentRecord;

    public ReportIterator(PreparedStatement ps, IResultSetObjectReader reader) {
        this.ps = ps;
        this.reader = reader;
    }

    public void prepare() throws SQLException {
        rs = ps.executeQuery();
    }

    public boolean hasNext() throws Exception {
        if (currentRecord == null && reader.canRead(rs)) {
            currentRecord = reader.read(rs);
        }
        return currentRecord != null;
    }

    public ReportRecord next() throws Exception {
        if (hasNext()) {
            ReportRecord temp = currentRecord;
            currentRecord = null;
            return temp;
        }
        return null;
    }

    @Override
    public void close() throws SQLException {
        SQLException cause = null;
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException rse) {
                cause = rse;
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException pse) {
                if (cause == null) {
                    cause = pse;
                } else {
                    cause = new SQLException(cause);
                }
            }
        }
        if (cause != null) {
            throw cause;
        }
    }

}
