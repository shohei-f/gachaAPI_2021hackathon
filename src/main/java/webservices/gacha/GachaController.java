package webservices.gacha;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GachaController {

	@GetMapping(path="/test")
	public String test() {
		return "Hello2";
	}

	@GetMapping(path="/pick1")
	public GachaMasterBean pick1(@RequestParam("userid") String userid) {
		GachaService service = new GachaService();
		return service.pick1(new BigDecimal(Math.random()), userid);
	}

	@GetMapping(path="/pick")
	@ResponseBody
	public List<GachaMasterBean> pick(
			@RequestParam("userid")    String userid,
			@RequestParam("times")     int times,
			@RequestParam("penalty")   BigDecimal penalty,
			@RequestParam("distance")  int distance
			) {

		BigDecimal incentive = new BigDecimal(distance/1000).add(penalty.negate());

		System.out.println("userid=" + userid + " times=" + times + " penalty=" + penalty + " distance=" + distance + " incentive=" + incentive);

		GachaService service = new GachaService();
		return service.pick(userid, times, incentive);
	}
}
