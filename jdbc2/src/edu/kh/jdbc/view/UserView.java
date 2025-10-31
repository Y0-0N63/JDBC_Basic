package edu.kh.jdbc.view;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import edu.kh.jdbc.model.dto.User;
import edu.kh.jdbc.model.service.UserService;

// View : 사용자와 직접 상호작용하는 화면(UI) 담당 > 사용자에게 입력을 받고 결과를 출력
public class UserView {
	// 필드
	private UserService service = new UserService();
	private Scanner sc = new Scanner(System.in);

	/** User 관리 프로그램의 메인 메뉴 UI (View)
	 * 
	 */
	public void mainMenu() {
		// 메뉴 선택용 변수
		int input = 0;
		
		do {
			try {
				System.out.println("\n===== User 관리 프로그램 =====\n");
				System.out.println("1. User 등록(INSERT)");
				System.out.println("2. User 전체 조회(SELECT)");
				System.out.println("3. User 중 이름에 검색어가 포함된 회원 조회 (SELECT)");
				System.out.println("4. USER_NO를 입력 받아 일치하는 User 조회(SELECT)");
				System.out.println("5. USER_NO를 입력 받아 일치하는 User 삭제(DELETE)");
				System.out.println("6. ID, PW가 일치하는 회원이 있을 경우 이름 수정(UPDATE)");
				System.out.println("7. User 등록(아이디 중복 검사)");
				System.out.println("8. 여러 User 등록하기");
				System.out.println("0. 프로그램 종료");
				System.out.print("메뉴 선택 : ");
				input = sc.nextInt();
				// 버퍼에 남은 개행문자 제거
				sc.nextLine();
				
				switch (input) {
				case 1: insertUser(); break;
				case 2: selectAll(); break;
				case 3: selectName(); break;
				case 4: selectUser(); break;
				case 5: deleteUser(); break;
				case 6: updateName(); break;
				case 7: insertUser2(); break;
				case 8: multiInsertUser(); break;
				case 0: System.out.println("\n[프로그램 종료]\n"); break;
				default: System.out.println("\n[메뉴 번호만 입력하세요]\n");
				}
				System.out.println("\n-------------------------------------\n");
			} catch (InputMismatchException e) {
				System.out.println("\n***잘못 입력하셨습니다***\n");
				// input이 0이면 while문이 종료되므로 > 메뉴에 없는 번호인 -1을 대입해 종료 방지
				input = -1;
				// 입력 버퍼에 남아있는 잘못된 문자 제거
				sc.nextLine();
			} catch (Exception e) {
				// 발생되는 예외를 모두 해당 catch 구문으로 모아서 처리
				e.printStackTrace();
			}
		} while (input != 0);
	}

	/**
	 * 1. User 등록과 관련된 View
	 * @throws Exception 
	 */
	private void insertUser() throws Exception {
		System.out.println("\n====1. User 등록====\n");
		
		System.out.print("ID : ");
		String userId = sc.next();
		
		System.out.print("PW : ");
		String userPw = sc.next();
		
		System.out.print("Name : ");
		String userName = sc.next();
		
		// 입력받은 세 개의 값을 한 번에 묶어 전달할 수 있도록 > User라는 이름의 DTO 생성 > 필드에 값 세팅
		User user = new User(); // 기본 생성자 > 내부 필드값 : JVM이 기본값으로 설정한 상태 (Heap 메모리)

		// setter를 이용해 값 넣어주기
		user.setUserId(userId); // 기본값으로 세팅된 상태
		user.setUserPw(userPw);
		user.setUserName(userName);
		
		// 서비스 호출(INSERT) > 결과 반환 받기(int, 결과 행의 개수)
		// service 객체(UserService)에 있는 insertUser()라는 이름의 메서드 호출하기
		int result = service.insertUser(user);
		
		// 반환된 결과에 따라 출력할 내용 선택
		if (result > 0) {
			System.out.println("\n" + userId + " 사용자가 등록되었습니다.\n");
		} else {
			System.out.println("\n***등록 실패***\n");
		}
	}
	
