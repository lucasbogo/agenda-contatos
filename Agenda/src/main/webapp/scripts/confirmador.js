function confirmar(idcon){
	let resposta = confirm("Confirma a exclusão deste contato?")
	if (resposta === true){
		
		/* Teste
		 *alert(idcon)
		 */
		 
		// encaminha requisição ao Servlet
		window.location.href = "delete?idcon=" + idcon
	}
}