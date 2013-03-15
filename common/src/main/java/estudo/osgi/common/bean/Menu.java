package estudo.osgi.common.bean;

import java.util.Collection;

public interface Menu {

    public void addChild(Menu child);

    public void addChildren(Collection<Menu> children);

    public String getAction();

    public Collection<Menu> getChildren();

    public String getDescription();

    public Menu getParent();

    public void setAction(String action);

    public void setChildren(Collection<Menu> children);

    public void setDescription(String description);

    public void setParent(Menu parent);
    
    public String action();
    
}
