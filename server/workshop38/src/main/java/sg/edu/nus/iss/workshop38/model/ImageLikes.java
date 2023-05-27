package sg.edu.nus.iss.workshop38.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class ImageLikes {
    private String key;
    private int likes;
    private int unlikes;

    public ImageLikes() {
    }

    public ImageLikes(String key, int likes, int unlikes) {
        this.key = key;
        this.likes = likes;
        this.unlikes = unlikes;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getUnlikes() {
        return unlikes;
    }

    public void setUnlikes(int unlikes) {
        this.unlikes = unlikes;
    }

    public JsonObject toJsonObject() {
        return Json.createObjectBuilder()
                .add("key", key)
                .add("likes", likes)
                .add("unlikes", unlikes)
                .build();
    }

    public static ImageLikes convertFromJson(String json) throws IOException {
        ImageLikes imageLikes = new ImageLikes();
        if (json != null) {
            try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
                JsonReader reader = Json.createReader(is);
                JsonObject obj = reader.readObject();
                imageLikes.setKey(obj.getString("key"));
                imageLikes.setLikes(obj.getInt("likes"));
                imageLikes.setUnlikes(obj.getInt("unlikes"));
            }
            return imageLikes;
        }

        return null;
    }
}