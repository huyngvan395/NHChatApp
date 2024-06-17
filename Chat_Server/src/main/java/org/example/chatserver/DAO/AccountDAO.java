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
            ps=con.prepareStatement("insert into client(ClientID, Name, Email, Password) values(?,?,?,?,?,?)");
            ps.setString(1,clientID);
            ps.setString(2,clientName);
            ps.setString(4,email);
            ps.setString(5,password);
            ps.setString(6,image);
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

    public void updateInfo(String clientID, String name, LocalDate birthdate, String email){
        PreparedStatement ps;
        try{
            ps=con.prepareStatement("update client set Name=?,BirthDate=?,Email=? where ClientID=?");
            ps.setString(1,name);
            ps.setDate(2,Date.valueOf(birthdate));
            ps.setString(3,email);
            ps.setString(4,clientID);
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
}
