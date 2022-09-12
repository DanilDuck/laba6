package server;

import collection.CollectionManager;
import collection.PersonCollectionManager;
import commands.CommandType;
import commands.Commandable;
import commands.ServerCommandManager;
import connection.*;
import data.Person;
import exception.ServerOnlyCommandException;
import exceptions.*;
import file.FileManager;
import file.ReaderWriter;
import log.Log;

import java.io.*;
import java.net.*;

import java.nio.channels.*;
import java.time.LocalDateTime;
import java.util.Scanner;


public class Server extends Thread implements SenderReceiver {

    private CollectionManager<Person> collectionManager;
    private ReaderWriter fileManager;
    private ServerCommandManager commandManager;
    private int port;
    private InetAddress clientAddress;
    private ServerSocketChannel serverSocketChannel;
    private ServerSocket serverSocket;
    ObjectInputStream clientReader ;
    ObjectOutputStream clientWriter;
    Socket socket = null;
    private Selector selector;

    private volatile boolean running;

    private void init(int p, String path) throws ConnectionException {
        running = true;
        port = p;
        collectionManager = new PersonCollectionManager();
        fileManager = new FileManager(path);
        commandManager = new ServerCommandManager(this);
        collectionManager.deserializeCollection(fileManager.read());
        host(port);
        setName("server thread");
        Log.logger.trace("starting server");
    }

    private void host(int p) throws ConnectionException {
        try {
            serverSocket = new ServerSocket(p);
        } catch (IOException e) {
            System.out.println("Error port");
        }
    }
    public Server(int p, String path) throws ConnectionException {
        init(p, path);
    }

    /**
     * receives request from client
     *
     * @return
     * @throws ConnectionException
     * @throws InvalidDataException
     */
    public Request receive() throws InvalidDataException, ConnectionException {
        try {
            clientReader = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new ConnectionException("something went wrong while reading request");
        }
        try {
            Request request = (Request) clientReader.readObject();
            return request;
        } catch (ClassNotFoundException e) {
            throw new InvalidDataException("Invalid data");
        } catch (IOException e) {
            throw new ConnectionException("connection");
        }
    }

    /**
     * sends response
     *
     * @param response
     * @throws ConnectionException
     */
    public void send(Response response) throws ConnectionException {
        try {
            clientWriter = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            clientWriter.writeObject(response);
            clientWriter.flush();
        } catch (IOException e) {
            throw new ConnectionException("Cant send");
        }
    }

    public void checkConnection(){
        while (true) {
            try {
                socket = serverSocket.accept();
                System.out.println("Client Connected");
                break;
            } catch (IOException e) {
                continue;
            }
        }
    }
    /**
     * runs server
     */
    public void run() {
        checkConnection();
        while (running) {
            AnswerMsg answerMsg = new AnswerMsg();
            try {
                try {
                    Request commandMsg = receive();
                    if (commandMsg.getPerson() != null) {
                        commandMsg.getPerson().setCreationDateTime(LocalDateTime.now());
                    }
                    if (commandManager.getCommand(commandMsg).getType() == CommandType.SERVER_ONLY) {
                        throw new ServerOnlyCommandException();
                    }
                    answerMsg = commandManager.runCommand(commandMsg);
                    if (answerMsg.getStatus() == Status.EXIT) {
                        close();
                    }
                } catch (CommandException e) {
                    answerMsg.error(e.getMessage());
                    Log.logger.error(e.getMessage());
                }
                send(answerMsg);
            } catch (InvalidDataException e) {
                e.printStackTrace();
                Log.logger.error(e.getMessage());
            }catch (ConnectionException e){
                System.err.println("Client disconnected");
                checkConnection();
            }
        }
    }


    public void consoleMode() {
        commandManager.consoleMode();
    }

    /**
     * close server and connection
     */
    public void close() {
        try {
            running = false;
            if(serverSocket!= null){
                serverSocket.close();
            }
            if(socket!= null) {
                socket.close();
            }
            if(clientReader!= null) {
                clientReader.close();
            }
            if(clientWriter!= null) {
                clientReader.close();
            }
        } catch (IOException e) {
            Log.logger.error("cannot close channel");
        }
        stop();
        throw new ExitException();
    }

    public Commandable getCommandManager() {
        return commandManager;
    }

    public ReaderWriter getFileManager() {
        return fileManager;
    }

    public CollectionManager<Person> getCollectionManager() {
        return collectionManager;
    }
}