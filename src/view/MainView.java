package view;

import java.util.Scanner;

import dao.ProductDAO;
import dao.ProductDAO;
import dao.Session;

public class MainView {
	public MainView() {
		Scanner sc = new Scanner(System.in);
		ProductDAO pdao = new ProductDAO();
		
		while(true) {
			//우리가 만든 프로젝트는 무조건 main()부터 시작하는 프로그램이다.
			//즉 진입점이 한개이기 때문에 아래와 같은 코드를 생략 가능하다.
			//*웹 상이나 다른 프로그램에서는 진입점이 여러개일 수 있으므로
			//아래처럼 로그인 되어있는지를 먼저 검사해주는것이 필요하다.
			if(Session.get("login_id") == null) {
				System.out.println("로그인 후 이용하세요");
				break;
			}
			System.out.println("☆★☆★☆★☆★"+Session.get("login_id")+"님 어서오세요~☆★☆★☆★☆★\n"
					+ "1. 상품추가\n2. 상품수정\n3. 상품삭제\n"
					+ "4. 내 상품 보기\n5. 상품 검색\n6. 내 정보 수정\n7. 로그아웃");
			int choice = sc.nextInt();
			
			if(choice == 7) {
				//로그아웃
				System.out.println(Session.get("login_id")+"님 안녕히가세요~");
				//로그인한 정보를 담아주는 Session에서 login_id라는 KEY 와 쌍을 이루고 있는 값은
				//로그아웃을 했다면 없애주어야 한다. 따라서 null로 초기화 해주어야 한다.
				Session.put("login_id", null);
				break;
			}
			switch(choice) {
			case 1:
				//상품추가
				new AddProductView();
				break;
			case 2:
				//상품수정
				new ModifyProductView();
				break;
			case 3:
				//상품삭제
				//내 상품 목록 띄워주고 번호 입력받아서 삭제시키기
				System.out.println(pdao.getList());
				System.out.print("삭제할 상품 번호 : ");
				int prodnum = sc.nextInt();
				if(pdao.removeProduct(prodnum)) {
					System.out.println(prodnum+"번 상품 삭제 성공!");
				}
				else {
					System.out.println("알 수 없는 오류 / 다음에 다시 시도해 주세요.");
				}
				break;
			case 4:
				//내 상품 보기
				System.out.println("===========내가 올린 상품 목록===========");
				System.out.println(pdao.getList());
				System.out.println("===================================");
				break;
			case 5:
				//상품 검색
				System.out.print("검색어를 입력하세요 : ");
				sc = new Scanner(System.in);
				String keyword = sc.nextLine();
				System.out.println(pdao.search(keyword));
				System.out.print("자세히 볼 상품 번호 : ");
				int pro = sc.nextInt();
				new ProductInfoView(pro);				
				//자세히 볼 상품번호를 선택했다면 그 번호에 해당하는 상품 정보를 띄워주는 ProductInfoView 만들기!
				//내부메뉴
				//1. 좋아요 누르기
				//2. 판매자 연락처 보기
				//3. 돌아가기
				break;
			case 6:
				//내 정보 수정
				new MyInfoView();
				break;
			}
			
		}
	}
}









