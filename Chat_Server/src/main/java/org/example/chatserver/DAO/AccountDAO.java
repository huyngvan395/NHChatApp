package org.example.chatserver.DAO;


import java.sql.*;
import java.time.LocalDate;

public class AccountDAO {
    private final Connection con=ConnectionDB.getConnectionDB();

    public boolean Login(String email, String password) {
        ResultSet rs;
        PreparedStatement ps;
        try{
            ps=con.prepareStatement("select Email,Password from client where Email=? and Password=?");
            ps.setString(1,email);
            ps.setString(2,password);
            rs=ps.executeQuery();
            if(rs.next()){
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

//    public String getClientIDViaName(String name) {
//        ResultSet rs;
//        PreparedStatement ps;
//        String clientID = null;
//        try{
//            ps=con.prepareStatement("select ClientID from client where Name=?");
//            ps.setString(1, name);
//            rs= ps.executeQuery();
//            if(rs.next()){
//                clientID=rs.getString("ClientID");
//            }
//        }catch (SQLException e){
//            e.printStackTrace();
//        }
//        return clientID;
//    }

    public String getEmail(String clientID) {
        ResultSet rs;
        PreparedStatement ps;
        String email = null;
        try{
            ps=con.prepareStatement("select Email from client where ClientID=?");
            ps.setString(1, clientID);
            rs= ps.executeQuery();
            if(rs.next()){
                email=rs.getString("Email");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return email;
    }

    public void addClient(String clientID, String clientName, String email, String password, String image) {
        PreparedStatement ps;
        try{
            ps=con.prepareStatement("insert into client(ClientID, Name, Email, Password,Image) values(?,?,?,?,?)");
            ps.setString(1,clientID);
            ps.setString(2,clientName);
            ps.setString(3,email);
            ps.setString(4,password);
            ps.setString(5,image);
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updatePassViaClientID(String clientID, String newPass) {
        PreparedStatement ps;
        try{
            ps=con.prepareStatement("update client set Password=? where ClientID=?");
            ps.setString(1,newPass);
            ps.setString(2,clientID);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void updatePassViaEmail(String email,String password){
        PreparedStatement ps;
        try{
            ps=con.prepareStatement("update client set Password=? where Email=?");
            ps.setString(1,password);
            ps.setString(2,email);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean checkEmail(String email) {
        ResultSet rs;
        PreparedStatement ps;
        try{
            ps=con.prepareStatement("select Email from client where Email=?");
            ps.setString(1,email);
            rs=ps.executeQuery();
            if(rs.next()){
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public void updateInfo(String clientID, String name, String email){
        PreparedStatement ps;
        try{
            ps=con.prepareStatement("update client set Name=?,Email=? where ClientID=?");
            ps.setString(1,name);
            ps.setString(2,email);
            ps.setString(3,clientID);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public ResultSet getClientInfo(String email){
        PreparedStatement ps;
        ResultSet rs=null;
        try{
            ps=con.prepareStatement("select ClientID,Name,Email,Image from client where Email=?");
            ps.setString(1,email);
            rs=ps.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return rs;
    }

    public void removeClient(String clientID) {
        PreparedStatement psDeleteGroupMembers = null;
        PreparedStatement psDeleteMessageGroup = null;
        PreparedStatement psDeleteMessageSingle = null;
        PreparedStatement psDeleteConversationSingle = null;
        PreparedStatement psDeleteClient = null;

        try {

            String sqlDeleteGroupMembers = "DELETE FROM conversation_group_members WHERE ClientID = ?";
            psDeleteGroupMembers = con.prepareStatement(sqlDeleteGroupMembers);
            psDeleteGroupMembers.setString(1, clientID);
            psDeleteGroupMembers.executeUpdate();

            String sqlDeleteMessageGroup = "DELETE FROM message_group WHERE SenderID = ?";
            psDeleteMessageGroup = con.prepareStatement(sqlDeleteMessageGroup);
            psDeleteMessageGroup.setString(1, clientID);
            psDeleteMessageGroup.executeUpdate();

            String sqlDeleteMessageSingle = "DELETE FROM message_single WHERE SenderID = ?";
            psDeleteMessageSingle = con.prepareStatement(sqlDeleteMessageSingle);
            psDeleteMessageSingle.setString(1, clientID);
            psDeleteMessageSingle.executeUpdate();

            String sqlDeleteConversationSingle = "DELETE FROM conversation_single WHERE ClientID1 = ? OR ClientID2 = ?";
            psDeleteConversationSingle = con.prepareStatement(sqlDeleteConversationSingle);
            psDeleteConversationSingle.setString(1, clientID);
            psDeleteConversationSingle.setString(2, clientID);
            psDeleteConversationSingle.executeUpdate();

            String sqlDeleteClient = "DELETE FROM client WHERE ClientID = ?";
            psDeleteClient = con.prepareStatement(sqlDeleteClient);
            psDeleteClient.setString(1, clientID);
            psDeleteClient.executeUpdate();

            System.out.println("Client deleted successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
