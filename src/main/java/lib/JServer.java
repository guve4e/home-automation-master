package lib;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class JServer {


    private ObjectOutputStream output; // output stream to client
    private BufferedReader input; // input stream from client
    private ServerSocket server; // server socket
    private Socket connection; // connection to client
    private int counter = 1; // counter of number of connections
    private int portNumber;
    private String message;

    public JServer(int portNumber)
    {
        this.portNumber = portNumber;
        runServer();
    }

    /**
     * Set up and run server
     */
    public void runServer()
    {
        try
        {
            this.server = new ServerSocket(this.portNumber);

            while (true)
            {
                try
                {
                    waitForConnection(); // wait for a connection
                    getStreams(); // get input & output streams
                    processConnection(); // process connection
                }
                catch (Exception e)
                {
                    System.out.println("\nServer terminated connection. Msg: " + e.getMessage());
                }
                finally
                {
                    closeConnection(); //  close connection
                    ++counter;
                }
            }
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    /**
     * Wait for connection to arrive,
     * then display connection info
     * @throws IOException
     */
    private void waitForConnection() throws IOException  {

        System.out.println("Waiting for connection\n");
        connection = server.accept(); // allow server to accept connection
        System.out.println("Connection " + counter + " received from: " +
                connection.getInetAddress().getHostName());
    }

    // get streams to send and receive data
    private void getStreams() throws IOException
    {
        // set up output stream for objects
        this.output = new ObjectOutputStream(connection.getOutputStream());
        this.output.flush(); // flush output buffer to send header information

        // set up input stream for objects
        this.input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        System.out.println("Client connected on port " + this.portNumber +". Servicing requests.");

        String inputLine;
        while ((inputLine = this.input.readLine()) != null) {
            System.out.println("Received message: " + inputLine + " from " + connection.toString());
           // out.println(inputLine);
        }


        // for (String line; (line = input.readLine()) != null; response += line);
    }

    // process connection with client
    private void processConnection() throws IOException {

        // send connection successful message
        sendData(this.message);

        System.out.println(message);
    }

    // close streams and socket
    private void closeConnection() {
        try
        {
            output.close(); // close output stream
            input.close(); // close input stream
            connection.close(); // close socket
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    /**
     * send message to client
     * @param message
     */
    private void sendData(String message) {
        try // send object to client
        {
            output.writeObject(message);
            output.flush(); // flush output to client
        }
        catch (IOException ioException)
        {
            System.out.println("\nError writing object");
        }
    }
}
