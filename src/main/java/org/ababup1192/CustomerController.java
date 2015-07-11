package org.ababup1192;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class CustomerController {
    @Autowired
    private CustomerRepository repository;

    @RequestMapping(value = "/Customer", method = RequestMethod.GET)
    @ResponseBody
    public Customer getCustomerByFirstName(
            @RequestParam(value = "firstName", required = false, defaultValue = "") String name) {
        return repository.findByFirstName(name);
    }

    @RequestMapping(value = "/Customers", method = RequestMethod.GET)
    @ResponseBody
    public List<Customer> getCustomersByLastName(
            @RequestParam(value = "lastName", required = false) String name) {
        return repository.findByLastName(name);
    }
}
