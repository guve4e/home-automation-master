

import lib.Bar;
import lib.JServer;

import java.io.IOException;


public class Main {
    public static void main(String[] args) {


        try {
            new JServer(12344);

        } catch (Exception e) {
            e.printStackTrace();
        }


        // View

        //Login loginView = new Login(userModel);

        // Controller
        //new LoginController(loginView, userModel);

    }
}
