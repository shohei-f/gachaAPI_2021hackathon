package webservices.gacha;

import java.math.BigDecimal;
import java.util.List;

public class GachaPropBean {

	/** レア度 */
	private int rarity;
	/** 排出率 */
	private BigDecimal prob;
	/** 排出カード */
	private List<GachaMasterBean> ids;
	/** ピックアップ */
	private List<Integer> pickups;

	public int getRarity() {
		return rarity;
	}
	public void setRarity(int rarity) {
		this.rarity = rarity;
	}
	public BigDecimal getProb() {
		return prob;
	}
	public void setProb(BigDecimal prob) {
		this.prob = prob;
	}
	public List<GachaMasterBean> getIds() {
		return ids;
	}
	public void setIds(List<GachaMasterBean> ids) {
		this.ids = ids;
	}
	public List<Integer> getPickups() {
		return pickups;
	}
	public void setPickups(List<Integer> pickups) {
		this.pickups = pickups;
	}
	@Override
	public String toString() {
		return "GachaPropBean [rarity=" + rarity + ", prob=" + prob + ", ids=" + ids + ", pickups=" + pickups + "]";
	}




}
