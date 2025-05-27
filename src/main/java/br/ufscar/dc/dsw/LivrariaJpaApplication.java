package br.ufscar.dc.dsw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import br.ufscar.dc.dsw.dao.IEditoraDAO;
import br.ufscar.dc.dsw.dao.ILivroDAO;
import br.ufscar.dc.dsw.domain.Editora;
import br.ufscar.dc.dsw.domain.Livro;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SpringBootApplication
public class LivrariaJpaApplication {

	private static final Logger log = LoggerFactory.getLogger(LivrariaJpaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(LivrariaJpaApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(ILivroDAO dao, IEditoraDAO editoraDAO) {
		return (args) -> {

			// Create and Save New Publisher "Seguinte"
			Editora editoraSeguinte = new Editora();
			editoraSeguinte.setCNPJ("87.557.922/0001-82");
			editoraSeguinte.setNome("Seguinte");
			editoraDAO.save(editoraSeguinte);
			log.info("Editora Seguinte salva: " + editoraSeguinte);

			// Fetch Publishers
			Editora editoraSeguinteRecuperada = editoraDAO.findByCNPJ("87.557.922/0001-82");
			Editora editoraCompanhia = editoraDAO.findByCNPJ("55.789.390/0008-99");

			if (editoraSeguinteRecuperada != null) {
				log.info("Editora Seguinte recuperada: " + editoraSeguinteRecuperada);
			} else {
				log.warn("Editora Seguinte não encontrada!");
			}

			if (editoraCompanhia != null) {
				log.info("Editora Companhia das Letras recuperada: " + editoraCompanhia);
			} else {
				log.warn("Editora Companhia das Letras não encontrada!");
			}
			
			// Create and Save "O Dia do Curinga" Book
			if (editoraSeguinteRecuperada != null) {
				Livro livroCuringa = new Livro();
				livroCuringa.setTitulo("O Dia do Curinga");
				livroCuringa.setAutor("Jostein Gaarder");
				livroCuringa.setAno(1996);
				livroCuringa.setPreco(new BigDecimal("29.99"));
				livroCuringa.setEditora(editoraSeguinteRecuperada);
				dao.save(livroCuringa);
				log.info("Livro 'O Dia do Curinga' salvo: " + livroCuringa);
			} else {
				log.warn("Não foi possível salvar 'O Dia do Curinga' pois a editora Seguinte não foi encontrada.");
			}

			// Create and Save "A Revolução dos Bichos" Book
			if (editoraCompanhia != null) {
				Livro livroRevolucao = new Livro();
				livroRevolucao.setTitulo("A Revolução dos Bichos");
				livroRevolucao.setAutor("George Orwell");
				livroRevolucao.setAno(2007);
				livroRevolucao.setPreco(new BigDecimal("23.90"));
				livroRevolucao.setEditora(editoraCompanhia);
				dao.save(livroRevolucao);
				log.info("Livro 'A Revolução dos Bichos' salvo: " + livroRevolucao);
			} else {
				log.warn("Não foi possível salvar 'A Revolução dos Bichos' pois a editora Companhia das Letras não foi encontrada.");
			}

			// Recupere todos livros

			log.info("Livros recuperados -- findAll():");
			log.info("--------------------------------");
			Iterable<Livro> iterableLivros = dao.findAll();
			List<Livro> livros = new ArrayList<>();
			iterableLivros.forEach(livros::add);
			livros.sort(Comparator.comparing(Livro::getPreco));
			for (Livro livro : livros) {
				log.info(livro.toString());
			}
			log.info("");

			// Recupere um livro por seu ID

			Livro livro = dao.findById(1L);
			if (livro != null) {
				log.info("Livro recuperado -- findById(1L):");
				log.info("---------------------------------");
				log.info(livro.toString());
				log.info("");
			}
		};
	}

}
