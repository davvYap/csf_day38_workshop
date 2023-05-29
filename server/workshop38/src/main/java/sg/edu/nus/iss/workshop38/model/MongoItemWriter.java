package sg.edu.nus.iss.workshop38.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.Document;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.mongodb.core.MongoTemplate;

public class MongoItemWriter implements ItemWriter<Document> {
    private final MongoTemplate mongoTemplate;

    public MongoItemWriter(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void write(Chunk<? extends Document> imageLikesDocs) throws Exception {
        List<Document> docs = new ArrayList<>();
        docs.addAll((Collection<? extends Document>) imageLikesDocs);

        for (Document document : docs) {
            mongoTemplate.insert(document, "image_likes");
        }
    }

}
