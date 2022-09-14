package client;


import commands.ClientCommandManager;
import connection.Request;
import connection.Response;
import connection.SenderReceiver;
import exceptions.*;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * client class
 */
public class Client  implements SenderReceiver {
    private boolean running;
    private ClientCommandManager commandManager;
    SocketChannel socketChannel;
    /**
     * initialize client
     * @param addr
     * @param p
     * @throws ConnectionException
     */
    private void init(String addr, int p) throws ConnectionException {
        connect(addr,p);
        running = true;
        commandManager = new ClientCommandManager(this);
    }

    public Client(String addr, int p) throws ConnectionException{
        init(addr,p);
    }

    /**
     * connects client to server
     * @param addr
     * @param p
     * @throws ConnectionException
     */
    public void connect(String addr, int p) throws InvalidAddressException, ConnectionTimeoutException {
        InetSocketAddress inetSocketAddress = null;
        try {
            inetSocketAddress = new InetSocketAddress(InetAddress.getByName(addr), p);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            socketChannel = SocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                System.out.println("Trying to connect to the server");
                socketChannel.connect(inetSocketAddress);
                System.out.println("Connected to the server");
                break;
            } catch (IOException e) {
                throw new ConnectionTimeoutException();
            }
        }
    }

    /**
     * sends request to server
     * @param request
     * @throws ConnectionException
     */
    public void send(Request request) throws ConnectionException {
        try{
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(BUFFER_SIZE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(request);
            socketChannel.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
        } catch(IOException e){
            throw new ConnectionException("something went wrong during sending request");
        }
    }

    /**
     * receive message from server
     * @return
     * @throws ConnectionException
     * @throws InvalidDataException
     */
    public Response receive()throws ConnectionException, InvalidDataException {
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ByteBuffer buf = ByteBuffer.allocate(BUFFER_SIZE);
        try {
            socketChannel.read(buf);
        } catch (IOException e) {
            throw new ConnectionException("something went wrong during receiving request");
        }
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(buf.array()));
            Response res = (Response) objectInputStream.readObject();
            return res;
        } catch (ClassNotFoundException e) {
            System.out.println("Class");
            throw new InvalidReceivedDataException();
        } catch (IOException e) {
            System.out.println("IO");
            throw new ConnectionException("something went wrong during receiving request");
        }
    }
    /**
     * runs client until interrupt
     */

    public void run(Scanner scanner){
        commandManager.consoleMode();
        scanner.close();
        close();
    }
    public void consoleModeStart(){
        commandManager.consoleMode();
    }

    /**
     * close client
     */
    public void close() {
        running = false;
        commandManager.close();
    }
}
