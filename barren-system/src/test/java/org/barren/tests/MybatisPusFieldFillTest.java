package org.barren.tests;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.barren.core.auth.utils.AuthUtil;
import org.barren.core.auth.utils.UserInfo;
import org.barren.modules.system.entity.SysTenant;
import org.barren.modules.system.service.ISysTenantService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

/**
 * 测试 baomidou 自动填充功能
 *
 * @author cxs
 */
@Slf4j
@Service
@SpringBootTest
public class MybatisPusFieldFillTest {
    @SpyBean
    ISysTenantService tenantService;

    private MockedStatic<AuthUtil> AuthUtilMock;

    @BeforeEach
    public void setup() {
        UserInfo user = new UserInfo();
        user.setId(1L);
        // mockito mock静态方法
        AuthUtilMock = mockStatic(AuthUtil.class);
        AuthUtilMock.when(AuthUtil::getCurrentUser).thenReturn(user);
        // given().willReturn(null);
        // given().willReturn(user);
    }

    @Test
    public void testFieldFill() {

        SysTenant entity = new SysTenant();
        entity.setName("111");
        entity.setContactPhone("123456");
        entity.setContactUserId(1L);
        entity.setContactName("123");
        entity.setAccountCount(1);
        entity.setExpireTime(LocalDateTime.parse("2025-12-24 12:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        tenantService.saveOrUpdate(entity, Wrappers.<SysTenant>lambdaUpdate().eq(SysTenant::getContactUserId, entity.getContactUserId()));
    }

    @AfterEach
    public void teardown() {
        AuthUtilMock.close();
    }
}
