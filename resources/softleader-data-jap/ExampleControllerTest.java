package {pkg}.example.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import tw.com.softleader.commons.json.jackson.JacksonObjectMapper;
import tw.com.softleader.domain.config.DefaultDomainConfiguration;
import {pkg}.config.DataSourceConfig;
import {pkg}.config.ServiceConfig;
import {pkg}.config.WebMvcConfig;
import {pkg}.example.entity.Example;

@WebAppConfiguration
@WithMockUser("exmaple")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class, DataSourceConfig.class,
    DefaultDomainConfiguration.class, WebMvcConfig.class})
public class ExampleControllerTest {

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private JacksonObjectMapper mapper;

  private MockMvc mvc;

  @Before
  public void setUp() {
    mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .alwaysDo(MockMvcResultHandlers.print()).build();
  }

  @Test
  public void testSaveAndDelete() throws Exception {
    final Example expected = new Example();
    expected.setCode(UUID.randomUUID().toString().replace("-", "").substring(0, 10));
    expected.setAge(18);
    expected.setBirthday(LocalDate.now().minusYears(expected.getAge()));
    expected.setEmail("example@softleader.com.tw");

    String respone = mvc
        .perform(post("/examples").contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(expected)))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    Assert.assertNotNull(respone);
    final Example actual = mapper.readValue(respone, Example.class);
    Assert.assertNotNull(actual.getId());
    Assert.assertNotNull(actual.getCreatedBy());
    Assert.assertNotNull(actual.getCreatedTime());
    Assert.assertNotNull(actual.getModifiedBy());
    Assert.assertNotNull(actual.getModifiedTime());
    Assert.assertEquals(expected.getCode(), actual.getCode());
    Assert.assertEquals(expected.getAge(), actual.getAge());
    Assert.assertEquals(expected.getBirthday(), actual.getBirthday());
    Assert.assertEquals(expected.getEmail(), actual.getEmail());

    mvc.perform(delete("/examples/" + actual.getId())).andExpect(status().isOk());

    respone = mvc.perform(get("/examples/" + actual.getId())).andExpect(status().isOk()).andReturn()
        .getResponse().getContentAsString();
    Assert.assertEquals("", respone);
  }

}
