package io;

import exceptions.InvalidDataException;
/**
 * user input wrapper
 * @param <T>
 */
public class Question<T>{
    private Askable<T> askable;
    private T answer;
    public Question(String msg,Askable<T> askable){
        this.askable = askable;
        while (true){
            try{
                System.out.print(msg + " ");
                T ans = this.askable.ask();
                answer = ans;
                break;
            }
            catch(InvalidDataException e){
                System.err.println("Error:"+e.getMessage());
            }
        }
    }
    public T getAnswer(){
        return answer;
    }
}