	/**
	 * 2. User 전체 조회 관련 View (Select)
	 */
	private void selectAll() throws Exception {
		System.out.println("\n====2. User 전체 조회====\n");
		
		// 서비스(SELECT) 호출 후 결과(List<User>) 반환받기
		// ResultSet에서 한 행씩 추출 > 'User' 객체(DTO)에 저장 > List(Collection) > List<User>
		List<User> userList = service.selectAll();

		// 조회 결과가 없는 경우
		if(userList.isEmpty()) {
			System.out.println("\n***조회 결과가 없습니다***\n");
			return;
		} else {
			// 조회 결과가 있을 경우 > uesrList에 있는 모든 User 객체 출력
			for(User user : userList){
				System.out.println(user);
			}
		}
	}

	/**
	 * 3. User 중 이름에 검색어가 포함된 회원 조회
	 * @throws SQLException 
	 */
	private void selectName() throws Exception {
		System.out.println("\n====3. User 중 이름에 검색어가 포함된 회원 조회====\n");
		
		System.out.print("검색어 입력 : ");
		String keyword = sc.next();
		
		List<User> userList = service.searchUserName(keyword);
		
		if(userList.isEmpty()) {
			System.out.println("\n***조회 결과가 없습니다***\n");
			return;
		} else {
			for(User user : userList) {
				System.out.println(user);
			}
		}
	}
	
	/**
	 * 4. USER_NO를 입력 받아 일치하는 User 조회
	 * USER_NO(PK) > 중복이 있을 수 없음 > 일치하는 경우 > 한 행만 조회됨 > User 객체 출력
	 * 일치하지 않는 경우 > 0행 > '일치하는 회원 없음' 
	 * @throws Exception 
	 */
	private void selectUser() throws Exception {
		System.out.println("\n====4. USER_NO를 입력 받아 일치하는 User 조회====\n");
		System.out.print("사용자 번호 입력 : ");
		int userNum = sc.nextInt();
		
		// service 호출 후 결과 반환 받기
		User result = service.searchUserNo(userNum);
		
		// 조회 결과가 없는 경우
		if(result == null) {
			System.out.println("\n***일치하는 회원이 없습니다***\n");
			return;
		} else {
			System.out.println(result);
		}
	}
	
	/**
	 * 5. USER_NO를 입력받아 일치하는 User 삭제 (DELETE)
	 * 삭제에 성공한 경우 > '삭제 성공'
	 * 삭제에 실패한 경우 > '사용자 번호가 일치하는 사용자가 존재하지 않음'
	 * @throws Exception 
	 */
	private void deleteUser() throws Exception {
		System.out.println("\n===5. USER_NO를 입력 받아 일치하는 User 삭제===\n");
		System.out.print("USER_NO 입력 : ");
		int userNum = sc.nextInt();
		
		// 서비스 호출(DELETE) > 결과 반환 받기(int, 결과 행의 개수)
		int result = service.deleteUser(userNum);
		
		// 반환된 결과에 따라 출력
		if(result > 0) {
			System.out.println("\n삭제에 성공하였습니다.\n");
		} else {
			System.out.println("\n사용자 번호가 일치하는 사용자가 존재하지 않습니다.\n");
		}
	}
	
	/**
	 * 6. ID, PW가 일치하는 회원이 있을(SELECT) 경우 > 이름 수정(UPDATE)
	 * @throws Exception 
	 */
	private void updateName() throws Exception {
		System.out.println("\n===6. ID, PW가 일치하는 회원이 있을 경우 이름 수정===\n");
		System.out.print("ID 입력 : ");
		String inputId = sc.nextLine();
		
		System.out.print("PW 입력 : ");
		String inputPw = sc.nextLine();
		
		// 입력받은 ID, PW가 일치하는 회원이 존재하는지 조회 (SELECT) > 수정할 때 필요한 데이터인 USER_NO 조회해오기
		int userNo = service.selectUserNo(inputId, inputPw);
		
		// 조회 결과가 없을 때
		if(userNo == 0) {
			System.out.println("아이디, 비밀번호가 일치하는 사용자가 없습니다.");
			return;
		} 

		// 조회 결과가 있을 때
		System.out.print("수정할 이름 : ");
		String inputName = sc.nextLine();
		
		// 서비스 호출(조회된 회원(userNo)의 이름을 수정, UPDATE) 후 결과(int) 반환 받기
		int result = service.updateUserName(userNo, inputName);
		
		// 반환된 결과에 따라 출력
		if (result > 0) {
			System.out.println("\n이름 수정에 성공하였습니다.\n");
		} else {
			System.out.println("\nID 또는 PW가 틀렸습니다.\n");
		}
	}

