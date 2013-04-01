package estudo.osgi.common.bean;

import java.util.ArrayList;
import java.util.Collection;

public class MenuImpl implements Menu {
    
    private String description;
    private String action;
    private Menu parent;
    private Collection<Menu> children;

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public Menu getParent() {
        return parent;
    }

    @Override
    public void setParent(Menu parent) {
        this.parent = parent;
    }

    @Override
    public Collection<Menu> getChildren() {
        return children;
    }

    @Override
    public void setChildren(Collection<Menu> children) {
        this.children = children;
    }
    
    @Override
    public void addChild(Menu child) {
        if (this.children == null)
            this.children = new ArrayList<Menu>();
        child.setParent(this);
        this.children.add(child);
    }
    
    @Override
    public void addChildren(Collection<Menu> children) {
        if (children != null && children.size() > 0)
            for (Menu child : children)
                this.addChild(child);
    }
    
    @Override
    public String doAction() {
        System.out.println("Executando o médoto doAction: " + this.action);
        return this.action;
    }
    
}
