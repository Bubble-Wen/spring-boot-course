package top.jwxiang.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import top.jwxiang.common.Result;
import top.jwxiang.dto.BookCreateDTO;
import top.jwxiang.dto.BookPageQuery;
import top.jwxiang.dto.BookUpdateDTO;
import top.jwxiang.dto.StockAdjustDTO;
import top.jwxiang.mapper.BookMapper;
import top.jwxiang.service.BookService;
import top.jwxiang.vo.BookVO;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;  // 注入BookMapper

    // 构造方法注入（同时注入Service和Mapper）
    public BookController(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    // 新增测试接口：检查BookMapper是否注入成功
    @GetMapping("/test/mapper-check")
    public Result<String> checkBookMapper() {
        try {
            // 尝试调用mapper方法来验证其是否正常工作
            Long count = bookMapper.selectCount(null);
            return Result.ok("BookMapper注入成功且可正常使用，当前图书总数: " + count);
        } catch (Exception e) {
            return Result.error("BookMapper注入成功但调用异常: " + e.getMessage());
        }
    }

    // 新增图书
    @PostMapping
    public Result<BookVO> create(@Valid @RequestBody BookCreateDTO dto) {
        return Result.success(bookService.create(dto));
    }

    // 更新图书
    @PutMapping("/{id}")
    public Result<? extends Object> update(@PathVariable Long id, @RequestBody BookUpdateDTO dto) {
        try {
            BookVO bookVO = bookService.update(id, dto);
            return Result.success(bookVO);
        } catch (Exception e) {
            // 直接打印异常（不管处理器是否生效）
            e.printStackTrace(); // 强制输出到控制台
            return Result.fail(500, "更新失败：" + e.getMessage());
        }
    }

    // 库存调整
    @PatchMapping("/{id}/stock/adjust")
    public Result<BookVO> adjustStock(
            @PathVariable Long id,
            @Valid @RequestBody StockAdjustDTO dto) {
        return Result.success(bookService.adjustStock(id, dto));
    }

    // 按ID查询
    @GetMapping("/{id}")
    public Result<BookVO> getById(@PathVariable Long id) {
        return Result.success(bookService.getById(id));
    }

    // ISBN唯一性检查
    @GetMapping("/exists/isbn/{isbn}")
    public Result<Boolean> isbnExists(@PathVariable String isbn) {
        return Result.success(bookService.isbnExists(isbn));
    }

    // 分页查询
    @GetMapping("/page")
    public Result<IPage<BookVO>> pageQuery(BookPageQuery query) {
        return Result.success(bookService.pageQuery(query));
    }

    // 逻辑删除
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return Result.success();
    }

    // 恢复删除
    @PutMapping("/{id}/restore")
    public Result<BookVO> restore(@PathVariable Long id) {
        return Result.success(bookService.restore(id));
    }
}
