/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package estudo.osgi.cadastro;

import estudo.osgi.common.bean.Menu;
import estudo.osgi.common.bean.MenuFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ServiceLoader;

/**
 *
 * @author 09275643784
 */
public class MenuFactoryCadastro implements MenuFactory {

    public Collection<Menu> factoryMenus() {
        Collection<Menu> result = new ArrayList<Menu>();
        Menu menu = ServiceLoader.load(Menu.class).iterator().next();
        menu.setDescription("Cadastro");
        menu.setAction("cadastro.xhtml");
        result.add(menu);
        return result;
    }
    
}
