package {pkg}.example.service;

import java.time.LocalDate;
import java.util.UUID;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.validation.ConstraintViolationException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tw.com.softleader.domain.config.DefaultDomainConfiguration;
import tw.com.softleader.domain.exception.AlreadyExistException;
import tw.com.softleader.domain.exception.OutOfDateException;
import {pkg}.config.DataSourceConfig;
import {pkg}.config.ServiceConfig;
import {pkg}.example.entity.ExampleEntity;

@WithMockUser("exmaple")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
    classes = {ServiceConfig.class, DataSourceConfig.class, DefaultDomainConfiguration.class})
@Transactional
public class ExampleServiceTest {

  @Autowired
  private ExampleService exampleService;

  /**
   * insert測試
   */
  @Test
  public void testInsert() throws Exception {
    final ExampleEntity entity = new ExampleEntity();
    entity.setCode(UUID.randomUUID().toString().replace("-", "").substring(0, 10));
    entity.setAge(10);
    entity.setBirthday(LocalDate.now().minusYears(entity.getAge()));
    final ExampleEntity saved = exampleService.save(entity);
    Assert.assertEquals(entity, saved);
    Assert.assertNotNull(entity.getId());
    Assert.assertNotNull(entity.getCreatedBy());
    Assert.assertEquals("exmaple", entity.getCreatedBy());
    Assert.assertNotNull(entity.getCreatedTime());
    Assert.assertNotNull(entity.getModifiedBy());
    Assert.assertEquals("exmaple", entity.getModifiedBy());
    Assert.assertNotNull(entity.getModifiedTime());
  }

  /**
   * 重複insert code相同的資料, 應該要丟出 {@link AlreadyExistException}
   */
  @Transactional(TxType.NOT_SUPPORTED)
  @Test(expected = AlreadyExistException.class)
  public void testDuplicateSave() {
    final ExampleEntity entity = new ExampleEntity();
    entity.setCode(UUID.randomUUID().toString().replace("-", "").substring(0, 10));
    entity.setAge(10);
    entity.setBirthday(LocalDate.now().minusYears(entity.getAge()));
    exampleService.save(entity);
    Assert.assertNotNull(entity.getId());

    entity.setId(null);
    exampleService.save(entity);
  }

  /**
   * 更新時 modifiedTime 小於資料庫時, 應該要丟出 {@link OutOfDateException}
   */
  @Transactional(TxType.NOT_SUPPORTED)
  @Test(expected = OutOfDateException.class)
  public void testOutOfDateSave() {
    ExampleEntity entity = new ExampleEntity();
    entity.setCode(UUID.randomUUID().toString().replace("-", "").substring(0, 10));
    entity.setAge(10);
    entity.setBirthday(LocalDate.now().minusYears(entity.getAge()));
    entity = exampleService.save(entity);

    entity.setModifiedTime(entity.getModifiedTime().minusSeconds(10));
    exampleService.save(entity);
  }

  @Test(expected = ConstraintViolationException.class)
  public void testAgeAndBirthdayViolation() {
    final ExampleEntity entity = new ExampleEntity();
    entity.setCode(UUID.randomUUID().toString().replace("-", "").substring(0, 10));
    entity.setAge(10);
    entity.setBirthday(LocalDate.now());

    try {
      exampleService.save(entity);
    } catch (final ConstraintViolationException ex) {
      Assert.assertEquals(1, ex.getConstraintViolations().size()); // 預期1筆錯誤
      Assert.assertFalse(
          ex.getConstraintViolations().iterator().next().getMessage().startsWith("example.")); // 預期錯誤要被翻譯過
      throw ex;
    }
  }

}
