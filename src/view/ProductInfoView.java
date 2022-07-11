package view;

import java.util.Scanner;

import dao.ProductDAO;
import dao.UserDAO;

public class ProductInfoView {
	public ProductInfoView(int pro) {
		Scanner sc = new Scanner(System.in);
		ProductDAO pdao = new ProductDAO();
		UserDAO udao = new UserDAO();
		
		System.out.println("1. 좋아요 누르기/n2. 판매자 연락처 보기/n3. 돌아가기");
		int choice = sc.nextInt();
		
		if(choice == 3) {
			System.out.println("메인으로 돌아갑니다.");
		}else {
			
			switch (choice) {
			case 1:
				if(pdao.likecntUp(pro)) {
					System.out.println("좋아요 누르기 성공~!");
					System.out.println("좋아요는 많이많이 눌러주세요");
				}
				else {
					System.out.println("좋아요 누르기 실패");
				}
				break;
			case 2:
				String userid = pdao.getUserId(pro);
				System.out.println(udao.getPone(userid));
				break;
			}
		}
		
	}
}
