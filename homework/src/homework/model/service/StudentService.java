package homework.model.service;

import java.sql.Connection;
import java.util.List;

import homework.common.JDBCTemplate;
import homework.model.dao.StudentDAO;
import homework.model.dto.Student;

public class StudentService {
	private StudentDAO dao = new StudentDAO();

	public int insertStd(Student student) throws Exception {
		// 1. JDBCTemplate의 getConnection() 사용하여 커넥션 생성 > DAO에게 전달
		Connection conn = JDBCTemplate.getConnection();
		
		// 2. 데이터 가공(USER 객체를 생성해 객체 내 담아주기 등...) > 할 필요가 없는 경우 > 생략
		
		// 3. DAO 메서드 호출 후 결과 반환받기
		int result = dao.insertStd(conn, student);
		
		// 4. DML(INSERT) 수행 결과에 따라 트랜잭션 제어 처리
		if(result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		// 5. Connection 반환하기
		JDBCTemplate.close(conn);
		
		// 6. 결과 반환		
		return result;
	}

	public List<Student> selectAll() throws Exception {
		Connection conn = JDBCTemplate.getConnection();
		
		// DAO 호출(SELECT) > 결과(stdList) 반환받기
		List<Student> stdList = dao.selectAll(conn);
		
		JDBCTemplate.close(conn);
		
		return stdList;
	}


	public int selectNo(int inputNo) throws Exception {
		Connection conn = JDBCTemplate.getConnection();
		
		int userNo = dao.selectNo(conn, inputNo);
		JDBCTemplate.close(conn);
		
		return userNo;
	}

	public int updateName(int selectResult, String newName) throws Exception {
		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.updateName(conn, selectResult, newName);
		
		if(result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		return result;
	}

	public int updateAge(int selectResult, int newAge) throws Exception {
		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.updateAge(conn, selectResult, newAge);
		
		if (result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		return result;
	}

	public int updateMajor(int selectResult, String newMajor) throws Exception {
		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.updateMajor(conn, selectResult, newMajor);
		
		if (result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		return result;
	}
}
