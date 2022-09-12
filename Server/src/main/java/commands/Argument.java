package commands;

import connection.Request;
import data.Person;

public class Argument implements Request {
    private String arg;
    private Person person;
    public Argument(String s, Person w){
        arg = s;
        person = w;
    }
    public String getStringArg(){
        return arg;
    }
    public Person getPerson(){
        return person;
    }
    public String getCommandName(){
        return "";
    }
}
