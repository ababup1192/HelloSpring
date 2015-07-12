package org.ababup1192;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CustomerController {
    @Autowired
    private CustomerRepository repository;

    /*
        $ curl -i http://localhost:9000/Customers
        $ curl -i http://localhost:9000/Customers?lastName=Smith
    */
    @RequestMapping(value = "/Customers", method = RequestMethod.GET)
    @ResponseBody
    public List<Customer> getCustomersByLastName(
            @RequestParam(value = "lastName", required = false, defaultValue = "") String name) {
        if (name.isEmpty()) {
            return repository.findAll();
        } else {
            return repository.findByLastName(name);
        }
    }

    /*
        $ curl -i http://localhost:9000/Customers?firstName=Alice
    */
    @RequestMapping(value = "/Customer", method = RequestMethod.GET)
    @ResponseBody
    public Customer getCustomerByFirstName(
            @RequestParam(value = "firstName", required = false, defaultValue = "") String name) {
        return repository.findByFirstName(name);
    }

    /*
        $ curl -X POST -i -H "Content-type: application/json" -d '{"firstName":"foo", "lastName":"bar"}' http://localhost:9000/Customers
    */
    @RequestMapping(value = "/Customers", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Customer postCustomer(@RequestBody Customer customer) {
        return repository.save(customer);
    }

    /*
        $ curl -X DELETE -i http://localhost:9000/Customers/55a11c3d0b34c9d8c8dfac2e
    */
    @RequestMapping(value = "/Customers/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void postCustomer(@PathVariable String id) {
        repository.delete(id);
    }


}
