package homework.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import homework.common.JDBCTemplate;
import homework.model.dto.Student;

public class StudentDAO {
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	public int insertStd(Connection conn, Student student) throws Exception {
		// 1. 결과 저장용 변수 선언
		int result = 0;
		
		try {
			// 2. SQL 작성
			String sql = """
					INSERT INTO KH_STUDENT VALUES(SEQ_STD_NO.NEXTVAL, ?, ?, ?, DEFAULT)
					""";
			
			// 3. PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 4. ? 위치 홀더에 알맞은 값 대입
			pstmt.setString(1, student.getStdName());
			pstmt.setInt(2, student.getStdAge());
			pstmt.setString(3, student.getMajor());

			// 5. SQL(INSERT) 수행(executeUpdate()) 후 결과(삽입된 행의 개수) 반환 받기
			result = pstmt.executeUpdate();
		} finally {
			// 6. 사용한 jdbc 객체 자원 반환
			JDBCTemplate.close(pstmt);
		}	
		// 7. 결과 저장용 변수에 저장된 최종 값 반환
		return result;
	}

	public List<Student> selectAll(Connection conn) throws Exception{
		List<Student> stdList = new ArrayList<>();
		
		try {
			String sql = """
					SELECT STD_NO 학번, STD_NAME 이름, STD_AGE 나이, MAJOR 전공,
					TO_CHAR(ENT_DATE, 'YYYY"년" MM"월" DD"일"') 입학일
					FROM KH_STUDENT
					""";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int stdNo = rs.getInt("학번");
				String stdName = rs.getString("이름");
				int stdAge = rs.getInt("나이");
				String major = rs.getString("전공");
				String enrollDate = rs.getString("입학일");
				
				Student std = new Student(stdNo, stdName, stdAge, major, enrollDate);
				
				stdList.add(std);
			}
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
			JDBCTemplate.close(conn);
		}
		return stdList;
	}


	public int selectNo(Connection conn, int inputNo) throws Exception {
		int userNo = 0;
		
		try {
			String sql = """
					SELECT STD_NO FROM KH_STUDENT WHERE STD_NO = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, inputNo);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				userNo = rs.getInt("STD_NO");
			}
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
		}
		return userNo;
	}

	public int updateName(Connection conn, int selectResult, String newName) throws Exception {
		int result = 0;
		
		try {
			String sql = """
					UPDATE KH_STUDENT SET STD_NAME = ?
					WHERE STD_NO = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, newName);
			pstmt.setInt(2, selectResult);
			result = pstmt.executeUpdate();
		} finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}

	public int updateAge(Connection conn, int selectResult, int newAge) throws Exception {
		int result = 0;
		
		try {
			String sql = """
					UPDATE KH_STUDENT SET STD_AGE = ?
					WHERE STD_NO = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, newAge);
			pstmt.setInt(2, selectResult);
			result = pstmt.executeUpdate();
		} finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}

	public int updateMajor(Connection conn, int selectResult, String newMajor) throws Exception {
		int result = 0;
		
		try {
			String sql = """
					UPDATE KH_STUDENT SET MAJOR = ?
					WHERE STD_NO = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, newMajor);
			pstmt.setInt(2, selectResult);
			result = pstmt.executeUpdate();
		} finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
	}
}
