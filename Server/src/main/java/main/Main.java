package main;

import exceptions.ConnectionException;
import exceptions.ExitException;
import exceptions.InvalidPortException;
import server.*;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws Exception {
        Server server;
        int port = 1337;
        Scanner scanner = new Scanner(System.in);
        System.out.print("enter port: ");
        while (true) {
            try {
                port = Integer.parseInt(scanner.nextLine());
            }catch (IllegalStateException e){
                break;
            }catch (NumberFormatException e){
                System.out.print("enter port: ");
                continue;
            }
            String strPort = "";
            String path = "person_database";
            server = new Server(port, path);
            server.start();
            try {
                server.consoleMode();
            }catch (ExitException e){
                break;
            }
            break;
        }
    }
}

