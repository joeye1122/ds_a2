import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AggregationServer {
    private List<WeatherData> allWeatherData;
    private List<String> lamportClocks;

    private ScheduledExecutorService executorService;

    public static void main(String[] args) {
        int port = 4567;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }

        AggregationServer server = new AggregationServer();


        File file = new File(UtilTool.BACKUP_FILE_LOCATION);
        server.allWeatherData = UtilTool.readInformation(UtilTool.BACKUP_FILE_LOCATION);


        server.start(port);

        if (file.exists()) {
            file.delete();
        }
    }



    public AggregationServer(){
        allWeatherData = new ArrayList<>();
        lamportClocks = new ArrayList<>();
        executorService = Executors.newScheduledThreadPool(5);

    }

    /**
     * Start the main loop of AggregationServer, listening on the specified port and handling HTTP requests.
     *
     * @param port The port number to listen on.
     */
    public void start(int port){
        
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[Aggregation Server] now start listening on port " + port);
            while (true){
                Socket socket = serverSocket.accept();
                String data = socketBuffertoString(socket);
                String firstLine = data.split("\n")[0];

                if (firstLine != null) {
                    String[] parts = firstLine.split(" ");
                    if (parts.length >= 1) {
                        String httpMethod = parts[0];
                        if ("GET".equalsIgnoreCase(httpMethod)) {
                            System.out.println("[Aggregation Server] receive new GET request");
                            handleGetRequest(socket, parts[1]);

                        } else if ("PUT".equalsIgnoreCase(httpMethod)) {
                            System.out.println("[Aggregation Server] receive new PUT request");
                            handlePutRequest(socket, data);

                        } else {
                            System.out.println("[Aggregation Server] unsupported HTTP request" + httpMethod);
                        }
                    }else{
                        //part < 1 not a valid httprequest
                        System.out.println("[Aggregation Server] invalid request type");
                        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), false);
                        out.write("HTTP/1.1 400 Method Not Allowed\r\n");
                        out.flush();
                        socket.shutdownOutput();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle an HTTP PUT request, respond to the content server, and add data to the aggregation server.
     *
     * @param socket     The socket connection with the client.
     * @param dataString The received PUT request data.
     * @throws IOException if an I/O error occurs.
     */
    private void handlePutRequest(Socket socket, String dataString) throws IOException{
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), false);

        int emptyLineIndex = dataString.indexOf("\n\n");

        if (emptyLineIndex != -1) {
            String header = dataString.substring(0, emptyLineIndex);
            String[] headerLines = header.split("\n");
            lamportClocks.add(headerLines[1]);

            String body = dataString.substring(emptyLineIndex + 2);

            WeatherData weatherData = UtilTool.parseJSON(body);

            if (allWeatherData.isEmpty()) {
                out.write("HTTP/1.1 201 Created\r\n");
            } else {
                out.write("HTTP/1.1 200 OK\r\n");
            }

            if (UtilTool.hasMatchingID(allWeatherData, weatherData.getId())) {
                allWeatherData.add(weatherData);
                resetTimer(weatherData.getId());
            }else{
                allWeatherData.add(weatherData);
                startTimer(weatherData.getId());
            }
            presistentBackUp();
            out.flush();
            socket.shutdownOutput();
            // UtilTool.reorderLamportClock(allWeatherData, lamportClocks);

        } else {
            System.out.println("[Aggregation Server] invalid put request format");
        }
    }

    /**
    * Handle an HTTP GET request, filter and respond with relevant weather data based on the station ID.
    *
    * @param socket      The socket connection with the client.
    * @param dataString  The data string from the GET request.
    * @throws IOException if an I/O error occurs.
    */
    private void handleGetRequest(Socket socket, String dataString)throws IOException{
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), false);
        WeatherData weatherData = stationIdFilter(dataString);

        if(weatherData != null){
            out.write(weatherData.toJson());

        }else{
            out.write("no matched station ID");
        }

        out.flush();
        socket.shutdownOutput();

    }


    private String socketBuffertoString(Socket socket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String data = reader.lines().collect(Collectors.joining("\n"));
        return data;
    }

    /**
     * Start a timer task to trigger data expiration after a certain time.
     *
     * @param str The identifier for the data.
     */
    private void startTimer(String str) {
        Runnable task = () -> {
            dataExpried(str);
        };
        executorService.schedule(task, 30, TimeUnit.SECONDS); // 30 seconds
    }

    /**
    * Reset the timer task by canceling the current task and rescheduling it.
    *
    * @param str The identifier for the data.
    */
    private void resetTimer(String str) {
        // Cancel and reschedule the task
        startTimer(str);
    }

    /**
     * Handle data expiration by removing the specified data from the aggregation data.
     *
     * @param str The identifier for the data.
     */
    private void dataExpried(String str) {
        UtilTool.removeItemsWithID(allWeatherData, str);
        System.out.println("Removed stationID: " + str);
    }


    /**
     * Filter and return the last matching weather data based on the station ID.
     *
     * @param stationID The station ID to filter by.
     * @return The last matching weather data, or null if no match is found.
     */
    public WeatherData stationIdFilter(String stationID) {
        WeatherData lastMatchingData = null;
    
        for (WeatherData data : allWeatherData) {
            if (data.getId().equals(stationID)) {
                lastMatchingData = data;
            }
        }
    
        return lastMatchingData;
    }

    /**
     * Persistently back up weather data to a file.
     */
    private void presistentBackUp(){
        try {
            FileWriter fileWriter = new FileWriter(UtilTool.BACKUP_FILE_LOCATION, false);
            PrintWriter out = new PrintWriter(fileWriter, true);

            for (WeatherData weatherData : allWeatherData) {
                String dataString = weatherData.toString();
                out.println(dataString);
                out.println();
            }

            out.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
