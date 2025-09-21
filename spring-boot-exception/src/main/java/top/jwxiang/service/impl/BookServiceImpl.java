package top.jwxiang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import top.jwxiang.dto.BookCreateDTO;
import top.jwxiang.dto.BookPageQuery;
import top.jwxiang.dto.BookUpdateDTO;
import top.jwxiang.dto.StockAdjustDTO;
import top.jwxiang.entity.Book;
import top.jwxiang.exception.ResourceNotFoundException;
import top.jwxiang.mapper.BookMapper;
import top.jwxiang.service.BookService;
import top.jwxiang.vo.BookVO;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Override
    public BookVO create(BookCreateDTO dto) {
        Book book = new Book();
        BeanUtils.copyProperties(dto, book);
        baseMapper.insert(book);
        return convertToVO(book);
    }

    @Override
    public BookVO update(Long id, BookUpdateDTO dto) {
        Book book = baseMapper.selectById(id);
        if (book == null) {
            throw new ResourceNotFoundException("图书ID不存在：" + id);
        }
        BeanUtils.copyProperties(dto, book);
        baseMapper.updateById(book);
        return convertToVO(book);
    }

    @Override
    public BookVO adjustStock(Long id, StockAdjustDTO dto) {
        Book book = baseMapper.selectById(id);
        if (book == null) {
            throw new ResourceNotFoundException("图书ID不存在：" + id);
        }
        if ("out".equals(dto.getType()) && book.getStock() < dto.getAmount()) {
            throw new IllegalArgumentException("库存不足，当前库存：" + book.getStock() + "，需扣减：" + dto.getAmount());
        }
        int newStock = "in".equals(dto.getType())
                ? book.getStock() + dto.getAmount()
                : book.getStock() - dto.getAmount();
        book.setStock(newStock);
        baseMapper.updateById(book);
        return convertToVO(book);
    }

    @Override
    public BookVO getById(Long id) {
        Book book = baseMapper.selectById(id);
        if (book == null) {
            throw new ResourceNotFoundException("图书ID不存在：" + id);
        }
        return convertToVO(book);
    }

    @Override
    public boolean isbnExists(String isbn) {
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Book::getIsbn, isbn);
        return baseMapper.exists(wrapper);
    }

    @Override
    public IPage<BookVO> pageQuery(BookPageQuery query) {
        Page<Book> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        if (query.getTitle() != null) {
            wrapper.like(Book::getTitle, query.getTitle());
        }
        if (query.getCategory() != null) {
            wrapper.eq(Book::getCategory, query.getCategory());
        }
        wrapper.eq(Book::getDeleted, 0); // 只查未删除的
        IPage<Book> bookPage = baseMapper.selectPage(page, wrapper);
        return bookPage.convert(this::convertToVO);
    }

    @Override
    public void delete(Long id) {
        // MyBatis-Plus自动处理逻辑删除（设置deleted=1）
        baseMapper.deleteById(id);
    }

    @Override
    public BookVO restore(Long id) {
        Book book = baseMapper.selectById(id);
        if (book == null) {
            throw new ResourceNotFoundException("图书ID不存在：" + id);
        }
        book.setDeleted(0); // 恢复删除（设置deleted=0）
        baseMapper.updateById(book);
        return convertToVO(book);
    }

    // Entity转VO工具方法
    private BookVO convertToVO(Book book) {
        BookVO vo = new BookVO();
        BeanUtils.copyProperties(book, vo);
        return vo;
    }
}
