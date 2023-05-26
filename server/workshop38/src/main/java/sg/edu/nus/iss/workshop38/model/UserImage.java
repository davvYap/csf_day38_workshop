package sg.edu.nus.iss.workshop38.model;

import java.util.List;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class UserImage {
    private String username;
    private List<Image> images;

    public UserImage() {
    }

    public UserImage(String username, List<Image> images) {
        this.username = username;
        this.images = images;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public JsonObject toJsonObject() {
        JsonArrayBuilder jsArr = Json.createArrayBuilder();
        for (Image img : this.images) {
            jsArr.add(img.toJsonObjectBuilder());
        }
        return Json.createObjectBuilder()
                .add("username", this.username)
                .add("images", jsArr)
                .build();
    }

    public static UserImage convertFromDocument(Document d) {
        UserImage userImage = new UserImage();
        userImage.setUsername(d.getString("username"));
        List<Document> imgList = d.getList("images", Document.class);
        List<Image> imgObjList = imgList.stream().map(doc -> Image.convertFromDocument(doc)).toList();
        userImage.setImages(imgObjList);
        return userImage;
    }

}
