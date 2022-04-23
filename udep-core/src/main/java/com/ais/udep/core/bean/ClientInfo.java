package com.ais.udep.core.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/18
 * @Description: id用于和连接通道进行绑定
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientInfo {
    private Long id;
}
