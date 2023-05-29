package sg.edu.nus.iss.workshop38.util;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class BatchProcessor implements ItemProcessor<Document, Document> {

    private static final Logger logger = LoggerFactory.getLogger(BatchProcessor.class);

    @Override
    public Document process(Document document) throws Exception {

        // can process some business logic here
        Document doc = document;

        logger.info("Returning processed ImageLikes: " + doc);

        return doc;
    }
}
