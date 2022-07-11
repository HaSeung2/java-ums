package view;

import java.util.Scanner;

import dao.UserDAO;
import dto.UserDTO;

public class JoinView {
	public JoinView() {
		Scanner sc = new Scanner(System.in);
		//새로 가입할 "회원의 정보들" 을 데이터베이스에 저장해야 하므로
		//기능을 구현해 놓은 DAO가 필요하고, 회원 관련된 데이터이므로
		//UserDAO를 사용해야 한다.
		UserDAO udao = new UserDAO();
		
		System.out.print("아이디 : ");
		String userid = sc.next();
		if(udao.checkDup(userid)) {
			System.out.print("비밀번호 : ");
			String userpw = sc.next();
			System.out.print("이름 : ");
			String username = sc.next();
			System.out.print("나이 : ");
			int userage = sc.nextInt();
			System.out.print("핸드폰 번호 : ");
			String userphone = sc.next();
			System.out.print("주소 : ");
			sc = new Scanner(System.in);
			String useraddr = sc.nextLine();

			//여기까지 왔다면 회원 가입에 필요한 모든 정보를 입력받았다는
			//뜻이므로, UserTable에 이 모든 데이터들을 저장해 주어야 한다.
			//저장하는 기능적인 코드들은 View에서 구현하는 것이 아니고
			//udao에 구현해 놓고 메소드를 호출하는 식으로 사용한다.
			//호출시 넘겨주어야 할 데이터들이 총 6개나 되므로 UserDTO 타입의
			//객체 하나로 포장해서 그 객체를 매개변수에 넘겨준다.
			UserDTO user = new UserDTO(userid, userpw, username, userage, userphone, useraddr);
			//위에서 포장된 DTO 객체를 넘겨준다.
			if(udao.join(user)) {
				System.out.println("회원가입 성공!");
				System.out.println(username+"님 가입을 환영합니다!");
			}
			else {
				System.out.println("회원가입 실패 / 다시 시도해 주세요.");
			}
		}
		else {
			System.out.println("중복된 아이디가 있습니다. 다시 시도해 주세요.");
		}
	}
}







