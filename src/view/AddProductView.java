package view;

import java.util.Scanner;

import dao.ProductDAO;
import dao.Session;
import dto.ProductDTO;

public class AddProductView {
	public AddProductView() {
		//상품에 관련된 정보에 접근할 객체인 pdao 생성(상품 관련된 기능들이 선언되어 있다.)
		ProductDAO pdao = new ProductDAO();
		Scanner sc = new Scanner(System.in);
		
		System.out.print("상품 이름 : ");
		String prodname = sc.nextLine();
		System.out.print("상품 가격 : ");
		int prodprice = sc.nextInt();
		System.out.print("상품 재고 : ");
		int prodamount = sc.nextInt();
		System.out.print("상품 소개 : ");
		sc = new Scanner(System.in);
		String prodinfo = sc.nextLine();
		
		//지금 추가중인 상품의 상품번호는 현재 "추가되어 있는 마지막 상품의 번호"+1로 설정해 주어야 한다.
		//pdao에 getLastNum()을 만들어서 리턴받고 +1 해준 것을 상품 번호로 사용한다.
		ProductDTO product = new ProductDTO(pdao.getLastNum()+1, prodname, prodprice,
				prodamount, prodinfo, Session.get("login_id"));//올린 사람은 현재
		//								로그인 되어 있는 유저이므로 세션에서 아이디를 받아온 후 사용
		
		//포장이 끝났다면 pdao의 메소드에 객체 넘겨주기
		if(pdao.addProduct(product)) {
			System.out.println(prodname+" 상품 추가 완료!");
		}
		else {
			System.out.println("알 수 없는 오류 / 다음에 다시 시도해 주세요.");
		}
	}
}














