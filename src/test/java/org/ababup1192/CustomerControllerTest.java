package org.ababup1192;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class CustomerControllerTest {
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private List<Customer> customerList;
    @Autowired
    private CustomerRepository repository;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        // Prepare mock
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        // Clear DB
        this.repository.deleteAll();

        // Create list
        customerList = Arrays.asList(new Customer("Alice", "Smith"), new Customer("Bob", "Smith"));

        // Save customers to DB
        repository.save(customerList);
    }

    @Test
    public void notFound() throws Exception {
        mockMvc.perform(get("/hogehoge")
        ).andExpect(status().isNotFound());
    }

    @Test
    public void all() throws Exception {
        mockMvc.perform(get("/Customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is(this.customerList.get(0).getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(this.customerList.get(0).getLastName())))
                .andExpect(jsonPath("$[1].firstName", is(this.customerList.get(1).getFirstName())))
                .andExpect(jsonPath("$[1].lastName", is(this.customerList.get(1).getLastName())));
    }

    @Test
    public void customersByLastName() throws Exception {
        mockMvc.perform(get("/Customers?lastName=Smith"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(this.contentType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is(this.customerList.get(0).getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(this.customerList.get(0).getLastName())))
                .andExpect(jsonPath("$[1].firstName", is(this.customerList.get(1).getFirstName())))
                .andExpect(jsonPath("$[1].lastName", is(this.customerList.get(1).getLastName())));
    }

    @Test
    public void noCustomersByWrongLastName() throws Exception {
        mockMvc.perform(get("/Customers?lastName=Brown"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void customerByFirstName() throws Exception {
        mockMvc.perform(get("/Customer?firstName=Alice"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.firstName", is(this.customerList.get(0).getFirstName())))
                .andExpect(jsonPath("$.lastName", is(this.customerList.get(0).getLastName())));
    }

    @Test
    public void noCustomerByWrongFirstName() throws Exception {
        mockMvc.perform(get("/Customer?firstName=John"))
                .andExpect(status().isOk());
    }

    @Test
    public void createCustomer() throws Exception {
        String newCustomerJson = json(new Customer("Hoge", "Foo"));

        mockMvc.perform(post("/Customers")
                .contentType(contentType)
                .content(newCustomerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", not("")))
                .andExpect(jsonPath("$.firstName", is("Hoge")))
                .andExpect(jsonPath("$.lastName", is("Foo")));
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
