/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chmaquina;

import static chmaquina.entrada.etiq;
import static chmaquina.entrada.instrucciones;
import static chmaquina.entrada.nvariables;
import static chmaquina.entrada.var;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author aguir
 */
public class ambiente extends javax.swing.JFrame {

    /**
     * Creates new form ambiente
     */
    String ruta = "";
    String nombrea="";
    entrada archivo=new entrada();
    int programa=archivo.programa;
    
    public ambiente() {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false); 
        setTitle("MI CH-MAQUINA EDITOR");
        setIconImage(new ImageIcon(getClass().getResource("/imagenes/icono.png")).getImage());// icono d ela ventana del programa
        
         //fundamento encargado de la imagen de fondo del ch-maquina
        ((JPanel)getContentPane()).setOpaque(false); 
        ImageIcon uno=new ImageIcon(this.getClass().getResource("/imagenes/fon2.jpg"));
        JLabel fondo= new JLabel(); 
        fondo.setIcon(uno); 
        getLayeredPane().add(fondo,JLayeredPane.FRAME_CONTENT_LAYER);
        fondo.setBounds(0,0,uno.getIconWidth(),uno.getIconHeight());
        
    }
    
    
    public void guardar(){
        javax.swing.JFileChooser jF1= new javax.swing.JFileChooser(); 
        ruta = ""; 
        try{ 
            if(jF1.showSaveDialog(null)==jF1.APPROVE_OPTION){ 
            ruta = jF1.getSelectedFile().getAbsolutePath(); 
            //Aqui ya tiens la ruta,,,ahora puedes crear un fichero n esa ruta y escribir lo k kieras... 
            String text = panel.getText();
            
            nombrea=jF1.getName();
            String nombreArchivo= ruta; // Aqui se le asigna el nombre y 
            
            FileWriter file = null;	 // la extension al archivo 
            try { 
                file = new FileWriter(nombreArchivo); 
                BufferedWriter escribir = new BufferedWriter(file); 
                PrintWriter archivo = new PrintWriter(escribir); 

                archivo.print(text); 
                archivo.close(); 
                
                JOptionPane.showMessageDialog(null,"SE A CREADO EL NUEVO ARCHIVO ");
            } 
                catch (Exception e) { 
            }
            
            } 
            }catch (Exception ex){ 
                ex.printStackTrace(); 
} 
    }
       
    
    public void cargararchivo(){
      // encargado de abrir el panel de busqueda de archivos y cargarlo a la funcion actualizar.
        JFileChooser ventana = new JFileChooser();
        // filtra las extenciones segun la que buscamos
        ventana.setFileFilter(new FileNameExtensionFilter("todos los archivos "
                                                  + "*.ch", "CH","ch"));
        int sel = ventana.showOpenDialog(ambiente.this);
        
        // incrementan en uno el contador 
        
        
        
        // condicional que le dara el nombre a el programa
        String prefijo;
        if (programa<10) {
             prefijo="000"+String.valueOf(programa);
            programa++;
        }else{
             prefijo="00"+String.valueOf(programa);
        }
        
        if (sel == JFileChooser.APPROVE_OPTION) {

            File file = ventana.getSelectedFile();
            
            String nombrea=file.getName();
            actualizar(file.getPath(), prefijo,nombrea );

        }
  }
    
    // funcion encargada de leer el archivo y hacer el token
  public void actualizar(String url, String pre,String nombre){
      
        try{
             
            // leee el archivo y lo carga en bufer
            FileReader file = new FileReader(url);
            BufferedReader leer = new BufferedReader(file);
            String sCadena;
            String contenido="";
            // CICLO QUE RECORRE CADA LINEA  HASTA QUE LA LINEA SEA NULL
            while ((sCadena = leer.readLine())!=null) {
                // crea el contenido del  .ch
             contenido=contenido+sCadena+"\r\n";
            }
            panel.setText(contenido);// ingresa el contenido del archivo al panel
            contenido="";
       } 
            catch (Exception ex){ 
                ex.printStackTrace(); 
} 
        
        }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        panel = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        LIMPIAR = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        panel.setColumns(20);
        panel.setRows(5);
        jScrollPane1.setViewportView(panel);

        jButton1.setText("GUARDAR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        LIMPIAR.setText("LIMPIAR");
        LIMPIAR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LIMPIARActionPerformed(evt);
            }
        });

        jButton2.setText("TERMINAR");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("CARGAR ARCHIVO");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(63, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(LIMPIAR, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE))
                    .addComponent(jButton3))
                .addGap(39, 39, 39))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(LIMPIAR)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        guardar();
        

    }//GEN-LAST:event_jButton1ActionPerformed

    private void LIMPIARActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LIMPIARActionPerformed
        // TODO add your handling code here:
        panel.setText("");
    }//GEN-LAST:event_LIMPIARActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        cargararchivo();
// TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    
    /**
     * @param args the command line arguments
     */
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton LIMPIAR;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea panel;
    // End of variables declaration//GEN-END:variables
}
