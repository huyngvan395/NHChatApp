package org.example.chatserver.DAO;

import org.example.chatserver.Model.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListClientDAO {
    private final Connection con=ConnectionDB.getConnectionDB();

    public List<Client> getListClient(){
        PreparedStatement ps;
        ResultSet rs;
        List<Client> listClient=new ArrayList<>();
        try{
            ps=con.prepareStatement("select * from client");
            rs=ps.executeQuery();
            while (rs.next()){
                String ID=rs.getString("ClientID");
                String name=rs.getString("Name");
                String email=rs.getString("Email");
                String image=rs.getString("Image");
                Client client=new Client(ID, name, email, image);
                listClient.add(client);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return listClient;
    }

}
