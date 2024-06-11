package org.example.chatserver.DAO;

import java.sql.*;
import java.time.LocalDateTime;

public class MessageDAO {
    private final Connection con=ConnectionDB.getConnectionDB();

    public void addMessageSingle(String ConversationID, String SenderID, String Message, String File, String Image, LocalDateTime TimeSend) {
        PreparedStatement ps = null;
        try{
            ps=con.prepareStatement("insert into message_single(ConversationID, SenderID, Message, File, Image, TimeSend) values (?,?,?,?,?,?)");
            ps.setString(1, ConversationID);
            ps.setString(2, SenderID);
            ps.setString(3, Message);
            ps.setString(4, File);
            ps.setString(5, Image);
            ps.setTimestamp(6, Timestamp.valueOf(TimeSend));
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void addMessageGroup(String ConversationID, String SenderID, String Message, String File, String Image, LocalDateTime TimeSend) {
        PreparedStatement ps = null;
        try{
            ps=con.prepareStatement("insert into message_group(ConversationID, SenderID, Message, File, Image, TimeSend) values (?,?,?,?,?,?)");
            ps.setString(1, ConversationID);
            ps.setString(2, SenderID);
            ps.setString(3, Message);
            ps.setString(4, File);
            ps.setString(5, Image);
            ps.setTimestamp(6, Timestamp.valueOf(TimeSend));
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
