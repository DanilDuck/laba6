package commands;

import connection.Request;
import connection.Response;

/**
 * Command callback interface
 */
public interface Command {
    public Response run();
    public String getName();
    public CommandType getType();
    public void setArgument(Request a);
}
