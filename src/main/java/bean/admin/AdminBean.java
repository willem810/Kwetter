package bean.admin;

import bean.RedirectBean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named("adminBean")
@RequestScoped
public class AdminBean {



    public String goToUsers()  {
        return RedirectBean.redirect("/admin/users/index.xhtml");
    }
    public String goToTweets()  {
        return RedirectBean.redirect("/admin/tweets/index.xhtml");
    }
}
