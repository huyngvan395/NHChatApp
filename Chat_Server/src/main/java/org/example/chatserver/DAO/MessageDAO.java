package org.example.chatserver.DAO;

import org.example.chatserver.Model.Message;
import org.example.chatserver.Model.Model;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    private final Connection con=ConnectionDB.getConnectionDB();

    public void addMessageSingle(String ConversationID, String SenderID, String Message,String typeMessage, LocalDateTime TimeSend) {
        PreparedStatement ps = null;
        try{
            ps=con.prepareStatement("insert into message_single(ConversationID, SenderID, Message, MessageType, TimeSend) values (?,?,?,?,?)");
            ps.setString(1, ConversationID);
            ps.setString(2, SenderID);
            ps.setString(3, Message);
            ps.setString(4,typeMessage);
            ps.setTimestamp(5, Timestamp.valueOf(TimeSend));
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void addMessageGroup(String ConversationID, String SenderID, String Message, String typeMessage, LocalDateTime TimeSend) {
        PreparedStatement ps = null;
        try{
            ps=con.prepareStatement("insert into message_group(ConversationID, SenderID, Message, MessageType, TimeSend) values (?,?,?,?,?)");
            ps.setString(1, ConversationID);
            ps.setString(2, SenderID);
            ps.setString(3, Message);
            ps.setString(4, typeMessage);
            ps.setTimestamp(5, Timestamp.valueOf(TimeSend));
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void addMessageBot(String ConversationID, String SenderID, String Message, String typeMessage, LocalDateTime TimeSend) {
        PreparedStatement ps = null;
        try{
            ps=con.prepareStatement("insert into message_bot(ConversationID, ID, Message, MessageType, TimeSend) values (?,?,?,?,?)");
            ps.setString(1, ConversationID);
            ps.setString(2, SenderID);
            ps.setString(3, Message);
            ps.setString(4, typeMessage);
            ps.setTimestamp(5, Timestamp.valueOf(TimeSend));
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public List<Message> getListMessageHistorySingle(String ClientID1, String ClientID2){
        String IDConversation=Model.getInstance().getConversationDAO().getConversationSingleID(ClientID1,ClientID2);
        List<Message> list=new ArrayList<>();
        PreparedStatement ps;
        ResultSet rs;
        try{
            ps=con.prepareStatement("select * from message_single where ConversationID=?");
            ps.setString(1, IDConversation);
            rs=ps.executeQuery();
            while(rs.next()){
                String senderID=rs.getString("SenderID");
                String messageContent=rs.getString("Message");
                String messageType=rs.getString("MessageType");
                LocalDateTime time=rs.getTimestamp("TimeSend").toLocalDateTime();
                String timeSend=time.toString();
                Message message =new Message(senderID, messageContent, messageType, timeSend);
                list.add(message);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }

    public List<Message> getListMessageHistoryGroup(String IDGroup){
        List<Message> list=new ArrayList<>();
        PreparedStatement ps;
        ResultSet rs;
        try{
            ps=con.prepareStatement("select * from message_group where ConversationID=?");
            ps.setString(1, IDGroup);
            rs= ps.executeQuery();
            while(rs.next()){
                String senderID=rs.getString("SenderID");
                String messageContent=rs.getString("Message");
                String messageType=rs.getString("MessageType");
                LocalDateTime time=rs.getTimestamp("TimeSend").toLocalDateTime();
                String timeSend=time.toString();
                Message message =new Message(senderID,messageContent,messageType,timeSend);
                list.add(message);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }

    public List<Message> getListMessageHistoryBot(String ClientID){
        List<Message> list=new ArrayList<>();
        String ConversationID=Model.getInstance().getConversationDAO().getConversationBotID(ClientID);
        PreparedStatement ps;
        ResultSet rs;
        try{
            ps=con.prepareStatement("select * from message_bot where ConversationID=?");
            ps.setString(1, ConversationID);
            rs= ps.executeQuery();
            while(rs.next()){
                String ID=rs.getString("ID");
                String messageContent=rs.getString("Message");
                String messageType=rs.getString("MessageType");
                LocalDateTime time=rs.getTimestamp("TimeSend").toLocalDateTime();
                String timeSend=time.toString();
                Message message =new Message(ID,messageContent,messageType,timeSend);
                list.add(message);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }

}
