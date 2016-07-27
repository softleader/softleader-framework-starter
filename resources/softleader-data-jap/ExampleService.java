package {pkg}.example.service;

import tw.com.softleader.domain.CrudCodeService;
import tw.com.softleader.domain.exception.ValidationException;
import tw.com.softleader.domain.guarantee.constraints.EntityUnique;
import tw.com.softleader.domain.guarantee.constraints.EntityUpToDate;
import {pkg}.example.entity.ExampleEntity;

public interface ExampleService extends CrudCodeService<ExampleEntity, Long> {

  @Override
  ExampleEntity save(@EntityUnique @EntityUpToDate ExampleEntity entity) throws ValidationException;

}
