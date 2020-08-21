package net.baumarkt.servermanager.utils.funciton;

/*
 * Created on 11.08.2020 at 09:25
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import com.google.common.collect.Lists;

import java.util.List;

public class FunctionHandler {

    private final List<Function> functions;

    public FunctionHandler(){
        functions = Lists.newArrayList();
    }

    public void registerFunction(final Function function){
        functions.add(function);
    }

    public Function getFunctionByName(final String name){
        for (Function function : functions) {
            if(function.getName().equals(name))
                return function;
        }
        return null;
    }

    public List<Function> getFunctions() {
        return functions;
    }
}
