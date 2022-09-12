package commands;

import connection.Request;
import connection.Response;
import exceptions.FileException;

public interface Commandable {
    public default boolean hasCommand(Request req){
        return hasCommand(req.getCommandName());
    }
    public default Command getCommand(Request req){
        return getCommand(req.getCommandName());
    }
    /**
     * adds command
     * @param key command name
     * @param cmd command callback
     */
    public default void addCommand(String key, Command cmd){
        addCommand(key,cmd);
    }
    /**
     * adds command
     * @param cmd
     */
    public void addCommand(Command cmd);
    /**
     * runs command
     * @param req
     * @return
     */
    public Response runCommand(Request req);
    public Command getCommand(String key);
    /**
     * checks if a command exists
     * @param s
     */
    public boolean hasCommand(String s);

    /**
     * runs in command interpreter in console
     */
    public void consoleMode();

    /**
     * executes script from file
     * @param path
     */
    public void fileMode(String path) throws FileException;
}
