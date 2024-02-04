package gabriel.dias.exe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import gabriel.dias.exe.models.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {}
