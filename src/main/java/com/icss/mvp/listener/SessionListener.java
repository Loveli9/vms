package com.icss.mvp.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

import com.icss.mvp.constant.Constants;
import com.icss.mvp.entity.system.UserEntity;

/**
 * Session监听器
 */
public class SessionListener implements HttpSessionListener {

    private static Logger logger = Logger.getLogger(SessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        ServletContext application = session.getServletContext();

        // 如果是一次新的会话
        if (session.isNew()) {
            UserEntity user = (UserEntity) session.getAttribute(Constants.USER_KEY);
            // 未登录用户当匿名用户处理
            String userName = (user == null) ? "Anonymous" : user.getName();

            Map<String, String> online = (Map<String, String>) application.getAttribute("online");
            if (online == null) {
                online = new HashMap<>();
            }

            // 将用户在线信息放入Map中
            online.put(session.getId(), userName);
            application.setAttribute("online", online);
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        ServletContext application = session.getServletContext();

        // 在session销毁的时候 把Map中保存的键值对清除
        UserEntity user = (UserEntity) session.getAttribute(Constants.USER_KEY);
        String userName = (user == null) ? "Anonymous" : user.getId();
        // if (user != null) {
        // StringMD5Util.userLogout(user.getUSERNAME());
        // }

        logger.info("sessionDestroyed. user:" + userName);

        // // 取得登录的用户名
        // String username = (String) session.getAttribute(Constants.USER_KEY);
        //
        // // 从在线列表中删除用户名
        // List onlineUserList = (List) application.getAttribute("onlineUserList");
        // onlineUserList.remove(username);
        //
        // System.out.println(username + "超时退出。");
    }
}
