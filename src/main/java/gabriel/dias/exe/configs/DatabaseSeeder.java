package gabriel.dias.exe.configs;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import gabriel.dias.exe.models.Company;
import gabriel.dias.exe.models.Customer;
import gabriel.dias.exe.repositories.CompanyRepository;
import gabriel.dias.exe.repositories.CustomerRepository;

@Configuration
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    CompanyRepository compRepo;

    Company comp1 = new Company("Apple", new BigDecimal("3"), "32.269.172/0001-51");
    Company comp2 = new Company("Microsoft", new BigDecimal("1"), "89148386000197");
    Company comp3 = new Company("Amazon", new BigDecimal("2"), "30966535000182");
    Company comp4 = new Company("Netflix", new BigDecimal("4"), "70.907.315/0001-00");
    Company comp5 = new Company("Itaú", new BigDecimal("5"), "71.963.396/0001-10");

    @Autowired
    CustomerRepository custRepo;

    Customer cust1 = new Customer("Gabriel Dias", "gabriel@email.com", "939.689.910-01", comp1);
    Customer cust2 = new Customer("Martin Hilst", "martin@email.com", "36990643013", comp2);
    Customer cust3 = new Customer("João Luccas", "joao@email.com", "64699579000", comp3);
    Customer cust4 = new Customer("Leonardo Pestana", "leonardo@email.com", "78785112097", comp4);
    Customer cust5 = new Customer("Rodolfo Sanches", "rodolfo@email.com", "858.885.250-06", comp5);

    @Override
    public void run(String... args) throws Exception {
        custRepo.saveAll(List.of(cust1, cust2, cust3, cust4, cust5));
        compRepo.saveAll(List.of(comp1, comp2, comp3, comp4, comp5));
    }
}
