package estudo.osgi.web.bean;

import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class MenuBean {
    
    private HashMap<String, String> menus;

    public MenuBean() {
    }
    
    @PostConstruct
    public void popularMenus() {
        if (this.menus == null)
            this.menus = new HashMap<String, String>();
        this.menus.put("Página Principal", "principal.xhtml");
    }
}
