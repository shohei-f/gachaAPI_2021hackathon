package webservices.gacha;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

public class GachaService {


	static List<GachaPropBean> props = new ArrayList<>();


	static BigDecimal pickupProb = new BigDecimal("0.8");

	static BigDecimal incentiveProb = new BigDecimal("0.05");

	static {
		//		// 検証用
		//		GachaPropBean bean1 = new GachaPropBean();
		//		bean1.setRarity(5);
		//		bean1.setProb(new BigDecimal ("0.1"));
		//
		//		GachaMasterBean master51 = new GachaMasterBean();
		//		master51.setId(5001);
		//		master51.setName("唐揚げ");
		//
		//		GachaMasterBean master52 = new GachaMasterBean();
		//		master52.setId(5002);
		//		master52.setName("ビール");
		//
		//		GachaMasterBean master53 = new GachaMasterBean();
		//		master53.setId(5003);
		//		master53.setName("焼き鳥");
		//
		//		GachaMasterBean master54 = new GachaMasterBean();
		//		master54.setId(5004);
		//		master54.setName("炒飯");
		//
		//		GachaMasterBean master55 = new GachaMasterBean();
		//		master55.setId(5005);
		//		master55.setName("餃子");
		//
		//		GachaMasterBean master56 = new GachaMasterBean();
		//		master56.setId(5006);
		//		master56.setName("ストロングゼロ");
		//
		//		GachaMasterBean master57 = new GachaMasterBean();
		//		master57.setId(5007);
		//		master57.setName("ピザ");
		//
		//		GachaMasterBean master58 = new GachaMasterBean();
		//		master58.setId(5008);
		//		master58.setName("オムライス");
		//
		//		bean1.setIds(Arrays.asList(master51, master52, master53, master54,
		//				master55, master56, master57, master58));
		//
		//		bean1.setPickups(Arrays.asList(5001, 5002, 5003));
		//
		//		GachaPropBean bean2 = new GachaPropBean();
		//		bean2.setRarity(4);
		//		bean2.setProb(new BigDecimal ("0.3"));
		//
		//		GachaMasterBean master41 = new GachaMasterBean();
		//		master41.setId(4001);
		//		master41.setName("発泡酒");
		//
		//		GachaMasterBean master42 = new GachaMasterBean();
		//		master42.setId(4002);
		//		master42.setName("酎ハイ");
		//
		//		GachaMasterBean master43 = new GachaMasterBean();
		//		master43.setId(4003);
		//		master43.setName("冷奴");
		//
		//		GachaMasterBean master44 = new GachaMasterBean();
		//		master44.setId(4004);
		//		master44.setName("青椒肉絲");
		//
		//		GachaMasterBean master45 = new GachaMasterBean();
		//		master45.setId(4005);
		//		master45.setName("レバニラ炒め");
		//
		//		GachaMasterBean master46 = new GachaMasterBean();
		//		master46.setId(4006);
		//		master46.setName("ハイボール");
		//
		//		GachaMasterBean master47 = new GachaMasterBean();
		//		master47.setId(4007);
		//		master47.setName("エビフライ");
		//
		//		GachaMasterBean master48 = new GachaMasterBean();
		//		master48.setId(4008);
		//		master48.setName("フライドポテト");
		//
		//		bean2.setIds(Arrays.asList(master41, master42, master43, master44,
		//				master45, master46, master47, master48));
		//
		//		bean2.setPickups(Arrays.asList(4001, 4002));
		//
		//		GachaPropBean bean3 = new GachaPropBean();
		//		bean3.setRarity(3);
		//		bean3.setProb(new BigDecimal ("0.6"));
		//
		//		GachaMasterBean master31 = new GachaMasterBean();
		//		master31.setId(3001);
		//		master31.setName("ポテサラ");
		//
		//		GachaMasterBean master32 = new GachaMasterBean();
		//		master32.setId(3002);
		//		master32.setName("サラダ");
		//
		//		GachaMasterBean master33 = new GachaMasterBean();
		//		master33.setId(3003);
		//		master33.setName("お茶");
		//
		//		GachaMasterBean master34 = new GachaMasterBean();
		//		master34.setId(3004);
		//		master34.setName("メンマ");
		//
		//		GachaMasterBean master35 = new GachaMasterBean();
		//		master35.setId(3005);
		//		master35.setName("鶏ガラスープ");
		//
		//		GachaMasterBean master36 = new GachaMasterBean();
		//		master36.setId(3006);
		//		master36.setName("ハイボール");
		//
		//		bean3.setIds(Arrays.asList(master31, master32, master33, master34,
		//				master35, master36));
		//
		//		bean3.setPickups(Arrays.asList(3001, 3002));
		//
		//
		//		props.add(bean1);
		//		props.add(bean2);
		//		props.add(bean3);
	}

	public GachaMasterBean pick1(BigDecimal rval, String userid) {

		GachaDao dao = new GachaDao();
		Connection conn = dao.open();
		Map<Integer, List<Integer>> userPickInfo = dao.getUserPickup(conn, userid);
		props = dao.getItems(conn, userPickInfo);
		dao.close(conn);

		Map<GachaMasterBean, BigDecimal> table = createTable(BigDecimal.ZERO);

		BigDecimal accum = BigDecimal.ZERO;
		for(Entry<GachaMasterBean, BigDecimal> record : table.entrySet()) {
			accum = accum.add(record.getValue());
			if (rval.compareTo(accum) < 0) {
				System.out.println("乱数:" + rval);
				return record.getKey();
			}
		}

		return null;
	}

