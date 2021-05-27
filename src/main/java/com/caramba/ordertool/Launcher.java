package com.caramba.ordertool;

public class Launcher {

    public static void main(String[] args) {
        if(args.length > 1 && args[0] == "commandline"){
            OrderToolCmd.main(removeFirstArg(args));
        }else{
            OrderToolGui.main(args);
        }
    }

    private static String[] removeFirstArg(String[] args){
        if(args.length > 1){
            String [] newArgs = new String[args.length - 1];
            System.arraycopy(args, 1, newArgs, 0, args.length - 1);
            return newArgs;
        }else{
            return args;
        }
    }
}
