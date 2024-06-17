package org.example.chat_client.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

public class GroupListUpdateListener implements MessageListener{
    private final Gson gson = new Gson();
    private ObservableList<Group> groups;
    private final String directoryImageGroup="client_storage/Image_Group";

    public GroupListUpdateListener(ObservableList<Group> groups) {
        this.groups = groups;
    }
    @Override
    public void onMessageReceived(String message) {
        String[] messageParts = message.split("\\|");
        if(message.startsWith("listGroup")){
            Platform.runLater(()->{
                List<Group> groupList=gson.fromJson(messageParts[1], new TypeToken<List<Group>>(){}.getType());
                groups.setAll(groupList);
            });
        }else if(message.startsWith("newGroup")){
            Platform.runLater(()->{
                Group group=gson.fromJson(messageParts[1], Group.class);
                groups.add(group);
                File localDir=new File(directoryImageGroup);
                if(!localDir.exists()){
                    localDir.mkdir();
                }
                File imageGroupFile=new File(directoryImageGroup,messageParts[2]);
                try{
                    Files.write(imageGroupFile.toPath(), Base64.getDecoder().decode(messageParts[3]));
                }catch (IOException e){
                    e.printStackTrace();
                }
            });
        }
    }
}
