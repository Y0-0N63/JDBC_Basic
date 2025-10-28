package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCExample3 {

	public static void main(String[] args) {
		// 입력받은 최소 급여 이상, 입력받은 최대 급여 이하를 받는 사원의 사번, 이름, 급여를 급여 내림차순으로 조회해 콘솔창에 출력
		// 1. JDBC 객체 참조용 변수(DB 연결 정보, SQL 수행, SELECT 수행) 선언
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Scanner sc = null;

		try {
			// 2. Connection 객체 생성
			Class.forName("oracle.jdbc.driver.OracleDriver");

			String dbInfo = "jdbc:oracle:thin:@localhost:1521:xe";
			String userName = "kh_cby";
			String password = "kh1234";

			conn = DriverManager.getConnection(dbInfo, userName, password);

			// 3. sql문 작성
			sc = new Scanner(System.in);

			System.out.print("최소 급여 : ");
			int minSal = sc.nextInt();

			System.out.print("최대 급여 : ");
			int maxSal = sc.nextInt();

			// Java 13 이상부터 지원하는 text block(""")문법
			String sql = """
					SELECT EMP_ID, EMP_NAME, SALARY
					FROM EMPLOYEE
					WHERE SALARY BETWEEN
					""" + minSal + " AND " + maxSal + " ORDER BY SALARY DESC";

			// 4. Statement 객체 생성
			stmt = conn.createStatement();

			// 5. sql 수행 후 결과 반환받기
			rs = stmt.executeQuery(sql);

			// 6. 각 행의 컬럼값 가져오기
			while (rs.next()) {
				String empId = rs.getString("EMP_ID");
				String empName = rs.getString("EMP_NAME");
				int salary = rs.getInt("SALARY");
				System.out.printf("%s / %s / %d원 \n", empId, empName, salary);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 7. 사용 완료한 객체 반환하기
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
				if (sc != null)
					sc.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
