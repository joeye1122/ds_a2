import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class GETClient {
    private String serverAddress;
    private int portNumber;
    private String stationID;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java GETClient <serveraddress:portnumber> <stationID>");
            System.exit(1);
        }

        String serverAddressPort = args[0];
        String[] addressPortParts = serverAddressPort.split(":");
        if (addressPortParts.length != 2) {
            System.out.println("Invalid server address format. Use 'serveraddress:portnumber'.");
            System.exit(1);
        }

        String serverAddress = addressPortParts[0];
        int portNumber = Integer.parseInt(addressPortParts[1]);
        String stationID = args[1];
        
        GETClient getClient = new GETClient(serverAddress, portNumber, stationID);
        getClient.start();

    }

    public GETClient(String serverAddress, int portNumber, String stationID){
        this.serverAddress = serverAddress;
        this.portNumber = portNumber;
        this.stationID = stationID;
    }

    public void start(){
        try{
            Socket socket = new Socket(serverAddress, portNumber);

            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), false);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("GET " + stationID + " HTTP/1.1");

            out.flush();
            socket.shutdownOutput();

            String responseLine;
            StringBuilder stringBuilder = new StringBuilder();

            while ((responseLine = in.readLine()) != null) {
                 stringBuilder.append(responseLine);
            }
            socket.close();
            WeatherData data = UtilTool.parseJSON(stringBuilder.toString());
            System.out.println(data.toString());

        } catch (IOException e) {
        }
    }
}
