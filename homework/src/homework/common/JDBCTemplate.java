package homework.common;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * JDBC 관련 작업을 위한 코드 미리 작성해서 제공하는 클래스
 * Connectino 생성, AutoCommit을 false로 설정, commit, rollback, 자원 반환(close) 
 * JDBCTemplate 클래스를 객체로 만들지 않고도 어디서든지 메서드를 사용할 수 있도록 하기 위해 > public static으로 선언
 */
public class JDBCTemplate {
	// 필드
	private static Connection conn = null;

	// 메서드
	/**
	 * - 호출 시 Connection 객체를 생성해 > 호출한 곳으로 Connection 객체 반환
	 * - AutoCommit 끄기(false)
	 * @return conn
	 */
	public static Connection getConnection() {
		try {
			// 이전에 Connection 객체가 만들어졌고(존재하고), 아직 close()된 상태가 아니라면 > 기존의 Connection 반환
			if(conn != null && !conn.isClosed()) {
				return 	conn;
			}
			
			// 1. Properties 객체 생성
			Properties prop = new Properties();
			
			// 2. Properties가 제공하는 메서드(loadFromXML)를 이용하여 > driver.xml 파일 안의 내용을 읽어오기
			prop.loadFromXML(new FileInputStream("driver.xml"));
			
			// 3. prop에 저장된 값(getProperty("키"))을 이용해 > Connection 객체 생성
			Class.forName(prop.getProperty("driver"));
			conn = DriverManager.getConnection(prop.getProperty("url"), prop.getProperty("userName"), prop.getProperty("password"));
			
			// 4. 만들어진 Connection에서 AutoCommit 끄기
			conn.setAutoCommit(false);
		} catch (Exception e) {
			System.out.println("커넥션 생성 중 예외 발생(JDBCTemplate의 getConnection())");
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * 전달받은 커넥션에서 수행한 SQL을 commit하는 메서드
	 * @param conn
	 */
	public static void commit(Connection conn) {
		try {
			// 이전에 Connection 객체가 만들어졌고(존재하고), 아직 close()된 상태가 아니라면 > 기존의 Connection 반환
			if(conn != null && !conn.isClosed()) {
				conn.commit();
			} 
		} catch (Exception e) {
			System.out.println("커밋 중 예외 발생");
			e.printStackTrace();
		}
	}
	
	/**
	 * 전달받은 커넥션에서 수행한 SQL을 rollback하는 메서드
	 * @param conn
	 */
	public static void rollback(Connection conn) {
		try {
			// 이전에 Connection 객체가 만들어졌고(존재하고), 아직 close()된 상태가 아니라면 > 기존의 Connection 반환
			if(conn != null && !conn.isClosed()) {
				conn.rollback();
			}
		} catch (Exception e) {
			System.out.println("롤백 중 예외 발생");
			e.printStackTrace();
		}
	}
	
	/**
	 * 전달받은 커넥션을 close(자원반환)하는 메서드
	 * @param conn
	 */
	public static void close(Connection conn) {
		try {
			// 이전에 Connection 객체가 만들어졌고(존재하고), 아직 close()된 상태가 아니라면 > close()
			if(conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (Exception e) {
			System.out.println("커넥션 close() 중 예외 발생");
			e.printStackTrace();
		}
	}
	
	/**
	 * 전달받은 Statement를 close(자원반환)하는 메서드
	 * @param conn
	 */
	public static void close(Statement stmt) {
		try {
			// 이전에 Statement 객체가 만들어졌고(존재하고), 아직 close()된 상태가 아니라면 > close()
			if(stmt != null && !stmt.isClosed()) {
				stmt.close();
			}
		} catch (Exception e) {
			System.out.println("Statement close() 중 예외 발생");
			e.printStackTrace();
		}
	}	
	
	/**
	 * 전달받은 ResultSet을 close(자원반환)하는 메서드
	 * @param conn
	 */
	public static void close(ResultSet rs) {
		try {
			// 이전에 ResultSet 객체가 만들어졌고(존재하고), 아직 close()된 상태가 아니라면 > close()
			if(rs != null && !rs.isClosed()) {
				rs.close();
			}
		} catch (Exception e) {
			System.out.println("ResultSet close() 중 예외 발생");
			e.printStackTrace();
		}
	}
}
