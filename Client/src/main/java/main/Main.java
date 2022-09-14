package main;

import client.Client;
import exceptions.ConnectionException;
import exceptions.ConnectionTimeoutException;
import exceptions.InvalidAddressException;

import java.util.Scanner;

public class Main {
    public static void main(String[] args)  {
        String addr  = "";
        int port = 0;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("enter address: ");
            try {
                addr = scanner.nextLine();
            }catch (IllegalStateException e){
                break;
            }
            System.out.print("enter port: ");
            try {
                port = Integer.parseInt(scanner.nextLine());
            }catch (NumberFormatException e){
                continue;
            }
            Client client = null;
            try {
                client = new Client(addr, port);
            } catch (InvalidAddressException e) {
                System.err.println("Connection fail");
                continue;
            } catch (ConnectionTimeoutException e){
                System.err.println("The server is working with another client, try connecting later");
                continue;
            } catch (ConnectionException e) {
                e.printStackTrace();
                break;
            }
            client.run(scanner);
        }
    }
}
