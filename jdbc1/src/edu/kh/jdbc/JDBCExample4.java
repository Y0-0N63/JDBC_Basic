package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCExample4 {

	public static void main(String[] args) {
		// 부서명을 입력받아 해당 부서에 근무하는 사원의 사번, 이름, 부서명, 직급명을 직급코드 오름차순으로 조회
		// 입력된 부서명이 존재하지 않는 경우 > 일치하는 부서가 없습니다!

		// 1. JDBC 객체 참조용 변수
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Scanner sc = null;

		try {
			// 2. Connection 객체 생성
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			String userName = "kh_cby";
			String password = "kh1234";
			conn = DriverManager.getConnection(url, userName, password);

			// 3. sql문 작성
			sc = new Scanner(System.in);

			System.out.print("부서명 입력 : ");
			String dept = sc.next();

			String sql = """
					SELECT EMP_ID, EMP_NAME, DEPT_TITLE, JOB_NAME
					FROM EMPLOYEE
					JOIN JOB ON (EMPLOYEE.JOB_CODE = JOB.JOB_CODE)
					LEFT JOIN DEPARTMENT ON (DEPT_CODE = DEPT_ID)
					WHERE DEPT_TITLE = '""" + dept + "' ORDER BY EMPLOYEE.JOB_CODE";
			
			// 4. Statement 객체 생성
			stmt = conn.createStatement();

			// 5. ResultSet 객체 생성
			rs = stmt.executeQuery(sql);

			// 6. SQL문 결과 컬럼 한 줄씩 받아오기
			// 6-1) flag를 이용하여 부서명이 존재하는 경우에만
			boolean flag = true; // 조회 결과가 있다면 > false
//			
//			while (rs.next()) {
//				flag = false; // 찾았음을 표시
//				String empId = rs.getString("EMP_ID");
//				String empName = rs.getString("EMP_NAME");
//				String deptTitle = rs.getString("DEPT_TITLE");
//				String jobName = rs.getString("JOB_NAME");
//				System.out.printf("%s / %s / %s / %s \n", empId, empName, deptTitle, jobName);
//			}
			
//			if (flag) {
//				// 여전히 flag 값이 true = 일치하지 않는 부서가 존재하지 않음
//				System.out.println("존재하지 않습니다.");
//			}
			
			// 6-2) return 사용법
			if(!rs.next()) {
				System.out.println("일치하는 부서가 없습니다.");
				return;
			}
			
			// while(rs.next())로 작성할 시 rs.next()가 if문에서 수행되었기에 커서가 맨 처음이 아닌 두 번째 컬럼을 가리키고 있음! > do - while()로 수정
			do {
				String empId = rs.getString("EMP_ID");
				String empName = rs.getString("EMP_NAME");
				String deptTitle = rs.getString("DEPT_TITLE");
				String jobName = rs.getString("JOB_NAME");
				System.out.printf("%s / %s / %s / %s \n", empId, empName, deptTitle, jobName);
			} while (rs.next());
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			// 7. 사용한 객체 닫아주기
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
				if (sc != null)
					sc.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}