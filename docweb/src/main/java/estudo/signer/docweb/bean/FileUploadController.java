package estudo.signer.docweb.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;

@ManagedBean
@RequestScoped
public class FileUploadController {
    
    private String uploadDir = "/tmp/upload/";
    
    public FileUploadController() {
        String initParam = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("upload-dir");
        if (initParam != null && initParam.trim().length() > 0)
            this.uploadDir = initParam;
    }

    public void handleFileUpload(FileUploadEvent event) {
        File path = new File(this.uploadDir);
        if (!path.exists()) {
            path.mkdir();
        }
        if (!path.exists()) {
            throw new RuntimeException("Diretório não existe");
        }
        File arquivo = new File(path.getAbsolutePath() + File.separatorChar + event.getFile().getFileName());
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(arquivo);
        } catch (IOException exception) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Não foi possível armazenar o arquivo no diretório de destino"));
            throw new RuntimeException("Não foi possível armazenar o arquivo no diretório de destino");
        }
        try {
            fos.write(event.getFile().getContents());
        } catch (IOException exception) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao tentar gravar o arquivo no diretório de destino"));
            try { 
                fos.close();
            } catch (IOException error) {
            }
            throw new RuntimeException("Erro ao tentar gravar o arquivo no diretório de destino");
        }
        try {
            fos.close();
        } catch (IOException exception) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao fechar o arquivo escrito no diretório de destino"));
            throw new RuntimeException("Erro ao fechar o arquivo escrito no diretório de destino");
        }
    }
}
