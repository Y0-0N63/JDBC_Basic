package edu.kh.jdbc.model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

// DAO : Model 중 하나로, 데이터가 저장된 곳(DB)에 접근해 Java에서 원하는 결과를 얻기 위해 SQL을 수행하고 결과를 반환받는 역할
public class UserDAO {
	// 필드
	// - DB 접근 관련한 JDBC 객체 참조 변수 선언
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
}
