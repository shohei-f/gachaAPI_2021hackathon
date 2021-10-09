package webservices.gacha;

public class GachaMasterBean {

	/** id */
	private Integer id;
	/** 名前 */
	private String name;
	/** 画像 */
	private String pic;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	@Override
	public String toString() {
		return "GachaMasterBean [id=" + id + ", name=" + name + ", pic=" + pic + "]";
	}

}
