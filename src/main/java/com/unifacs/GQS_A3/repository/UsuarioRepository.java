package com.unifacs.GQS_A3.repository;

import com.unifacs.GQS_A3.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query("SELECT c FROM Usuario AS c WHERE c.email = ?1")
    Usuario findByEmailAddress(String emailAddress);

}
