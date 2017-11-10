package edu.upc.fib.meetnrun.exceptions;

import java.util.Map;

/**
 * Created by Awais Iqbal on 03/11/2017.
 */

public class GenericException extends Exception {
    public GenericException(){};
    private Map<String, String> problems;

    public GenericException(Map<String,String> problems) {
        this.problems = problems;
    }

    public Map<String, String> getProblems() {
        return problems;
    }

    public void setProblems(Map<String, String> problems) {
        this.problems = problems;
    }
}
