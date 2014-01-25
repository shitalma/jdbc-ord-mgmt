package shitalma.ordMgmt.lib;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static junit.framework.Assert.assertEquals;

public class OrderMgmtTest {
    final String DB_URL = "jdbc:mysql://localhost";

    final String USER = "shital";
    final String PASS = "nirankari";

    java.sql.Connection conn = null;
    Statement stmt = null;

    @Before
    public void setUp() throws Exception {
        try{
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql = "CREATE SCHEMA ordMgmt";
            stmt.executeUpdate(sql);

            String createProductTable = "CREATE TABLE ordMgmt.product (\n" +
                    "\tprod_id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                    "\tprod_name VARCHAR(30),\n" +
                    "\tunit_price FLOAT\n" +
                    ");";
            stmt.executeUpdate(createProductTable);

            String createCustomerTable = "CREATE TABLE ordMgmt.customer (\n" +
                    "\tcust_id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                    "\tcust_name VARCHAR(30),\n" +
                    "\taddress VARCHAR(30),\n" +
                    "\tcity VARCHAR(30),\n" +
                    "\tstate VARCHAR(30),\n" +
                    "\tcontact BIGINT\n" +
                    ")";
            stmt.executeUpdate(createCustomerTable);

            String createOrderInfoTable = "CREATE TABLE ordMgmt.orderInfo (\n" +
                    "\torder_id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                    "\tcust_id INT,\n" +
                    "\tdate_of_order DATETIME,\n" +
                    "\tdelivery_date DATETIME \n" +
                    ")";
            stmt.executeUpdate(createOrderInfoTable);

            String createOrderItemTable = "CREATE TABLE ordMgmt.orderItems (\n" +
                    "\torder_item_id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                    "\torder_id INT,\n" +
                    "\tprod_id INT,\n" +
                    "\tquantity INT,\n" +
                    "\titem_price FLOAT\n" +
                    ")";
            stmt.executeUpdate(createOrderItemTable);

            String addForeignKeyToOrderInfo = "ALTER TABLE ordMgmt.orderInfo \n" +
                    "\tADD CONSTRAINT orderInfo_custID_fk FOREIGN KEY(cust_id)\n" +
                    "\tREFERENCES customer(cust_id);\n";
            stmt.executeUpdate(addForeignKeyToOrderInfo);

            String addForeignKeyToOrderItems = "ALTER TABLE ordMgmt.orderItems \n" +
                    "\tADD CONSTRAINT orderIems_orderId_fk FOREIGN KEY(order_id)\n" +
                    "\tREFERENCES orderInfo(order_id)\n";
            stmt.executeUpdate(addForeignKeyToOrderItems);

            String addForeignKeyToOrderItemsForProduct = "ALTER TABLE ordMgmt.orderItems \n" +
                    "\tADD CONSTRAINT orderIems_prodId_fk FOREIGN KEY(prod_id)\n" +
                    "\tREFERENCES product(prod_id);\n";
            stmt.executeUpdate(addForeignKeyToOrderItemsForProduct);

        } catch(SQLException se){
            se.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testInsertFirstRecordIntoProductTable() throws Exception{
        String sql = "INSERT INTO ordMgmt.product(prod_name,unit_price) VALUES('Pen',10)";
        int expected = 1;
        int actual = stmt.executeUpdate(sql);
        assertEquals(expected , actual);
    }
    @Test
    public void testInsertRecordIntoCustomerTable() throws Exception{
        String sql = "INSERT INTO ordMgmt.customer(cust_name ,address ,city ,state ,contact ) VALUES('Shital','3rd block','Kormangala','Karnataka',8123852388);";
        int expected = 1;
        int actual = stmt.executeUpdate(sql);
        assertEquals(expected , actual);
    }

    @After
    public void tearDown() throws Exception {
        try
        {
            String dropQuery = "DROP SCHEMA ordMgmt";
            stmt.executeUpdate(dropQuery);
            stmt.close();
            conn.close();
        } catch(SQLException se){
            se.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
