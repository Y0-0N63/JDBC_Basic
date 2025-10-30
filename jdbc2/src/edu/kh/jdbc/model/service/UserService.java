package edu.kh.jdbc.model.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.model.dao.UserDAO;
import edu.kh.jdbc.model.dto.User;

// Service : Model 중 하나로, 비즈니스 로직을 처리하는 계층 > 데이터를 가공하고 트랜잭션(commit, rollback)을 관리
public class UserService {
	// 필드
	private UserDAO dao = new UserDAO();

	/**
	 * 1. User 등록 서비스
	 * @param user : 입력받은 id, pw, name이 세팅된 객체
	 * @return insert된 결과 행의 개수
	 */
	public int insertUser(User user) throws Exception {
		// 1. JDBCTemplate의 getConnection() 사용하여 커넥션 생성 > DAO에게 전달
		Connection conn = JDBCTemplate.getConnection();
		
		// 2. 데이터 가공 > 할 필요가 없는 경우 > 생략
		
		// 3. DAO 메서드 호출 후 결과 반환받기
		int result = dao.insertUser(conn, user);
		
		// 4. DML(INSERT) 수행 결과에 따라 트랜잭션 제어 처리
		if (result > 0) { // INSERT에 성공했다면
			JDBCTemplate.commit(conn);
		} else { // INSERT에 실패했다면
			JDBCTemplate.rollback(conn);
		}
		
		// 5. Connection 반환하기
		JDBCTemplate.close(conn);
		
		// 6. 결과 반환		
		return result;
	}

	/**
	 * 2. User 전체 조회 서비스
	 * @return 조회된 User 객체들이 담긴 List
	 */
	public List<User> selectAll() throws Exception {
		// 1. 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 2. 데이터 가공 > 할 필요가 없는 경우 > 생략
		
		// 3. DAO 메서드 호출(SELECT) 후 결과(List<User>) 반환받기
		List<User> userList = dao.selectAll(conn);
		
		// 4. 수행 결과에 따라 트랜잭션 제어 처리 > SELECT이므로 생략
		
		// 5. Connection 반환
		JDBCTemplate.close(conn);

		// 6. 결과 반환
		return userList;
	}

	/**
	 * 3. User 중 이름에 검색어가 포함된 회원 조회
	 * @param input
	 * @return userList
	 * @throws SQLException 
	 */
	public List<User> searchUserName(String keyword) throws Exception {
		// 1. 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 2. 데이터 가공 > 할 필요가 없는 경우 > 생략
		
		// 3. DAO 메서드 호출(SELECT) 후 결과(List<User>) 반환받기
		List<User> userList = dao.searchUserName(conn, keyword);
		
		// 4. 수행 결과에 따라 트랜잭션 제어 처리 > SELECT이므로 생략
		
		// 5. Connection 반환
		JDBCTemplate.close(conn);

		// 6. 결과 반환
		return userList;
	}


	/**
	 * 4. USER_NO를 입력 받아 일치하는 User 조회
	 * @param userNo
	 * @return result
	 * @throws Exception 
	 */
	public User searchUserNo(int userNum) throws Exception {
		// 1. 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 2. 데이터 가공 > 할 필요 없는 경우 > 생략
		
		// 3. DAO 메서드 호출(SELECT) 후 결과(result) 반환받기
		User result = dao.searchUserNo(conn, userNum);
		
		// 4. 수행 결과에 따라 트랜잭션 제어 처리 > SELECT이므로 생략

		// 5. Connection 반환
		JDBCTemplate.close(conn);
		
		// 6. 결과 반환
		return result;
	}

	/**
	 * 5. USER_NO를 입력받아 일치하는 User 삭제 (DELETE)
	 * @param userNum
	 * @return result
	 * @throws Exception 
	 */
	public int deleteUser(int userNum) throws Exception {
		// 1. 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 2. 데이터 가공 > 할 필요 없는 경우 > 생략
		
		// 3. DAO 메서드 호출(SELECT) 후 결과(result) 반환받기
		int result = dao.deleteUser(conn, userNum);
		
		// 4. DML(DELETE) 수행 결과에 따라 트랜잭션 제어 처리
		if (result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		// 5. Connection 반환
		JDBCTemplate.close(conn);
		
		// 6. 결과 반환
		return result;
	}

	/**
	 * 6. ID, PW가 일치하는 회원이 있을 경우 이름 수정(UPDATE)
	 * @param inputId
	 * @param inputPw
	 * @return int result
	 * @throws Exception 
	 */
	public int updateUser(String inputId, String inputPw, String inputName) throws Exception {
		// 1. 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 2. 데이터 가공 > 할 필요 없는 경우 > 생략
		
		// 3. DAO 메서드 호출(SELECT) 후 결과(result) 반환받기
		int result = dao.updateUser(conn, inputId, inputPw, inputName);
		
		// 4. DML(UPDATE) 수행 결과에 따라 트랜잭션 처리
		if (result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		// 5. Connection 반환
		JDBCTemplate.close(conn);
		
		// 6. 결과 반환
		return result;
	}

	/**
	 * 입력한 아이디가 존재한다면 > true 반환
	 * @param inputId
	 * @return
	 * @throws Exception 
	 */
	public boolean existUserId(String inputId) throws Exception {
		// 1. 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 2. 데이터 가공 > 할 필요 없는 경우 > 생략
		
		// 3. DAO 메서드 호출(SELECT) 후 결과(result) 반환받기
		boolean result = dao.existUserId(conn, inputId);
		
		return result;
	}
}
