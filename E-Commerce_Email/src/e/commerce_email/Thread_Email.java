package e.commerce_email;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

public class Thread_Email extends Thread{
    
    //static String host = "u2.tech.hepl.be";
    static String host = "smtp.gmail.com";
    
    Thread_Email()
    {
        
    }
    
    public void run()
    {
        while(!isInterrupted()) {
            /*try
            {
                Properties prop = System.getProperties();
                System.out.println("Création d'une session mail");
                Session sess = Session.getDefaultInstance(prop, null);
                
                System.out.println("Obtention d'un objet store");
                Store st = sess.getStore("pop3");
                
                st.connect(host, "driessens.lionel@gmail.com", "pexgmvwdlbjihukg");
                System.out.println("Obtention d'un objet folder");
                Folder f = st.getFolder("INBOX");
                f.open(Folder.READ_ONLY);
                
                System.out.println("Obtention des messages");
                Message msg[] = f.getMessages();
                System.out.println("Nombre de messages : " + f.getMessageCount());
                System.out.println("Nombre de nouveaux messages : " + f.getNewMessageCount());
                System.out.println("Liste des messages : ");
                
                for (int i=0; i<msg.length; i++)
                {
                    System.out.println("Message n° " + i);
                    System.out.println("Expéditeur : " + msg[i].getFrom() [0]);
                    System.out.println("Sujet = " + msg[i].getSubject());
                    
                    System.out.println("Date : " + msg[i].getSentDate());
                    
                    // Récupération du conteneur Multipart
                    Multipart msgMP = (Multipart)msg[i].getContent();
                    int np = msgMP.getCount();
                    System.out.println("-- Nombre de composantes = " + np);

                    for (int j=0; j<np; j++)
                    {
                        System.out.println("--Composante n° " + j);
                        Part p = msgMP.getBodyPart(j);
                        String d = p.getDisposition();
                        if (p.isMimeType("text/plain"))
                        System.out.println("Texte : " + (String)p.getContent());
                        
                        if (d!=null && d.equalsIgnoreCase(Part.ATTACHMENT))
                        {
                            InputStream is = p.getInputStream();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            int c;
                            while ((c = is.read()) != -1) 
                                baos.write(c);
                            
                            baos.flush();
                            String nf = p.getFileName();
                            FileOutputStream fos =new FileOutputStream(nf);
                            baos.writeTo(fos);
                            fos.close();
                            System.out.println("Pièce attachée " + nf + " récupérée");
                        }
                    }
                }
                System.out.println("Fin des messages");
                
                System.out.println("sleep 5 min");
                Thread.sleep(300000); //300000 milisec = 5 min
            } 
            catch (InterruptedException ex) { 
                Logger.getLogger(Thread_Email.class.getName()).log(Level.SEVERE, null, ex);
            } 
            catch (MessagingException ex) {
                Logger.getLogger(Thread_Email.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Thread_Email.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        }
    }
}
