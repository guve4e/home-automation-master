package lib.database;

import com.mysql.jdbc.JDBC4Connection;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import lib.utility.Tuple;
import junit.framework.TestCase;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class MySqlTest {

    private MySqlConnection mySqlConnection;

    @Mock
    private MysqlDataSource ds;

    @Mock
    private JDBC4Connection jdbcConnection;

    @Mock
    private PreparedStatement statement;

    @Mock
    private ResultSet resultSet;

    @Before
    public void setUp() throws Exception {

        // check if class is wrapped with Runner
        assertNotNull(ds);

        // when getConnection is invoked stub it with jdbcConnection
        when(ds.getConnection()).thenReturn(jdbcConnection);

        // when isClosed method is invoked, give it false,
        // mock an open connection
        when(jdbcConnection.isClosed()).thenReturn(false);

        // when prepareStatement is invoked stub it wit mock PrepareStatement
        when(jdbcConnection.prepareStatement(anyString())).thenReturn(statement);

        // create real MySqlConnection, but with mocked MysqlDataSet
        this.mySqlConnection = new MySqlConnection(ds);

        // create ResultSet with 3 rows
        Mockito.when(resultSet.next())
        .thenReturn(true)
        .thenReturn(true)
        .thenReturn(true)
        .thenReturn(false);

        Mockito.when(resultSet.getString("R_COMMENT")).thenReturn("Some comment");
        Mockito.when(resultSet.getInt("R_ID")).thenReturn(12345);

        // when Prepare Statement is invoked with executeQuery,
        // give it the mock Result Set
        when(statement.executeQuery()).thenReturn(resultSet);

       // when(statement.executeUpdate()).the
    }

    @After
    public void tearDown() throws Exception {
        // TODO
    }

    @Test
    public void testConnection() throws Exception {
        Mockito.when(jdbcConnection.createStatement())
                .thenReturn(statement);
        Mockito.when(jdbcConnection.createStatement()
                .executeUpdate(Mockito.anyString())).thenReturn(1);
    }

    @Test
    public void queryWrite() throws Exception {

        MySql m = new MySql(mySqlConnection);

        m.setQuery("INSERT INTO REVIEW (R_COMMENT, R_STAR) VALUES (?, ?)",
                new ArrayList<Tuple>() {
                    {
                        add(new Tuple<>("Some Comment", "string"));
                        add(new Tuple<>(12345, "numeric"));
                    }
                })
                .queryWrite();

        when(statement.executeUpdate()).then((Answer<String>) invocationOnMock -> {
            String query = (String) invocationOnMock.getArgument(0);
            if (query.equals("INSERT INTO REVIEW (R_COMMENT, R_STAR) VALUES ('S', ?)")) {
                return null;
            }
            throw new RuntimeException("Update string is not as expected: " + query);
        });
    }

    @Test
    public void testQueryReadGetBackNestedListOfTuple() throws Exception {
        // Arrange
        List<List<Tuple<String, Object>>> actualRows = null;
        List<List<Tuple<String, Object>>> expectedRows = new ArrayList<List<Tuple<String, Object>>>() {
            {
                add(new ArrayList<Tuple<String, Object>>() {
                    {
                        add(new Tuple<>("R_COMMENT", "Some comment"));
                        add(new Tuple<>("R_ID", 12345));
                    }
                });

                add(new ArrayList<Tuple<String, Object>>() {
                    {
                        add(new Tuple<>("R_COMMENT", "Some comment"));
                        add(new Tuple<>("R_ID", 12345));
                    }
                });

                add(new ArrayList<Tuple<String, Object>>() {
                    {
                        add(new Tuple<>("R_COMMENT", "Some comment"));
                        add(new Tuple<>("R_ID", 12345));
                    }
                });
            }
        };

        MySql m = new MySql(mySqlConnection);

        m.setQuery("SELECT * FROM REVIEW WHERE R_ID = ?",
                new ArrayList<Tuple>() {
                    {
                        add(new Tuple<>(1, "numeric"));
                    }
                })
                .setDataSetSchema(new ArrayList<Tuple>() {
                    {
                        add(new Tuple<>("R_COMMENT", "string"));
                        add(new Tuple<>("R_ID", "numeric"));
                    }
                });
        actualRows = m.queryRead().getDataSetAsNestedListOfTuple();

        // Assert
        TestCase.assertEquals("Verify same metrics series",
                CollectionUtils.getCardinalityMap(expectedRows),
                CollectionUtils.getCardinalityMap(actualRows));
    }

    @Test
    public void testQueryReadGetBackMap() throws Exception {
        // Arrange
        Map<String, Object> actualMap = null;
        Map<String, Object> expectedMap = new HashMap<String, Object>() {
            {
                put("R_COMMENT", "Some comment");
                put("R_ID", 12345);
            }
        };

        MySql m = new MySql(mySqlConnection);

        m.setQuery("SELECT * FROM REVIEW WHERE R_ID = ?",
                new ArrayList<Tuple>() {
                    {
                        add(new Tuple<>(1, "numeric"));
                    }
                })
                .setDataSetSchema(new ArrayList<Tuple>() {
                    {
                        add(new Tuple<>("R_COMMENT", "string"));
                        add(new Tuple<>("R_ID", "numeric"));
                    }
                });
        actualMap = m.queryRead().getDataSetAsMap();

        // Assert
        TestCase.assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testRetrieveRow() throws Exception {
        // Arrange
        MySql m = new MySql(new MySqlConnection(this.ds));
        m.setDataSetSchema(new ArrayList<Tuple>() {
                    {
                        add(new Tuple<>("R_COMMENT", "string"));
                        add(new Tuple<>("R_ID", "numeric"));
                    }
                });

        ResultSet rs = mock(ResultSet.class);
        when(rs.getString("R_COMMENT")).thenReturn("Some comment");
        when(rs.getInt("R_ID")).thenReturn(12345);

        List<Tuple<String, Object>> expectedList = new ArrayList<Tuple<String, Object>>() {
            {
                add(new Tuple<>("R_COMMENT", "Some comment"));
                add(new Tuple<>("R_ID", 12345));
            }
        };


        // Act
       List<Tuple<String, Object>> actualList =  m.retrieveRow(rs);

        // Assert
        TestCase.assertEquals("Verify same metrics series",
                CollectionUtils.getCardinalityMap(expectedList),
                CollectionUtils.getCardinalityMap(actualList));
    }

    @Test
    public void disconnect() {


    }
}