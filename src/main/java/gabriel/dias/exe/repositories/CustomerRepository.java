package gabriel.dias.exe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import gabriel.dias.exe.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {}
