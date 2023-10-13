import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UtilTool {
    public static final String BACKUP_FILE_LOCATION = "weather_data_backup.txt";

    public static WeatherData parseJSON(String json) {
        WeatherData data = new WeatherData();

        json = json.replaceAll("\\s+", "");

        if (json.startsWith("{") && json.endsWith("}")) {
            json = json.substring(1, json.length() - 1);

            String[] fields = json.split(",");

            for (String field : fields) {
                String[] keyValue = field.split(":", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0].replaceAll("\"", "");
                    String value = keyValue[1].replaceAll("\"", "");

                    switch (key) {
                        case "id":
                            data.setId(value);
                            break;
                        case "name":
                            data.setName(value);
                            break;
                        case "state":
                            data.setState(value);
                            break;
                        case "time_zone":
                            data.setTimeZone(value);
                            break;
                        case "lat":
                            data.setLat(Double.parseDouble(value));
                            break;
                        case "lon":
                            data.setLon(Double.parseDouble(value));
                            break;
                        case "local_date_time":
                            data.setLocalDateTime(value);
                            break;
                        case "local_date_time_full":
                            data.setLocalDateTimeFull(value);
                            break;
                        case "air_temp":
                            data.setAirTemp(Double.parseDouble(value));
                            break;
                        case "apparent_t":
                            data.setApparentT(Double.parseDouble(value));
                            break;
                        case "cloud":
                            data.setCloud(value);
                            break;
                        case "dewpt":
                            data.setDewpt(Double.parseDouble(value));
                            break;
                        case "press":
                            data.setPress(Double.parseDouble(value));
                            break;
                        case "rel_hum":
                            data.setRelHum(Integer.parseInt(value));
                            break;
                        case "wind_dir":
                            data.setWindDir(value);
                            break;
                        case "wind_spd_kmh":
                            data.setWindSpdKmh(Integer.parseInt(value));
                            break;
                        case "wind_spd_kt":
                            data.setWindSpdKt(Integer.parseInt(value));
                            break;
                    }
                }
            }
        }
        return data;
    }

    public static List<WeatherData> readInformation(String fileLocation){
        List<WeatherData> weatherDataList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileLocation))) {
            String line;
            WeatherData data = new WeatherData();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) { // Ensure line contains valid data
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    switch (key) {
                        case "id":
                            data.setId(value);
                            break;
                        case "name":
                            data.setName(value);
                            break;
                        case "state":
                            data.setState(value);
                            break;
                        case "time_zone":
                            data.setTimeZone(value);
                            break;
                        case "lat":
                            data.setLat(Double.parseDouble(value));
                            break;
                        case "lon":
                            data.setLon(Double.parseDouble(value));
                            break;
                        case "local_date_time":
                            data.setLocalDateTime(value);
                            break;
                        case "local_date_time_full":
                            data.setLocalDateTimeFull(value);
                            break;
                        case "air_temp":
                            data.setAirTemp(Double.parseDouble(value));
                            break;
                        case "apparent_t":
                            data.setApparentT(Double.parseDouble(value));
                            break;
                        case "cloud":
                            data.setCloud(value);
                            break;
                        case "dewpt":
                            data.setDewpt(Double.parseDouble(value));
                            break;
                        case "press":
                            data.setPress(Double.parseDouble(value));
                            break;
                        case "rel_hum":
                            data.setRelHum(Integer.parseInt(value));
                            break;
                        case "wind_dir":
                            data.setWindDir(value);
                            break;
                        case "wind_spd_kmh":
                            data.setWindSpdKmh(Integer.parseInt(value));
                            break;
                        case "wind_spd_kt":
                            data.setWindSpdKt(Integer.parseInt(value));
                            break;
                    }
                } else if (line.trim().isEmpty() && data.getId() != null) { // Empty line, end of record
                    weatherDataList.add(data);
                    data = new WeatherData(); // Create new object for next record
                }
            }
            if (data.getId() != null && !weatherDataList.contains(data)) {
                weatherDataList.add(data);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return weatherDataList;
    }


    public static void removeItemsWithID(List<WeatherData> allWeatherData, String idToRemove) {
        allWeatherData.removeIf(data -> data.getId().equals(idToRemove));
    }
    public static boolean hasMatchingID(List<WeatherData> allWeatherData, String targetID) {
        for (WeatherData data : allWeatherData) {
            if (data.getId().equals(targetID)) {
                return true; 
            }
        }
        return false;
    }

    public static int findMaxRemoteTime(String filePath) {
        int maxInt = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 3) {
                    int integerPart = Integer.parseInt(parts[2].trim());
                    maxInt = Math.max(maxInt, integerPart);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return maxInt;
    }
}
