package edu.kh.jdbc.model.service;

import edu.kh.jdbc.model.dao.UserDAO;

// Service : Model 중 하나로, 비즈니스 로직을 처리하는 계층 > 데이터를 가공하고 트랜잭션(commit, rollback)을 관리
public class UserService {
	// 필드
	private UserDAO dao = new UserDAO();
}
