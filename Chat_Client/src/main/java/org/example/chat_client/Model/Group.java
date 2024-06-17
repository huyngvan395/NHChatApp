package org.example.chat_client.Model;

public class Group {
    private String groupID;
    private String name;
    private String image;
    public Group(String groupID, String name, String image) {
        this.groupID = groupID;
        this.name = name;
        this.image = image;
    }
    public String getGroupID() {
        return groupID;
    }
    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}
