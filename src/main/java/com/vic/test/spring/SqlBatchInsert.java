package com.vic.test.spring;

import com.vic.test.util.ConnectionUtil;
import org.springframework.util.StopWatch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Author: vic
 * @CreateTime : 2018/10/15 09:56
 */
public class SqlBatchInsert {

    /**
     * jdbc实现批处理
     *
     * @param args
     */
    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        Connection connection = ConnectionUtil.getConnection(ConnectionUtil.LOCALHOST_VIC);
        stopWatch.start();
        PreparedStatement preparedStatement = null;
        try {
            String sql = "insert into Log_Batch (num) values(?)";
            preparedStatement = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            // 总插入条数
            final int total = 100000;
            for (int i = 0; i < total; i++) {
                preparedStatement.setInt(1, i);
                preparedStatement.addBatch();
                // 每1000条提交一次，防止内存溢出
                if (i % 1000 == 0) {
                    preparedStatement.executeBatch();
                    preparedStatement.clearBatch();
                }
            }
            preparedStatement.executeBatch();
            preparedStatement.clearBatch();
            connection.commit();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (null != preparedStatement) {
                    preparedStatement.close();
                }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }

    /**
     * 通过mybatis的SqlSessionFactory实现批处理
     */
    public void insert2() {
//        List<User> userList = Lists.newArrayList();
//        SqlSessionFactory sqlSessionFactory = null;
//        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
//        UserDao userDao = sqlSession.getMapper(UserDao.class);
//        for (int i = 0; i < userList.size(); i++) {
//            userDao.insert(userList.get(i));
//            if (i % 1000 == 999) {
//                sqlSession.commit();
//                sqlSession.clearCache();
//            }
//        }
//        // mapper
//        sqlSession.commit();
//        sqlSession.clearCache();
    }
}
