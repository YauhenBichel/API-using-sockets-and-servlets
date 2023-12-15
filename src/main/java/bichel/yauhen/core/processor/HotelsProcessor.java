package bichel.yauhen.core.processor;

import bichel.yauhen.core.model.Hotel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Processor of hotel files
 */
public class HotelsProcessor {
    private static final Logger logger = LogManager.getLogger(HotelsProcessor.class);

    /**
     * Parses files with hotels
     * @param filePath String
     * @return list of Hotel
     */
    public List<Hotel> parseHotels(String filePath) {
        Gson gson = new Gson();
        logger.debug("File path is {}", filePath);

        List<Hotel> hotels = new ArrayList<>();

        try (FileReader fr = new FileReader(filePath)) {
            JsonParser parser = new JsonParser();
            JsonObject jo = (JsonObject) parser.parse(fr);
            JsonArray jsonArr = jo.getAsJsonArray("sr");
            Hotel[] arrHotel = gson.fromJson(jsonArr, Hotel[].class);

            hotels.addAll(List.of(arrHotel));

            logger.debug("Hotel files are parsed. Number of hotels is {}", hotels.size());
        } catch (IOException ex) {
            logger.error("Could not read the file: {}", ex);
        }

        return hotels;
    }
}
