package homework.common;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Scanner;

/**
 * driver.xml을 만들기 위해 > XML 파일을 만들어주는 기능이 있는 클래스 생성
 */
public class createXMLFile {

	public static void main(String[] args) {
		Scanner sc = null;
		FileOutputStream fos = null;
		
		try {
			sc = new Scanner(System.in);
			
			// Properties 객체 생성
			Properties prop = new Properties();
			String fileName = sc.next();

			// FileOutputStream 생성 > 새로운 파일 만들거나 쓰기 모드로 열어 > 스트림 준비
			fos = new FileOutputStream(fileName + ".xml");
			
			// Properties 객체의 데이터를 xml 형식으로 변환하여 파일에 기록 및 생성
			prop.storeToXML(fos, fileName + ".xml 파일");
			
			System.out.println(fileName + ".xml 파일을 생성하였습니다.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(fos != null) fos.close();
				if(sc != null) sc.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
