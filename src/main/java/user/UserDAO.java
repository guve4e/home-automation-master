package user;

import lib.database.MySql;
import lib.database.MySqlConnection;
import models.User;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import lib.utility.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDAO implements IUserDAO {

    final ArrayList<Tuple> userSchema = new ArrayList<Tuple>() {
        {
            add(new Tuple<>("U_ID", "numeric"));
            add(new Tuple<>("U_HASH", "string"));
            add(new Tuple<>("U_USERNAME", "string"));
            add(new Tuple<>("U_PASSWORD", "string"));
        }
    };


    private List<Tuple> packUser(User user) {
        return  new ArrayList<Tuple>() {
            {
                add(new Tuple<>(user.getHash(), "string"));
                add(new Tuple<>(user.getUsername(), "string"));
                add(new Tuple<>(user.getPassword(), "string"));
            }
        };
    }

    private User unpackUser(Map<String, Object> userMap) {
        User user = new User();
        // user.setFirstName(userMap.);

        userMap.entrySet().stream().forEach(
                (entry) -> {
                if (entry.getKey().equals("U_USERNAME"))
                        user.setUsername((String) entry.getValue());
                    else if (entry.getKey().equals("U_PASSWORD"))
                        user.setPassword((String) entry.getValue());
                    else if (entry.getKey().equals("U_ID"))
                        user.setUserId((Integer) entry.getValue());
                    else
                        try {
                            throw new Exception("Something is wrong with user object, " +
                                    "retrieved from DB!");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                });

        return user;
    }

    public User authenticateUser(String username, String password) {

        Map<String, Object> userRows = null;
        User user = null;

        try {

            MySql db = new MySql(new MySqlConnection(new MysqlDataSource()));

            db.setQuery("SELECT * FROM USER WHERE U_USERNAME = ? AND U_PASSWORD = ?",
                    new ArrayList<Tuple>() {
                        {
                            add(new Tuple<>(username, "string"));
                            add(new Tuple<>(password, "string"));
                        }
                    })
                    .setDataSetSchema(userSchema);

            userRows = db.queryRead().getDataSetAsMap();
            user = this.unpackUser(userRows);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public User getUser(int userId) {

        Map<String, Object> userRows = null;
        User user = null;

        try {

            MySql db = new MySql(new MySqlConnection(new MysqlDataSource()));

            db.setQuery("SELECT * FROM USER WHERE U_ID = ?",
                    new ArrayList<Tuple>() {
                        {
                            add(new Tuple<>(userId, "numeric"));
                        }
                    })
                    .setDataSetSchema(userSchema);

            userRows = db.queryRead().getDataSetAsMap();
            user = this.unpackUser(userRows);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void createUser(User user) {

        try {

            MySql db = new MySql(new MySqlConnection(new MysqlDataSource()));

            db.setQuery("INSERT INTO USER (U_FNAME, U_LNAME, U_HASH, U_USERNAME, U_PASSWORD) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    packUser(user))
                    .queryWrite();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(User user) {
        try {

            MySql db = new MySql(new MySqlConnection(new MysqlDataSource()));

            db.setQuery("UPDATE USER" +
                             "SET U_FNAME = ?, U_LNAME = ?, U_HASH = ?, U_USERNAME = ?, U_PASSWORD = ?" +
                            "WHERE U_ID = ?",
                    packUser(user))
                    .queryWrite();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(User user) {

    }
}
