package domain;

import lombok.Data;

/**
 * @author: dongsheng
 * @CreateTime: 2022/3/8
 * @Description:
 *
 */
@Data
public class Student {
        /*
                CREATE TABLE `a_student` (
                `id` int(11) NOT NULL,
                `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
                 PRIMARY KEY (`id`)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
        * */
        private int id;
        private String name;

}
