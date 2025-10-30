package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class JDBCExample6 {

	public static void main(String[] args) {
		// 아이디, 비밀번호, 이름을 입력받아 > 아이디, 비밀번호가 일치하는 사용자의 이름을 수정 (UPDATE)
		// 성공 시 "수정 성공!" 출력 / 실패 시 "아이디 또는 비밀번호 불일치"

		// 1. JDBC 객체 참조 변수 선언
		Connection conn = null;
		PreparedStatement pstmt = null;
		Scanner sc = null;
		
		try {
			// 2. DriverManager를 이용해서 Connection 객체 생성
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			String userName = "kh_cby";
			String password = "kh1234";
			conn = DriverManager.getConnection(url, userName, password);
			
			// 3. SQL 작성
			sc = new Scanner(System.in);
			
			System.out.print("아이디 입력 : ");
			String id = sc.next();
			
			System.out.print("비밀번호 입력 : ");
			String pwd = sc.next();
			
			System.out.print("이름 입력 : ");
			String name = sc.next();
			
			String sql = """
					UPDATE TB_USER
					SET USER_NAME = ?
					WHERE USER_ID = ? AND USER_PW = ?
					""";
			
			// 4. PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 5. 위치홀더에 알맞은 값 대입
			pstmt.setString(1, name);
			pstmt.setString(2, id);
			pstmt.setString(3, pwd);
			
			// 6. SQL(UPDATE) 수행 후 결과(int) 반환 받기 + DML 수행 전에 Connection의 AUTO COMMIT 끄기
			conn.setAutoCommit(false);
			int result = pstmt.executeUpdate();
			
			if(result > 0) {
				System.out.println("수정 성공!");
				conn.commit();
			} else {
				System.out.println("아이디 또는 비밀번호 불일치");
				conn.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
				if(sc != null) sc.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	}

}
