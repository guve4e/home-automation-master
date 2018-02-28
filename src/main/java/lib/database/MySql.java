package lib.database;

import lib.utility.Tuple;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySql extends JDBC {

    List<List<Tuple<String, Object>>> dataSet;

    public MySql(MySqlConnection connection) throws SQLException {
        super(connection);
    }

    @Override
    public void queryWrite() throws SQLException {
        // guard
        if (!isPreparedStatementSet()) throw new SQLException("Prepared Statement must be set first!");

        Integer numId = this.getPreparedStatement().executeUpdate();
        // save the last inserted row
        this.setLastInsertedRowId(numId);
    }

    @Override
    public MySql queryRead() throws Exception {
        // guard
        if (!isPreparedStatementSet()) throw new SQLException("Prepared Statement must be set first!");

        ResultSet resultSet = this.getPreparedStatement().executeQuery();

        List<List<Tuple<String, Object>>> dataSet = new ArrayList<>();

        // walk through the result set and collect rows
        while(resultSet.next()) {
            dataSet.add (this.retrieveRow(resultSet));
        }

        // save
        this.dataSet = dataSet;
        // clean up
        this.disconnect();

        return this;
    }

    @Override
    public JDBC setQuery(String sql, List<Tuple> values) throws Exception {
        return this.setPreparedStatement(sql, values);
    }

    public List<List<Tuple<String, Object>>> getDataSetAsNestedListOfTuple() {
        return this.dataSet;
    }

    public Map getDataSetAsMap() {
        Map<String, Object> map = new HashMap<>();
        for (List<Tuple<String, Object>> row : this.dataSet) {
            for(Tuple<String, Object> field : row) {
                map.put(field.x, field.y);
            }
        }
        return map;
    }
}
