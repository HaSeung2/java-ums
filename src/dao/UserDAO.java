package dao;

import java.util.HashSet;

import dto.UserDTO;

public class UserDAO {
	
	DBConnection conn = null;
	
	public UserDAO() {
		//UserDAO 클래스는 User에 관한 데이터만 관리하는 클래스 이므로
		//접근할 파일은 "database/UserTable.txt" 이다.
		//객체가 생성되자 마자 conn을 그 파일로 잡아준다.
		conn = new DBConnection("database/UserTable.txt");
	}
	
	public boolean join(UserDTO user) {
		//회원 가입은 새로운 데이터를 추가하는 것이므로
		//conn에 있는 insert()를 이용한다.
		return conn.insert(user.toString());
	}

	public boolean checkDup(String userid) {
		//기존에 가입되어 있는 유저들의 id를 검사하는 것이므로,
		//select()를 이용해서 데이터를 검색해온다.
		//UserTable.txt 에는 0번방이 userid들이 담겨있는 열 이므로
		//0번에서 내가 입력한 userid가 존재하는지를 검색한다.
		HashSet<String> rs = conn.select(0,userid);
		//검색된 결과가 0개라면 중복되지 않는다는 뜻이므로 true를 리턴
		return rs.size() == 0;
	}

	//							apple		 abcd1234
	public boolean login(String userid, String userpw) {
		//우리가 입력받아서 넘겨준 userid를 UserTable의 0번방에서 검색
		HashSet<String> rs = conn.select(0, userid);
		//아이디는 중복이 불가능하므로 검색된 결과는 1개 아니면 0개이다.
		//1개가 있다면 제대로 입력을 했을 때이다.
		if(rs.size() == 1) {
			//rs에서 안에 있는 line 한 줄을 꺼내오면서 반복
			for(String line : rs) {
				//line : "apple	abcd1234	김사과	10	01012341234	서울시 강남구 역삼동"
				//"문자열1".split("문자열2") : "문자열1" 을 "문자열2" 기준으로 나누기
				// 						나뉘어진 문자열들이 담겨있는 배열 return
				//{"apple","abcd1234","김사과","10","01012341234","서울시 강남구 역삼동"}
				//1번방에 있는 비밀번호가 내가 입력받아서 넘겨준 userpw와 같은지를 비교
				if(line.split("\t")[1].equals(userpw)) {
					//로그인 성공
//					Session.datas.put("login_id",userid);
					//로그인 된 정보를 저장하는 Session에 세팅
					//KEY : "login_id" / VALUE : "apple"(로그인 성공한 유저의 id)
					Session.put("login_id", userid);
					return true;
				}
				
			}
		}
		return false;
	}

	public String myInfo() {
		HashSet<String> rs = conn.select(0, Session.get("login_id"));
		String result = "";
		
		for(String line : rs) {
			String[] datas = line.split("\t");
			result += "======"+datas[2]+"님의 회원정보======\n";
			result += "아이디 : "+datas[0]+"\n"; 
			result += "비밀번호 : "+datas[1]+"\n"; 
			result += "핸드폰 번호 : "+datas[4]+"\n"; 
			result += "주소 : "+datas[5]+"\n"; 
		}
		return result;
	}

	public boolean modifyUser(int col, String newData) {
		//회원 정보 수정은 현재 로그인 된 유저의 정보를 수정해야 하므로 세션의 "login_id"로 저장된 것을 키값으로 넘겨준다.
		return conn.update(Session.get("login_id"), col, newData);
	}

	public boolean checkPw(String userpw) {
		HashSet<String> rs = conn.select(0,Session.get("login_id"));
		for(String line : rs) {
			return line.split("\t")[1].equals(userpw);
		}
		return false;
	}

	public boolean leaveId() {
		//탈퇴 진행시 현재 로그인한 유저의 아이디가 필요하므로 아래서 초기화 하기 전에 미리 백업 
		String userid = Session.get("login_id");
		//회원이 탈퇴되었으므로 로그인 유지 불가능, Session을 초기화 해준다.
		Session.put("login_id", null);
		return conn.delete(userid);
	}

	public String getPone(String userid) {
		HashSet<String> str = conn.select(0, userid);
		String result = "";
		
		for(String line : str) {
			String [] datas = line.split("\t");
			
			result += "판매자 연락처 : "+ datas[4];
		}
		return result;
	}
}










