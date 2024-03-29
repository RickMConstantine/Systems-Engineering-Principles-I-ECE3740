package standardio;

import java.io.*;

public class StandardIO implements Runnable, userinterface.UserInterface{

    BufferedReader console = null;
    usercommandhandler.UserCommandHandler myCommand;

    public StandardIO() {
        console = new BufferedReader(new InputStreamReader(System.in));
        if (console == null) {
            System.err.println("No Standard Input device, exiting program.");
            System.exit(1);
        }
    }
    
    @Override
    public void update(String theMessage){
        System.out.println(theMessage);
    }
    
    public void setCommand(usercommandhandler.UserCommandHandler myCommand) {
        this.myCommand = myCommand;
    }

    public String getUserInput() {
        String userInput = "no input";

        try {
            userInput = console.readLine();
            return userInput;
        } catch (IOException e) {
            System.err.println("Cannot read input");
            System.exit(1);
        }
        return userInput;
    }

    @Override
    public void run() {
        while (true) {
            String userInput = getUserInput();
            myCommand.execute(userInput);
        }
    }
}
