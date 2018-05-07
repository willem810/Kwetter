package bean;

import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

@Named("loginBean")
@Dependent
public class LoginBean implements Serializable{
    public void doLogout() {
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).invalidate();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(RedirectBean.redirect("/index.xhtml"));
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isLoggedIn() {
        return FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal() != null;
    }

    public String getPrincipalName() {

        if (isLoggedIn()) {
            return FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
        } else {
            return "ANONYMOUS";
        }

    }

}
