package com.peixinchen.servlet;

import com.peixinchen.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.sql.*;

@WebServlet("/save")
public class SaveServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 读取用户填写的内容
        req.setCharacterEncoding("utf-8");
        String todo = req.getParameter("todo");
        // 2. 执行 SQL，把内容存入表中
        int id;
        try (Connection c = DBUtil.connection()) {
            String sql = "INSERT INTO 待办事项 (待办事项) VALUES (?)";
            try (PreparedStatement s = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                s.setString(1, todo);

                s.executeUpdate();

                try (ResultSet rs = s.getGeneratedKeys()) {
                    rs.next();
                    id = rs.getInt(1);
                }
            }
        } catch (SQLException exc) {
            throw new ServletException(exc);
        }
        // 3. 手动写一个 JSON 成功
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        resp.getWriter().println(id);
    }
}
