package util.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Client;
import poi.XLSXReader;
import util.JDBCUtils;

public class ClientsMapperImpl implements ClientsMapper {

    private static String DB_NAME = "CLIENTSDB";
    private static String TABLE_NAME = "CLIENTS";
    private static String COLUMNS = "ID, FIRST_NAME, LAST_NAME, GENDER, COUNTRY, AGE, REG_DATE, CREATED_TS, UPDATED_TS";
    private static String SELECT_ALL_CLIENTS = String.format("SELECT * FROM %s.%s;", DB_NAME, TABLE_NAME);
    private static String SELECT_CLIENTS_BY_ID = String.format("SELECT * FROM %s.%s WHERE ID = ?;", DB_NAME, TABLE_NAME);
    private static String INSERT_CLIENTS = String.format("INSERT INTO %s.%s (%s) VALUES (1*, '2*', '3*', '4*', '5*', 6*, '7*', 8*, null);", DB_NAME, TABLE_NAME, COLUMNS);
    
    @Override
    public void save(Client client) {
    	String sql = INSERT_CLIENTS.replace("1*", String.valueOf(client.getId()));
    	 sql = sql.replace("2*", client.getFirstName());
    	 sql = sql.replace("3*", client.getLastName());
    	 sql = sql.replace("4*", client.getGender());
    	 sql = sql.replace("5*", client.getCountry());
    	 sql = sql.replace("6*", String.valueOf(client.getAge()));
    	 sql = sql.replace("7*", client.getDate().toString());
    	 sql = sql.replace("8*", "CURRENT_TIMESTAMP");

    	 Connection conn = JDBCUtils.createConnection();
         Statement stmt = null;
         try{
        	 stmt = conn.createStatement();
        	 int result = stmt.executeUpdate(sql);
        	 if(result == 1){
        		 System.out.println("NEW CLIENT #"+client.getId()+" INSERTED");
        	 }
         } catch(SQLException e){
        	 e.printStackTrace();
         }  
    }

    @Override
    public void update(Client client) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void delete(Client client) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void saveAll(List<Client> clients) {
        for (Client client : clients) {
			save(client);
		}
        
    }

    @Override
    public void deleteAll(List<Client> clients) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateAll(List<Client> clients) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Client load(Client client) {
    	String sql = SELECT_CLIENTS_BY_ID.replace("?", String.valueOf(client.getId()));
    	 Connection conn = JDBCUtils.createConnection();
         Statement stmt = null;
         try {
             stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
             if(rs.next()) {
            	 client = mapClient(rs);
             } else {
            	 System.out.println("CLIENT #"+client.getId() + " IS NOT FOUND!");
            	 return null;
             }
         } catch (SQLException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
        return client;
    }

    @Override
    public List<Client> loadAll(List<Client> clients) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Client> loadAll() {
        Connection conn = JDBCUtils.createConnection();
        Statement stmt = null;
        List<Client> clients = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SELECT_ALL_CLIENTS);
            while(rs.next()) {
                clients.add(mapClient(rs));
            }
            JDBCUtils.release(conn, stmt, null);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return clients;
    }
    
    private Client mapClient(ResultSet rs) throws SQLException{
    	Client client = new Client();
        client.setId(rs.getInt("ID")); 
        client.setFirstName(rs.getString("FIRST_NAME"));
        client.setLastName(rs.getString("LAST_NAME"));
        client.setGender(rs.getString("GENDER"));
        client.setCountry(rs.getString("COUNTRY"));
        client.setAge(rs.getInt("AGE"));
        client.setDate(rs.getDate("REG_DATE"));
        client.setCreatedTs(rs.getTimestamp("CREATED_TS"));
        client.setUpdatedTs(rs.getTimestamp("UPDATED_TS"));
		return client;
    }
    
    public static void main(String[] args) {
        ClientsMapper dbMapper = new ClientsMapperImpl();
//        System.out.println(dbMapper.loadAll().size());
//        System.out.println(dbMapper.load(new Client(23)));
//        Client client = dbMapper.load(new Client(23));
//        client.setId(client.getId() + 1);
//        
//        dbMapper.save(client);
        System.out.println("---------");
        XLSXReader a = new XLSXReader("resources\\file_example_XLS_1000.xlsx");
        try {
			dbMapper.saveAll(a.getClients());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
}

