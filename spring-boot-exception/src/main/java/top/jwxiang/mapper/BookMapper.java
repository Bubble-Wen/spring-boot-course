package top.jwxiang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import top.jwxiang.entity.Book;

@Mapper
@MapperScan("top.jwxiang.mapper")  // 关键：扫描Mapper接口
public interface BookMapper extends BaseMapper<Book> {
    // 可自定义SQL（如复杂查询），基础CRUD由BaseMapper封装
}