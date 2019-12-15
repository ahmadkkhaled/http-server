package com.company;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * V2.0 of HTTP Task Client
 * this version reads the response from the server and does the following
 * 1- reads and discards the header that was sent by the server
 * 2- reads the body of the response and writes it to a file (responseFile.html)
 * 3- runs the response file
 */
public class Client {
    static void openHtml(File file, String FilePath)
    {
        try
        {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE) && !FilePath.contains(".txt")) {
                Desktop.getDesktop().browse(file.toURI()); /// URI throws exception that must be handled
                /**
                 * the browse() function opens websites with standard paths e.g. www.example.com
                 * or runs the actual file using .toURI() function
                 */
            }
        }
        catch (Exception e) { System.out.println(e.getMessage()); }
    }
    public static void main(String args[]) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8080); //connect to host 127.0.0.1:8080
        Scanner userInput = new Scanner(System.in), socketInput = new Scanner(socket.getInputStream());
        PrintStream socketOutput = new PrintStream(socket.getOutputStream());
        System.out.println("What function would you like to do?:\n" +
                "1- GET\n" +
                "2- POST\n" +
                "3- DELETE\n" +
                "4- PUT\n");
        int choice;
        choice = userInput.nextInt();
        String URL, Request = "";
        if (choice == 1)
        {
            System.out.print("Enter URL: ");
            Request += "GET ";
            URL = userInput.next();
            Request += URL;
            Request += " HTTP/1.1";
            Request += "\r\n\r\n";
        }
        if (choice == 2)
        {
            System.out.println("Enter URL: ");
            Request += "POST ";
            URL = userInput.next();
            Request += URL;
            Request += " HTTP/1.1";
            Request += "\r\n";
            System.out.println("Enter Body (DONE! to stop): \n");
            String line;
            while(true)
            {
                line = userInput.nextLine();
                if (line.equals("DONE!"))
                    break;
                Request += line;
                Request += "\r\n";
            }
            Request += "\r\n\r\n";
        }
        else if (choice == 3)
        {
            System.out.print("Enter URL: ");
            Request += "DELETE ";
            URL = userInput.next();
            Request += URL;
            Request += " HTTP/1.1";
            Request += "\r\n\r\n";
        }
        else if (choice == 4)
        {
            System.out.println("Enter URL: ");
            Request += "PUT ";
            URL = userInput.next();
            Request += URL;
            Request += " HTTP/1.1";
            Request += "\r\n";
            System.out.println("Enter Body (DONE! to stop): \n");
            String line;
            while(true)
            {
                line = userInput.nextLine();
                if (line.equals("DONE!"))
                    break;
                Request += line;
                Request += "\r\n";
            }
            Request += "\r\n\r\n";

        }
        socketOutput.print(Request);
        File responseFile = new File ("D:/htdocs/response.html");
        FileWriter responseWriter = new FileWriter("D:/htdocs/response.html"); //append = false by default
        String response = "";
        if(socketInput.hasNextLine()) // if there exists a response
        {
            for (int i = 0; i < 5; ++i) // read response header then discard it.
            {
                String discard = "";
                discard = socketInput.nextLine();
                if(discard.contains("HTTP/1.0 200 OK")) /// handling 6 lines header
                {
                    discard = socketInput.nextLine();
                }
            }
            while(socketInput.hasNextLine())
            {
                response += socketInput.nextLine();
                response += '\n';
            }
            responseWriter.write(response);
            responseWriter.close();
            openHtml(responseFile, "D:/htdocs/response.html");
        }
    }
}
