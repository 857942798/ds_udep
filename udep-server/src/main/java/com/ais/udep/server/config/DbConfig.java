package com.ais.udep.server.config;

import lombok.Data;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

/**
 * @author: dongsheng
 * @CreateTime: 2022/3/8
 * @Description: 用于判断是否哪个文件夹下读取文件，由udep-persistence-mysql.xml中指定
 */
@Data
public class DbConfig {
    public String xmlReadPath = null;
}
