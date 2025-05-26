package br.ufscar.dc.dsw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import br.ufscar.dc.dsw.dao.IEditoraDAO;
import br.ufscar.dc.dsw.dao.ILivroDAO;
import br.ufscar.dc.dsw.domain.Editora;
import br.ufscar.dc.dsw.domain.Livro;

@SpringBootApplication
public class LivrariaJpaApplication {

	private static final Logger log = LoggerFactory.getLogger(LivrariaJpaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(LivrariaJpaApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(ILivroDAO livroDAO, IEditoraDAO editoraDAO) {
		return (args) -> {

			// Add new publisher "Seguinte"
			log.info("Adding publisher Seguinte...");
			Editora editoraSeguinte = new Editora();
			editoraSeguinte.setCNPJ("87.557.922/0001-82");
			editoraSeguinte.setNome("Seguinte");
			editoraDAO.save(editoraSeguinte);
			log.info("Publisher Seguinte added.");

			// Add new book "O Dia do Curinga"
			log.info("Adding book O Dia do Curinga...");
			Editora editoraCuringa = editoraDAO.findByCNPJ("87.557.922/0001-82");
			if (editoraCuringa != null) {
				Livro livroCuringa = new Livro();
				livroCuringa.setTitulo("O Dia do Curinga");
				livroCuringa.setAutor("Jostein Gaarder");
				livroCuringa.setAno(1996);
				livroCuringa.setPreco(new BigDecimal("29.99"));
				livroCuringa.setEditora(editoraCuringa);
				livroDAO.save(livroCuringa);
				log.info("Book O Dia do Curinga added.");
			} else {
				log.error("Publisher Seguinte not found for book O Dia do Curinga.");
			}
			
			// Add new book "A Revolução dos Bichos"
			log.info("Adding book A Revolução dos Bichos...");
			Editora editoraCompanhia = editoraDAO.findByCNPJ("55.789.390/0008-99");
			if (editoraCompanhia == null) {
				log.info("Publisher Companhia das Letras not found, creating new one...");
				editoraCompanhia = new Editora();
				editoraCompanhia.setCNPJ("55.789.390/0008-99");
				editoraCompanhia.setNome("Companhia das Letras");
				editoraDAO.save(editoraCompanhia);
				log.info("Publisher Companhia das Letras added.");
			}
			
			Livro livroRevolucao = new Livro();
			livroRevolucao.setTitulo("A Revolução dos Bichos");
			livroRevolucao.setAutor("George Orwell");
			livroRevolucao.setAno(2007);
			livroRevolucao.setPreco(new BigDecimal("23.90"));
			livroRevolucao.setEditora(editoraCompanhia);
			livroDAO.save(livroRevolucao);
			log.info("Book A Revolução dos Bichos added.");

			// Recupere todos livros ordenados por preco
			log.info("Livros recuperados -- findAllByOrderByPrecoAsc():");
			log.info("--------------------------------");
			for (Livro livro : livroDAO.findAllByOrderByPrecoAsc()) {
				log.info(livro.toString());
			}
			log.info("");

			// Recupere um livro por seu ID
			Livro livroById = livroDAO.findById(1L);
			if (livroById != null) {
				log.info("Livro recuperado -- findById(1L):");
				log.info("---------------------------------");
				log.info(livroById.toString());
				log.info("");
			}
		};
	}

}
