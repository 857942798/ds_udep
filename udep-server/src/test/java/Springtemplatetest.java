import domain.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author: dongsheng
 * @CreateTime: 2022/3/8
 * @Description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/udep-persistence-mysql.xml")
public class Springtemplatetest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void inserttest(){
        int count = jdbcTemplate.update("INSERT INTO a_student (name) VALUES (?)", null, "ddd");
        System.out.println(count);
    }

    @Test
    public void deletetest(){
        int count = jdbcTemplate.update("DELETE FROM a_student where id=?", 2);
        System.out.println(count);
    }

    @Test
    public void updatetest(){
        int count = jdbcTemplate.update("UPDATE a_student set name=? where id=?", "hahah","1");
        System.out.println(count);
    }

    @Test
    public void queryalltest(){
        List<Student> querylist = jdbcTemplate.query("select * from a_student", new BeanPropertyRowMapper<Student>(Student.class));
        System.out.println(querylist);
    }

    @Test
    public void queryonetest(){
        Student queryone = jdbcTemplate.queryForObject("select * from a_student where id =?",
                new BeanPropertyRowMapper<Student>(Student.class),"1");
        System.out.println(queryone);
    }

    @Test
    public void querycounttest(){
        Long count = jdbcTemplate.queryForObject("select COUNT(*) from a_student ", Long.class);
        System.out.println(count);
    }
}
