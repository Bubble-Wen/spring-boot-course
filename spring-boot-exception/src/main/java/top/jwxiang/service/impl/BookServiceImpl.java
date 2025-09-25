package top.jwxiang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.jwxiang.dto.BookCreateDTO;
import top.jwxiang.dto.BookPageQuery;
import top.jwxiang.dto.BookUpdateDTO;
import top.jwxiang.dto.StockAdjustDTO;
import top.jwxiang.entity.Book;
import top.jwxiang.enums.ErrorCode;
import top.jwxiang.exception.BusinessException;
import top.jwxiang.exception.ResourceNotFoundException;
import top.jwxiang.mapper.BookMapper;
import top.jwxiang.service.BookService;
import top.jwxiang.vo.BookVO;

@Service
@Slf4j
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Override
    public BookVO create(BookCreateDTO dto) {
        // 添加非空校验
        if (dto == null) {
            throw new BusinessException("创建图书的请求参数不能为空");
        }
        if (dto.getStock() == null) {
            throw new BusinessException("图书库存不能为空");
        }
        if (dto.getStock() < 0) {
            throw new BusinessException("图书库存不能为负数");
        }
        Book book = new Book();
        BeanUtils.copyProperties(dto, book);
        baseMapper.insert(book);
        return convertToVO(book);
    }

    @Override
    @Transactional
    public BookVO update(Long id, BookUpdateDTO dto) {
        // 新增ID非空校验
        if (id == null || id <= 0) {
            throw new BusinessException("图书ID不能为空且必须为正数");
        }
        // 新增DTO非空校验
        if (dto == null) {
            throw new BusinessException("更新图书的请求参数不能为空");
        }

        log.info("开始更新图书，ID: {}, DTO: {}", id, dto);

        // 查询原始对象
        Book book = baseMapper.selectById(id);
        log.info("查询到的图书: {}, ID: {}", book, id);

        if (book == null) {
            log.warn("尝试更新不存在的图书，ID: {}", id);
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }

        if (book.getDeleted() == 1) {
            log.warn("尝试更新已删除的图书，ID: {}", id);
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }

        // 版本号非空校验
        if (dto.getVersion() == null) {
            throw new BusinessException("版本号不能为空");
        }
        // 库存非空校验（如果提供了库存参数）
        if (dto.getStock() != null && dto.getStock() < 0) {
            throw new BusinessException("库存不能为负数");
        }

        // 更新图书信息
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setCategory(dto.getCategory());
        book.setStock(dto.getStock());
        book.setVersion(dto.getVersion());

        boolean updated = updateById(book);
        if (!updated) {
            log.error("更新图书失败，ID: {}", id);
            throw new BusinessException(ErrorCode.SERVER_ERROR);
        }

        return convertToVO(book);
    }

    @Override
    public BookVO adjustStock(Long id, StockAdjustDTO dto) {
        // 新增ID非空校验
        if (id == null || id <= 0) {
            throw new BusinessException("图书ID不能为空且必须为正数");
        }
        // 新增DTO及字段非空校验
        if (dto == null) {
            throw new BusinessException("库存调整参数不能为空");
        }
        if (dto.getAmount() == null || dto.getAmount() <= 0) {
            throw new BusinessException("调整数量必须为正数");
        }
        if (dto.getType() == null || (!"in".equals(dto.getType()) && !"out".equals(dto.getType()))) {
            throw new BusinessException("调整类型必须为'in'或'out'");
        }

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
        // 新增ID非空校验
        if (id == null || id <= 0) {
            throw new BusinessException("图书ID不能为空且必须为正数");
        }

        Book book = baseMapper.selectById(id);
        if (book == null) {
            throw new ResourceNotFoundException("图书ID不存在：" + id);
        }
        return convertToVO(book);
    }

    @Override
    public boolean isbnExists(String isbn) {
        // 新增ISBN非空校验
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new BusinessException("ISBN不能为空");
        }

        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Book::getIsbn, isbn);
        return baseMapper.exists(wrapper);
    }

    @Override
    public IPage<BookVO> pageQuery(BookPageQuery query) {
        // 新增查询参数非空校验
        if (query == null) {
            throw new BusinessException("分页查询参数不能为空");
        }
        // 页码和页大小校验
        if (query.getPageNum() == null || query.getPageNum() < 1) {
            throw new BusinessException("页码必须为正数");
        }
        if (query.getPageSize() == null || query.getPageSize() < 1 || query.getPageSize() > 100) {
            throw new BusinessException("页大小必须在1-100之间");
        }

        Page<Book> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        if (query.getTitle() != null) {
            wrapper.like(Book::getTitle, query.getTitle());
        }
        if (query.getCategory() != null) {
            wrapper.eq(Book::getCategory, query.getCategory());
        }
        wrapper.eq(Book::getDeleted, 0);
        IPage<Book> bookPage = baseMapper.selectPage(page, wrapper);
        return bookPage.convert(this::convertToVO);
    }

    @Override
    public void delete(Long id) {
        // 新增ID非空校验
        if (id == null || id <= 0) {
            throw new BusinessException("图书ID不能为空且必须为正数");
        }
        // 校验图书是否存在
        Book book = baseMapper.selectById(id);
        if (book == null) {
            throw new ResourceNotFoundException("删除失败：id=" + id + "的图书不存在");
        }

        baseMapper.deleteById(id);
    }

    @Override
    public BookVO restore(Long id) {
        // 新增ID非空校验
        if (id == null || id <= 0) {
            throw new BusinessException("图书ID不能为空且必须为正数");
        }

        Book book = baseMapper.selectById(id);
        if (book == null) {
            throw new ResourceNotFoundException("图书ID不存在：" + id);
        }
        book.setDeleted(0);
        baseMapper.updateById(book);
        return convertToVO(book);
    }

    private BookVO convertToVO(Book book) {
        BookVO vo = new BookVO();
        BeanUtils.copyProperties(book, vo);
        return vo;
    }
}