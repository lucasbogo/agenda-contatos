package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import model.DAO;
import model.JavaBeans;

@WebServlet(urlPatterns = { "/Controller", "/main", "/insert", "/select", "/update", "/delete", "/report" })
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Injeção DAO
	DAO dao = new DAO();

	// Injeção JavaBeans contato
	JavaBeans contato = new JavaBeans();

	public Controller() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getServletPath();
		System.out.println(action);
		if (action.equals("/main")) {
			contatos(request, response);
		} else if (action.equals("/insert")) {
			adicionarContato(request, response);
		} else if (action.equals("/select")) {
			listarContato(request, response);
		} else if (action.equals("/update")) {
			editarContato(request, response);
		} else if (action.equals("/delete")) {
			excluirContato(request, response);
		} else if (action.equals("/report")) {
			gerarRelatorio(request, response);
		} else {
			response.sendRedirect("index.html");
		}
	}

	// Listar contatos
	protected void contatos(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Criando um objeto que irá receber os dados JavaBeans
		ArrayList<JavaBeans> lista = dao.listarContatos();

		// Encaminhar a lista ao documento agenda.jsp
		request.setAttribute("contatos", lista);
		RequestDispatcher rd = request.getRequestDispatcher("agenda.jsp");
		rd.forward(request, response);
		/*
		 * // Teste de recebimento da lista for (int i = 0; i < lista.size(); i++);
		 * System.out.println(lista.get(i).getIdcon());
		 * System.out.println(lista.get(i).getNome());
		 * System.out.println(lista.get(i).getFone());
		 * System.out.println(lista.get(i).getEmail()); }
		 */
	}

	// Novo contato
	protected void adicionarContato(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * // Teste de recebimento dos dados do formulário
		 * System.out.println(request.getParameter("nome"));
		 * System.out.println(request.getParameter("fone"));
		 * System.out.println(request.getParameter("email"));
		 */

		// Setar as variáveis JavaBeans
		contato.setNome(request.getParameter("nome"));
		contato.setFone(request.getParameter("fone"));
		contato.setEmail(request.getParameter("email"));
		// Invocar o método inserirContato passando o objeto contato
		dao.inserirContato(contato);
		// Redirecionar para o documento agenda.jsp
		response.sendRedirect("main");
	}

	// Listar contato (necessário listar antes de poder editar...)
	protected void listarContato(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		contato.setIdcon(request.getParameter("idcon"));
		/*
		 * // Executar o método selecionarContato(DAO) dao.selecionarContato(contato);
		 *
		 * // Teste de recebimento System.out.println(contato.getIdcon());
		 * System.out.println(contato.getNome()); System.out.println(contato.getFone());
		 * System.out.println(contato.getEmail());
		 */

		// Setar os atributos do formulário com o conteúdo do JavaBeans
		request.setAttribute("idcon", contato.getIdcon());
		request.setAttribute("nome", contato.getNome());
		request.setAttribute("fone", contato.getFone());
		request.setAttribute("email", contato.getEmail());

		// Encaminhar ao documento editar.jsp
		RequestDispatcher rd = request.getRequestDispatcher("editar.jsp");
		rd.forward(request, response);
	}

	// Editar contato
	protected void editarContato(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * // Teste de recebimento System.out.println(request.getParameter("idcon"));
		 * System.out.println(request.getParameter("nome"));
		 * System.out.println(request.getParameter("fone"));
		 * System.out.println(request.getParameter("email"));
		 */
		
		// Setar as variáveis JavaBeans
		contato.setIdcon(request.getParameter("idcon"));
		contato.setIdcon(request.getParameter("nome"));
		contato.setIdcon(request.getParameter("fone"));
		contato.setIdcon(request.getParameter("email"));
		// Executar método alterarContato(JavaBeans Contato)
		dao.alterarContato(contato);
		// Redirecionar p/ o documento agenda.jsp (atualizando as alterações)
		response.sendRedirect("main");

	}

	// Excluir contato
	protected void excluirContato(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// Recebimento do id do contato a ser excluído (validador.js)
		@SuppressWarnings("unused")
		String idcon = request.getParameter("idcon");
		// Setar a variável JavaBeans
		contato.setIdcon(request.getParameter("idcon"));
		// Executar método alterarContato(JavaBeans Contato)
		dao.excluirContato(contato);
		// Redirecionar p/ o documento agenda.jsp (atualizando as alterações)
		response.sendRedirect("main");
	}

	// Gerar relatorio em PDF
	protected void gerarRelatorio(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// Gerar objeto de nome documento...
		Document documento = new Document();
		
		// Tratar execessões (try catch)
		try {
			// Tipo de conteúdo (PDF)
			response.setContentType("application/pdf");
			// Nome do documento
			response.addHeader("Content-Disposition ", "inline; filename=" + "contatos.pdf");
			// Criar o documento PDF
			PdfWriter.getInstance(documento, response.getOutputStream());
			// Abrir o documento para gerar o conteúdo PDF
			documento.open();
			// Especificar criação de paragráfo
			documento.add(new Paragraph("Lista de contatos:"));
			// Quebrar linha no documento PDF
			documento.add(new Paragraph(" "));
			// Criar uma tabela com 3 colunas
			PdfPTable tabela = new PdfPTable(3);
			// Criar cabeçalho (estático)
			PdfPCell col1 = new PdfPCell(new Paragraph("Nome"));
			PdfPCell col2 = new PdfPCell(new Paragraph("Fone"));
			PdfPCell col3 = new PdfPCell(new Paragraph("E-mail"));
			// Formatar em tabela (estático)
			tabela.addCell(col1);
			tabela.addCell(col2);
			tabela.addCell(col3);
			// Popular a tabela estática com contedo dinâmico
			ArrayList<JavaBeans> lista = dao.listarContatos();

			// Laço for para percorrer o vetor e gerar tabela de forma dinâmica
			for (int i = 0; i < lista.size(); i++) {
				
				tabela.addCell(lista.get(i).getNome());
				tabela.addCell(lista.get(i).getFone());
				tabela.addCell(lista.get(i).getEmail());
			}
			// Adicionar tabela
			documento.add(tabela);
			// Fechar o documento PDF
			documento.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
