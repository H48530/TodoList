package com.peixinchen.servlet;

import com.peixinchen.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/finish")
public class FinishServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 获取用户点击的事项的 id
        req.setCharacterEncoding("utf-8");
        String id = req.getParameter("id");
        // 2. 进行数据库表的更新
        try (Connection c = DBUtil.connection()) {
            String sql = "UPDATE 待办事项 SET 是否完成 = 1 WHERE id = ?";
            try (PreparedStatement s = c.prepareStatement(sql)) {
                s.setString(1, id);
                s.executeUpdate();
            }
        } catch (SQLException exc) {
            throw new ServletException(exc);
        }
        // 3. 手动写一个 JSON 成功
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        resp.getWriter().println("\"成功\"");
    }
}
