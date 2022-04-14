package br.senai.sp.cfp138.clinicguia.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import br.senai.sp.cfp138.clinicguia.annotation.Publico;

@Component
public class AppInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		//variavel pra descobrir pra onde esta tentando ir 
		String uri = request.getRequestURI();
		//mostrando a uri
		System.out.println(uri);
		//verificando se o handler é um HandlerMethod, o que indica que foi encontrado um método em algum controller para a requisição
		if(handler instanceof HandlerMethod) {
			//liberando o acesso a pagina inicial
			if(uri.equals("/")) {
				return true;
			}
			//fazer o casting para o HandlerMethod
			HandlerMethod metodoChamado = (HandlerMethod) handler;
			//se o método for publico, libera
			if(metodoChamado.getMethodAnnotation(Publico.class) != null) {
				return true;
				
			}
			//verfica se existe um usuario logado
			if(request.getSession().getAttribute("usuarioLogado") != null) {
				return true;
			}else {
				//redirecionado para a página inicial
				response.sendRedirect("/");
				return false;
			}
		}
		
		return true;
	}
}
