package org.example.chatserver.DAO;

import org.example.chatserver.Model.Group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConversationDAO {
    private final Connection con=ConnectionDB.getConnectionDB();

    public String getConversationSingleID(String SenderID, String ReceiverID){
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{
            ps=con.prepareStatement("select ConversationID from conversation_single where (ClientID1=? AND ClientID2=?) OR (ClientID1=? AND ClientID2=?)");
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

    public boolean checkConversationExist(String SenderID, String ReceiverID){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps=con.prepareStatement("select ConversationID from conversation_single where (ClientID1=? AND ClientID2=?) OR (ClientID1=? AND ClientID2=?)");
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


    public void createConversationGroup(String nameGroup,String imagePath){
        PreparedStatement ps = null;
        try{
            ps=con.prepareStatement("insert into conversation_group(GroupName,Image_Group) values (?,?)");
            ps.setString(1, nameGroup);
            ps.setString(2, imagePath);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void addMemberGroup(String ConversationID, String ClientID){
        PreparedStatement ps = null;
        try{
            ps=con.prepareStatement("insert into conversation_group_members(ConversationID, ClientID) values (?,?) ");
            ps.setString(1, ConversationID);
            ps.setString(2, ClientID);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public String getIDGroup(String nameGroup){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps=con.prepareStatement("select ConversationID from conversation_group where GroupName=?");
            ps.setString(1, nameGroup);
            rs=ps.executeQuery();
            if(rs.next()){
                return rs.getString("ConversationID");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Group> getListGroup(String clientID){
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Group> groups=new ArrayList<>();
        try{
            ps=con.prepareStatement("select conversation_group.ConversationID as GroupID, GroupName, Image_Group from conversation_group join conversation_group_members on conversation_group.ConversationID=conversation_group_members.ConversationID where ClientID=?");
            ps.setString(1, clientID);
            rs=ps.executeQuery();
            while(rs.next()){
                String GroupID=rs.getString("GroupID");
                String GroupName=rs.getString("GroupName");
                String Image_Group=rs.getString("Image_Group");
                Group group=new Group(GroupID, GroupName, Image_Group);
                groups.add(group);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return groups;
    }

    public List<String> getIDMembers(String ConversationID){
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> list = null;
        try{
            ps=con.prepareStatement("select ClientID from conversation_group_members where ConversationID=?");
            ps.setString(1, ConversationID);
            rs=ps.executeQuery();
            list=new ArrayList<String>();
            while (rs.next()){
                list.add(rs.getString("ClientID"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }
}
