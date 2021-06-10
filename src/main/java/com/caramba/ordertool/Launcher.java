package com.caramba.ordertool;

public class Launcher {

    public static void main(String[] args) {
        if(args.length > 1 && args[0].equals("commandline")){
            switch (args[0]){
                //case "commandline" -> OrderToolCmd.main(removeFirstArg(args)); command line version is broken
                case "report" -> OrderTool.updateReports();
                case "gui" -> OrderTool.main(removeFirstArg(args));
            }
        }else{
            OrderTool.main(args);
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
