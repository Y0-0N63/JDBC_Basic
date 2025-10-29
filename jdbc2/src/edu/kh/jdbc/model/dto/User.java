package edu.kh.jdbc.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// DTO(Data Transfer Object, 데이터 전송 객체) : DB에 데이터를 묶어 전달하거나, DB에서 조회한 결과를 가져올 때 사용
// DB 내 특정 테이블의 한 행의 데이터를 저장할 수 있는 형태로 class 정의

@Getter
@Setter
@NoArgsConstructor // 기본생성자
@AllArgsConstructor // 모든 매개변수가 있는 생성자
@ToString
public class User {
	private int userNo;
	private String userId;
	private String userPw;
	private String userName;
	// DB 조회 시 날짜 데이터를 원하는 형태의 문자열로 변환하여(TO_CHAR(2025년 10월 28일) > 2025년 10월 28일) 조회할 예정
	// > java.sql.Date 타입이 아니라 String 사용
	private String enrollDate;
	
	// getter, setter, 기본 생성자 대신 > lombok 사용하기
}