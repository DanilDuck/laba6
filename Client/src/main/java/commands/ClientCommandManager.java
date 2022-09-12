package commands;

import client.Client;
import connection.AnswerMsg;
import connection.Request;
import connection.Status;
import exceptions.ConnectionException;
import exceptions.ConnectionTimeoutException;
import exceptions.InvalidDataException;

/**
 * command manager for client
 */
public class ClientCommandManager extends CommandManager{
    private Client client;
    public ClientCommandManager(Client c){
        client = c;
        addCommand(new ExecuteScriptCommand(this));
        addCommand(new ExitCommand());
        addCommand(new HelpCommand());
    }

    public Client getClient(){
        return client;
    }
    @Override

    public AnswerMsg runCommand(Request msg) {
        AnswerMsg res = new AnswerMsg();
        if (hasCommand(msg)){
            Command cmd =  getCommand(msg);
            cmd.setArgument(msg);
            res = (AnswerMsg)cmd.run();
        } else {
            try{
                client.send(msg);
                res = (AnswerMsg)client.receive();
            }
            catch (ConnectionTimeoutException e){
                res.info("no attempts left, shutting down").setStatus(Status.EXIT);
            }
            catch(InvalidDataException | ConnectionException e){
                res.error(e.getMessage());
            }
        }
        System.out.println(res);
        return res;
    }
}
