package commands;



import connection.AnswerMsg;
import connection.Request;
import data.Person;
import exceptions.CommandException;
import file.ReaderWriter;
import server.*;
import log.*;

import collection.CollectionManager;

import java.util.LinkedList;

public class ServerCommandManager extends CommandManager{
    private Server server;
    private CollectionManager<Person> collectionManager;
    private ReaderWriter fileManager;
    LinkedList<String> history = new LinkedList<String>();
    public ServerCommandManager(Server  serv){
        server = serv;
        collectionManager = server.getCollectionManager();
        fileManager = server.getFileManager();
        addCommand(new ExitCommand());
        addCommand(new HelpCommand());
        addCommand(new InfoCommand(collectionManager));
        addCommand(new AddCommand(collectionManager));
        addCommand(new AddIfMaxCommand(collectionManager));
        addCommand(new UpdateCommand(collectionManager));
        addCommand(new ClearCommand(collectionManager));
        addCommand(new ShowCommand(collectionManager));
        addCommand(new SaveCommand(collectionManager,fileManager));
        addCommand(new LoadCommand(collectionManager,fileManager));
        addCommand(new PrintAscendingCommand(collectionManager));
        addCommand(new UpdateCommand(collectionManager));
        addCommand(new RemoveGreaterCommand(collectionManager));
        addCommand(new RemoveByIdCommand(collectionManager));
        addCommand(new PrintFieldAscendingEyeColor(collectionManager));
        addCommand(new FilterContainsNameCommand(collectionManager));
        addCommand(new HistoryCommand(collectionManager, history));
    }
    public Server getServer(){
        return server;
    }
    @Override
    public AnswerMsg runCommand(Request msg) {
        AnswerMsg res = new AnswerMsg();
        try {
            Command cmd = getCommand(msg.getCommandName());
            cmd.setArgument(msg);
            res = (AnswerMsg) cmd.run();
            if (msg.getStringArg() != null) {
                history.add(msg.getCommandName() + " " + msg.getStringArg());
            }
            else {
                history.add(msg.getCommandName());
            }
        } catch (CommandException e){
            res.error(e.getMessage());
        }
        switch (res.getStatus()){
            case EXIT:
                Log.logger.fatal(res.getMessage());
                server.close();
                break;
            case ERROR:
                Log.logger.error(res.getMessage());
                break;
            default:
                Log.logger.info(res.getMessage());
                break;
        }
        return res;
    }
}
