package com.ais.udep.server.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author: dongsheng
 * @CreateTime: 2022/3/7
 * @Description:
 */
@Data
public class UdepJdbcTemplatePool {
    public Map<String, JdbcTemplate> udepJdPool = null;
}
