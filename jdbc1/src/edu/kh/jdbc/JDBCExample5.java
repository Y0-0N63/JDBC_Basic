package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class JDBCExample5 {

	public static void main(String[] args) {
		// 아이디, 비밀번호, 이름을 입력받아 TB_USER 테이블에 데이터 삽입하기
		
		/*java.sql.PreparedStatement
		 : Statement의 자식으로 SQL 중간에 ?(위치 홀더, placeholder)를 작성하여, ? 자리에 java 값을 대입할 준비가 되어있는 Statement
		 - 장점 : SQL 작성이 간편해지고, ?(위치 홀더)에 값 대입 시 자료형에 맞는 형태의 리터럴으로 대입됨
		 									ex) String 대입 -> '값' (자동으로 '' 추가), int 대입 -> 값
		 		또한 내부적으로 성능, 속도에서 statement보다 우위를 가지고 있음 */
		
		// 1. JDBC 객체 참조 변수 선언
		// SELECT문을 작성하는 것이 아니기 때문에, ResultSet은 필요 없음
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
			String pw = sc.next();
			
			System.out.print("이름 입력 : ");
			String name = sc.next();
			
			String sql = """
					INSERT INTO TB_USER
					VALUES(SEQ_USER_NO.NEXTVAL, ?, ?, ?, DEFAULT)
					""";
			
			// 4. PreparedStatement 객체 생성
			// 위치홀더(?)가 있을 때 > 무조건 pstmt // 없을 때 > stmt, pstmt 둘 다 가능
			// ?(위치 홀더)에 값을 넣어주기 위해 객체 생성과 동시에 SQL문에 작성
			pstmt = conn.prepareStatement(sql);
			
			// 5. ?(위치홀더)에 알맞은 값 대입
			// pstmt.set자료형(?순서, 대입할 값);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			pstmt.setString(3, name);
			
			// 6. SQL(INSERT) 수행 후 결과(int) 반환 받기 + DML 수행 전에 Connection의 AUTO COMMIT 끄기 (개발자가 트랜잭션을 제어하기 위해서)
			conn.setAutoCommit(false);
			// executeUpdate : DML 수행, 결과 행 개수(int) 반환
			// 일반적으로 DML 실패 시 0, 성공 시 0 초과 값 반환
			// pstmt에서 executeQuery(), executeUpdate() > 매개변수 자리에 sql이 들어오면 안 됨
			int result = pstmt.executeUpdate();
			
			// 7. result 값에 따른 결과 처리 + 트랜잭션 제어 처리
			if(result > 0) { // INSERT 성공했을 시 > commit > DB에 INSERT한 데이터가 영구적으로 반영됨
				conn.commit();
				System.out.println(name + "님이 추가되었습니다.");
			} else { // 실패 > rollback
				conn.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 8. 사용한 JDBC 객체 자원 반환
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
