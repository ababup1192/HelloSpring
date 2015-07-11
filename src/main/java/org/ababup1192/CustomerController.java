package org.ababup1192;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/Customer")
public class CustomerController {
    @Autowired
    private CustomerRepository repository;

    /*
    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    Customer getCustomerByFirstName(@RequestParam(value = "firstName", required = false, defaultValue = "") String name) {
        return repository.findByFirstName(name);
    }*/

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    List<Customer> getCustomersByLastName(@RequestParam(value = "lastName", required = false) String name) {
        return repository.findByLastName(name);
    }
}
