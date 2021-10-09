package webservices.user;

import java.net.URI;
import java.sql.Connection;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UserController {

	@GetMapping(path="/user_test")
	public String test() {
		return "Hello!!";
	}

	@PostMapping("/users")
	public ResponseEntity<Object> createUser (@RequestBody UserBean user) {

		UserDao dao = new UserDao();

		Connection conn = dao.open();
		UserBean savedUser = dao.selUser(conn, user.getId());
		if(savedUser == null) {
			savedUser = dao.saveUser(conn, user);
		} else {
			savedUser = dao.updUser(conn, user);
		}
		dao.close(conn);

		URI location = ServletUriComponentsBuilder
				.fromCurrentRequestUri()
				.path("/{id}")
				.buildAndExpand(savedUser.getId()).toUri();

		return ResponseEntity.created(location).build();
	}

	@GetMapping(path="/user")
	@ResponseBody
	public UserBean select(@RequestParam("id") String id) {
		UserDao dao = new UserDao();

		Connection conn = dao.open();
		UserBean savedUser = dao.selUser(conn, id);
		savedUser = dao.selUser(conn, id);
		dao.close(conn);
		return savedUser;
	}

	@DeleteMapping(path="/user")
	@ResponseBody
	public UserBean delete(@RequestParam("id") String id) {
		UserDao dao = new UserDao();

		Connection conn = dao.open();
		UserBean savedUser = dao.delUser(conn, id);
		savedUser = dao.selUser(conn, id);
		dao.close(conn);
		return savedUser;
	}


}
