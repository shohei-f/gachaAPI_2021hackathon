package webservices.gacha;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class GachaDao {

	public Connection open() {
		try {
			return DriverManager.getConnection(
					// ホスト名、データベース名
					"XXXXXXXXXXXXXXXXXXXXXXXX",
					// ユーザー名
					"XXXXXXXXXXXXXXXXXXXXXXXX",
					// パスワード
					"XXXXXXXXXXXXXXXXXXXXXXXX");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void close(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Map<Integer, List<Integer>> getUserPickup(Connection conn, String id) {

		Map<Integer, List<Integer>> result = new HashMap<>();

		try(
				PreparedStatement stmt = conn.prepareStatement(""
						+ "SELECT rarity, Item.id AS id "
						+ "FROM Item, (select * from User WHERE id = ?) AS user "
						+ "WHERE Item.type in (user.type, 0) OR "
						+ "Item.kind  in (user.kind, 0) "
						+ "ORDER BY rarity DESC, id;"
						);
				) {

			stmt.setString(1, id);

			try (ResultSet rs = stmt.executeQuery();) {
				int tmp = -1;
				List<Integer> list = new ArrayList<>();

				// 取得できた場合
				while(rs.next()) {

					// レア度取得
					int rarity = rs.getInt("rarity");

					if (tmp == -1) {
						tmp = rarity;
						list.add(rs.getInt("id"));
					}else if (tmp != rarity) {
						result.put(tmp, list);
						tmp = rarity;
						list = new ArrayList<>();
						list.add(rs.getInt("id"));
					} else {
						list.add(rs.getInt("id"));
					}
					result.put(tmp, list);
				}
				System.out.println(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<GachaPropBean> getItems(Connection conn, Map<Integer, List<Integer>> userPickInfo) {

		List<GachaPropBean> result = new ArrayList<>();

		try(
				PreparedStatement stmt = conn.prepareStatement(""
						+ "SELECT id, name, pic, Rarity.rarity, prop "
						+ "FROM Item, Rarity "
						+ "WHERE Item.rarity = Rarity.rarity "
						+ "ORDER BY Rarity.rarity DESC, id;"
						);
				ResultSet rs = stmt.executeQuery();
				) {

			int tmp = -1;

			GachaMasterBean item = new GachaMasterBean();
			List<GachaMasterBean> ids = new ArrayList<>();
			GachaPropBean prop = new GachaPropBean();

			// 取得できた場合
			while(rs.next()) {

				// レア度取得
				int rarity = rs.getInt("rarity");

				if (tmp == -1) {
					prop.setProb(rs.getBigDecimal("prop"));
					prop.setRarity(rarity);
					prop.setPickups(userPickInfo.get(rarity));
					tmp = rarity;
					item = new GachaMasterBean();
					item.setId(rs.getInt("id"));
					item.setName(rs.getString("name"));
					item.setPic(rs.getString("pic"));
					ids.add(item);
				} else if (tmp != rarity) {
					prop.setIds(ids);
					result.add(prop);
					tmp = rarity;
					ids = new ArrayList<>();
					item = new GachaMasterBean();
					prop = new GachaPropBean();
					prop.setProb(rs.getBigDecimal("prop"));
					prop.setRarity(rarity);
					prop.setPickups(userPickInfo.get(rarity));
					item.setId(rs.getInt("id"));
					item.setName(rs.getString("name"));
					item.setPic(rs.getString("pic"));
					ids.add(item);
				} else {
					item = new GachaMasterBean();
					item.setId(rs.getInt("id"));
					item.setName(rs.getString("name"));
					item.setPic(rs.getString("pic"));
					ids.add(item);
				}
			}
			prop.setIds(ids);
			result.add(prop);

		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}


		return result;
	}


	public static void main(String[] args) {

		GachaDao dao = new GachaDao();
		Connection conn = dao.open();
		Map<Integer, List<Integer>> userPickInfo = dao.getUserPickup(conn, "123456");
		List<GachaPropBean> gachaPropBean = dao.getItems(conn, userPickInfo);
		dao.close(conn);

		System.out.println(gachaPropBean);
	}

}
