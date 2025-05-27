package br.ufscar.dc.dsw.dao;

import br.ufscar.dc.dsw.domain.Editora;
import org.springframework.data.repository.CrudRepository;

public interface IEditoraDAO extends CrudRepository<Editora, Long> {
    Editora findById(long id);
    Editora findByCNPJ(String cnpj);
}
