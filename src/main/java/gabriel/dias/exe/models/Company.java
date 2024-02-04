package gabriel.dias.exe.models;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.math.BigDecimal;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;

import gabriel.dias.exe.controllers.CompanyController;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(
    name = "tb_company",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_company_cnpj",
        columnNames = "company_cnpj"
    )
)
public class Company {

    @Id
    @GeneratedValue(
        generator = "company_seq",
        strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(
        name = "company_seq",
        sequenceName = "company_seq",
        allocationSize = 1
    )
    @Column(name = "company_id")
    private Long id;

    @NotBlank
    @Column(name = "company_name")
    private String name;

    @Column(name = "company_balance")
    private BigDecimal balance = new BigDecimal("0");

    @NotNull
    @Min(
        value = 0,
        message = "Company's tax must be greater than 0% and lesser than 5%!"
    )
    @Max(
        value = 6,
        message = "Company's tax must be greater than 0% and lesser than 5%!"
    )
    @Column(name = "company_tax")
    private BigDecimal tax;

    @NotBlank
    @Pattern(regexp = "^\\d{2}\\.?\\d{3}\\.?\\d{3}\\/?\\d{4}\\-?\\d{2}$")
    @Column(name = "company_cnpj")
    private String cnpj;
    
    public Company() {}

    public Company(Long id, @NotBlank String name, @NotNull BigDecimal tax, @NotBlank @Pattern(regexp = "^\\d{2}\\.?\\d{3}\\.?\\d{3}\\/?\\d{4}\\-?\\d{2}$") String cnpj) {
        this.id = id;
        this.name = name;
        this.tax = tax;
        this.cnpj = cnpj;
    }

    public Company(@NotBlank String name, @NotNull BigDecimal tax, @NotBlank @Pattern(regexp = "^\\d{2}\\.?\\d{3}\\.?\\d{3}\\/?\\d{4}\\-?\\d{2}$") String cnpj) {
        this.name = name;
        this.tax = tax;
        this.cnpj = cnpj;
    }

    public EntityModel<Company> toModel() {
        return EntityModel.of(
            this,
            linkTo(methodOn(CompanyController.class).show(id)).withSelfRel(),
            linkTo(methodOn(CompanyController.class).destroy(id)).withRel("delete"),
            linkTo(methodOn(CompanyController.class).index(Pageable.unpaged())).withRel("listAll")
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    @Override
    public String toString() {
        return "Company [id=" + id + ", name=" + name + ", balance=" + balance + ", cnpj=" + cnpj + "]";
    }
}
