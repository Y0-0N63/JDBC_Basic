package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class JDBCExample7 {

	public static void main(String[] args) {
		// EMPLOYEE	테이블에서 사번, 이름, 성별, 급여, 직급명, 부서명을 조회
		// 단, 입력 받은 조건에 맞는 결과만 조회하고 정렬할 것						
		// - 조건 1 : 성별 (M, F)
		// - 조건 2 : 급여 범위
		// - 조건 3 : 급여 오름차순/내림차순
		
		// 1. JDBC 객체 참조 변수 선언
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Scanner sc = null;

		try {
			// 2. DriverManager를 이용해 Connection 객체 생성
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			String userName = "kh_cby";
			String userPwd = "kh1234";
			conn = DriverManager.getConnection(url, userName, userPwd);
			
			// 3. SQL문 작성
			sc = new Scanner(System.in);
			
			System.out.print("조회할 성별(M/F) : ");
			String gender = sc.next().toUpperCase();
			
			System.out.print("급여 범위(최소, 최대 순서로 작성) : ");
			String minSal = sc.next();
			String maxSal = sc.next();
				
			System.out.print("급여 정렬(1. ASC, 2. DESC) : ");
			int order = sc.nextInt();
			
			String sql = """
					SELECT EMP_ID, EMP_NAME,
					DECODE(SUBSTR(EMP_NO, 8, 1), '1', 'M', '2', 'F') GENDER,
					SALARY, JOB_NAME, NVL(DEPT_TITLE, '없음') DEPT_TITLE
					FROM EMPLOYEE
					LEFT JOIN DEPARTMENT ON (DEPT_CODE = DEPT_ID)
					JOIN JOB USING (JOB_CODE)
					WHERE DECODE(SUBSTR(EMP_NO, 8, 1), '1', 'M', '2', 'F') = ?
					AND SALARY BETWEEN ? AND ?
					ORDER BY SALARY
					""";
			// ORDER BY 절에는 ?(위치 홀더)를 사용할 수 없음! > SQL 명령어가 올바르게 종료되지 않았습니다
			// > PreparedStatement 위치 홀더(?)는 데이터 값(리터럴)을 대체하는 용도로만 사용 가능
			// > SQL에서 ORDER BY 절의 정렬 기준(ASC/DESC)와 같은 SQL 구문(문법)의 일부는 위치 홀더로 대체될 수 없음!
			
			if(order == 1) {
				sql += " ASC";
			} else {
				sql += " DESC";
			}
			
			// 4. PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 5. 위치 홀더에 알맞은 값 대입
			pstmt.setString(1, gender);
			pstmt.setString(2, minSal);
			pstmt.setString(3, maxSal);
			
			// 6. sql 수행
			conn.setAutoCommit(false);
			rs = pstmt.executeQuery();
			
			// 7. 커서를 이용해 한 행씩 접근해 컬럼 값 얻어와 출력하기
			// 조회 결과가 없다면 > true, 있다면
			boolean flag = true;
			System.out.println("사번 |    이름    | 성별 |   급여   | 직급명 |  부서명  ");
			System.out.println("------------------------------------------------------------");
			
			// while문이 1회 이상 반복됨 = 조회 결과가 1행 이상
			while(rs.next()) {
				flag = false;
				
				String empId = rs.getString("EMP_ID");
				String empName = rs.getString("EMP_NAME");
				String empGender = rs.getString("GENDER");
				int salary = rs.getInt("SALARY");
				String jobName = rs.getString("JOB_NAME");
				String deptTitle = rs.getString("DEPT_TITLE");
				System.out.printf("%-4s | %3s | %-4s | %7d | %-3s  | %s \n", empId, empName, empGender, salary, jobName, deptTitle);
			}
			
			if (flag) { // flag == true : while문 수행 X > 조회 결과가 1행도 없음
				System.out.println("조회 결과 없음");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
				if (sc != null) sc.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