	/**
	 * 7. User 등록(아이디 중복 검사)
	 * @throws Exception
	 */
	private void insertUser2() throws Exception {
		System.out.println("\n===7. User 등록(아이디 중복 검사)===\n");
		
		String inputId = null;
		
		while(true) {
			System.out.print("ID 입력 : ");
			inputId = sc.next();
			
			// 입력받은 userId가 중복인지 검사하는 서비스(SELECT) 호출 후 > 결과(int, 중복 == 1, 그렇지 않으면 == 0) 반환 받기
			int count = service.idCheck(inputId);
			
			if(count == 0) { // 중복이 아니라면
				System.out.println("사용 가능한 아이디입니다.");
				break;
			}
			
			System.out.println("이미 사용중인 아이디입니다. 다시 입력하세요.");
		}
		
		System.out.print("PW 입력 : ");
		String inputPw = sc.next();
		
		System.out.print("사용자 이름 입력 : ");
		String inputName = sc.next();
		
		// 입력받은 ID, PW, 사용자 이름을 묶어서 한 번에 전달할 수 있도록 > User DTO 객체를 생성한 후 > 필드에 값 세팅
		User user = new User();
		user.setUserId(inputId);
		user.setUserPw(inputPw);
		user.setUserName(inputName);
		
		// 서비스 호출 후 결과 반환받기
		int result = service.insertUser(user);
		
		if(result > 0) {
			System.out.println("\n" + inputId + "사용자가 등록되었습니다.\n");
		} else {
			System.out.println("\n등록에 실패하였습니다.\n");
		}
	}	
	
	/**
	 * 여러 명의 User 등록
	 * @throws Exception
	 */
	private void multiInsertUser() throws Exception {
		System.out.println("\n===8. 여러 User 등록하기===\n");

		System.out.print("등록할 User 수 : ");
		int input = sc.nextInt();
		sc.nextLine();
		
		// 입력받은 회원 정보를 저장할 List 객체 생성
		List<User> userList = new ArrayList<User>();
		
		for(int i = 0; i< input; i++) {
			String userId = null;
			while(true) {
				System.out.print((i + 1) + "번째 ID 입력 : ");
				userId = sc.next();
				
				int count = service.idCheck(userId);
				
				if(count == 0) { // 중복이 아니라면
					System.out.println("사용 가능한 아이디입니다.");
					break;
				}
				
				System.out.println("이미 사용중인 아이디입니다. 다시 입력하세요.");
			}
			
			// pw, name 입력받기
			System.out.print((i + 1) + "번째 PW 입력 : ");
			String inputPw = sc.next();
			
			System.out.print((i + 1) + "번째 이름 입력 : ");
			String inputName = sc.next();
			
			User user = new User();
			user.setUserId(userId);
			user.setUserPw(inputPw);
			user.setUserName(inputName);
			
			// userList에 user 추가
			userList.add(user);
		}
		
		// 입력받은 모든 사용자(userList)를 DB에 INSERT하는 서비스 호출 > 결과로 삽입된 행의 개수 반환
		int result = service.multiInsertUser(userList);
		
		if(result == userList.size()) {
			System.out.println("사용자 등록에 성공하였습니다.");
		} else {
			System.out.println("사용자 등록에 실패하였습니다.");
		}
	}
}