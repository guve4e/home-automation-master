package lib.database;

import lib.utility.Tuple;

import java.sql.SQLException;
import java.util.List;

public interface Rdbms {

    void disconnect() throws SQLException;
    void queryWrite() throws SQLException;
    MySql queryRead() throws Exception;
    JDBC setQuery(String sql, List<Tuple> values) throws Exception;
}
