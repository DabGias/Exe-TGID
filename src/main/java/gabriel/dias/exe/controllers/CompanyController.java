package gabriel.dias.exe.controllers;

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
import gabriel.dias.exe.repositories.CompanyRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/company")
public class CompanyController {
    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    CompanyRepository repo;

    @Autowired
    PagedResourcesAssembler<Company> assembler;

    @GetMapping
    public PagedModel<EntityModel<Company>> index(@PageableDefault(size = 20) Pageable pageable) {
        log.info("Return all companies!");

        Page<Company> page = repo.findAll(pageable);

        return assembler.toModel(page);
    }

    @GetMapping("{id}")
    public EntityModel<Company> show(@PathVariable Long id) {
        log.info("Return company with ID: " + id);

        Company company = repo.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found!")
        );

        return company.toModel();
    }

    @PostMapping
    public ResponseEntity<EntityModel<Company>> create(@RequestBody @Valid Company company) {
        log.info("Creating company: " + company);

        repo.save(company);

        return ResponseEntity.created(company.toModel().getRequiredLink("self").toUri()).body(company.toModel());
    }

    @PutMapping("{id}")
    public ResponseEntity<Company> update(@PathVariable Long id, @RequestBody @Valid Company company) {
        log.info("Updating company with ID: " + id);

        Optional<Company> comp = repo.findById(id);

        if (comp.isEmpty()) return ResponseEntity.notFound().build();

        BeanUtils.copyProperties(company, comp.get(), "id");

        repo.save(comp.get());

        return ResponseEntity.ok(comp.get());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Company> destroy(@PathVariable Long id) {
        log.info("Deleting company with ID: " + id);

        Company company = repo.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found!")
        );

        repo.delete(company);

        return ResponseEntity.noContent().build();
    }
}
