/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.davidellner.shottracker.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.table.DefaultTableModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 *
 * @author davidellner
 */
public class DbUtilities {
    private Connection conn = null; // connection object
    private String hostName; // server address + port number
    private String dbName; // default database name
    private String dbUserName; // login name for the database server
    private String dbPassword; // password for the database server

    /**
     * Default constructor creates a connection to database at the time of instantiation.
     */
    public DbUtilities() {
       Properties dbconfig = new Properties();
       String configFilePath = DbUtilities.class.getResource("databaseconfig.properties").getFile();
        try {
            dbconfig.load(new FileInputStream(configFilePath));
            this.hostName = dbconfig.getProperty("host")+":"+dbconfig.getProperty("port");
            this.dbName = dbconfig.getProperty("defaultdatabase");
            this.dbUserName = dbconfig.getProperty("username");
            this.dbPassword = dbconfig.getProperty("password");
        } catch (IOException ex) {
            ErrorLogger.log(ex.getMessage());
        }
    }

    /**
     * Alternate constructor - use it to connect to any MySQL database
     * @param hostName - server address/name of MySQL database
     * @param dbName - name of the database to connect to
     * @param dbUserName - user name for MySQL database
     * @param dbPassword - password that matches dbUserName for MySQL database
     */
    public DbUtilities(String hostName, String dbName, String dbUserName, String dbPassword) {
        // Set class-level (instance) variables
        this.hostName = hostName;
        this.dbName = dbName;
        this.dbUserName = dbUserName;
        this.dbPassword = dbPassword;
        // Create new database connection
        createDbConnection();
    }

    
    /**
     * Creates database connection using credentials stored in class variables.  
     * Connection to database is the most resource-consuming part of the database transaction. 
     * That's why we create a connection once when the object is instantiated and keep it alive through the life of this object.
     * Note that this is a private method and cannot be accessed from outside of this class.
     */
    private void createDbConnection() {
        try {
            // Build connection string
            String mySqlConn = "jdbc:mysql://" + hostName + "/" + dbName + "?user=" + dbUserName + "&password=" + dbPassword;
            // Instantiate the MySQL database connector driver
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            // Connect to the database
            conn = DriverManager.getConnection(mySqlConn);
        } catch (Exception e) {
            System.err.print(e.toString());
            System.err.println("Unable to connect to database");
        }
    }

    
    /**
     * Get SQL result set (data set) based on an SQL query
     * @param sql - SQL SELECT query
     * @return - ResultSet - java.sql.ResultSet object, contains results from SQL query argument
     * @throws SQLException
     */
    public ResultSet getResultSet(String sql) throws SQLException {
        try {
            if (conn == null) { // Check if connection object already exists
                createDbConnection(); // If does not exist, create new connection
            }
            Statement statement = conn.createStatement();
            return statement.executeQuery(sql); // Return ResultSet
        } catch (Exception e) {
            e.printStackTrace(); // debug
            ErrorLogger.log(e.getMessage()); // Log error
            ErrorLogger.log(sql); // Log INSERT, UPDATE, DELETE query
        }
        return null;
    }

    
    /**
     * Executes INSERT, UPDATE, DELETE queries
     * @param sql - SQL statement - a well-formed INSERT, UPDATE, or DELETE query
     * @return true if execution succeeded, false if failed 
     * @throws com.davidellner.shottracker.utilities.QueryFailureException 
     */
    public boolean executeQuery(String sql) throws QueryFailureException {
        try {
            if (conn == null) {
                createDbConnection();
            }
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql); // execute query
            return true;
        } catch (Exception e) {
            e.printStackTrace(); // debug
            ErrorLogger.log(e.getMessage()); // Log error
            ErrorLogger.log(sql); // Log INSERT, UPDATE, DELETE query
            throw new QueryFailureException("Query failed.");
        }
        
    }
    
    /**
     * This method converts a ResultSet into a JSON object
     * @param sqlQuery - an SQL query - we need to get a data set from a database 
     *      and convert it to JSON
     * @return JSON object
     * @throws SQLException
     * @throws JSONException 
     */
    public JSONArray getJsonDataTable(String sqlQuery) throws SQLException, JSONException {
        ResultSet rs = this.getResultSet(sqlQuery);
        /* 
         * Create new JSON object.  Note that you will need to download and install java-json library
         * http://www.java2s.com/Code/JarDownload/java/java-json.jar.zip
         */
        JSONArray json = new JSONArray(); 
        
        // We need the metadata object to get each database field's data type
        ResultSetMetaData rsmd = rs.getMetaData();

        /*
         * Loop through the ResultSet.  For each value, we need to get corresponding field's data type
         */
        while (rs.next()) {
            int numColumns = rsmd.getColumnCount();
            JSONObject obj = new JSONObject();

            for (int i = 1; i < numColumns + 1; i++) {
                String column_name = rsmd.getColumnName(i);

                switch (rsmd.getColumnType(i)) {
                    case java.sql.Types.ARRAY:
                        obj.put(column_name, rs.getArray(column_name));
                        break;
                    case java.sql.Types.BIGINT:
                        obj.put(column_name, rs.getInt(column_name));
                        break;
                    case java.sql.Types.BOOLEAN:
                        obj.put(column_name, rs.getBoolean(column_name));
                        break;
                    case java.sql.Types.BLOB:
                        obj.put(column_name, rs.getBlob(column_name));
                        break;
                    case java.sql.Types.DOUBLE:
                        obj.put(column_name, rs.getDouble(column_name));
                        break;
                    case java.sql.Types.FLOAT:
                        obj.put(column_name, rs.getFloat(column_name));
                        break;
                    case java.sql.Types.INTEGER:
                        obj.put(column_name, rs.getInt(column_name));
                        break;
                    case java.sql.Types.NVARCHAR:
                        obj.put(column_name, rs.getNString(column_name));
                        break;
                    case java.sql.Types.VARCHAR:
                        obj.put(column_name, rs.getString(column_name));
                        break;
                    case java.sql.Types.TINYINT:
                        obj.put(column_name, rs.getInt(column_name));
                        break;
                    case java.sql.Types.SMALLINT:
                        obj.put(column_name, rs.getInt(column_name));
                        break;
                    case java.sql.Types.DATE:
                        obj.put(column_name, rs.getDate(column_name));
                        break;
                    case java.sql.Types.TIMESTAMP:
                        obj.put(column_name, rs.getTimestamp(column_name));
                        break;
                    default:
                        obj.put(column_name, rs.getObject(column_name));
                        break;
                }
            }

            json.put(obj);
        }

        return json;

    }
    
    
    /**
     * 
     * @param sqlQuery
     * @param tableID
     * @return
     * @throws SQLException 
     */
    public String getHtmlDataTable(String sqlQuery, String tableID) throws SQLException {
        ResultSet rs = this.getResultSet(sqlQuery);
        ResultSetMetaData metaData = rs.getMetaData();
        String tbl = "<table id='" + tableID + "'><tr>";
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            tbl += "<td>" + metaData.getColumnName(column) + "</td>";
        }
        tbl += "</tr>";

        while (rs.next()) {
            tbl += "<tr>";
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                tbl += "<td>" + rs.getString(columnIndex) + "</td>";
            }
            tbl += "</tr>";
        }
        return tbl;
    }
    public void close(){
        this.closeDbConnection();
    }
    private void closeDbConnection(){
        if(this.conn != null){
            try {
                conn.close();
            } catch (SQLException ex) {
                ErrorLogger.log(ex.toString());
            }
        }
    }
}
