package estudo.osgi.web.bean;

import estudo.osgi.common.bean.Menu;
import estudo.osgi.common.bean.MenuFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ServiceLoader;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class MenuBean {
    
    private Collection<Menu> menus;

    public MenuBean() {
    }
    
    @PostConstruct
    public void popularMenus() {
        if (this.menus == null)
            this.menus = new ArrayList<Menu>();
        Menu menu = ServiceLoader.load(Menu.class).iterator().next();
        menu.setDescription("Página Principal");
        menu.setAction("principal.xhtml");
        this.menus.add(menu);
        ServiceLoader<MenuFactory> services = ServiceLoader.load(MenuFactory.class);
        for (Iterator<MenuFactory> i = services.iterator(); i.hasNext();) {
            MenuFactory factory = (MenuFactory) i.next();
            Collection<Menu> menus = factory.factoryMenus();
            this.menus.addAll(menus);
        }
    }
    
    public Collection<Menu> getMenus() {
        return this.menus;
    }
}
