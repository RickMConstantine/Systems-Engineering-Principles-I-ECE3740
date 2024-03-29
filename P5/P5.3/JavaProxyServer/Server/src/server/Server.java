package server;

import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class Server extends abstractserver.AbstractServer{
    userinterface.UserInterface myUI = null;
    String clientStringMessage = "";

    public Server(int portNumber, int backlog, clientcommandhandler.ClientCommandHandler myClientCommandHandler, userinterface.UserInterface myUI, mx7serverconnection.Client mx7Client) {
        super(portNumber, backlog, myClientCommandHandler, mx7Client);
        this.myUI = myUI;
    }

    private String byteToString(byte theByte){
        byte[] theByteArray = new byte[1];
        theByteArray[0] = theByte;
        String theString = "";
        try {
            theString = new String(theByteArray, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            System.err.println("Cannot convert from UTF-8 to String; exiting program.");
            System.exit(1);
        }
        return theString;
    }
    
    @Override
    public void handleMessageFromClient(clientconnection.ClientConnection myClientConnection, byte clientByteMessage){
        if (clientByteMessage != (byte) 0xFF) {
            String theString = byteToString(clientByteMessage);
            clientStringMessage += theString;
        } else {
            myClientCommandHandler.execute(myClientConnection, clientStringMessage);
            clientStringMessage = "";
        }
    }
    @Override
    public void connectionException(String Message){
        myUI.update(Message);
    }
    @Override
    public void serverStarted(){
        myUI.update("Server started");
    }
    @Override
    public void serverStopped(){
        myUI.update("Server stopped");
    }
    @Override
    public void clientConnected(clientconnection.ClientConnection theCC, Socket clientSocket){
        sendWelcomeMessage(theCC, clientSocket);
        myUI.update("Client connected:\n\tRemote Socket Address = "+clientSocket.getRemoteSocketAddress() + "\n\tLocal Socket Address = " +clientSocket.getLocalSocketAddress());
    }
    @Override
    public void clientDisconnected(clientconnection.ClientConnection theCC, Socket clientSocket){
        sendDisconnectMessage(theCC, clientSocket);
    }
    @Override
    public void clientQuit(clientconnection.ClientConnection theCC, Socket clientSocket){
        sendQuitMessage(theCC, clientSocket);
    }
    private void sendWelcomeMessage(clientconnection.ClientConnection theCC, Socket clientSocket) {
        theCC.sendMessageToClient((byte) 'H');
        theCC.sendMessageToClient((byte) 'e');
        theCC.sendMessageToClient((byte) 'l');
        theCC.sendMessageToClient((byte) 'l');
        theCC.sendMessageToClient((byte) 'o');
        theCC.sendMessageToClient((byte) ':');
        theCC.sendMessageToClient((byte) ' ');
        for (int i = 0; i < clientSocket.getRemoteSocketAddress().toString().length(); i++) {
            theCC.sendMessageToClient((byte) clientSocket.getRemoteSocketAddress().toString().charAt(i));
        }
        theCC.sendMessageToClient((byte) 0xFF);
    }
    private void sendDisconnectMessage(clientconnection.ClientConnection theCC, Socket clientSocket){
        theCC.sendMessageToClient((byte)'D');
        theCC.sendMessageToClient((byte)'i');
        theCC.sendMessageToClient((byte)'s');
        theCC.sendMessageToClient((byte)'c');
        theCC.sendMessageToClient((byte)'o');
        theCC.sendMessageToClient((byte)'n');
        theCC.sendMessageToClient((byte)'n');
        theCC.sendMessageToClient((byte)'e');
        theCC.sendMessageToClient((byte)'c');
        theCC.sendMessageToClient((byte)'t');
        theCC.sendMessageToClient((byte)':');
        theCC.sendMessageToClient((byte)' ');
        for(int i = 0; i < clientSocket.getRemoteSocketAddress().toString().length(); i++)
            theCC.sendMessageToClient((byte)clientSocket.getRemoteSocketAddress().toString().charAt(i));
        theCC.sendMessageToClient((byte)0xFF);
    }
    private void sendQuitMessage(clientconnection.ClientConnection theCC, Socket clientSocket) {
        theCC.sendMessageToClient((byte) 'Q');
        theCC.sendMessageToClient((byte) 'u');
        theCC.sendMessageToClient((byte) 'i');
        theCC.sendMessageToClient((byte) 't');
        theCC.sendMessageToClient((byte) ':');
        theCC.sendMessageToClient((byte) ' ');
        for (int i = 0; i < clientSocket.getRemoteSocketAddress().toString().length(); i++) {
            theCC.sendMessageToClient((byte) clientSocket.getRemoteSocketAddress().toString().charAt(i));
        }
        theCC.sendMessageToClient((byte) 0xFF);
    }

}

