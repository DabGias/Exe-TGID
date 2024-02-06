package gabriel.dias.exe.models;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;

import gabriel.dias.exe.controllers.CustomerController;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(
    name = "tb_customer",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_customer_email",
            columnNames = "customer_email"
        ),
        @UniqueConstraint(
            name = "uk_customer_cpf",
            columnNames = "customer_cpf"
        )
    }
)
public class Customer {

    @Id
    @GeneratedValue(
        generator = "customer_seq",
        strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(
        name = "customer_seq",
        sequenceName = "customer_seq",
        allocationSize = 1
    )
    @Column(name = "customer_id")
    private Long id;

    @NotBlank
    @Pattern(regexp = "^[A-Z][a-z].* [A-Z][a-z]+$")
    @Column(name = "customer_name")
    private String name;

    @Email
    @Column(name = "customer_email")
    private String email;

    @NotBlank
    @Pattern(regexp = "^\\d{3}\\.?\\d{3}\\.?\\d{3}\\-?\\d{2}$")
    @Column(name = "customer_cpf")
    private String cpf;

    @ManyToOne(
        fetch = FetchType.EAGER,
        cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
        }
    )
    @JoinColumn(
        name = "company_id",
        referencedColumnName = "company_id",
        foreignKey = @ForeignKey(name = "fk_tb_company")
    )
    private Company company;

    public Customer() {}

    public Customer(@NotBlank @Pattern(regexp = "^[A-Z][a-z].* [A-Z][a-z]+$") String name, @Email String email, @NotBlank @Pattern(regexp = "^\\d{3}\\.?\\d{3}\\.?\\d{3}\\-?\\d{2}$") String cpf, Company company) {
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.company = company;
    }

    public EntityModel<Customer> toModel() {
        return EntityModel.of(
            this,
            linkTo(methodOn(CustomerController.class).show(id)).withSelfRel(),
            linkTo(methodOn(CustomerController.class).destroy(id)).withRel("delete"),
            linkTo(methodOn(CustomerController.class).index(Pageable.unpaged())).withRel("listAll")
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "Customer [id=" + id + ", name=" + name + ", email=" + email + ", cpf=" + cpf + ", company=" + company + "]";
    }
}
