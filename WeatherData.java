public class WeatherData {
    
    private String id;
    private String name;
    private String state;
    private String timeZone;
    private double lat;
    private double lon;
    private String localDateTime;
    private String localDateTimeFull;
    private double airTemp;
    private double apparentT;
    private String cloud;
    private double dewpt;
    private double press;
    private int relHum;
    private String windDir;
    private int windSpdKmh;
    private int windSpdKt;

    public WeatherData(){

    }

    public WeatherData(String id, String name, String state, String timeZone, double lat, double lon, 
                       String localDateTime, String localDateTimeFull, double airTemp, double apparentT,
                       String cloud, double dewpt, double press, int relHum, String windDir, 
                       int windSpdKmh, int windSpdKt) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.timeZone = timeZone;
        this.lat = lat;
        this.lon = lon;
        this.localDateTime = localDateTime;
        this.localDateTimeFull = localDateTimeFull;
        this.airTemp = airTemp;
        this.apparentT = apparentT;
        this.cloud = cloud;
        this.dewpt = dewpt;
        this.press = press;
        this.relHum = relHum;
        this.windDir = windDir;
        this.windSpdKmh = windSpdKmh;
        this.windSpdKt = windSpdKt;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTimeZone() {
        return this.timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public double getLat() {
        return this.lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return this.lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getLocalDateTime() {
        return this.localDateTime;
    }

    public void setLocalDateTime(String localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getLocalDateTimeFull() {
        return this.localDateTimeFull;
    }

    public void setLocalDateTimeFull(String localDateTimeFull) {
        this.localDateTimeFull = localDateTimeFull;
    }

    public double getAirTemp() {
        return this.airTemp;
    }

    public void setAirTemp(double airTemp) {
        this.airTemp = airTemp;
    }

    public double getApparentT() {
        return this.apparentT;
    }

    public void setApparentT(double apparentT) {
        this.apparentT = apparentT;
    }

    public String getCloud() {
        return this.cloud;
    }

    public void setCloud(String cloud) {
        this.cloud = cloud;
    }

    public double getDewpt() {
        return this.dewpt;
    }

    public void setDewpt(double dewpt) {
        this.dewpt = dewpt;
    }

    public double getPress() {
        return this.press;
    }

    public void setPress(double press) {
        this.press = press;
    }

    public int getRelHum() {
        return this.relHum;
    }

    public void setRelHum(int relHum) {
        this.relHum = relHum;
    }

    public String getWindDir() {
        return this.windDir;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }

    public int getWindSpdKmh() {
        return this.windSpdKmh;
    }

    public void setWindSpdKmh(int windSpdKmh) {
        this.windSpdKmh = windSpdKmh;
    }

    public int getWindSpdKt() {
        return this.windSpdKt;
    }

    public void setWindSpdKt(int windSpdKt) {
        this.windSpdKt = windSpdKt;
    }

    @Override
    public String toString() {
        return "id:" + id + "\n" +
               "name:" + name + "\n" +
               "state:" + state + "\n" +
               "time_zone:" + timeZone + "\n" +
               "lat:" + lat + "\n" +
               "lon:" + lon + "\n" +
               "local_date_time:" + localDateTime + "\n" +
               "local_date_time_full:" + localDateTimeFull + "\n" +
               "air_temp:" + airTemp + "\n" +
               "apparent_t:" + apparentT + "\n" +
               "cloud:" + cloud + "\n" +
               "dewpt:" + dewpt + "\n" +
               "press:" + press + "\n" +
               "rel_hum:" + relHum + "\n" +
               "wind_dir:" + windDir + "\n" +
               "wind_spd_kmh:" + windSpdKmh + "\n" +
               "wind_spd_kt:" + windSpdKt;
    }

    public String toJson() {
        StringBuilder json = new StringBuilder();
        json.append("{").append("\n");
        json.append("\"id\":\"").append(id).append("\",").append("\n");
        json.append("\"name\":\"").append(name).append("\",").append("\n");
        json.append("\"state\":\"").append(state).append("\",").append("\n");
        json.append("\"time_zone\":\"").append(timeZone).append("\",").append("\n");
        json.append("\"lat\":").append(lat).append(",").append("\n");
        json.append("\"lon\":").append(lon).append(",").append("\n");
        json.append("\"local_date_time\":\"").append(localDateTime).append("\",").append("\n");
        json.append("\"local_date_time_full\":\"").append(localDateTimeFull).append("\",").append("\n");
        json.append("\"air_temp\":").append(airTemp).append(",").append("\n");
        json.append("\"apparent_t\":").append(apparentT).append(",").append("\n");
        json.append("\"cloud\":\"").append(cloud).append("\",").append("\n");
        json.append("\"dewpt\":").append(dewpt).append(",").append("\n");
        json.append("\"press\":").append(press).append(",").append("\n");
        json.append("\"rel_hum\":").append(relHum).append(",").append("\n");
        json.append("\"wind_dir\":\"").append(windDir).append("\",").append("\n");
        json.append("\"wind_spd_kmh\":").append(windSpdKmh).append(",").append("\n");
        json.append("\"wind_spd_kt\":").append(windSpdKt).append("\n");
        json.append("}");
        return json.toString();
    }
}
