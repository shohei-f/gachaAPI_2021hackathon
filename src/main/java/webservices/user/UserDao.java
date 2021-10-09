package webservices.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserDao {

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

	public UserBean saveUser(Connection conn, UserBean user) {

		try(
				PreparedStatement stmt = conn.prepareStatement("insert into User values (?, ?, ?, ?, ?, ?,?);");
				) {

			stmt.setString(1, user.getId());
			stmt.setString(2, user.getName());
			stmt.setBigDecimal(3, user.getLat());
			stmt.setBigDecimal(4, user.getLng());
			stmt.setInt(5, user.getScore());
			stmt.setInt(6, user.getType());
			stmt.setInt(7, user.getKind());

			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	public UserBean selUser(Connection conn, String id) {

		UserBean user = null;

		try(
				PreparedStatement stmt = conn.prepareStatement("select * from User where id = ?;");
				) {

			stmt.setString(1, id);

			try (ResultSet rs = stmt.executeQuery();) {

				// 取得できた場合
				if(rs.next()) {

					user = new UserBean();

					user.setId(rs.getString("id"));
					user.setName(rs.getString("name"));
					user.setLat(rs.getBigDecimal("lat"));
					user.setLng(rs.getBigDecimal("lng"));
					user.setScore(rs.getInt("score"));
					user.setKind(rs.getInt("kind"));
					user.setType(rs.getInt("type"));
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	public UserBean updUser(Connection conn, UserBean user) {

		try(
				PreparedStatement stmt = conn.prepareStatement(""
						+ "update User set "
						+ "name = ?, "
						+ "lat = ?, "
						+ "lng = ?, "
						+ "score = ?, "
						+ "type = ?, "
						+ "kind = ? "
						+ "where id = ?;");
				) {

			stmt.setString(1, user.getName());
			stmt.setBigDecimal(2, user.getLat());
			stmt.setBigDecimal(3, user.getLng());
			stmt.setInt(4, user.getScore());
			stmt.setInt(5, user.getType());
			stmt.setInt(6, user.getKind());
			stmt.setString(7, user.getId());

			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	public UserBean delUser(Connection conn, String id) {

		UserBean user = null;

		try(
				PreparedStatement stmt = conn.prepareStatement("delete from User where id = ?;");
				) {

			stmt.setString(1, id);
			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

}
