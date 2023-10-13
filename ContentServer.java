import java.net.Socket;
import java.util.List;
import java.io.*;


public class ContentServer {
    private List<WeatherData> weatherDataList;
    private String serverAddress;
    private int portnumber;
    private LamportClock lamportClock;


    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java ContentServer <serveraddress:portnumber> <location/of/the/feeds>");
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
        String fileLocation = args[1];

        ContentServer  cs = new ContentServer(serverAddress, portNumber, fileLocation);
        cs.start();
    }

    public ContentServer(String serverAddress, int portNumber, String fileLocation){
        weatherDataList = UtilTool.readInformation(fileLocation);
        lamportClock = new LamportClock();

        this.serverAddress = serverAddress;
        this.portnumber = portNumber;
        
    }


    public void start(){

        for(WeatherData weatherData : weatherDataList){
            System.out.println(sendPutRequest(weatherData, serverAddress, portnumber));
            try{
                Thread.sleep(20000);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public String sendPutRequest(WeatherData weatherData, String serverAddress, int port) {
        try{
            Socket socket = new Socket(serverAddress, port);
            lamportClock.setStationID(weatherData.getId());
            lamportClock.tick();
            lamportClock.updateTime();

            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), false);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String contentLength = String.valueOf(weatherData.toJson().length());
            
            out.println("PUT /weather.json HTTP/1.1");
            out.println(lamportClock.toString());
            out.println("User-Agent: ATOMClient/1.0");
            out.println("Content-Type: application/json; charset=utf-8");
            out.println("Content-Length: " + contentLength);
            out.println(""); //end of headers
            out.println(weatherData.toJson());
            out.flush();
            socket.shutdownOutput();

            String responseLine;
            StringBuilder stringBuilder = new StringBuilder();

            while ((responseLine = in.readLine()) != null) {
                 stringBuilder.append(responseLine);
            }
            socket.close();

            return stringBuilder.toString();
        } catch (IOException e) {
            return "";
        }
    }
}




