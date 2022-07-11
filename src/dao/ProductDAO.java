package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import dto.ProductDTO;

public class ProductDAO {
	DBConnection conn = null;
	
	public ProductDAO() {
		conn = new DBConnection("database/ProductTable.txt");
	}
	
	public int getLastNum() {
		String lastPK = conn.lastPK();
		if(lastPK == null) {
			return 0;
		}
		else {
			return Integer.parseInt(lastPK);
		}
	}
	
	public boolean addProduct(ProductDTO product) {
		return conn.insert(product.toString());
	}

	public String getList() {
		//상품 테이블의 6번 열 에는 상품의 주인이 누구인지 아이디로 담겨있다.
		//select()를 통해 6번 열에서 세션의 아이디로 검색을 해온다.(세션이 올린 모든 상품이 담겨온다)
		HashSet<String> rs = conn.select(6,Session.get("login_id"));
		String result = "";
		//1. 구찌 지우개 : 10000원(재고 : 3개)
		//2. 루이비똥 연필 : 10000원(재고 : 3개) 
		
		//rs에서 한 줄씩 꺼내며 반복
		for(String line : rs) {
			//꺼내온 line을 split 해주면 데이터들이 담겨있는 배열로 리턴
			String[] datas = line.split("\t");
			//포매팅 한 후 result에 누적연결
			result += String.format("%s. %s : %s원(재고 : %s개)\n", datas[0],datas[1],datas[2],datas[3]);
		}
		//모든 문자열이 연결된 result 리턴
		return result;
	}

	public boolean removeProduct(int prodnum) {
		return conn.delete(prodnum+"");
	}

	public boolean modifyProduct(int prodnum, int choice, String newData) {
		//상품의 어떤 정보를 수정할 것인지가 choice에 담겨있는데, 그 수정되어야 하는 열 번호는
		//choice+1번 열이다. 따라서 update()에 상품번호와 열 번호, 새로운 정보 세가지를 넘겨주며 수정시킨다.
		return conn.update(prodnum+"", choice+1, newData);
	}

	public void removeAll() {
		HashSet<String> rs = conn.select(6,Session.get("login_id"));
		for(String line : rs) {
			conn.delete(line.split("\t")[0]);
		}
	}

	public String search(String keyword) {
		HashSet<String> rs = conn.select();
		ArrayList<String> result = new ArrayList<String>();
		for(String line : rs) {
			String[] datas = line.split("\t");
			if(datas[1].contains(keyword)) {
				result.add(line);
			}
		}
		
		//"지우개"로 검색된 결과
		String resultMsg = "\""+keyword+"\"로 검색된 결과\n";
		if(result.size() == 0) {
			resultMsg += "해당하는 상품이 없습니다\n";
		}
		else {
			for(String line : result) {
				String[] datas = line.split("\t");
				//포매팅 한 후 result에 누적연결
				resultMsg += String.format("%s. %s : %s원(재고 : %s개) - %s\n", datas[0],
						datas[1],datas[2],datas[3],datas[6]);
			}
		}
		return resultMsg;
		
	}

	public boolean likecntUp(int pro) {
		//기존에 있는 좋아요 개수에서 +1을 해주는 것 이기 때문에 기존의 좋아요 수 부터 구해야한다 
		HashSet<String> like = conn.select(0, pro+"");
		
		String result = "";
		
		for(String line : like) {
			String [] datas = line.split("\t");
			result += datas[5];
		}
		int likecnt = Integer.parseInt(result) + 1;
		
		return conn.update(pro+"", 5, likecnt+"");
	}

	public String getUserId(int pro) {
		HashSet<String> str = conn.select(0,pro+"");
		
		String result = "";
		
		for(String line : str) {
			String [] datas = line.split("\t");
			result += datas[6];
		}
		return result;
	}
}


















