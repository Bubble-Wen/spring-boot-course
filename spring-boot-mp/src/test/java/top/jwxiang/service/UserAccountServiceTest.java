package top.jwxiang.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import top.jwxiang.entity.UserAccount;
import top.jwxiang.mapper.UserAccountMapper;
import top.jwxiang.service.impl.UserAccountServiceImpl;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {
    @Mock
    private UserAccountMapper userAccountMapper;
    @InjectMocks
    private UserAccountServiceImpl userAccountService;
    private UserAccount testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserAccount();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("hashedpassword");
        testUser.setNickname("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPhone("13800138000");
        testUser.setAvatarUrl("https://example.com/avatar.jpg");
        testUser.setStatus(1);
        testUser.setDeleted(0);
        testUser.setVersion(1);
        testUser.setCreateTime(LocalDateTime.now());
        testUser.setUpdateTime(LocalDateTime.now());
    }

    @Test
    void testMapperInsert() {
        when(userAccountMapper.insert(any(UserAccount.class))).thenReturn(1);
        int result = userAccountMapper.insert(testUser);
        assertEquals(1, result);
        verify(userAccountMapper, times(1)).insert(testUser);
    }

    @Test
    void testMapperSelectById() {
        when(userAccountMapper.selectById(1L)).thenReturn(testUser);
        UserAccount result = userAccountMapper.selectById(1L);
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        verify(userAccountMapper, times(1)).selectById(1L);
    }

    @Test
    void testMapperSelectByIdNotFound() {
        when(userAccountMapper.selectById(99L)).thenReturn(null);
        UserAccount result = userAccountMapper.selectById(99L);
        assertNull(result);
        verify(userAccountMapper, times(1)).selectById(99L);
    }

    @Test
    void testMapperUpdateById() {
        when(userAccountMapper.updateById(any(UserAccount.class))).thenReturn(1);
        testUser.setNickname("Updated Nickname");
        int result = userAccountMapper.updateById(testUser);
        assertEquals(1, result);
        verify(userAccountMapper, times(1)).updateById(testUser);
    }

    @Test
    void testMapperUpdateByIdFailed() {
        when(userAccountMapper.updateById(any(UserAccount.class))).thenReturn(0);
        int result = userAccountMapper.updateById(testUser);
        assertEquals(0, result);
        verify(userAccountMapper, times(1)).updateById(testUser);
    }

    @Test
    void testMapperDeleteById() {
        when(userAccountMapper.deleteById(1L)).thenReturn(1);
        int result = userAccountMapper.deleteById(1L);
        assertEquals(1, result);
        verify(userAccountMapper, times(1)).deleteById(1L);
    }

    @Test
    void testMapperDeleteByIdFailed() {
        when(userAccountMapper.deleteById(99L)).thenReturn(0);
        int result = userAccountMapper.deleteById(99L);
        assertEquals(0, result);
        verify(userAccountMapper, times(1)).deleteById(99L);
    }

    @Test
    void testUserAccountEntityFields() {
        assertNotNull(testUser.getId());
        assertNotNull(testUser.getUsername());
        assertNotNull(testUser.getEmail());
        assertNotNull(testUser.getNickname());
        assertNotNull(testUser.getStatus());
        assertNotNull(testUser.getDeleted());
        assertNotNull(testUser.getVersion());
        assertEquals(1L, testUser.getId());
        assertEquals("testuser", testUser.getUsername());
        assertEquals("test@example.com", testUser.getEmail());
        assertEquals("Test User", testUser.getNickname());
        assertEquals(1, testUser.getStatus());
        assertEquals(0, testUser.getDeleted());
        assertEquals(1, testUser.getVersion());
    }
}