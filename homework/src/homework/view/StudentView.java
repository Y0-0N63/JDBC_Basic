package homework.view;

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

				switch (menuNum) {
				case 1:
					insertStd();
					break;
				case 2:
					/* searchAll(); */ break;
				case 3:
					/* updateInfo(); */ break;
				case 4:
					/* deleteStd(); */ break;
				case 5:
					/* searchByDept(); */ break;
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
			System.out.println("\n" + inputName + "학생이 추가되었습니다.\n");
		} else {
			System.out.println("\n학생 추가에 실패하였습니다.\n");
		}
	}
}
