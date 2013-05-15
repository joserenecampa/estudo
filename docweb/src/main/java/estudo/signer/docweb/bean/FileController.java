package estudo.signer.docweb.bean;

import java.io.IOException;
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
        String uploadDir = this.getUploadDir();
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
    
    private String getUploadDir() {
        String result = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("upload-dir");
        if (result == null || result.trim().length() <= 0)
            result = "/tmp/upload/";
        return result;
    }
    
    public void removeFiles() {
        if (this.selectedNodes == null || this.selectedNodes.length <= 0) {
            System.out.println("Não há arquivos a serem apagados");
            return;
        }
        for (TreeNode treeNode : this.selectedNodes) {
            if (treeNode.isLeaf()) {
                File file = (File)treeNode.getData();
                String filePath = this.getUploadDir() + file.getName();
                try {
                    java.io.File fileToRemove = new java.io.File(filePath);
                    fileToRemove.delete();
                } catch (Throwable error) {
                    System.out.println("Erro ao tentar apagar o arquivo [" + filePath + "]");
                }
            }
        }
        this.fillNodes();
    }
    
    private boolean isSigned(java.io.File file) {
        return false;
    }
}
