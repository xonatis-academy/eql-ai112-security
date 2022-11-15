package fr.eql.akatz.demo.petpal.spring.front.controller;

import fr.eql.akatz.demo.petpal.spring.front.entity.Owner;
import fr.eql.akatz.demo.petpal.spring.front.controller.webclient.LoginWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

@Component(value = "mbLogin")
@Scope(value = "session")
public class LoginManagedBean implements Serializable {

	/** Injecté par le setter. */
	private LoginWebClient loginWebClient;

	private String login;
	private String password;
	private Owner connectedOwner;

	@RequestMapping(value = {"/"})
	public String loginPage() {
		return "login.xhtml";
	}

	public String connect() {
		String forward;
		connectedOwner = loginWebClient.authenticate(login, password);
		if (connectedOwner != null) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
			session.setAttribute("connectedOwner", connectedOwner);
			forward = "/index.xhtml?faces-redirect=true";
		} else {
			FacesMessage facesMessage = new FacesMessage(
					FacesMessage.SEVERITY_WARN,
					"Identifiant et/ou mot de passe incorrect(s)",
					"Identifiant et/ou mot de passe incorrect(s)"
					);
			FacesContext.getCurrentInstance().addMessage("loginForm:inpLogin", facesMessage);
			FacesContext.getCurrentInstance().addMessage("loginForm:inpPassword", facesMessage);
			forward = "/login.xhtml?faces-redirect=false";
		}
		return forward;
	}

	public boolean isConnected() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
		Owner owner = (Owner) session.getAttribute("connectedOwner");
		return owner != null;
	}

	public void authorise() {
		FacesContext context = FacesContext.getCurrentInstance();
		ConfigurableNavigationHandler handler = (ConfigurableNavigationHandler)
				context.getApplication().getNavigationHandler();
		if (!isConnected()) {
			handler.performNavigation("/login.xhtml?faces-redirect=true");
		}
	}

	public String disconnect() {
		HttpSession session = (HttpSession) FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.getSession(true);
		session.invalidate();
		login = "";
		password = "";
		connectedOwner = null;
		return "/index.xhtml?faces-redirect=true";
	}

	/// Getters ///

	public String getLogin() {
		return login;
	}
	public String getPassword() {
		return password;
	}
	public Owner getConnectedOwner() {
		return connectedOwner;
	}

	/// Setters ///

	@Autowired
	public void setLoginWebClient(LoginWebClient loginWebClient) {
		this.loginWebClient = loginWebClient;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setConnectedOwner(Owner connectedOwner) {
		this.connectedOwner = connectedOwner;
	}
}
