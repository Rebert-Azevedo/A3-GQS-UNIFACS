package com.unifacs.GQS_A3.Repository;

import com.unifacs.GQS_A3.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    @Query("SELECT c FROM Cliente AS c WHERE c.email = ?1")
    Cliente findByEmailAddress(String emailAddress);

}
