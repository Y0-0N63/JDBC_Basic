package homework.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

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

}
