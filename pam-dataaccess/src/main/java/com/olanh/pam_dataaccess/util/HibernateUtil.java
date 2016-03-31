package com.olanh.pam_dataaccess.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil{

	private static SessionFactory factory;

	public HibernateUtil() {

	}

	public static Session getSession() {
		Session hibernateSession = null;
		if (factory == null) {
			try {
				Configuration config = new Configuration().configure();
				factory = config.buildSessionFactory();
			} catch (Throwable ex) {
				System.err.println("Failed to create sessionFactory object." + ex);
				throw new ExceptionInInitializerError(ex);
			}
		}
		
		try{
			hibernateSession = factory.getCurrentSession();
		}catch (Throwable e){
			e.printStackTrace();
			hibernateSession = factory.openSession();
		}
		
		return hibernateSession;
	}
	
	
	private void TestConnection(String[] args) {
		try {
			/* Uncomment the next line for more connection information */
			// DriverManager.setLogStream(System.out);
			/*
			 * Set the host, port, and sid below to match the entries in the
			 * listener.ora
			 */
			String host = "pam.ckf6qel5wnlb.us-west-1.rds.amazonaws.com";
			String port = "1521";
			String sid = "orcl";
			// or pass on command line arguments for all three items
			if (args.length >= 3) {
				host = args[0];
				port = args[1];
				sid = args[2];
			}

			String s1 = "jdbc:oracle:thin:@" + host + ":" + port + ":" + sid;
			if (args.length == 1) {
				s1 = "jdbc:oracle:oci8:@" + args[0];
			}
			if (args.length == 4) {
				s1 = "jdbc:oracle:" + args[3] + ":@" + "(description=(address=(host=" + host + ")(protocol=tcp)(port="
						+ port + "))(connect_data=(sid=" + sid + ")))";
			}
			System.out.println("Connecting with: ");
			System.out.println(s1);
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			Connection conn = DriverManager.getConnection(s1, "olanhector", "ae010589");
			DatabaseMetaData dmd = conn.getMetaData();
			System.out.println("DriverVersion:[" + dmd.getDriverVersion() + "]");
			System.out.println("DriverMajorVersion: [" + dmd.getDriverMajorVersion() + "]");
			System.out.println("DriverMinorVersion: [" + dmd.getDriverMinorVersion() + "]");
			System.out.println("DriverName:[" + dmd.getDriverName() + "]");
			if (conn != null)
				conn.close();
			System.out.println("Done.");
		} catch (SQLException e) {
			System.out.println("\n*** Java Stack Trace ***\n");
			e.printStackTrace();
			System.out.println("\n*** SQLException caught ***\n");
			while (e != null) {
				System.out.println("SQLState: " + e.getSQLState());
				System.out.println("Message:  " + e.getMessage());
				System.out.println("Error Code:   " + e.getErrorCode());
				e = e.getNextException();
				System.out.println("");
			}
		}
	}

}
