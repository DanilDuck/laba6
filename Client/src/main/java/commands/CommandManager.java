package commands;

import connection.CommandMsg;
import connection.Response;
import connection.Status;
import collection.CollectionManager;
import data.Person;
import exceptions.*;
import file.ReaderWriter;
import io.ConsoleInputManager;
import io.FileInputManager;
import io.InputManager;

import java.util.*;

public abstract class CommandManager implements Commandable {
    public boolean Running;
    private Map<String,Command> map;
    private InputManager inputManager;
    private String currentScriptFileName;
    private CollectionManager<Person> collectionManager;
    private HashSet<String> passportId;
    private ReaderWriter fileManager;
    private LinkedList<String> historyCommands = new LinkedList<>();
    private static Stack<String> callStack = new Stack<>();
    public void clearStack(){
        callStack.clear();
    }
    public Stack<String> getStack(){
        return callStack;
    }
    public CommandManager(){
        Running = false;
        currentScriptFileName = "";
        map = new HashMap<String,Command>();
    }
    public void addCommand(Command c) {
        map.put(c.getName(),c);
    }
    public void addCommand(String key, Command c){
        map.put(key, c);
    }
    public Command getCommand(String s){
        if (! hasCommand(s)) throw new NoSuchCommandException();
        Command cmd =  map.get(s);
        return cmd;
    }
    public boolean hasCommand(String s){
        return map.containsKey(s);
    }
    public String getCurrentScriptFileName(){
        return currentScriptFileName;
    }
    public void fileMode(String path) throws FileException {
        currentScriptFileName = path;
        inputManager = new FileInputManager(path);
        Running = true;
        while(Running && inputManager.getScanner().hasNextLine()){
            CommandMsg commandMsg= inputManager.readCommand();
            Response answerMsg = runCommand(commandMsg);
            if(answerMsg.getStatus()==Status.EXIT) {
                close();
            }
        }
    }
    public void consoleMode(){
        inputManager = new ConsoleInputManager();
        Running = true;
        while(Running){
            System.out.print("enter command (help to get command list): ");
            CommandMsg commandMsg = inputManager.readCommand();
            Response answerMsg = runCommand(commandMsg);
            if(answerMsg.getStatus()== Status.EXIT) {
                close();
            }
        }
    }
    public void setInputManager(InputManager in){
        inputManager = in;
    }
    public InputManager getInputManager(){
        return inputManager;
    }

    public static String getHelp(){
        return "\r\nhelp : show help for available commands\r\n\r\ninfo : Write to standard output information about the collection (type,\r\ninitialization date, number of elements, etc.)\r\n\r\nshow : print to standard output all elements of the collection in\r\nstring representation\r\n\r\nadd {element} : add a new element to the collection\r\n\r\nupdate id {element} : update the value of the collection element whose id\r\nequal to given\r\n\r\nremove_by_id id : remove an element from the collection by its id\r\n\r\nclear : clear the collection\r\n\r\nsave (file_name - optional) : save the collection to a common.file\r\n\r\nload (file_name - optional): load collection from common.file\r\n\r\nexecute_script file_name : read and execute script from specified common.file.\r\nThe script contains commands in the same form in which they are entered\r\nuser is interactive.\r\n\r\nexit : exit the program (without saving to a common.file)\r\n\r\nremove_first : remove the first element from the collection\r\n\r\nadd_if_max {element} : add a new element to the collection if its\r\n\r\nvalue is greater than the value of the largest element of this collection\r\n\r\nadd_if_min {element} : add a new element to the collection if it\r\nthe value is less than the smallest element of this collection\r\n\r\ngroup_counting_by_end_date : group the elements of the collection by\r\nthe value of the endDate field, display the number of elements in each group\r\n\r\nfilter_starts_with_name name : output elements, value of field name\r\nwhich starts with the given substring\r\n\r\nprint_unique_salary : print the unique values of the salary field of all\r\nitems in the collection\r\n";
    }
    public boolean isRunning(){
        return Running;
    }
    public void setRunning(boolean running){
        Running = running;
    }
    public void close(){
        setRunning(false);
    }
}
