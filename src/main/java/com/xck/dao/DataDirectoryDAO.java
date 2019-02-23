package com.xck.dao;

import com.xck.bean.ColumnNameEnum;
import com.xck.bean.DataDirectory;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository("mysql_dao")
public class DataDirectoryDAO {
    private static Logger log = Logger.getLogger(DataDirectoryDAO.class);

    @Resource(name = "dataSource")
    public DataSource dataSource;

    /**
     * 获取表字段信息
     *
     * @param dataDirectory
     * @return
     */
    public Map<String, List<String>> getTableColumnsInfo(DataDirectory dataDirectory) {
        Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();

        String databaseName = dataDirectory.getDatabaseName();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select table_name from information_schema.Tables where table_schema=?";
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, databaseName);
            rs = ps.executeQuery();
            while (rs.next()) {
                String table_name = rs.getString("table_name");
                map.put(table_name, getColumnsInfo(dataDirectory, table_name));
            }
        } catch (SQLException e) {
            log.error("get connection error!", e);
        } finally {
            freeConnection(ps, conn);
        }
        return map;
    }

    private List<String> getColumnsInfo(DataDirectory dataDirectory, String table_name) {
        List<String> list = new ArrayList<String>();

        String databaseName = dataDirectory.getDatabaseName();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select " + dataDirectory.getColumnSql() + " from information_schema.COLUMNS where table_schema=? and table_name=?";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, databaseName);
            ps.setString(2, table_name);
            rs = ps.executeQuery();
            while (rs.next()) {
                StringBuffer sb = new StringBuffer("");
                Iterator<ColumnNameEnum> iterator = dataDirectory.getTableHeader().keySet().iterator();
                while(iterator.hasNext()){
                    sb.append(isEmptyReplaceStr(rs.getString(iterator.next().name()), "-")).append("@");
                }
                list.add(sb.delete(sb.length()-1, sb.length()).toString());
            }
        } catch (SQLException e) {
            log.error("get connection error!", e);
        } finally {
            freeConnection(ps, conn);
        }
        return list;
    }

    /**
     * 获取表的注释
     *
     * @param databaeName
     * @return
     */
    public Map<String, String> getTableComment(String databaeName) {
        Map<String, String> tableCommentMap = new HashMap<String, String>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select table_name, table_comment " +
                "from information_schema.TABLES where table_schema=?";

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, databaeName);
            rs = ps.executeQuery();
            while (rs.next()) {
                String table_name = isEmptyReplaceStr(rs.getString("table_name"), "");
                String table_comment = isEmptyReplaceStr(rs.getString("table_comment"), "");
                tableCommentMap.put(table_name, table_comment);
            }
        } catch (SQLException e) {
            log.error("get connection error!", e);
        } finally {
            freeConnection(ps, conn);
        }
        return tableCommentMap;
    }

    private String isEmptyReplaceStr(String str, String replaceStr) {
        if (StringUtils.isEmpty(str)) {
            return replaceStr;
        }
        return str;
    }

    private void freeConnection(PreparedStatement ps, Connection conn) {
        try {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            log.error("close connection error!", e);
        }
    }
}
