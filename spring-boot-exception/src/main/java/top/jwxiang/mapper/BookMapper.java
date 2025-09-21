package top.jwxiang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.jwxiang.entity.Book;

@Mapper
public interface BookMapper extends BaseMapper<Book> {
    // 可自定义SQL（如复杂查询），基础CRUD由BaseMapper封装
}