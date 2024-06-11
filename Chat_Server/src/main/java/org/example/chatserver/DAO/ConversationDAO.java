package org.example.chatserver.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConversationDAO {
    private final Connection con=ConnectionDB.getConnectionDB();

    public String getConversationSingleID(String SenderID, String ReceiverID){
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{
            ps=con.prepareStatement("select ConversationID from conversation_single where (SenderID=? AND ReceiverID=?) OR (SenderID=? AND ReceiverID=?)");
            ps.setString(1, SenderID);
            ps.setString(2, ReceiverID);
            ps.setString(3,ReceiverID);
            ps.setString(4,SenderID);
            rs=ps.executeQuery();
            if(rs.next()){
                return rs.getString(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkConversationExists(String SenderID, String ReceiverID){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps=con.prepareStatement("select * from conversation_single(ClientID1,ClientID2) where (SenderID=? AND ReceiverID=?) OR (SenderID=? AND ReceiverID=?)");
            ps.setString(1, SenderID);
            ps.setString(2, ReceiverID);
            ps.setString(3, ReceiverID);
            ps.setString(4, SenderID);
            rs=ps.executeQuery();
            if(rs.next()){
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public void addMemberGroup(String ClientID){
        PreparedStatement ps = null;
        try{
            ps=con.prepareStatement("insert into conversation_group_member(ClientID) values (?)");
            ps.setString(1, ClientID);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void addConversationSingle(String SenderID, String ReceiverID) {
        PreparedStatement ps = null;
        try{
            ps=con.prepareStatement("insert into conversation_single(ClientID1,ClientID2) values (?,?)");
            ps.setString(1, SenderID);
            ps.setString(2, ReceiverID);
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    public void addConversationGroup(String nameGroup){
        PreparedStatement ps = null;
        try{
            ps=con.prepareStatement("insert into conversation_group(GroupName) values (?)");
            ps.setString(1, nameGroup);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
