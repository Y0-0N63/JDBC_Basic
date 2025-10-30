package edu.kh.jdbc.model.dao;

// import static : 지정된 경로에 존재하는 static 구문을 모두 얻어와 > 클래스명.메서드명()이 아닌 메서드명()만 작성해도 호출 가능하게 함
// JDBCTemplate의 static 메서드, 필드 모두 > 메서드명, 필드명만 작성해도 사용 가능하게
// ex) JDBCTemplate.close(pstmpt) > close(pstmt)
import static edu.kh.jdbc.common.JDBCTemplate.close;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.kh.jdbc.model.dto.User;

// DAO : Model 중 하나로, 데이터가 저장된 곳(DB)에 접근해 Java에서 원하는 결과를 얻기 위해 SQL을 수행하고 결과를 반환받는 역할
public class UserDAO {
	// 필드
	// - DB 접근 관련한 JDBC 객체 참조 변수 선언
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	/**
	 * 1. User 등록 DAO
	 * @param conn : DB 연결 정보가 담긴 Connection 객체
	 * @param user : 입력받은 id, pw, name이 세팅된 User 객체
	 * @return INSERT된 결과 행의 개수
	 */
	public int insertUser(Connection conn, User user) throws Exception {
		// 1. 결과 저장용 변수 선언
		int result = 0;	
		
		// throws를 사용하기 때문에 catch는 제외
		try {
			// 2. SQL 작성
			String sql = """
					INSERT INTO TB_USER
					VALUES(SEQ_USER_NO.NEXTVAL, ?, ?, ?, DEFAULT)
					""";
			
			// 3. PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 4. ? 위치 홀더에 알맞은 값 대입
			pstmt.setString(1, user.getUserId());
			pstmt.setString(2, user.getUserPw());
			pstmt.setString(3, user.getUserName());
			
			// 5. SQL(INSERT) 수행(executeUpdate()) 후 결과(삽입된 행의 개수) 반환 받기
			result = pstmt.executeUpdate();
		} finally {
			// 6. 사용한 jdbc 객체 자원 반환
			close(pstmt);
		}

		// 결과 저장용 변수에 저장된 최종 값 반환
		return result;
	}

	/**
	 * 2. User 전체 조회 DAO
	 * @param conn
	 * @return List<User> userList
	 */
	public List<User> selectAll(Connection conn) throws Exception {
		// 1. 결과 저장용 변수 선언
		List<User> userList = new ArrayList<>();
		
		try {
			// 2. SQL 작성
			String sql = """
					SELECT USER_NO, USER_ID, USER_PW, USER_NAME,
					TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일"') ENROLL_DATE
					FROM TB_USER
					ORDER BY USER_NO
					""";
			
			// 3. PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 4. ? 위치 홀더에 알맞은 값 대입 > 할 필요가 없는 경우 > 생략
			
			// 5. SQL(SELECT) 수행(executeQuery()) 후 결과(ResultSet) 반환 받기
			rs = pstmt.executeQuery();
			
			// 6. 조회 결과(rs)를 1행씩 접근하여 컬럼 값 얻어오기
			// 몇 행이 조회될지 모르는 경우 > while, 무조건 한 행만 조회 > if
			while(rs.next()) {
				int userNo = rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");
				String userPw = rs.getString("USER_PW");
				String userName = rs.getString("USER_NAME");
				// SELECT 문에서 TO_CHAR()를 이용해 문자열 형태로 변환했기 때문에 > java.sql.Date 타입이 아닌 String 타입으로 저장
				String enrollDate = rs.getString("ENROLL_DATE");
				
				// DB에서 가져온 컬럼값들을 User 객체로 새로 생성해 필드로 세팅하기
				User user = new User(userNo, userId, userPw, userName, enrollDate);
				
				userList.add(user);
			}
		} finally {
			// 7. 사용한 자원 반환
			close(rs);
			close(pstmt);
		}
		// 조회 결과가 담긴 List 반환
		return userList;
	}

	/**
	 * 3. User 중 이름에 검색어가 포함된 회원 조회
	 * @param conn
	 * @return List<User> userList
	 * @throws SQLException 
	 */
	public List<User> searchUserName(Connection conn, String keyword) throws SQLException {
		// 1. 결과 저장용 변수 선언
		List<User> userList = new ArrayList<>();
		
		try {
			// 2. SQL문 작성
			// '%?%' 로 작성하였을 때 > 문자 '?(물음표)'로 인식하게 됨
			// >> 따로따로 '%' '%'를 만들어준 후 ||를 통해 '%' || ? || '%'로 이어쓰기 해야 함
			String sql = """
					SELECT USER_NO, USER_ID, USER_PW, USER_NAME,
					TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일"') ENROLL_DATE
					FROM TB_USER
					WHERE USER_NAME LIKE '%'||?||'%'
					ORDER BY USER_NO
					""";
			
			// 3. PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 4. ? 위치 홀더에 알맞은 값 대입
			pstmt.setString(1, keyword);
			
			// 5. SQL(SELECT) 수행(executeQuery()) 후 결과(ResultSet) 반환 받기
			rs= pstmt.executeQuery();
			
			// 6. 조회 결과(rs)를 1행씩 접근하여 컬럼 값 얻어오기
			while(rs.next()) {
				int userNo = rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");
				String userPw = rs.getString("USER_PW");
				String userName = rs.getString("USER_NAME");
				String enrollDate = rs.getString("ENROLL_DATE");
				
				User user = new User(userNo, userId, userPw, userName, enrollDate);			
				userList.add(user);
			}
		} finally {
			// 7. 사용한 자원 반환
			close(rs);
			close(pstmt);
		}	
		return userList;
	}

