package homework.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import homework.model.dto.Student;
import homework.model.service.StudentService;

public class StudentView {
	private Scanner sc = new Scanner(System.in);
	StudentService service = new StudentService();

	public void displayMenu() {
		int menuNum = 0;

		do {
			try {
				System.out.println("\n=== 학생 관리 프로그램 ===\n");
				System.out.println("1. 학생 등록");
				System.out.println("2. 전체 학생 조회");
				System.out.println("3. 학생 정보 수정");
				System.out.println("4. 학생 삭제");
				System.out.println("5. 전공별 학생 조회");
				System.out.println("0. 프로그램 종료");
				System.out.print("메뉴 선택 : ");
				menuNum = sc.nextInt();
				sc.nextLine();

				switch (menuNum) {
				case 1:
					insertStd();
					break;
				case 2:
					searchAll(); break;
				case 3:
					updateInfo(); break;
				case 4:
					deleteStd(); break;
				case 5:
					searchByMajor(); break;
				case 0:
					System.out.println("\n프로그램을 종료합니다.\n");
					break;
				default:
					System.out.println("\n메뉴에 있는 번호만 입력하세요.\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} while (menuNum != 0);
	}

	public void insertStd() throws Exception {
		System.out.println("\n=== 1. 학생 등록===\n");
		System.out.print("이름 입력 : ");
		String inputName = sc.next();

		System.out.print("나이 입력 : ");
		int inputAge = sc.nextInt();

		System.out.print("학과 입력 : ");
		String inputDept = sc.next();

		Student student = new Student();
		
		student.setStdName(inputName);
		student.setStdAge(inputAge);
		student.setMajor(inputDept);
		
		// 서비스 호출(INSERT) > 결과 반환 받기(int, 결과 행의 개수)
		// service 객체(StudentService)에 있는 insertStd()라는 이름의 메서드 호출하기
		int result = service.insertStd(student);

		// 반환된 결과에 따라 출력할 내용 선택
		if(result > 0) {
			System.out.println("\n" + inputName + "학생이 추가됐습니다.\n");
		} else {
			System.out.println("\n학생 추가에 실패했습니다.\n");
		}
	}
	
	public void searchAll() throws Exception {
		System.out.println("\n===전체 학생 조회===\n");
		List<Student> stdList = new ArrayList<>(); 
		
		// 서비스 호출(SELECT) > 결과 반환 받기(stdList)
		stdList = service.selectAll();
		
		if(stdList.isEmpty()) {
			System.out.println("등록된 학생이 없습니다.");
			return;
		}
		
		for(Student std : stdList) {
			System.out.println(std);
		}
	}
	
	public void updateInfo() throws Exception {
		System.out.println("\n===학생 정보 수정===\n");
		
		System.out.print("수정할 학생 번호 입력 : ");
		int inputNo = sc.nextInt();
		sc.nextLine();
		
		int selectResult = service.selectNo(inputNo);
		
		if (selectResult == 0) {
			System.out.println("\n일치하는 학생 번호가 없습니다.\n");
			return;
		}

		int inputNum = 0;
		do {
			System.out.println("3-1. 학생 이름 수정");
			System.out.println("3-2. 학생 나이 수정");
			System.out.println("3-3. 학생 전공 수정");
			System.out.println("3-0. 수정 종료");
			System.out.print("메뉴 선택 : ");
			inputNum = sc.nextInt();
			sc.nextLine();
			
			switch(inputNum) {
			case 1 : updateName(selectResult); break;
			case 2 : updateAge(selectResult); break;
			case 3 : updateMajor(selectResult); break;
			case 0 : System.out.println("\n수정을 종료합니다.\n"); break;
			default : System.out.println("\n메뉴에 있는 번호만 입력해주세요.\n");
			}
		} while(inputNum != 0);

	}

	private void updateName(int selectResult) throws Exception {
		System.out.println("\n=== 3-1. 학생 이름 수정 ===\n");
		
		System.out.print("수정할 이름 : ");
		String newName = sc.nextLine();
		
		int result = service.updateName(selectResult, newName);
		if (result > 0) {
			System.out.println("\n학생 이름을 수정했습니다.\n");
		} else {
			System.out.println("\n학생 이름 수정에 실패했습니다.\n");
		}
	}
	
	private void updateAge(int selectResult) throws Exception {
		System.out.println("\n=== 3-2. 학생 나이 수정 ===\n");
		
		System.out.print("수정할 나이 : ");
		int newAge = sc.nextInt();
		sc.nextLine();
		
		int result = service.updateAge(selectResult, newAge);
		if (result > 0) {
			System.out.println("\n학생 나이를 수정했습니다.\n");
		} else {
			System.out.println("\n학생 나이 수정에 실패했습니다.\n");
		}
	}

	private void updateMajor(int selectResult) throws Exception {
		System.out.println("\n=== 3-3. 학생 전공 수정 ===\n");
		
		System.out.print("수정할 전공 : ");
		String newMajor = sc.nextLine();
		
		int result = service.updateMajor(selectResult, newMajor);
		if (result > 0) {
			System.out.println("\n학생 전공을 수정했습니다.\n");
		} else {
			System.out.println("\n학생 전공 수정에 실패했습니다.\n");
		}
	}
	
	private void deleteStd() throws Exception {
		System.out.println("\n=== 4. 학생 삭제 ===\n");
		
		System.out.print("삭제할 학생의 학번 : ");
		int inputNo = sc.nextInt();
		
		int result = service.deleteStd(inputNo);
		if (result > 0) {
			System.out.println("\n학생 삭제에 성공했습니다.\n");
		} else {
			System.out.println("\n학생 삭제에 실패했습니다.\n");
		}
	}
	
	private void searchByMajor() throws Exception {
		System.out.println("\n=== 5. 전공별 학생 조회 ===\n");
		
		System.out.print("검색할 전공 : ");
		String inputDept = sc.nextLine();
		
		List<Student> stdList = service.searchByMajor(inputDept);
		
		if (stdList.isEmpty()) {
			System.out.println("\n조회된 학생이 없습니다.\n");
			return;
		}
		
		System.out.println("\n=== "+ inputDept + "의 학생 명단 ===\n");
		for(Student std : stdList) {
			System.out.println(std);
		}
	}
}
