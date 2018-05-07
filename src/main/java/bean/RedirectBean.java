package bean;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;

@Named("redirect")
@RequestScoped
public class RedirectBean implements Serializable {


    public static String redirect(String url) {
        return getRoot() + url;
    }

    public String goToAdminPanel() {
        return redirect("/admin/index.xhtml");
    }

    private static String getRoot() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
    }
}
