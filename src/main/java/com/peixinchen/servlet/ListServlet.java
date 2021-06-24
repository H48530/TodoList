package com.peixinchen.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peixinchen.model.待办事项;
import com.peixinchen.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/list")
public class ListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 从数据库中查出 List<待办事项> todoList
        List<待办事项> todoList = new ArrayList<>();
        try (Connection c = DBUtil.connection()) {
            String sql = "SELECT id, 待办事项, 是否完成 FROM 待办事项 ORDER BY id";
            try (PreparedStatement s = c.prepareStatement(sql)) {
                try (ResultSet rs = s.executeQuery()) {
                    while (rs.next()) {
                        待办事项 todo = new 待办事项();
                        todo.id = rs.getInt("id");
                        todo.待办事项 = rs.getString("待办事项");
                        todo.是否完成 = rs.getBoolean("是否完成");

                        todoList.add(todo);
                    }
                }
            }
        } catch (SQLException exc) {
            throw new ServletException(exc);
        }

        // 2. 把 todoList 序列化成 JSON 格式的字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(todoList);
        // 3. 进行响应输出
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        resp.getWriter().print(s);
    }
}
