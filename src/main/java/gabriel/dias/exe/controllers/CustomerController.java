package gabriel.dias.exe.controllers;

import java.math.BigDecimal;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import gabriel.dias.exe.models.Company;
import gabriel.dias.exe.models.Customer;
import gabriel.dias.exe.repositories.CompanyRepository;
import gabriel.dias.exe.repositories.CustomerRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    CustomerRepository repo;

    @Autowired
    CompanyRepository compRepo;

    @Autowired
    PagedResourcesAssembler<Customer> assembler;
    
    @GetMapping
    public PagedModel<EntityModel<Customer>> index(@PageableDefault(size = 20) Pageable pageable) {
        log.info("Returning all customers!");

        Page<Customer> page = repo.findAll(pageable);

        return assembler.toModel(page);
    }

    @GetMapping("{id}")
    public EntityModel<Customer> show(@PathVariable Long id) {
        log.info("Returning customer with ID: " + id);

        Customer custumer = repo.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Custumer not found!")
        );

        return custumer.toModel();
    }
    
    @PostMapping
    public ResponseEntity<EntityModel<Customer>> create(@RequestBody @Valid Customer customer) {
        log.info("Creating customer: " + customer);

        repo.save(customer);

        return ResponseEntity.created(customer.toModel().getRequiredLink("self").toUri()).body(customer.toModel());
    }

    @PostMapping("/deposit/{id}")
    public ResponseEntity<Company> deposit(@PathVariable Long id, @RequestBody double value) {
        log.info("Value of deposit: " + value);

        if (value < 0) return ResponseEntity.badRequest().build();

        Optional<Company> company = compRepo.findById(id);

        if (company.isEmpty()) return ResponseEntity.notFound().build();

        company.get().setBalance(company.get().getBalance().add(new BigDecimal(value)));

        compRepo.save(company.get());

        return ResponseEntity.ok(company.get());
    }

    @PostMapping("/withdraw/{id}")
    public ResponseEntity<Company> withdraw(@PathVariable Long id, @RequestBody double value) {
        log.info("Value of deposit: " + value);

        if (value < 0) return ResponseEntity.badRequest().build();

        Optional<Company> company = compRepo.findById(id);

        if (company.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else if (
            (value - (value * (100 * company.get().getTax().doubleValue()))) > company.get().getBalance().doubleValue() 
            || value > company.get().getBalance().doubleValue()
        ) {
            return ResponseEntity.badRequest().build();
        }

        company.get().setBalance(new BigDecimal(company.get().getBalance().doubleValue() - (value - (value * (100 * company.get().getTax().doubleValue())))));

        compRepo.save(company.get());

        return ResponseEntity.ok(company.get());
    }

    @PutMapping("{id}")
    public ResponseEntity<Customer> update(@PathVariable Long id, @RequestBody @Valid Customer customer) {
        log.info("Updating customer with ID: " + id);

        Optional<Customer> cust = repo.findById(id);

        if (cust.isEmpty()) return ResponseEntity.notFound().build();

        BeanUtils.copyProperties(customer, cust.get(), "id");

        repo.save(cust.get());

        return ResponseEntity.ok(cust.get());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Customer> destroy(@PathVariable Long id) {
        log.info("Deleting customer with ID: " + id);

        Customer customer = repo.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found!")
        );

        repo.delete(customer);

        return ResponseEntity.noContent().build();
    }
}
