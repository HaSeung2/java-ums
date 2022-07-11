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
		//apple	abcd1234	김사과	10	01012341234	서울시 강남구 역삼동
		return userid+"\t"+userpw+"\t"+username+
				"\t"+userage+"\t"+userphone+"\t"+useraddr;
	}
}













