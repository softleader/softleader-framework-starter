package {pkg}.example.service;

import tw.com.softleader.domain.CrudCodeService;
import tw.com.softleader.domain.exception.ValidationException;
import tw.com.softleader.domain.guarantee.constraints.EntityUnique;
import tw.com.softleader.domain.guarantee.constraints.EntityUpToDate;
import {pkg}.example.entity.Example;

public interface ExampleService extends CrudCodeService<Example, Long> {

  @Override
  Example save(@EntityUnique @EntityUpToDate Example entity) throws ValidationException;

}
