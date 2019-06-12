package ha.controller;

import java.net.InetAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页
 * @author wangym
 *
 */
@Controller
public class IndexController {
	
	@RequestMapping(path = "/")
	public String index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		InetAddress addr = InetAddress.getLocalHost();
		String hostName = addr.getHostName().toString(); 
		HttpSession session = request.getSession(true);
		session.setAttribute("hostName", hostName);
		return "index";
	}

}
