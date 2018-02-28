package lib.database;

import lib.utility.Tuple;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class JDBC implements Rdbms {

    private Integer lastInsertedRowId;
    private Connection connection;
    private PreparedStatement preparedStatement = null;
    private ResultSetMetaData metaData; // TODO ??
    private List<Tuple> dataSetSchema;

    protected JDBC setPreparedStatement(String sql, List<Tuple> values) throws Exception {

        if (connection.isClosed()) throw new Exception("Not connected to DB!");

        this.preparedStatement = this.connection.prepareStatement(sql);

        int counter = 1;
        for (Tuple value : values)
        {
            // TODO ugly!!
            if (value.y == "string")
                preparedStatement.setString(counter, value.getXvalueAsString());
            else if (value.y == "numeric")
                preparedStatement.setInt(counter, value.getXvalueAsInt());
            else
                throw new Exception("Method does not know type!");

            counter++;
        }
        return this;
    }

    public List<Tuple<String, Object>> retrieveRow(ResultSet res) throws Exception {

        List<Tuple<String, Object>> listOfColumns = new ArrayList<>();

        for (Tuple value : this.dataSetSchema)
        {
            // TODO better Tuple class to give yiu the ability => equalUpper
            Object v = null;
            if (value.y == "string")
                v = res.getString(value.getXvalueAsString());
            else if (value.y == "numeric")
                v = res.getInt(value.getXvalueAsString());
            else
                throw new Exception("Method does not know type!");

            listOfColumns.add(new Tuple<>(value.getXvalueAsString(),v));
        }

        return listOfColumns;
    }


    public JDBC(MySqlConnection connection) throws SQLException {
        this.connection = connection.getConnection();
    }

    @Override
    public void disconnect() throws SQLException {
        if (!connection.isClosed()) {
            // close Statement and Connection
            try {
                preparedStatement.close();
                connection.close();

            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }

    public void setLastInsertedRowId(Integer lastInsertedRowId) {
        this.lastInsertedRowId = lastInsertedRowId;
    }

    public JDBC setDataSetSchema(ArrayList<Tuple> dataSetSchema) throws Exception {
        this.dataSetSchema = dataSetSchema;
        return this;
    }

    public boolean isPreparedStatementSet() {
        return this.preparedStatement != null;
    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }
}
