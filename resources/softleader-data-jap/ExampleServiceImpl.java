package {pkg}.example.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.com.softleader.data.dao.CrudCodeDao;
import tw.com.softleader.domain.AbstractCrudCodeService;
import tw.com.softleader.domain.exception.ValidationException;
import tw.com.softleader.domain.guarantee.constraints.EntityUnique;
import tw.com.softleader.domain.guarantee.constraints.EntityUpToDate;
import {pkg}.example.dao.ExampleDao;
import {pkg}.example.entity.ExampleEntity;
import {pkg}.example.service.ExampleService;

/**
 * 相關文件: https://github.com/softleader/softleader-framework-docs/wiki/Entity-Guarantee
 * 
 * @author Matt S.Y. Ho
 *
 */
@Service
public class ExampleServiceImpl extends AbstractCrudCodeService<ExampleEntity, Long>
    implements ExampleService {

  @Autowired
  private ExampleDao sampleDao;

  @Override
  public CrudCodeDao<ExampleEntity, Long> getDao() {
    return sampleDao;
  }

  @Override
  public ExampleEntity save(@EntityUnique @EntityUpToDate ExampleEntity entity)
      throws ValidationException {
    return super.save(entity);
  }

}
