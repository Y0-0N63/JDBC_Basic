package homework.model.service;

import java.sql.Connection;

import homework.common.JDBCTemplate;
import homework.model.dao.StudentDAO;
import homework.model.dto.Student;

public class StudentService {
	private StudentDAO dao = new StudentDAO();

	/**
	 * 학생 등록 서비스
	 * @param inputName
	 * @param inputAge
	 * @param inputDept
	 * @return
	 * @throws Exception 
	 */
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

}
