package estudo.signer.docweb.bean;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@ManagedBean
@RequestScoped
public class FileController {
    
    private TreeNode root;
    
    private TreeNode[] selectedNodes;

    public FileController() {
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }
    
    @PostConstruct
    public void fillNodes() {
        root = new DefaultTreeNode("root", null);
        String uploadDir = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("upload-dir");
        if (uploadDir == null || uploadDir.trim().length() <= 0)
            uploadDir = "/tmp/upload";
        java.io.File path = new java.io.File(uploadDir);
        if (!path.exists()) {
            new DefaultTreeNode(new File("There is not file on directory", 0l, false), root);
            return;
        }
        java.io.File[] files = path.listFiles();
        if (files != null && files.length > 0) {
            for (java.io.File file : files) {
                new DefaultTreeNode(new File(file.getName(), file.length(), this.isSigned(file)), root);
            }
        }
    }
    
    private boolean isSigned(java.io.File file) {
        return false;
    }
}
