package {pkg}.example.web;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import {pkg}.example.entity.Example;
import {pkg}.example.service.ExampleService;

/**
 * 相關文件: https://github.com/softleader/softleader-framework-docs/wiki/QueryPrompt
 * 
 * @author Matt S.Y. Ho
 *
 */
@RestController
@RequestMapping("/examples")
public class ExampleController {

  @Autowired
  private ExampleService exampleService;

  @RequestMapping(method = {RequestMethod.POST})
  public Example save(@RequestBody @Validated Example entity,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      // FIXME: 專案應該影自己的處理資料驗證失敗的邏輯
      throw new IllegalArgumentException(bindingResult.getFieldErrors().stream()
          .map(f -> f.getField() + ": " + f.getDefaultMessage()).collect(Collectors.joining(",")));
    }
    return exampleService.save(entity);
  }

  @RequestMapping(value = "/{id}", method = {RequestMethod.GET})
  public Example getOne(@PathVariable("id") long id) {
    return exampleService.getOne(id)
        // FIXME: 專案應該影自己的撈不到資料的邏輯, 如: .orElseThrow(() -> new NotFoundException(Example.class, id));
        .orElse(null);
  }

  @RequestMapping(value = "/{ids}", method = {RequestMethod.DELETE, RequestMethod.POST})
  public void delete(@PathVariable("ids") List<Long> ids) {
    exampleService.delete(ids);
  }

}
