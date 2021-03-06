package com.reforms.orm.dao.bobj.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.reforms.orm.dao.IResultSetObjectReader;

public class OrmIterator<OrmType> implements AutoCloseable {

    private PreparedStatement ps;
    private ResultSet rs;

    private IResultSetObjectReader reader;
    private OrmType currentRecord;

    public OrmIterator(PreparedStatement ps, IResultSetObjectReader reader) {
        this.ps = ps;
        this.reader = reader;
    }

    public void prepare() throws SQLException {
        if (rs == null) {
            rs = ps.executeQuery();
        }
    }

    public boolean hasNext() throws Exception {
        if (currentRecord == null && reader.canRead(rs)) {
            currentRecord = reader.read(rs);
        }
        return currentRecord != null;
    }

    public OrmType next() throws Exception {
        if (hasNext()) {
            OrmType temp = currentRecord;
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
