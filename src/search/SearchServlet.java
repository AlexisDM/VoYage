package search;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import model.Search;
import dao.SearchDao;

@SuppressWarnings("serial")
public class SearchServlet extends HttpServlet{
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String cmd = req.getParameter("cmd");
		PrintWriter out = resp.getWriter();
		
		if (cmd != null) {
			if("LoadSearch".equals(cmd)) {
				resp.setContentType("application/json");
				
				List<Search> queries = SearchDao.getSearches(req.getSession().getAttribute("login").toString());
				
				out.write(new Gson().toJson(queries));
			}
		}
	}
}
