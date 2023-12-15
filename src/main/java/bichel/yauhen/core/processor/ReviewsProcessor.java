package bichel.yauhen.core.processor;

import bichel.yauhen.core.data.ThreadSafeHotelReviewData;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import bichel.yauhen.core.model.Review;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

import static bichel.yauhen.core.Constants.JSON_FILE_EXTENSION;

/**
 * Process review files
 */
public class ReviewsProcessor {
    private static final Logger logger = LogManager.getLogger(ReviewsProcessor.class);

    private final Phaser phaser = new Phaser();
    private class FileWorker implements Callable<ThreadSafeHotelReviewData> {
        private final Path filePath;
        private final ThreadSafeHotelReviewData hotelReviewData;

        public FileWorker(Path filePath) {
            this.filePath = filePath;
            hotelReviewData = new ThreadSafeHotelReviewData();
        }

        @Override
        public ThreadSafeHotelReviewData call() {
            return parseReviewFile(filePath.toString());
        }

        private ThreadSafeHotelReviewData parseReviewFile(String filePath) {
            Gson gson = new Gson();
            logger.debug("File path is {}", filePath);

            try (FileReader fr = new FileReader(filePath)) {
                JsonParser parser = new JsonParser();
                JsonObject jo = (JsonObject) parser.parse(fr);
                JsonArray jsonArr = jo.getAsJsonObject().get("reviewDetails")
                        .getAsJsonObject().get("reviewCollection")
                        .getAsJsonObject()
                        .getAsJsonArray("review");
                Review[] arrReview = gson.fromJson(jsonArr, Review[].class);

                for(int i = 0; i < arrReview.length; i++) {
                    hotelReviewData.addReview(arrReview[i]);
                }

            } catch (IOException ex) {
                logger.error("Could not read the file: {}", ex);
            }

            return hotelReviewData;
        }
    }

    /**
     * Parses reviews
     * @param directory directory with nested directories and files
     * @param executor ExecutorService
     * @param hotelReviewData HotelReviewData
     */
    public void parseReviews(String directory, ExecutorService executor,
                             ThreadSafeHotelReviewData hotelReviewData) {
        List<Future<ThreadSafeHotelReviewData>> localHotelReviewDataList = new ArrayList<>();

        Path dirPath = Paths.get(directory);
        Deque<Path> stack = new LinkedList<>();
        stack.push(dirPath);

        while(!stack.isEmpty()) {
            Path currDirPath = stack.pop();

            try (DirectoryStream<Path> pathsInDir = Files.newDirectoryStream(currDirPath)) {
                for (Path currPath : pathsInDir) {
                    if (!Files.isDirectory(currPath) && (currPath.toString().endsWith(JSON_FILE_EXTENSION))) {
                        Future<ThreadSafeHotelReviewData> localHotelReviewDataFuture = executor.submit(new FileWorker(currPath));
                        //phaser.register();
                        localHotelReviewDataList.add(localHotelReviewDataFuture);
                    } else if (Files.isDirectory(currPath)) {
                        stack.push(currPath);
                    }
                }
            } catch (IOException ex) {
                logger.error("Can not open directory: {}. Exception is {}",
                        directory, ex.getMessage());
            } finally {
                if(phaser.getRegisteredParties() > 0) {
                    phaser.arriveAndDeregister();
                }
                logger.debug("Worker working on {} finished work", currDirPath);
            }
        }

        merge(localHotelReviewDataList, hotelReviewData);
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            logger.error(ex);
        }
    }

    private void merge(List<Future<ThreadSafeHotelReviewData>> localHotelReviewDataList, ThreadSafeHotelReviewData hotelReviewData) {
        for(Future<ThreadSafeHotelReviewData> localHotelReviewData: localHotelReviewDataList) {
            try {
                hotelReviewData.merge(localHotelReviewData.get());
            } catch (InterruptedException | ExecutionException ex) {
                logger.error(ex);
            }
        }
    }
}
