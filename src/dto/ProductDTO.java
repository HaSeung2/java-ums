package dto;

public class ProductDTO {
	int prodnum;
	String prodname;
	int prodprice;
	int prodamount;
	String prodinfo;
	int likecnt;
	
	//어떤 유저가 올린 상품인지를 같이 저장해야 하므로 외부의 테이블과 연결이 필요하다.
	String userid;	//FK(Foreign Key)

	public ProductDTO(int prodnum, String prodname, int prodprice, int prodamount, String prodinfo,
			String userid) {
		this.prodnum = prodnum;
		this.prodname = prodname;
		this.prodprice = prodprice;
		this.prodamount = prodamount;
		this.prodinfo = prodinfo;
		this.likecnt = 0;
		this.userid = userid;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ProductDTO) {
			ProductDTO target = (ProductDTO)obj;
			return target.hashCode() == this.hashCode();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return prodnum;
	}
	
	@Override
	public String toString() {
		return prodnum+"\t"+prodname+"\t"+prodprice+"\t"+
	prodamount+"\t"+prodinfo+"\t"+likecnt+"\t"+userid;
	}
}