	/**
	 * 4. USER_NO를 입력 받아 일치하는 User 조회
	 * @param conn
	 * @param userNo
	 * @return
	 * @throws Exception 
	 */
	public User searchUserNo(Connection conn, int userNum) throws Exception {
		// 1. 결과 저장용 변수 선언
		User result = null;
		
		try {
			// 2. SQL문 작성
			String sql = """
					SELECT USER_NO, USER_ID, USER_PW, USER_NAME,
					TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일"') ENROLL_DATE
					FROM TB_USER
					WHERE USER_NO = ?
					ORDER BY USER_NO
					""";
			
			// 3. PreparedStatement 생성
			pstmt = conn.prepareStatement(sql);
			
			// 4. ? 위치 홀더에 알맞은 값 대입
			pstmt.setInt(1, userNum);
			
			// 5. SQL(SELECT) 수행 후 결과(ResultSet) 반환 받기
			rs = pstmt.executeQuery();
			
			// 6. 조회 결과(rs)에 접근해 컬럼값 가져오기
			while(rs.next()) {
				int userNo = rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");
				String userPw = rs.getString("USER_PW");
				String userName = rs.getString("USER_NAME");
				String enrollDate = rs.getString("ENROLL_DATE");
				
				User user = new User(userNo, userId, userPw, userName, enrollDate);	
				result = user;
			}
		} finally {
			// 7. 사용한 자원 반환
			close(rs);
			close(pstmt);
		}
		

		return result;
	}

	public int deleteUser(Connection conn, int userNum) throws Exception {
		// 1. 결과 저장용 변수 선언
		int result = 0;
		
		try {
			// 2. SQL문 작성
			String sql = "DELETE FROM TB_USER WHERE USER_NO = ?";
			
			// 3. PreparedStatement 생성
			pstmt = conn.prepareStatement(sql);
			
			// 4. ? 위치 홀더에 알맞은 값 대입
			pstmt.setInt(1, userNum);
			
			// 5. SQL(DELETE) 수행 후 결과(삭제된 행의 개수) 반환 받기
			result = pstmt.executeUpdate();
		} finally {
			// 6. 사용한 자원 반환
			close(pstmt);
		}
		return result;
	}

	public int updateUser(Connection conn, String inputId, String inputPw, String inputName) throws Exception {
		// 1. 결과 저장용 변수 선언
		int result = 0;
		
		try{
			// 2. SQL문 작성
			String sql = """
					UPDATE TB_USER SET USER_NAME = ?
					WHERE USER_ID = ?
					AND USER_PW = ?
					""";
			
			// 3. PreparedStatement 생성
			pstmt = conn.prepareStatement(sql);
			
			// 4. ? 위치 홀더에 알맞은 값 대입
			pstmt.setString(1, inputName);
			pstmt.setString(2, inputId);
			pstmt.setString(3, inputPw);
			
			// 5. SQL(UPDATE) 수행 후 결과(수정된 행의 개수) 반환 받기
			result = pstmt.executeUpdate();
		} finally {
			// 6. 사용한 자원 반환
			close(pstmt);
		}
		return result;
	}

	public boolean existUserId(Connection conn, String inputId) throws Exception {
		// 1. 결과 저장용 변수 선언
		boolean result = false;
		
		try {
			// 2. SQL문 작성
			String sql = """
					SELECT USER_NO FROM TB_USER WHERE USER_ID = ?
					""";
			
			// 3. PreparedStatement 생성
			pstmt = conn.prepareStatement(sql);
			
			// 4. ? 위치 홀더에 알맞은 값 대입
			pstmt.setString(1, inputId);
			
			// 5. SQL(SELECT) 수행(executeQuery()) 후 결과(ResultSet) 반환 받기
			rs = pstmt.executeQuery();
			
			// 6. 조회 결과(rs)가 존재한다면
			while(rs.next()) {
				result = true;
			}
		} finally {
			 	close(rs);
			 	close(pstmt);
		}
		return result;
	}
}
