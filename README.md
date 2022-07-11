# java-ums
### view / index
package view;
```java
import java.util.Scanner;

//index : 시작하는 페이지
public class Index {
	public static void main(String[] args) {
		System.out.println("22.03 개강반 최종 프로젝트 / UMS 프로그램 입니다.");
		
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.println("1. 회원가입\n2. 로그인\n3. 나가기");
			int choice = sc.nextInt();
			
			//Controller
			if(choice == 3) {
				System.out.println("안녕히가세요");
				break;
			}
			switch(choice) {
			case 1:
				//회원가입
				//많은 데이터들의 입출력이 일어나기 때문에 코드가 길어진다.
				//따라서 새로운 View를 띄워준다.(흐름을 이동시킨다)
				new JoinView();
				break;
			case 2:
				//로그인
				new LoginView();
				break;
			default:
				System.out.println("다시 입력하세요");
			}
		}
	}
}
```
### view / joinView
```java
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
```
### view / loginView
```java
package view;

import java.util.Scanner;

import dao.UserDAO;

public class LoginView {
	public LoginView() {
		Scanner sc = new Scanner(System.in);
        //UserDAO에 있는 login메소드를 사용하기 위해 UserDAO를 객체화
		UserDAO udao = new UserDAO();

		System.out.print("아이디 : ");
		String userid = sc.next();
		System.out.print("비밀번호 : ");
		String userpw = sc.next();
		
		if(udao.login(userid,userpw)) {
			System.out.println(userid+"님 어서오세요~");
			//성공하였다면 Session에 로그인 정보를 담아주고 MainView로 넘겨준다.
			//메인창 띄우기
			new MainView();
		}
		else {
			System.out.println("로그인 실패 / 다시 시도해 주세요.");
		}
	}
}
```
### DTO / userDTO
```java
package dto;

public class UserDTO {
	//Alt + Shift + A : 그리드 편집 모드(여러줄 동시에 편집)
	public String userid;
	public String userpw;
	public String username;
	public int userage;
	public String userphone;
	public String useraddr;
	
	public UserDTO(String userid, String userpw, String username, int userage, String userphone, String useraddr) {
		this.userid = userid;
		this.userpw = userpw;
		this.username = username;
		this.userage = userage;
		this.userphone = userphone;
		this.useraddr = useraddr;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof UserDTO) {
			UserDTO target = (UserDTO)obj;
			
			if(target.userid.equals(this.userid)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		//apple	abcd1234	이하승	23	01012341234	서울시 강남구 역삼동
		return userid+"\t"+userpw+"\t"+username+
				"\t"+userage+"\t"+userphone+"\t"+useraddr;
	}
}
```
### DAO / userDAO
```java
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
}
```
### DBConnection
```java
package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class DBConnection {
	String file;
	
	public DBConnection(String file) {
		this.file = file;
	}
	
	boolean insert(String data) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
			bw.write(data+"\r\n");
			bw.close();
			return true;
		} catch (IOException e) {
			System.out.println("======오류 발생 : DB 연결 실패======");
			System.out.println(e);
			System.out.println("===============================");
		}
		return false;
	}
	boolean update(String key,int col,String newData) {
		String result = "";
		boolean check = false;
		try {
			BufferedReader br =  new BufferedReader(new FileReader(file));
			while(true) {
				String line = br.readLine();
				if(line ==null) {
					break;
				}
				String[] datas=line.split("\t");
				if(datas[0].equals(key)) {
					result+=datas[0];
					for (int i = 1; i < datas.length; i++) {
						if(i==col) {
							result+="\t"+newData;
							check = true;
						}else {
							result+="\t"+datas[i];
						}
					}
					result+="\r\n";
				}else {
					result+=line+"\r\n";
				}
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("======오류 발생 : DB 파일 오류======");
			System.out.println(e);
			System.out.println("===============================");
		} catch (IOException e) {
			System.out.println("======오류 발생 : DB 연결 실패======");
			System.out.println(e);
			System.out.println("===============================");
		}	
		if(check) {
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(file));
				bw.write(result);
				bw.close();
			} catch (IOException e) {
				System.out.println("======오류 발생 : DB 연결 실패======");
				System.out.println(e);
				System.out.println("===============================");
			}
		}
		return check;
	}
	boolean delete(String key) {
		String result = "";
		boolean check = false;
		try {
			BufferedReader br =  new BufferedReader(new FileReader(file));
			while(true) {
				String line = br.readLine();
				if(line ==null) {
					break;
				}
				String[] datas=line.split("\t");
				if(datas[0].equals(key)) {
					check = true;				
				}else {
					result+=line+"\r\n";
				}
			}
			
		}catch (FileNotFoundException e) {
			System.out.println("======오류 발생 : DB 파일 오류======");
			System.out.println(e);
			System.out.println("===============================");
		} catch (IOException e) {
			System.out.println("======오류 발생 : DB 연결 실패======");
			System.out.println(e);
			System.out.println("===============================");
		}	
		if(check) {
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(file));
				bw.write(result);
				bw.close();
			} catch (IOException e) {
				System.out.println("======오류 발생 : DB 연결 실패======");
				System.out.println(e);
				System.out.println("===============================");
			}
		}
		return check;
	}
	//"file" 안에 있는 모든 정보 검색
	HashSet<String> select() {
		HashSet<String> resultSet = new HashSet<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while(true) {
				String line = br.readLine();
				if(line==null) {
					break;
				}
				resultSet.add(line);
			}
		} catch (FileNotFoundException e) {
			System.out.println("======오류 발생 : DB 파일 오류======");
			System.out.println(e);
			System.out.println("===============================");
		} catch (IOException e) {
			System.out.println("======오류 발생 : DB 연결 실패======");
			System.out.println(e);
			System.out.println("===============================");
		}	
		return resultSet;
	}
	HashSet<String> select(int col,String data) {
		HashSet<String> resultSet = new HashSet<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while(true) {
				String line = br.readLine();
				if(line==null) {
					break;
				}
				String[] datas = line.split("\t");
				if(datas[col].equals(data)) {
					resultSet.add(line);//apple	abcd1234	김사과	10	01012341234	서울시 강남구 역삼동
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("======오류 발생 : DB 파일 오류======");
			System.out.println(e);
			System.out.println("===============================");
		} catch (IOException e) {
			System.out.println("======오류 발생 : DB 연결 실패======");
			System.out.println(e);
			System.out.println("===============================");
		}	
		return resultSet;
	}
	String lastPK() {
		String pk = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while(true) {
				String line = br.readLine();
				if(line==null) {
					break;
				}
				pk=line.split("\t")[0];
			}
		} catch (FileNotFoundException e) {
			System.out.println("======오류 발생 : DB 파일 오류======");
			System.out.println(e);
			System.out.println("===============================");
		} catch (IOException e) {
			System.out.println("======오류 발생 : DB 연결 실패======");
			System.out.println(e);
			System.out.println("===============================");
		}
		return pk;
	}
}
```
### Session
```java
package dao;

import java.util.HashMap;

public class Session {
	//Session.datas.put("login_id","apple");
	private static HashMap<String, String> datas = new HashMap<String, String>();

	
	//Session.put("login_id","apple");
	public static void put(String key, String value) {
		datas.put(key, value);
	}
	public static String get(String key) {
		return datas.get(key);
	}
}
```
