package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DAO {
	/** Módulo de Conexão **/
	// Paràmetros de Conexão

	// Driver
	private String driver = "com.mysql.cj.jdbc.Driver";
	
	// URL
	private String url = "jdbc:mysql://localhost:3306/dbagenda?useTimezone=true&serverTimezone=UTC";
	
	// User
	private String user = "root";
	
	// Password
	private String password = "sistemas23";

	// Método de Conexão
	private Connection conectar() {
		Connection con = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			return con;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	/*
	 * // Teste de conexão 
	 * public void testeConexao() { 
	 * try { 
	 * Connection con = conectar(); 
	 * System.out.println(con); 
	 * con.close(); 
	 * } 
	 * catch (Exception e) {
	 * System.out.println(e); 
	 * }
	 * 
	 * }
	 */

	/** CRUD CREATE **/
	public void inserirContato(JavaBeans contato) {
		String create = "insert into contatos (nome,fone,email) values (?,?,?)";

		try {
			// Abrir conexão com o BD
			Connection con = conectar();
			
			// Preparar a query para execução no banco de dados
			PreparedStatement pst = con.prepareStatement(create);
			
			// Substituir os parâmetros (?) pelo conteúdo das variáveis JavaBeans
			pst.setString(1, contato.getNome());
			pst.setString(2, contato.getFone());
			pst.setString(3, contato.getEmail());
			// Executar a query
			pst.executeUpdate();
			// Encerrar a conexão com o BD
			con.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/** CRUD READ **/
	public ArrayList<JavaBeans> listarContatos() {
		// Criando um objeto para acessar a classe JavaBeans
		ArrayList<JavaBeans> contatos = new ArrayList<>();
		
		String read = "select * from contatos order by nome";
		try {
			Connection con = conectar();
			PreparedStatement pst  = con.prepareStatement(read);
			ResultSet rs = pst.executeQuery();
			// Laço abaixo será executado enquanto houver contatos
			while (rs.next()) {
				// Variáveis de apoio que recebem os dados do BD
				String idcon = rs.getString(1);
				String nome = rs.getString(2);
				String fone = rs.getString(3);
				String email = rs.getString(4);
				// Populando o ArrayList
				contatos.add(new JavaBeans(idcon,nome,fone,email));
			}
			con.close();
			return contatos;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
		
	}
	
	/** CRUD UPDATE **/
	// Selecionar o contato
	@SuppressWarnings("unused")
	private void selecionarContato(JavaBeans contato) {
		String read2 = "select * from contatos where idcon = ?";
		try {
			Connection con = conectar();
			PreparedStatement pst  = con.prepareStatement(read2);
			pst.setString(1, contato.getIdcon());
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				// Setar as variáveis JavaBeans
				contato.setIdcon(rs.getString(1));
				contato.setNome(rs.getString(2));
				contato.setFone(rs.getString(3));
				contato.setEmail(rs.getString(4));
			}
			con.close();
		} catch (Exception e) {
			System.out.println(e);
			
		}
	
	}
	// Editar o contato
	public void alterarContato(JavaBeans contato) {
		String create = "update contatos set nome= ?, fone= ?, email= ? where idcon = ?;";
		try {
			Connection con = conectar();
			PreparedStatement pst = con.prepareStatement(create);
			pst.setString(1, contato.getNome());
			pst.setString(2, contato.getFone());
			pst.setString(1, contato.getEmail());
			pst.setString(1, contato.getIdcon());
			pst.executeUpdate();
			con.close();
			
		} catch (Exception e) {
			System.out.println(e);
			
		}
	}

	/* CRUD EXCLUIR CONTATO */
		public void excluirContato(JavaBeans contato) {
			String delete = "delete * from contatos where idcon = ?";
			try {
				Connection con = conectar();
				PreparedStatement pst  = con.prepareStatement(delete);
				pst.setString(1, contato.getIdcon());
				pst.executeUpdate();
				con.close();
			} catch (Exception e) {
				System.out.println(e);
				
			}
		
		}
}

