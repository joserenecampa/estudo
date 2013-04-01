package estudo.signer.docweb.bean;

public class File {
    
    private String name;
    private long size;
    private boolean signed;
    
    public File(String name, long size, boolean signed) {
        this.name = name;
        this.signed = signed;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isSigned() {
        return signed;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
    }
    
    
}