	public List<GachaMasterBean> pick(String userid, int times, BigDecimal incentive) {

		List<BigDecimal> rvals = new ArrayList<>();

		// ガチャを引く回数分乱数を生成
		for (int i = 0; i < times; i++) {
			rvals.add(new BigDecimal(Math.random()));
		}

		List<GachaMasterBean> result = new ArrayList<>();

		// DBから情報取得
		GachaDao dao = new GachaDao();
		Connection conn = dao.open();
		Map<Integer, List<Integer>> userPickInfo = dao.getUserPickup(conn, userid);
		props = dao.getItems(conn, userPickInfo);
		dao.close(conn);

		// 排出テーブル作成
		Map<GachaMasterBean, BigDecimal> table = createTable(incentive);

		// ガチャ排出
		for (BigDecimal rval : rvals) {
			BigDecimal accum = BigDecimal.ZERO;
			for(Entry<GachaMasterBean, BigDecimal> record : table.entrySet()) {
				accum = accum.add(record.getValue());
				if (rval.compareTo(accum) < 0) {
					result.add(record.getKey());
					break;
				}
			}
		}
		return result;
	}

	private Map<GachaMasterBean, BigDecimal> createTable(BigDecimal incentive) {

		Map<GachaMasterBean, BigDecimal> table = new LinkedHashMap<>();

		// 排出率のインセンティブ設定
		setIncentive(incentive);

		// レア度ごとの処理
		for(GachaPropBean prop : props) {

			// 非ピックアップ排出率
			BigDecimal nonPickProb = BigDecimal.ZERO;

			// ピックアップが存在する場合
			if(prop.getPickups() != null && prop.getPickups().size() != 0) {

				System.out.println("★レア度" + prop.getRarity());

				nonPickProb = BigDecimal.ONE.add(pickupProb.negate());

				System.out.println(prop.getProb() + "*" + nonPickProb + "/" + prop.getIds().size() + "-" + prop.getPickups().size());

				// レア度ごとの非ピックアップの排出率
				if (prop.getIds().size() - prop.getPickups().size() == 0) {
					nonPickProb = prop.getProb().divide(new BigDecimal(prop.getIds().size()), 6, RoundingMode.CEILING);
				} else {
					nonPickProb = prop.getProb().multiply(nonPickProb).
							divide(new BigDecimal(prop.getIds().size() - prop.getPickups().size()), 6, RoundingMode.CEILING);
				}
				System.out.println("=" + nonPickProb);

			}
			// レア度ごとのアイテム１つ１つの確率をテーブルに設定
			for (GachaMasterBean master : prop.getIds()) {
				// 排出率
				BigDecimal prob = BigDecimal.ZERO;
				// ピックアップが存在する場合
				if(prop.getPickups() != null && prop.getPickups().size() != 0) {
					// ピックアップ対象判定
					Optional<Integer> pickup = prop.getPickups().stream().
							filter(e -> master.getId().equals(e)).findFirst();
					// ピックアップ対象の場合
					if(pickup.isPresent() && prop.getIds().size() - prop.getPickups().size() != 0) {
						prob = prop.getProb().multiply(pickupProb).divide(new BigDecimal(prop.getPickups().size()), 6, RoundingMode.CEILING);
					}
					// 上記以外
					else {
						prob = nonPickProb;
					}
				}
				// ピックアップが存在しない場合
				else {
					prob = prop.getProb().divide(new BigDecimal(prop.getIds().size()),6, RoundingMode.CEILING);
				}
				// 排出テーブル作成
				table.put(master, prob);
			}
		}
		System.out.println(table);

		BigDecimal sum = BigDecimal.ZERO;
		for(Entry<GachaMasterBean, BigDecimal> record : table.entrySet()) {
			System.out.println(record);
			sum = sum.add(record.getValue());
		}
		System.out.println("確率の合計:" + sum);

		return table;
	}

	private void setIncentive(BigDecimal incentive) {

		// 排出率の合計
		BigDecimal sum = BigDecimal.ZERO;

		// 最低レアの確率の下限を設定
		// 最低レアの確率 / インセンティブ係数 * (レア度の数 - 1)
		BigDecimal kagen = props.get(props.size() - 1).getProb().divide(
				incentiveProb.multiply(new BigDecimal(props.size() - 1)));

		System.out.println(kagen);


		// レア度ごとの処理
		for(int i = 1; i <= Math.min(props.size(), kagen.intValue() - 1); i++) {

			GachaPropBean prop = props.get(i - 1);

			// 最低レアでない場合
			if (i < props.size()) {
				// レア度ごとの排出率 ← レア度ごとの排出率 + (インセンティブ排出率 * インセンティブ係数)
				prop.setProb(prop.getProb().add(incentive.multiply(incentiveProb)));
				// 合計値更新
				sum = sum.add(prop.getProb());
			}
			// 最低レアの場合
			else {
				// レア度ごとの排出率 ← 1 - 各排出率の合計
				prop.setProb(BigDecimal.ONE.add(sum.negate()));
			}
		}
	}

}
