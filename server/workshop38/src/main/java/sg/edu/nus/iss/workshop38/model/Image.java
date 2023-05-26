package sg.edu.nus.iss.workshop38.model;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;

public class Image {
    private String imageKey;
    private String comments;

    public Image(String imageKey, String comments) {
        this.imageKey = imageKey;
        this.comments = comments;
    }

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public JsonObjectBuilder toJsonObjectBuilder() {
        return Json.createObjectBuilder()
                .add("image_key", imageKey)
                .add("comments", comments);

    }

    public static Image convertFromDocument(Document d) {
        Image img = new Image(d.getString("image_key"), d.getString("comments"));
        return img;
    }

}
