/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package estudo.osgi.web.bean;

import javax.faces.bean.ManagedBean;

/**
 *
 * @author 09275643784
 */
@ManagedBean
public class HelloWorldManagedBean {
    
    private String message = "Hello World JSF 2.0";
    
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
