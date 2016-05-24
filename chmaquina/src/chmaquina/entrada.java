/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chmaquina;


import java.awt.Desktop;
import java.awt.print.PrinterException;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
 
/**
 *
 * @author aguir
 */
public class entrada extends JFrame {

    /**
     * Creates new form entrada
     */
    DefaultTableModel modelo,tprocesos,tvariables, tetiquetas;
    String arc = "documentacion.pdf";
    String mt="manual tecnico.pdf";
    String mu="manual usuario.pdf";
    int programa=1; // cantidad de programas cargados
    String memoriaprin[]; // vector de memoria principal
    public static ArrayList<String> instrucciones;
    public static ArrayList<String> nvariables;
    public static ArrayList<Object[]> var;
    public static ArrayList<Object[]> etiq;
    public static ArrayList<String[]> resultados;
    
    int pivote=0;
    int rlp;
    int inicialproceso=0, inicialvariables=0,inicialetiquetas=0;
    int llegada =1;
    
    public entrada() {
        initComponents();
        instrucciones=new ArrayList();
        nvariables=new ArrayList();
         var=new ArrayList();
         etiq=new ArrayList();
        setLocationRelativeTo(null);
        setResizable(true);                          // permite que la ventana principal se pueda maximizar o minimizar
        setExtendedState(JFrame.MAXIMIZED_BOTH);    // hace que la ventana siempre aparesca maximizada
        setTitle("MI CH-MAQUINA");                   // titulo  del programa
        setIconImage(new ImageIcon(getClass().getResource("/imagenes/icono.png")).getImage());// icono d ela ventana del programa
        cargarprograma.setEnabled(false);// impide cargar programas sin prender maquina
        // impide apagar la maquina sin encenderla
        apagarmaquina1.setEnabled(false);
        apagarmaquina2.setEnabled(false);
        // desactiva botones que no pueden ser inicializados sin orden previa
        botoncargar.setEnabled(false);
        ejecutar.setVisible(false);
        editor.setVisible(false);
        pasoapaso.setVisible(false);
        IMP.setEnabled(false);
        EJEC.setEnabled(false);
        resultados=new ArrayList();
        
        
        //fundamento encargado de la imagen de fondo del ch-maquina
        ((JPanel)getContentPane()).setOpaque(false); 
        ImageIcon uno=new ImageIcon(this.getClass().getResource("/imagenes/fon1.jpg"));
        JLabel fondo= new JLabel(); 
        fondo.setIcon(uno); 
        getLayeredPane().add(fondo,JLayeredPane.FRAME_CONTENT_LAYER);
        fondo.setBounds(0,0,uno.getIconWidth(),uno.getIconHeight());
        
        
        
        
        //definenlos valores por defecto de la memoria del kernel y la memoria disponible para programas
        int maxmemo=9999, maxkerner=1000;
        memoria.setModel(new javax.swing.SpinnerNumberModel(100, 2, maxmemo, 1));
        kernel.setModel(new javax.swing.SpinnerNumberModel(29, 1, maxkerner, 1));
        total_memoria.setText("71");
       
        
        //CREA EL TIPO DE MODELO DE TABLA para mapa de memoria
        modelo = new DefaultTableModel();
        tabla.setModel(modelo);
        // CREAN LOS NOMBRES DE LAS COLUMNAS
        modelo.addColumn("POS-MEMO");
        modelo.addColumn("PROGRAMA");
        modelo.addColumn("INSTRUCCION");
        modelo.addColumn("ARGUMENTO");
        modelo.addColumn("VALOR");
        //redimenciona la columna
        TableColumn columna = tabla.getColumn("POS-MEMO");
        columna.setPreferredWidth(80);// pixeles por defecto
        columna.setMinWidth(50);//pixeles minimo
        columna.setMaxWidth(90);// pixeles maximo
        
        TableColumn PRE = tabla.getColumn("PROGRAMA");
        PRE.setPreferredWidth(80);// pixeles por defecto
        PRE.setMinWidth(10);//pixeles minimo
        PRE.setMaxWidth(200);// pixeles maximo
        
        TableColumn PREE = tabla.getColumn("INSTRUCCION");
        PREE.setPreferredWidth(120);// pixeles por defecto
        PREE.setMinWidth(10);//pixeles minimo
        PREE.setMaxWidth(200);// pixeles maximo
        
        TableColumn PREEE = tabla.getColumn("VALOR");
        PREEE.setPreferredWidth(80);// pixeles por defecto
        PREEE.setMinWidth(10);//pixeles minimo
        PREEE.setMaxWidth(200);// pixeles maximo
        
        //CREA EL TIPO DE MODELO DE TABLA para procesos
        tprocesos = new DefaultTableModel();
        tabla2.setModel(tprocesos);
        // CREAN LOS NOMBRES DE LAS COLUMNAS
        tprocesos.addColumn("ID");
        tprocesos.addColumn("PROGRAMAS");
        tprocesos.addColumn("#INST");
        tprocesos.addColumn("RB");
        tprocesos.addColumn("RLC");
        tprocesos.addColumn("RLP");
        tprocesos.addColumn("PRIO");
        tprocesos.addColumn("T LLEG");
        
        //redimenciona la columna
        TableColumn id = tabla2.getColumn("ID");
        id.setPreferredWidth(40);// pixeles por defecto
        id.setMinWidth(10);//pixeles minimo
        id.setMaxWidth(41);// pixeles maximo
        
        TableColumn pro = tabla2.getColumn("PROGRAMAS");
        pro.setPreferredWidth(100);// pixeles por defecto
        pro.setMinWidth(10);//pixeles minimo
        pro.setMaxWidth(501);// pixeles maximo
        
        TableColumn ins = tabla2.getColumn("#INST");
        ins.setPreferredWidth(50);// pixeles por defecto
        ins.setMinWidth(10);//pixeles minimo
        ins.setMaxWidth(51);// pixeles maximo
        
        TableColumn rb = tabla2.getColumn("RB");
        rb.setPreferredWidth(40);// pixeles por defecto
        rb.setMinWidth(10);//pixeles minimo
        rb.setMaxWidth(41);// pixeles maximo
        
        TableColumn rcl = tabla2.getColumn("RLC");
        rcl.setPreferredWidth(40);// pixeles por defecto
        rcl.setMinWidth(10);//pixeles minimo
        rcl.setMaxWidth(41);// pixeles maximo
        
        TableColumn rlp = tabla2.getColumn("RLP");
        rlp.setPreferredWidth(40);// pixeles por defecto
        rlp.setMinWidth(10);//pixeles minimo
        rlp.setMaxWidth(41);// pixeles maximo
        
        TableColumn prio = tabla2.getColumn("PRIO");
        prio.setPreferredWidth(40);// pixeles por defecto
        prio.setMinWidth(10);//pixeles minimo
        prio.setMaxWidth(41);// pixeles maximo
        
        TableColumn tt = tabla2.getColumn("T LLEG");
        tt.setPreferredWidth(40);// pixeles por defecto
        tt.setMinWidth(10);//pixeles minimo
        tt.setMaxWidth(51);// pixeles maximo
        
        //CREA EL TIPO DE MODELO DE TABLA para variables
        tvariables = new DefaultTableModel();
        tablavariables.setModel(tvariables);
        // CREAN LOS NOMBRES DE LAS COLUMNAS
        tvariables.addColumn("POS");
        tvariables.addColumn("PROG");
        tvariables.addColumn("TIPO");
        tvariables.addColumn("VARIABLES");
        tvariables.addColumn("VALOR");
        
        //redimenciona la columna
        TableColumn POS = tablavariables.getColumn("POS");
        POS.setPreferredWidth(40);// pixeles por defecto
        POS.setMinWidth(40);//pixeles minimo
        POS.setMaxWidth(41);// pixeles maximo
        
        TableColumn prog = tablavariables.getColumn("PROG");
        prog.setPreferredWidth(60);// pixeles por defecto
        prog.setMinWidth(10);//pixeles minimo
        prog.setMaxWidth(61);// pixeles maximo
        
        
        
        TableColumn vLr = tablavariables.getColumn("VALOR");
        vLr.setPreferredWidth(60);// pixeles por defecto
        vLr.setMinWidth(10);//pixeles minimo
        vLr.setMaxWidth(61);// pixeles maximo
        
        
        //CREA EL TIPO DE MODELO DE TABLA para etiquetas
        tetiquetas = new DefaultTableModel();
        tablaetiquetas.setModel(tetiquetas);
        // CREAN LOS NOMBRES DE LAS COLUMNAS
        tetiquetas.addColumn("POS");
        tetiquetas.addColumn("PROG");
        tetiquetas.addColumn("ETIQUETAS");
        tetiquetas.addColumn("ARGUMENTO");
        //redimenciona la columna
        TableColumn POSS = tablaetiquetas.getColumn("POS");
        POSS.setPreferredWidth(50);// pixeles por defecto
        POSS.setMinWidth(40);//pixeles minimo
        POSS.setMaxWidth(90);// pixeles maximo
        
        TableColumn POG = tablaetiquetas.getColumn("PROG");
        POG.setPreferredWidth(50);// pixeles por defecto
        POG.setMinWidth(40);//pixeles minimo
        POG.setMaxWidth(90);// pixeles maximo
        
        TableColumn POGG = tablaetiquetas.getColumn("ETIQUETAS");
        POGG.setPreferredWidth(70);// pixeles por defecto
        POGG.setMinWidth(40);//pixeles minimo
        POGG.setMaxWidth(90);// pixeles maximo
              
        // evita editar el contenido de los jtextpanel
        monitor.setEditable(false);
        impresora.setEditable(false);
         
    }
    
    
    
    
    //funcion encargada de capturar los valores  de kernel y memoria  y mostrar la cantidad de 
    //memoria que queda disponible para  la asignacion de los programas
    public void memtotal(){
        int mem = (int) memoria.getValue();
        int ker = (int) kernel.getValue();
        int total= mem - ker;
        total_memoria.setText(String.valueOf(total));
        
    }
    
    
   // funcion  para reproducier sonidos 
   public Clip clip;
   public String ruta="/audio/";
   
           
  public void son(String archivo){
      //carga en bufer el archivo de audio
      BufferedInputStream Mystream = new BufferedInputStream(getClass().getResourceAsStream(ruta+archivo+".wav")); 
    
      try{
          //ejecuta el audio 
           AudioInputStream song = AudioSystem.getAudioInputStream(Mystream); 
           Clip sonido = AudioSystem.getClip(); 
           sonido.open(song); 
           sonido.start();
           }catch(Exception e){
           
       }
  }  
  
  
  
  
  
  // funcion enargada de encender la maquina y cargar la memoria
  public void encender(){
       // HACE EL LLAMADO A LA FUNCION PARA QUE REPRODUSCA EL SONIDO DE ENSENDIDO
        // desactiva los spinner
        kernel.setEnabled(false);
        memoria.setEnabled(false);
        encender.setEnabled(false);
        encender2.setEnabled(false);
        cargarprograma.setEnabled(true);
        apagarmaquina1.setEnabled(true);
        apagarmaquina2.setEnabled(true);
        botoncargar.setEnabled(true);
        estado.setText("MODO KEREL");
        editor.setVisible(true);
        procesos.setEnabled(false);
        
         //sonidoencender("inicio");
        son("inicio");
        JOptionPane.showOptionDialog(this, "CH-MAQUINA  TRABAJA CON PARTICION DE MEMORIA POR TAMAÑO DE PROGRAMA\n"
                        , "PARTICION DE MEMORIA DE  CH-MAQUINA.", 
                JOptionPane.INFORMATION_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{" OK "},"OK");
        rlp = (int)kernel.getValue()+1; // inicualiza el primer rcl
        // INSTANCIA OBJETO PARA LLENAR  LA TABLA
        Object []object = new Object[5];
        // inicializa  la memoria principal con el tamaño de memoria establecido
        memoriaprin= new String[(int)memoria.getValue()];
        // VALORES POR DEFECTO DE LA PRIMERA POSICION DEL MAPA D EMEMORIA
        object[0]="0";
        object[1]="0000";
        object[2]="----";
        object[3]="acumulador";
        object[4]="0";
        memoriaprin[0]="acumulador";// carga en la memoria  
        modelo.addRow(object);
     
      // CICLOS ENCARGADOS DE LLENAR  EL MAPA DE MEMORIA
     int contador = 0;
     
     int mem = Integer.parseInt(total_memoria.getText());
     int ker = (int) kernel.getValue();
        for (int i = 0; i < ker; i++) {
            contador++;
            object[0]=String.valueOf(contador);
            object[1]="0000";
            object[2]="----";
            object[3]="-----sistema operativo-----";
            object[4]="----";
            modelo.addRow(object);
            memoriaprin[i+1]="-----sistema operativo-----";
        }
        
        pivote = pivote + ker +1; // crea un pivote marcador de inicio de primer programa
        for (int i = 0; i <mem-1; i++) {
            contador++;
            object[0]=String.valueOf(contador);
            object[1]="";
            object[2]="";
            object[3]="";
            object[4]="";
            modelo.addRow(object);
        }
        
        
        // se encarga de  crear el contenido de un programa en la tabla de procesos 
           Object []objectprocesos = new Object[8];
           objectprocesos[0]="0000";// # instancias
           objectprocesos[1]="SISTEMA OPERATIVO ch-maquina"; // nombre del programa
           objectprocesos[2]=ker; // numero  de lineas del programa
           objectprocesos[3]=1; // rb
           objectprocesos[4]=ker; //registro limite de el programa
           objectprocesos[5]=ker ; // crea el rlp
           objectprocesos[6]="oo";
           objectprocesos[7]="oo";
           tprocesos.addRow(objectprocesos);// adiciona a la tabla
           
  }
  
  // se encarga de borrar los archivos temporales
  public void temporales(){
      
      File temp = new File(arc);
      File temp1 = new File(mt);
      File temp2 = new File(mu);
        temp.delete();
        temp1.delete();
        temp2.delete();
     File temp3 = new File("editor.txt");
            temp3.delete();
      
  }
  
  // funcion encargada de apagar la maquina y regresarla  asu estado  inicial
  public void apagar(){
      // codigo encargado de apagar la maquina y regresarla a el estado inicial
    temporales();// borra los archivos temporales
    
  if(JOptionPane.showOptionDialog(this, "¿ESTA SEGURO QUE DESEA APAGAR LA MAQUINA?", "Mensaje de Alerta",
          JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{" SI "," NO "},"NO")==0)
{
       son("cierre");
       // se encarga de  detener un instante el proceso
        try {
            Thread.sleep(2000);// el tiempo es en milisegundos
            } catch (InterruptedException ex) {

        }
       setVisible(false);
       new entrada().setVisible(true);
}
else
{
       JOptionPane.showMessageDialog(this, "PUEDE CONTINUAR CON LA EJECUCION DEL PROGRAMA");
}
  
  
  
  }
  
  public void cargararchivo(){
      // encargado de abrir el panel de busqueda de archivos y cargarlo a la funcion actualizar.
        JFileChooser ventana = new JFileChooser();
        // filtra las extenciones segun la que buscamos
        ventana.setFileFilter(new FileNameExtensionFilter("todos los archivos "
                                                  + "*.ch", "CH","ch"));
        int sel = ventana.showOpenDialog(entrada.this);
        
        // incrementan en uno el contador 
        inicialproceso++;
        
        
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
      
      // carga en el sistema la prioridad que tendra el programa
     int prioridad=0;
      SpinnerNumberModel sModel = new SpinnerNumberModel(1, 1, 100, 1);
        JSpinner spinner = new JSpinner(sModel);
        int pri = JOptionPane.showOptionDialog(null, spinner, "INGRESE VALOR DE LA PRIORIDAD", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (pri == JOptionPane.OK_OPTION)
        {
            prioridad = (int) spinner.getValue();
        }
       
        
      
      
       int lexa =0;
       long lNumeroLineas = 0;// INICIALIZA EL CONTADOR DE LAS LINEAS DEL ARCHIVO
       // ALMACENARA LA LISTA DELOS ERRORES ENCONTRADOS 
            String  errores= "";
        try{
             instrucciones.clear();
            nvariables.clear();
            // limpia los arreglos para que no queden rastros del programa anterior
          etiq.clear();
          var.clear();
            // leee el archivo y lo carga en bufer
            FileReader file = new FileReader(url);
            BufferedReader leer = new BufferedReader(file);
            
            //Inicializo todas las variables a leer
            //de forma general
            
            String operacion="";// alamcena el primer token  de la linea examinada
            
            String variablenueva="", tipo="", valor="";
            String nombreetiqueta="", numerolinea="";
            String variablealmacene="", variablecargue="";
           
            
            // variables  para calculos matematicos
            String variablesume="";
            String variablereste="";
            String variablemultiplique="";
            String variabledivida="";
            String variablepotencia="";
            String variablemodulo="";
            String variableconcatene="";
            String variableelimine="",variableextraiga="";
            
            
            
            // ciclos
            String etiquetaini="";
            String etiquetainicio="", etiquetafin="";
            
            // entrega de resultados
            String variablemuestre="";
            String variableimprimir="";
            
            
            //operaciones con cadenas
            String variablelea="";
            
            
           
            
            // SE ENCARGA DE RECORRER EL ARCHIVO Y CONTAR LA CANTIDAD DE LINEAS
            
            String sCadena;
            // CICLO QUE RECORRE CADA LINEA  HASTA QUE LA LINEA SEA NULL
            while ((sCadena = leer.readLine())!=null) {
            lNumeroLineas++;
            }
            
           
           
           
            FileReader file2 = new FileReader(url);
            BufferedReader leer2 = new BufferedReader(file2);
          
            
            int inicialmemoria=pivote;
            int inicialprocesos= inicialproceso;
            int inicialvariable=inicialvariables; 
            int inicialetiqueta=inicialetiquetas;
            
            int posi=pivote-1; // nos dice en que pisision va almacenando instrucciones
            
            
            // captura la cantidad de filas ocupadas de la tabla d evariables
            int q=tvariables.getRowCount();
            // FOR ENCARGADO DE RECORRER  EL ARCHIVO LINEA POR LINEA PARA HACER LOS TOKENS
            for (int i=0; i<lNumeroLineas; i++){
                errores= "**** SUGERENCIAS PARA CORREGIR LOS ERRORES ENCONTRADOS ****\n\n";
                //Se usa 'StringTokenizer' para tomar toda la linea  examinada
                posi++; // aumenta en uno  las posiciones d ememoria para ocupar
                String linea=leer2.readLine().trim();
                
                lexa++;
                StringTokenizer tk = new StringTokenizer(linea);
            
                // condiciona la linea para saber si esta vacia
                if (linea.length()>0) {
                 operacion= (tk.nextToken());
                
                }else{
                    // en caso tal que la linea este vacia  
                    operacion=" ";
                
                }
                // evalua por casos  cada linea y hace los tokens  correspondientes
                 switch (operacion) {
                        case "cargue":
                            //verifica  si esta bn el formato sino salta el error
                            if (tk.countTokens()==1) {
                                 // hace el segundo token de la linea
                            variablecargue= (tk.nextToken());
                                                        
                            //agrega la linea completa al mapa de memoria
                            modelo.setValueAt(pre, posi, 1);// guarda en la tabla  de memoria el numero del programa
                            modelo.setValueAt(operacion, posi, 2);// guarda en la tabla  la instruccion del programa
                            modelo.setValueAt(linea, posi, 3);// guarda en la tabla el argumento de memoria
                            modelo.setValueAt(variablecargue, posi, 4);// guarda en la tabla el valor de memoria
                            memoriaprin[posi]=linea; // guarda en el vector principal de memoria
                            instrucciones.add(pre + " " + linea);
                            break;
                            }else{
                                errores=errores+"CARGUE debe tener dos argumentos en esta linea";
                                throw new Exception("Invalid entry");
                            }
                            
                        
                        case "almacene":
                             //verifica  si esta bn el formato sino salta el error
                            if (tk.countTokens()==1) {
                            // hace el segundo token de la linea
                            variablealmacene= (tk.nextToken());
                            //agrega en el array list de instrucciones
                            
                            modelo.setValueAt(pre, posi, 1);// guarda en la tabla  de memoria el numero del programa
                            modelo.setValueAt(operacion, posi, 2);// guarda en la tabla  la instruccion del programa
                            modelo.setValueAt(linea, posi, 3);// guarda en la tabla el argumento de memoria
                            modelo.setValueAt(variablealmacene, posi, 4);// guarda en la tabla el valor de memoria
                            memoriaprin[posi]=linea; // guarda en el vector principal de memoria
                            instrucciones.add(pre + " " + linea);
                            break;
                            }else{
                                errores=errores+"ALAMCENE debe tener dos argumentos en esta linea";
                                throw new Exception("Invalid entry");
                            }
                            
                        case "vaya":
                            //verifica  si esta bn el formato sino salta el error
                            if (tk.countTokens()==1) {
                            // hace el segundo token de la linea
                            etiquetaini= (tk.nextToken());
                            //agrega en el array list de instrucciones
                            
                            //agrega la linea completa al mapa de memoria
                            modelo.setValueAt(pre, posi, 1);// guarda en la tabla  de memoria el numero del programa
                            modelo.setValueAt(operacion, posi, 2);// guarda en la tabla  la instruccion del programa
                            modelo.setValueAt(linea, posi, 3);// guarda en la tabla el argumento de memoria
                            modelo.setValueAt(etiquetaini, posi, 4);// guarda en la tabla el valor de memoria
                            
                            memoriaprin[posi]=linea; // guarda en el vector principal de memoria
                            instrucciones.add(pre + " " + linea);
                            break;
                            }else{
                                errores=errores+"VAYA debe tener dos argumentos en esta linea";
                                throw new Exception("Invalid entry");
                            }
                        
                        case "vayasi":
                            //verifica  si esta bn el formato sino salta el error
                            if (tk.countTokens()==2) {
                            // hace el segundo token de la linea
                            etiquetaini= (tk.nextToken());
                            etiquetafin=(tk.nextToken());
                            //agrega en el array list de instrucciones
                            //agrega la linea completa al mapa de memoria
                            modelo.setValueAt(pre, posi, 1);// guarda en la tabla  de memoria el numero del programa
                            modelo.setValueAt(operacion, posi, 2);// guarda en la tabla  la instruccion del programa
                            modelo.setValueAt(linea, posi, 3);// guarda en la tabla el argumento de memoria
                            modelo.setValueAt(etiquetaini+";"+etiquetafin, posi, 4);// guarda en la tabla el valor de memoria
                            memoriaprin[posi]=linea; // guarda en el vector principal de memoria
                            instrucciones.add(pre + " " + linea);
                            break;  
                            }else{
                                errores=errores+"VAYASI debe tener tres argumentos en esta linea";
                                throw new Exception("Invalid entry");
                            }
                            
                        case "nueva":
                            
                            if (tk.countTokens()>1 && tk.countTokens()<4) {
                                
                               inicialvariables++;
                                System.out.println("cantidad de tokens "+tk.countTokens());
                            // hace el segundo token de la linea
                            variablenueva= (tk.nextToken());
                                
                            tipo=(tk.nextToken());
                            System.out.println("tipo  "+tipo);
                                
                                if (tk.countTokens()<1) {
                                    if ("c".equals(tipo) || "C".equals(tipo)  ) {
                                        valor=" ";
                                    }else{
                                        valor="0";
                                    }
                                }else{
                                     valor= (tk.nextToken());
                                }
                             
                           
                            //agrega en el array list de instrucciones
                            //agrega la linea completa al mapa de memoria
                            modelo.setValueAt(pre, posi, 1);// guarda en la tabla  de memoria el numero del programa
                            modelo.setValueAt(operacion, posi, 2);// guarda en la tabla  la instruccion del programa
                            modelo.setValueAt(linea, posi, 3);// guarda en la tabla el argumento de memoria
                            modelo.setValueAt(valor, posi, 4);// guarda en la tabla el valor de memoria
                            
                            memoriaprin[posi]=linea; // guarda en el vector principal de memoria
                            
                            Object nuevo[]=new Object[5];
                            nuevo[0]="";
                            nuevo[1]=pre;
                            nuevo[3]=variablenueva;
                            switch (tipo){
                                case "i":
                                case "I":
                                    tipo="ENTERO";
                                    break;
                                
                                case "r":
                                case "R":
                                    tipo="REAL";
                                    break;
                                            
                                case "c":
                                case "C":
                                    tipo="CADENA";
                                    break;
                                default:
                                    errores= errores + "*parece error en el argumento tipo de variable en NUEVA\n"
                                            + "debe ser '(i)' para enteros '(r)' para reales\n"
                                            + "o '(c)' para caracteres";
                                    System.out.println("entro");
                                    throw new Exception("Invalid entry");
                                    
                                    
                            }
                            nuevo[2]=tipo;
                            nuevo[4]=valor;
                            var.add(nuevo); // almacena en laun array list para luego pasarlo a la  tabla variables 
                            
                            nvariables.add(variablenueva) ;
                            nvariables.add(valor) ;
                            break; 
                            }else{
                                errores=errores+"NUEVA debe tener tres  o cuatro argumentos en esta linea";
                                throw new Exception("Invalid entry");
                            }
                            
                        
                        case "etiqueta":
                            //verifica  si esta bn el formato sino salta el error
                            if (tk.countTokens()==2) {
                             // hace el segundo token de la linea
                            nombreetiqueta =(tk.nextToken());
                            numerolinea=(tk.nextToken());
                            
                            
                            inicialetiquetas++;
                            //agrega la linea completa al mapa de memoria
                            modelo.setValueAt(pre, posi, 1);// guarda en la tabla  de memoria el numero del programa
                            modelo.setValueAt(operacion, posi, 2);// guarda en la tabla  la instruccion del programa
                            modelo.setValueAt(linea, posi, 3);// guarda en la tabla el argumento de memoria
                            modelo.setValueAt(nombreetiqueta, posi, 4);// guarda en la tabla el valor de memoria
                            memoriaprin[posi]=linea; // guarda en el vector principal de memoria
                            
                            Object netiqueta[]=new Object[4];
                            
                            // carga de nuevo el documento para recorrerlo de nuevo y encontrar la linea a etiquetar
                            FileReader file3 = new FileReader(url);
                            BufferedReader leer3 = new BufferedReader(file3);
                            String eti="";
                            int j;
                            // recorre el documento hasta la linea requerida 
                            for ( j = 0; j < Integer.parseInt(numerolinea); j++) {
                                eti=leer3.readLine();
                            }
                            
                            netiqueta[0]=pivote+j-1;//posicion en memoria
                            netiqueta[1]=pre; //programa al q pertenece
                            netiqueta[2]=nombreetiqueta;// nombre etiqueta mas
                            netiqueta[3]=eti; // la linea renombrada
                            etiq.add(netiqueta); // adiciona el arreglo a la tabla etiquetas
                            break;
                            }else{
                                errores=errores+"ETIQUETA debe tener tres   argumentos en esta linea";
                                throw new Exception("Invalid entry");
                            }
                        
                        case "lea":
                             //verifica  si esta bn el formato sino salta el error
                            if (tk.countTokens()==1) {
                            variablelea=(tk.nextToken());
                            //agrega la linea completa al mapa de memoria
                            modelo.setValueAt(pre, posi, 1);// guarda en la tabla  de memoria el numero del programa
                            modelo.setValueAt(operacion, posi, 2);// guarda en la tabla  la instruccion del programa
                            modelo.setValueAt(linea, posi, 3);// guarda en la tabla el argumento de memoria
                            modelo.setValueAt(variablelea, posi, 4);// guarda en la tabla el valor de memoria
                            memoriaprin[posi]=linea; // guarda en el vector principal de memoria
                            break;
                            }else{
                                errores=errores+"LEA debe tener tres   argumentos en esta linea";
                                throw new Exception("Invalid entry");
                            }
                        
                        case "sume":
                             //verifica  si esta bn el formato sino salta el error
                            if (tk.countTokens()==1) {
                            variablesume=(tk.nextToken());
                             //agrega la linea completa al mapa de memoria
                            modelo.setValueAt(pre, posi, 1);// guarda en la tabla  de memoria el numero del programa
                            modelo.setValueAt(operacion, posi, 2);// guarda en la tabla  la instruccion del programa
                            modelo.setValueAt(linea, posi, 3);// guarda en la tabla el argumento de memoria
                            modelo.setValueAt(variablesume, posi, 4);// guarda en la tabla el valor de memoria
                            memoriaprin[posi]=linea; // guarda en el vector principal de memoria
                            break;
                            }else{
                                errores=errores+"SUME debe tener dos   argumentos en esta linea";
                                throw new Exception("Invalid entry");
                            }
                            
                        case "reste":
                             //verifica  si esta bn el formato sino salta el error
                            if (tk.countTokens()==1) {
                           variablereste=(tk.nextToken());
                             //agrega la linea completa al mapa de memoria
                            modelo.setValueAt(pre, posi, 1);// guarda en la tabla  de memoria el numero del programa
                            modelo.setValueAt(operacion, posi, 2);// guarda en la tabla  la instruccion del programa
                            modelo.setValueAt(linea, posi, 3);// guarda en la tabla el argumento de memoria
                            modelo.setValueAt(variablereste, posi, 4);// guarda en la tabla el valor de memoria
                            memoriaprin[posi]=linea; // guarda en el vector principal de memoria
                            break;
                            }else{
                                errores=errores+"RESTE debe tener dos   argumentos en esta linea";
                                throw new Exception("Invalid entry");
                            }
                            
                        case "multiplique":
                             //verifica  si esta bn el formato sino salta el error
                            if (tk.countTokens()==1) {
                            variablemultiplique=(tk.nextToken());
                             //agrega la linea completa al mapa de memoria
                            modelo.setValueAt(pre, posi, 1);// guarda en la tabla  de memoria el numero del programa
                            modelo.setValueAt(operacion, posi, 2);// guarda en la tabla  la instruccion del programa
                            modelo.setValueAt(linea, posi, 3);// guarda en la tabla el argumento de memoria
                            modelo.setValueAt(variablemultiplique, posi, 4);// guarda en la tabla el valor de memoria
                            memoriaprin[posi]=linea; // guarda en el vector principal de memoria
                            break;
                            }else{
                                errores=errores+"MULTIPLIQUE debe tener dos   argumentos en esta linea";
                                throw new Exception("Invalid entry");
                            }
                         
                        case "divida":
                             //verifica  si esta bn el formato sino salta el error
                            if (tk.countTokens()==1) {
                            variabledivida=(tk.nextToken());
                             //agrega la linea completa al mapa de memoria
                            modelo.setValueAt(pre, posi, 1);// guarda en la tabla  de memoria el numero del programa
                            modelo.setValueAt(operacion, posi, 2);// guarda en la tabla  la instruccion del programa
                            modelo.setValueAt(linea, posi, 3);// guarda en la tabla el argumento de memoria
                            modelo.setValueAt(variabledivida, posi, 4);// guarda en la tabla el valor de memoria
                            memoriaprin[posi]=linea; // guarda en el vector principal de memoria
                            break;
                             }else{
                                errores=errores+"DIVIDA debe tener dos   argumentos en esta linea";
                                throw new Exception("Invalid entry");
                            }
                            
                        case "potencia":
                            //verifica  si esta bn el formato sino salta el error
                            if (tk.countTokens()==1) {
                            variablepotencia=(tk.nextToken());
                             //agrega la linea completa al mapa de memoria
                            modelo.setValueAt(pre, posi, 1);// guarda en la tabla  de memoria el numero del programa
                            modelo.setValueAt(operacion, posi, 2);// guarda en la tabla  la instruccion del programa
                            modelo.setValueAt(linea, posi, 3);// guarda en la tabla el argumento de memoria
                            modelo.setValueAt(variablepotencia, posi, 4);// guarda en la tabla el valor de memoria
                            memoriaprin[posi]=linea; // guarda en el vector principal de memoria
                            break;
                            }else{
                                errores=errores+"POTENCIA debe tener dos   argumentos en esta linea";
                                throw new Exception("Invalid entry");
                            }
                        
                        case "modulo":
                            //verifica  si esta bn el formato sino salta el error
                            if (tk.countTokens()==1) {
                            variablemodulo=(tk.nextToken());
                             //agrega la linea completa al mapa de memoria
                            modelo.setValueAt(pre, posi, 1);// guarda en la tabla  de memoria el numero del programa
                            modelo.setValueAt(operacion, posi, 2);// guarda en la tabla  la instruccion del programa
                            modelo.setValueAt(linea, posi, 3);// guarda en la tabla el argumento de memoria
                            modelo.setValueAt(variablemodulo, posi, 4);// guarda en la tabla el valor de memoria
                            memoriaprin[posi]=linea; // guarda en el vector principal de memoria
                            break;
                            }else{
                                errores=errores+"MODULO debe tener dos   argumentos en esta linea";
                                throw new Exception("Invalid entry");
                            }
                            
                        case "concatene":
                            //verifica  si esta bn el formato sino salta el error
                            if (tk.countTokens()==1) {
                             variableconcatene=(tk.nextToken());
                             //agrega la linea completa al mapa de memoria
                            modelo.setValueAt(pre, posi, 1);// guarda en la tabla  de memoria el numero del programa
                            modelo.setValueAt(operacion, posi, 2);// guarda en la tabla  la instruccion del programa
                            modelo.setValueAt(linea, posi, 3);// guarda en la tabla el argumento de memoria
                            modelo.setValueAt(variableconcatene, posi, 4);// guarda en la tabla el valor de memoria
                            memoriaprin[posi]=linea; // guarda en el vector principal de memoria
                            break;
                            }else{
                                errores=errores+"CONCATENE debe tener dos   argumentos en esta linea";
                                throw new Exception("Invalid entry");
                            }
                            
                        case "elimine":
                            //verifica  si esta bn el formato sino salta el error
                            if (tk.countTokens()==1) {
                            variableelimine=(tk.nextToken());
                             //agrega la linea completa al mapa de memoria
                            modelo.setValueAt(pre, posi, 1);// guarda en la tabla  de memoria el numero del programa
                            modelo.setValueAt(operacion, posi, 2);// guarda en la tabla  la instruccion del programa
                            modelo.setValueAt(linea, posi, 3);// guarda en la tabla el argumento de memoria
                            modelo.setValueAt(variableelimine, posi, 4);// guarda en la tabla el valor de memoria
                            memoriaprin[posi]=linea; // guarda en el vector principal de memoria
                            break;
                            }else{
                                errores=errores+"ELIMINE debe tener dos   argumentos en esta linea";
                                throw new Exception("Invalid entry");
                            }
                        
                        case "extraiga":
                            //verifica  si esta bn el formato sino salta el error
                            if (tk.countTokens()==1) {
                             variableextraiga=(tk.nextToken());
                             //agrega la linea completa al mapa de memoria
                            modelo.setValueAt(pre, posi, 1);// guarda en la tabla  de memoria el numero del programa
                            modelo.setValueAt(operacion, posi, 2);// guarda en la tabla  la instruccion del programa
                            modelo.setValueAt(linea, posi, 3);// guarda en la tabla el argumento de memoria
                            modelo.setValueAt(variableextraiga, posi, 4);// guarda en la tabla el valor de memoria
                            memoriaprin[posi]=linea; // guarda en el vector principal de memoria
                            break;
                            }else{
                                errores=errores+"EXTRAIGA debe tener dos   argumentos en esta linea";
                                throw new Exception("Invalid entry");
                            }
                            
                        case "muestre":
                            //verifica  si esta bn el formato sino salta el error
                            if (tk.countTokens()==1) {
                            variablemuestre=(tk.nextToken());
                             //agrega la linea completa al mapa de memoria
                            modelo.setValueAt(pre, posi, 1);// guarda en la tabla  de memoria el numero del programa
                            modelo.setValueAt(operacion, posi, 2);// guarda en la tabla  la instruccion del programa
                            modelo.setValueAt(linea, posi, 3);// guarda en la tabla el argumento de memoria
                            modelo.setValueAt(variablemuestre, posi, 4);// guarda en la tabla el valor de memoria
                            memoriaprin[posi]=linea; // guarda en el vector principal de memoria
                            break;
                            }else{
                                errores=errores+"MUESTRE debe tener dos   argumentos en esta linea";
                                throw new Exception("Invalid entry");
                            }
                            
                        case "imprima":
                             //verifica  si esta bn el formato sino salta el error
                            if (tk.countTokens()==1) {
                             variableimprimir=(tk.nextToken());
                             //agrega la linea completa al mapa de memoria
                            modelo.setValueAt(pre, posi, 1);// guarda en la tabla  de memoria el numero del programa
                            modelo.setValueAt(operacion, posi, 2);// guarda en la tabla  la instruccion del programa
                            modelo.setValueAt(linea, posi, 3);// guarda en la tabla el argumento de memoria
                            modelo.setValueAt(variableimprimir, posi, 4);// guarda en la tabla el valor de memoria
                            memoriaprin[posi]=linea; // guarda en el vector principal de memoria
                            break;
                            }else{
                                errores=errores+"IMPRIMA debe tener dos   argumentos en esta linea";
                                throw new Exception("Invalid entry");
                            }
                            
                        case "retorne":
                              //verifica  si esta bn el formato sino salta el error
                            if (tk.countTokens()==0 || tk.countTokens()==1) {
                             //agrega la linea completa al mapa de memoria
                            modelo.setValueAt(pre, posi, 1);// guarda en la tabla  de memoria el numero del programa
                            modelo.setValueAt(operacion, posi, 2);// guarda en la tabla  la instruccion del programa
                            modelo.setValueAt(linea, posi, 3);// guarda en la tabla el argumento de memoria
                            modelo.setValueAt("----", posi, 4);// guarda en la tabla el valor de memoria
                            memoriaprin[posi]=linea; // guarda en el vector principal de memoria
                            break;
                            }else{
                                errores=errores+"RETORNE debe tener uno argumento o dos, y este debe ser 0 en esta linea";
                                throw new Exception("Invalid entry");
                            }
                            
                        case "//":
                           //agrega la linea completa al mapa de memoria
                            modelo.setValueAt(pre, posi, 1);// guarda en la tabla  de memoria el numero del programa
                            modelo.setValueAt("COMENTARIO", posi, 2);// guarda en la tabla  la instruccion del programa
                            modelo.setValueAt(linea, posi, 3);// guarda en la tabla el argumento de memoria
                            modelo.setValueAt("----", posi, 4);// guarda en la tabla el valor de memoria
                            memoriaprin[posi]=linea; // guarda en el vector principal de memoria
                            break;
                        
                        case " ":
                            //agrega la linea completa al mapa de memoria
                            modelo.setValueAt(pre, posi, 1);// guarda en la tabla  de memoria el numero del programa
                            modelo.setValueAt("LINEA VACIA", posi, 2);// guarda en la tabla  la instruccion del programa
                            modelo.setValueAt(linea, posi, 3);// guarda en la tabla el argumento de memoria
                            modelo.setValueAt("----", posi, 4);// guarda en la tabla el valor de memoria
                            memoriaprin[posi]=linea; // guarda en el vector principal de memoria
                            break;
                            
                          default:
                              // borra el ultimo programa cargado de la memoria 
                              for (int h = inicialmemoria; h < (int)memoria.getValue(); h++) {
                                  modelo.setValueAt("", h, 1);
                                  memoriaprin[h]="";
                              }
                              // hace el llamado a la exeption si algo esta mal en el archivo
                              errores=errores+"esta linea no cumple con ninguna de las reglas de sintaxis de .ch";
                              throw new Exception("Invalid entry");
                  }
             }
            // carga si no hay problema las variables a la tabla variables
            for (int b = 0; b < var.size(); b++) {
                
                tvariables.addRow(var.get(b));
                
            }
            
            // ciclo que le asigna el valor de la posicion de memoria donde esta
            //hubicado  y lo muetra en tabla de variables
            int tempoposi=posi;
            // toma el valor  de las variables del nuevo archivo mas las filas ocupadas de la tabla
            int lim=(nvariables.size()/2)+q;
            for ( int r=q; r < lim; r++) {
                tempoposi++;
                // le da el valor de la posicion  de la variable en la memoria a  la tabala d evariables
                tvariables.setValueAt(tempoposi, r, 0);
            }
           //concatena el prefijo con la instruccion en el mapa de memoria las variables defidas
            for (int a = 0; a < nvariables.size(); a+=2) {
                posi++;
                // agrega toda la instruccion a la memoria 
               
                            modelo.setValueAt(pre, posi, 1);// guarda en la tabla  de memoria el numero del programa
                            modelo.setValueAt("VARIABLE", posi, 2);// guarda en la tabla  la instruccion del programa
                            modelo.setValueAt(nvariables.get(a), posi, 3);// guarda en la tabla el argumento de memoria
                            modelo.setValueAt(nvariables.get(a+1), posi, 4);// guarda en la tabla el valor de memoria
                memoriaprin[posi]=nvariables.get(a); // guarda en el vector principal de memoria
            }
             for (int c = 0; c < etiq.size(); c++) {
                tetiquetas.addRow(etiq.get(c));
            }
          
       // se encarga de  crear el contenido de un programa en la tabla de procesos
           Object []objectprocesos = new Object[8];
           objectprocesos[0]=pre;// # instancias
           objectprocesos[1]=nombre; // nombre del programa
           objectprocesos[2]=lNumeroLineas; // numero  de lineas del programa
           objectprocesos[3]=pivote; // rb
           
           objectprocesos[4]=  pivote+lNumeroLineas-1;     //posi; //registro limite de el programa
           objectprocesos[5]=posi;      //pivote ; // crea el rlp
           objectprocesos[6]=prioridad;      // ASIGNA LA PRIORIDAD
           float tllegada=0;
           
           
           // calculo tiempo de llegada
            if (llegada==1) {
                llegada=0;
            }else{
                int cantidadfilas=tprocesos.getRowCount();
                 float tanterior=Float.parseFloat(tprocesos.getValueAt(cantidadfilas-1, 7).toString());
                 
                tllegada=(tanterior +lNumeroLineas)/4;
            }
           objectprocesos[7]=tllegada;      // ASIGNA LA PRIORIDAD
           tprocesos.addRow(objectprocesos);// adiciona a la tabla 
           pivote=posi+1;// crea el nuevo pivote
           
           
      }catch(Exception e){
          // retrocede el ide del programa en 1  pues el programa que lo ocupava no se cargo
          programa--;
           // recupera el valor de la memoria restante 
          int memoriarestante=((int)memoria.getValue()- pivote);
          // condicion que  define que tipo de error surgio en el proceso
            if (lNumeroLineas>memoriarestante) {
                // borra lo que se halla subido a la memoria si por casulaidad salta un error
                int tamaño=tabla2.getRowCount();
                int posisi=(int) tabla2.getValueAt(tamaño-1, 5);
            for (int i = posisi+1; i < (int)memoria.getValue(); i++) {
                 modelo.setValueAt("", i, 1);
                 modelo.setValueAt("", i, 2);
                 modelo.setValueAt("", i, 3);
                 modelo.setValueAt("", i, 4);
                 
            }
                //Messaje que se muestra cuando hay error dentro del 'try'
            JOptionPane.showMessageDialog(null, "Se generó un error al cargar el archivo \n"
                   +"pues  el tamaño de este es superior a la memoria restante");
            }else{
                int tamaño=tabla2.getRowCount();
                int posisi=(int) tabla2.getValueAt(tamaño-1, 5);
            for (int i = posisi+1; i < (int)memoria.getValue(); i++) {
                 modelo.setValueAt("", i, 1);
                 modelo.setValueAt("", i, 2);
                 modelo.setValueAt("", i, 3);
                 modelo.setValueAt("", i, 4);
            
            
            }
                //Messaje que se muestra cuando hay error dentro del 'try'
            JOptionPane.showMessageDialog(null, "TENEMOS UN INCONVENIENTE:\n"
                                              + "Se generó un error al cargar el archivo en la\n"
                                              + "linea "+lexa+" es posible que uno de los datos\n"
                                              + "del archivo no coincida con el formato\n\n"
                                              + errores);
           
            }
  
      }
         
  }
  
  // funcion encargada de tomar el valor de una variable y asignarselo a el acumulador 
  public void cargue(String programa, String variable){
       int tamaño=tvariables.getRowCount();
       int filas=0;
       // recorre la tabla de variable en busca de la condicion
       while(filas<tamaño){
           // captura las variables y las caste a a cadenas
           String prog=(String) tvariables.getValueAt(filas, 1);
           String vari=(String) tvariables.getValueAt(filas, 3);
           if (prog.equals(programa) && vari.equals(variable)) {
               // agrega toda la instruccion a la memoria  en el acumulador
                modelo.setValueAt(tvariables.getValueAt(filas, 4), 0, 4);
                filas=tamaño;
           }
           filas++;
       }
      
      
  }
  
  // FUNCION ENCARGADA DE RECORRER LA TABLA DE VARIABLES 
  // Y ALMACENAR EL VALOR DEL ACUMULADOR EN UNA VARIABLE 
  //DADA
  public void almacene (String programa, String variable){
      String acumu = (String) modelo.getValueAt(0,4).toString();
      float acumulador =Float.parseFloat(acumu);
      int filas=0;
      int tamaño=tvariables.getRowCount();
       // recorre la tabla de variable en busca de la condicion
       while(filas<tamaño){
            String prog=(String) tvariables.getValueAt(filas, 1);
           String vari=(String) tvariables.getValueAt(filas, 3);
           if (prog.equals(programa) && vari.equals(variable)) {
               int posicion= (int) tvariables.getValueAt(filas, 0);
               // agrega toda la instruccion a la memoria  en el acumulador 
                modelo.setValueAt(acumulador, posicion, 4);
                // agrega el nuevo valor a la tabla de variables
                tvariables.setValueAt(acumulador, filas, 4);
               
                filas=tamaño;
           }
           filas++;
       }
      
  }
  
  // funcion que crea un ciclo y retorna la posicion de memoria 
  // donde debe continuar  esta funcion se aplica para vaya  y vayasi
  public String vaya(String programa, String etiqueta ){
      
      int filas=0;
      String pos="";
      int tamaño=tetiquetas.getRowCount();
       // recorre la tabla de variable en busca de la condicion
       while(filas<tamaño){
           String prog=(String)tetiquetas.getValueAt(filas, 1);
           String etique=(String) tetiquetas.getValueAt(filas,2 );
           if (prog.equals(programa) && etique.equals(etiqueta)) {
            // optiene el valor de la posicion donde debe iniciar el ciclo
             pos  = (String) tetiquetas.getValueAt(filas, 0).toString();
              filas=tamaño;
            }
           filas++;
  }
        return pos;
  }
  
  
  // funcion que le pide al usuario que  ingrese un valor requerido
  //y lo retorna 
  public void lea(String programa, String variable){
      
      int filas=0;
      int tamaño=tvariables.getRowCount();
      String tipo="";
      int posicion=0,fil=0;
       // recorre la tabla de variable en busca de la condicion
       while(filas<tamaño){
            String prog=(String) tvariables.getValueAt(filas, 1);
           String vari=(String) tvariables.getValueAt(filas, 3);
           if (prog.equals(programa) && vari.equals(variable)) {
               // captura el tipo de la variable con la cual se busca  captuara un dato
               tipo=  (String) tvariables.getValueAt(filas, 2);
               posicion= (int) tvariables.getValueAt(filas, 0);
               fil=filas;
              filas=tamaño;
           }
           filas++;
       }
      //solicota el dato al usuario
      String datodeusuario=JOptionPane.showInputDialog("INGRESE UN VALOR DE TIPO "+tipo); 
      
      // agrega el nuevo valor  a la memoria  
                modelo.setValueAt(datodeusuario, posicion, 4);
                // agrega el nuevo valor a la tabla de variables
                tvariables.setValueAt(datodeusuario, fil, 4);
  }
 
  //funcion  que suma el valor del acumulador con el valor de una variable 
  public void sume(String programa, String variable){
      String acumu = (String) modelo.getValueAt(0,4).toString();
      float acumulador =Float.parseFloat(acumu);
      int filas=0;
      int tamaño=tvariables.getRowCount();
       // recorre la tabla de variable en busca de la condicion
       while(filas<tamaño){
           String prog=(String) tvariables.getValueAt(filas, 1);
           String vari=(String) tvariables.getValueAt(filas, 3);
           if (prog.equals(programa) && vari.equals(variable)) {
              // captura la posicion de memoria dond eesta la variable
               String val=String.valueOf(tvariables.getValueAt(filas, 4));
               float valor=  Float.parseFloat(val);
               
               float resultado=acumulador + valor;
               // agrega el nuevo valor  a la memoria  en el acumulador 
                modelo.setValueAt(resultado, 0, 4);
                filas=tamaño;
           }
           filas++;
       }
      
  }
  
  //funcion  que reste el valor del acumulador con el valor de una variable
  public void reste(String programa, String variable){
      String acumu = (String) modelo.getValueAt(0,4).toString();
      float acumulador =Float.parseFloat(acumu);
      int filas=0;
      int tamaño=tvariables.getRowCount();
       // recorre la tabla de variable en busca de la condicion
       while(filas<tamaño){
            String prog=(String) tvariables.getValueAt(filas, 1);
           String vari=(String) tvariables.getValueAt(filas, 3);
           if (prog.equals(programa) && vari.equals(variable)) {
              // captura la posicion de memoria dond eesta la variable
               int posicion= (int) tvariables.getValueAt(filas, 0);
               String val=(String) tvariables.getValueAt(filas, 4);
               float valor=  Float.parseFloat(val);
               
               float resultado=acumulador - valor;
               // agrega el nuevo valor  a la memoria  en el acumulador 
                modelo.setValueAt(resultado, 0, 4);
                filas=tamaño;
           }
           filas++;
       }
       
      
  }
  
  //funcion  que multiplique el valor del acumulador con el valor de una variable
  public void multiplique(String programa, String variable){
      String acumu = (String) modelo.getValueAt(0,4).toString();
      float acumulador =Float.parseFloat(acumu);
      int filas=0;
      int tamaño=tvariables.getRowCount();
       // recorre la tabla de variable en busca de la condicion
       while(filas<tamaño){
           String prog=(String) tvariables.getValueAt(filas, 1);
           String vari=(String) tvariables.getValueAt(filas, 3);
           if (prog.equals(programa) && vari.equals(variable)) {
              // captura la posicion de memoria dond eesta la variable
               String val=String.valueOf(tvariables.getValueAt(filas, 4));
               float valor=  Float.parseFloat(val);
               
               float resultado=acumulador * valor;
               // agrega el nuevo valor  a la memoria  en el acumulador 
                modelo.setValueAt(resultado, 0, 4);
                filas=tamaño;
           }
           filas++;
       }
     
  }
  
   //funcion  que divide el valor del acumulador con el valor de una variable
  public void divide(String programa, String variable){
      String acumu = (String) modelo.getValueAt(0,4).toString();
      float acumulador =Float.parseFloat(acumu);
      int filas=0;
      int tamaño=tvariables.getRowCount();
       // recorre la tabla de variable en busca de la condicion
       while(filas<tamaño){
           String prog=(String) tvariables.getValueAt(filas, 1);
           String vari=(String) tvariables.getValueAt(filas, 3);
           if (prog.equals(programa) && vari.equals(variable)) {
              // captura la posicion de memoria dond eesta la variable
               String val=String.valueOf(tvariables.getValueAt(filas, 4));
               float valor=  Float.parseFloat(val);
               //EN CASO TAL DE QUE LA VARIABLE TENGA UN CERO  VERIFICA  PRIMERO
               if(valor!=0){
                   float resultado=acumulador / valor;
               // agrega el nuevo valor  a la memoria  en el acumulador 
                modelo.setValueAt(resultado, 0, 4);
                
               }else{
                  JOptionPane.showOptionDialog(this, "HAY DIVICION CON CERO POR TANTO"
                           + "EL ACUMULADOR CONCERVA SU VALOR ORIGINAL", "ALERTA X/0",
                           JOptionPane.INFORMATION_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{" OK "},"OK");
               }filas=tamaño;
               
           }
           filas++;
       }
  }
  
   //funcion  que potencia el valor del acumulador con el valor de una variable
  public void potencia(String programa, String variable){
      String acumu = (String) modelo.getValueAt(0,4).toString();
      float acumulador =Float.parseFloat(acumu);
      int filas=0;
      int tamaño=tvariables.getRowCount();
       // recorre la tabla de variable en busca de la condicion
       while(filas<tamaño){
           String prog=(String) tvariables.getValueAt(filas, 1);
           String vari=(String) tvariables.getValueAt(filas, 3);
           if (prog.equals(programa) && vari.equals(variable)) {
              // captura la posicion de memoria dond eesta la variable
               String val=String.valueOf(tvariables.getValueAt(filas, 4));
               float valor=  Float.parseFloat(val);
               
               float resultado = (float) Math.pow(acumulador, valor);
               // agrega el nuevo valor  a la memoria  en el acumulador 
                modelo.setValueAt(resultado, 0, 4);
                filas=tamaño;
           }
           filas++;
       }
      
  }
  
   //funcion  de modulo el  valor del acumulador con el valor de una variable
  public void modulo(String programa, String variable){
      String acumu = (String) modelo.getValueAt(0,4).toString();
      float acumulador =Float.parseFloat(acumu);
      int filas=0;
      int tamaño=tvariables.getRowCount();
       // recorre la tabla de variable en busca de la condicion
       while(filas<tamaño){
           String prog=(String) tvariables.getValueAt(filas, 1);
           String vari=(String) tvariables.getValueAt(filas, 3);
           if (prog.equals(programa) && vari.equals(variable)) {
              // captura la posicion de memoria dond eesta la variable
               String val=String.valueOf(tvariables.getValueAt(filas, 4));
               float valor=  Float.parseFloat(val);
               //EN CASO TAL DE QUE LA VARIABLE TENGA UN CERO  VERIFICA  PRIMERO
               if(valor!=0){
                   float resultado=acumulador % valor;
               // agrega el nuevo valor  a la memoria  en el acumulador 
                modelo.setValueAt(resultado, 0, 4);
                
               }else{
                  JOptionPane.showOptionDialog(this, "HAY DIVICION CON CERO POR TANTO"
                           + "EL ACUMULADOR CONCERVA SU VALOR ORIGINAL", "ALERTA X/0",
                           JOptionPane.INFORMATION_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{" OK "},"OK");
               }filas=tamaño;}
           filas++;
       }
      
      
  }
  
  //funcion  de concatenar  el  valor del acumulador con el valor de una variable
  public void concatene(String programa, String variable){
      String acumulador=  (String) modelo.getValueAt(0, 4).toString();
      int filas=0;
      int tamaño=tvariables.getRowCount();
       // recorre la tabla de variable en busca de la condicion
       while(filas<tamaño){
           String prog=(String) tvariables.getValueAt(filas, 1);
           String vari=(String) tvariables.getValueAt(filas, 3);
           if (prog.equals(programa) && vari.equals(variable)) {
              // captura la posicion de memoria dond eesta la variable
               int posicion= (int) tvariables.getValueAt(filas, 0);
               
               
               // hace la concatenacion del  acumulador y el valor de la variable
               String resultado = tvariables.getValueAt(filas, 4)+acumulador ;
               // agrega el nuevo valor  a la memoria  en el acumulador 
                modelo.setValueAt(resultado, posicion, 4);
                tvariables.setValueAt(resultado, filas, 4);
               
                filas=tamaño;
           }
           filas++;
       }
    
  }
  
   //funcion  que elimina una parte del acumulador con el valor de una variable
  public void elimine(String programa, String variable){
      String acumulador=  (String) tabla.getValueAt(0, 4),resultado="";
      int filas=0;
      int tamaño=tvariables.getRowCount();
       // recorre la tabla de variable en busca de la condicion
       while(filas<tamaño){
           String prog=(String) tvariables.getValueAt(filas, 1);
           String vari=(String) tvariables.getValueAt(filas, 3);
           if (prog.equals(programa) && vari.equals(variable)) {
              // captura la posicion de memoria dond eesta la variable
               int posicion= (int) tvariables.getValueAt(filas, 0);
               String valor=   (String) tvariables.getValueAt(filas, 4);
               // hace la eliminacion de una parte del   acumulador con el valor de valor de la variable
               resultado = acumulador.replace(valor, "");
               // agrega el nuevo valor  a la memoria  en el acumulador 
                modelo.setValueAt(resultado, posicion, 4);
                tvariables.setValueAt(resultado, filas, 4);
               
                filas=tamaño;
           }
           filas++;
       }
      
  }
  
   //funcion  que extraiga los primeros caracteres del acumulador deacuerdo con el valor de una variable
  public void extraiga(String programa, String variable){
      String acumulador=  (String) tabla.getValueAt(0, 4),resultado="";
      int filas=0;
      int tamaño=tvariables.getRowCount();
       // recorre la tabla de variable en busca de la condicion
       while(filas<tamaño){
           String prog=(String) tvariables.getValueAt(filas, 1);
           String vari=(String) tvariables.getValueAt(filas, 3);
           if (prog.equals(programa) && vari.equals(variable)) {
              // captura la posicion de memoria dond eesta la variable
               int posicion= (int) tvariables.getValueAt(filas, 0);
               int valor=    (int) tvariables.getValueAt(filas, 4);
               // hace la extraccion de una parte del   acumulador con el valor de  la variable
               resultado = acumulador.substring(0, valor);
               // agrega el nuevo valor  a la memoria  en el acumulador 
                 modelo.setValueAt(resultado, posicion, 4);
                tvariables.setValueAt(resultado, filas, 4);
               
                filas=tamaño;
           }
           filas++;
       }
      
  }
   
  //funcion   mostrar en el monitor los primeros caracteres del acumulador deacuerdo con el valor de una variable
  public void mostrar(String programa, String variable){
      String resultado="";
      int filas=0;
      int tamaño=tvariables.getRowCount();
       // recorre la tabla de variable en busca de la condicion
       while(filas<tamaño){
           String prog=(String) tvariables.getValueAt(filas, 1);
           String vari=(String) tvariables.getValueAt(filas, 3);
           if (prog.equals(programa) && vari.equals(variable)) {
           
              // captura la posicion de memoria dond eesta la variable
              
               String valor=    (String) tvariables.getValueAt(filas, 4).toString();
               // hace la extraccion de una parte del   acumulador con el valor de  la variable
               resultado = valor;
               int bandera=0;
               String[] dato = new String[3];
               if (resultados.size()==0) {
                    dato[0]=programa;
                    dato[1]="RESULTADO DEL PROGRAMA "+ programa+".ch\nMOSTRANDO VALOR DE LA VARIABLE "+variable+" = "+resultado+"\n\n";
                    dato[2]="";
                    resultados.add(dato);
                    bandera=1;
               }else{
                   int r=0;
                   while(r<resultados.size()){
                       if (programa==resultados.get(r)[0]) {
                            dato[0]=programa;
                            dato[1]=resultados.get(r)[1]+"RESULTADO DEL PROGRAMA "+ programa+".ch\nMOSTRANDO VALOR DE LA VARIABLE "+variable+" = "+resultado+"\n\n";
                            dato[2]="";
                            resultados.set(r, dato);
                            bandera=1;
                       }
                       r++;
                   }
                   if (bandera==0) {
                        dato[0]=programa;
                        dato[1]="RESULTADO DEL PROGRAMA "+ programa+".ch\nMOSTRANDO VALOR DE LA VARIABLE "+variable+" = "+resultado+"\n\n";
                        dato[2]="";
                        resultados.add(dato);
                        bandera=1;
                   }
                   
               }
                
               
               /*String muestra=monitor.getText();
               muestra=muestra+"RESULTADO DEL PROGRAMA "+ programa+".ch\nMOSTRANDO VALOR DE LA VARIABLE "+variable+" = "+resultado+"\n\n";
               monitor.setText(muestra);*/
               
                break;
           }
           filas++;
       }
  }
  
  //funcion   mostrar en el impresora los primeros caracteres del acumulador deacuerdo con el valor de una variable
  public void imprimir(String programa, String variable){
      /*String resultado="";
      int filas=0;
      int tamaño=tvariables.getRowCount();
       // recorre la tabla de variable en busca de la condicion
       while(filas<tamaño){
           String prog=(String) tvariables.getValueAt(filas, 1);
           String vari=(String) tvariables.getValueAt(filas, 3);
           if (prog.equals(programa) && vari.equals(variable)) {
              // captura la posicion de memoria dond eesta la variable
               
               String valor=    (String) tvariables.getValueAt(filas, 4).toString();
               // hace la extraccion de una parte del   acumulador con el valor de  la variable
               resultado = valor;
               String muestra=impresora.getText();
               muestra=muestra+"RESULTADO DEL PROGRAMA "+ programa +".ch\nMOSTRANDO VALOR DE LA VARIABLE "+variable+" = "+resultado+"\n\r\n\r";
               
               impresora.setText(muestra);
              
               
                break;
           }
           filas++;
       }*/
      
  }
  
  public void ejecutar(){
      // toma el valor de el kernel para iniciar a ejecutar
      int inicio=(int) kernel.getValue();
      // define el limite  de las  instrucciones en memoria
      int ultimaf= tabla2.getRowCount();
      
      int limite= (int) tabla2.getValueAt(ultimaf-1, 5);
      
      
      
      // ciclo encargado de recorrer todas las instrucciones en la memoria
      for (int i = inicio+1; i <= limite; i++) {
        
          // variables capturadoras de cada fila de la tabla de memoria
          String pos_memoria=  (String) modelo.getValueAt(i, 0).toString();
          String programaa=  (String) modelo.getValueAt(i, 1).toString();
          String instruccion=  (String) modelo.getValueAt(i, 2).toString();
          String argumento=  (String) modelo.getValueAt(i, 3).toString();
          String valor=  (String) modelo.getValueAt(i, 4).toString();
         
          macumulador.setText(modelo.getValueAt(0, 4).toString());
          mpos_mem.setText(pos_memoria);
          minst.setText(argumento);
          mvalor.setText(valor);

          switch (instruccion) {
                        case "cargue":
                            cargue( programaa, valor);
                            break;
                        
                        case "almacene":
                            almacene (programaa, valor);
                            break;
                            
                        case "vaya":
                            vaya(programaa, valor );
                            
                            break;
                        
                        case "vayasi":
                            StringTokenizer etiquetas = new StringTokenizer(valor, ";");
                            String inicioo = etiquetas.nextToken();
                            String fin = etiquetas.nextToken();
                            String continua= String.valueOf(i);
                            float acum =(float) modelo.getValueAt(0,4);
                            if (acum>0.0) {
                                continua=vaya(programaa, inicioo );
                               i=Integer.parseInt(continua)-1;
                               
                            }
                                else if(acum<0.0){
                                    continua=vaya(programaa, fin );
                                    i=Integer.parseInt(continua)-1;
                                }
                            break;   
                            
                        case "lea":
                            lea( programaa, valor);
                            break;
                        
                        case "sume":
                            sume(programaa,valor);
                            break;
                            
                        case "reste":
                           reste(programaa,valor);
                            break;
                            
                        case "multiplique":
                            multiplique(programaa,valor);
                            break;
                         
                        case "divida":
                            divide(programaa,valor);
                            break;
                            
                        case "potencia":
                            potencia(programaa,valor);
                            break;
                            
                        case "modulo":
                            modulo(programaa,valor);
                            break;
                            
                        case "concatene":
                            concatene(programaa,valor);
                            break;
                            
                        case "elimine":
                            elimine(programaa,valor);
                            break;
                        
                        case "extraiga":
                            extraiga(programaa,valor);
                            break;
                            
                        case "muestre":
                           mostrar(programaa,valor);
                            break;
                            
                        case "imprima":
                            imprimir(programaa,valor);
                            break;
                            
                        case "retorne":
                             modelo.setValueAt(0, 0, 4);
                            
                            break;
                         
           } 
          
      } 
      
      
  }
   
  
  public void pasoapaso(){
      // toma el valor de el kernel para iniciar a ejecutar
      int inicio=(int) kernel.getValue();
      // define el limite  de las  instrucciones en memoria
      int ultimaf= tabla2.getRowCount();
     
      int limite= (int) tabla2.getValueAt(ultimaf-1, 5);
      int i = inicio+1;
      // ciclo encargado de recorrer todas las instrucciones en la memoria
      while ( i < limite) {
          
          // variables capturadoras de cada fila de la tabla de memoria
          String pos_memoria=  (String) modelo.getValueAt(i, 0).toString();
          String programaa=  (String) modelo.getValueAt(i, 1).toString();
          String instruccion=  (String) modelo.getValueAt(i, 2).toString();
          String argumento=  (String) modelo.getValueAt(i, 3).toString();
          String valor=  (String) modelo.getValueAt(i, 4).toString();
          
          // muestra en la interfaz los procesos que se estan ejecutando
         macumulador.setText(modelo.getValueAt(0, 4).toString());
          mpos_mem.setText(pos_memoria);
          minst.setText(argumento);
          mvalor.setText(valor);
          
            
          
           switch (instruccion) {
                        case "cargue":
                            cargue( programaa, valor);
                            break;
                        
                        case "almacene":
                            almacene (programaa, valor);
                            break;
                            
                        case "vaya":
                            vaya(programaa, valor );
                            
                            break;
                        
                        case "vayasi":
                            StringTokenizer etiquetas = new StringTokenizer(valor, ";");
                            String inicioo = etiquetas.nextToken();
                            String fin = etiquetas.nextToken();
                            String continua= String.valueOf(i);
                            float acum =(float) modelo.getValueAt(0,4);
                            if (acum>0.0) {
                              
                               continua=vaya(programaa, inicioo );
                               i=Integer.parseInt(continua)-1;
                               
                            }
                                else if(acum<0.0){
                                    continua=vaya(programaa, fin );
                                    i=Integer.parseInt(continua)-1;
                                }
                            break;   
                            
                        case "lea":
                            lea( programaa, valor);
                            break;
                        
                        case "sume":
                            sume(programaa,valor);
                            break;
                            
                        case "reste":
                           reste(programaa,valor);
                            break;
                            
                        case "multiplique":
                            multiplique(programaa,valor);
                            break;
                         
                        case "divida":
                            divide(programaa,valor);
                            break;
                            
                        case "potencia":
                            potencia(programaa,valor);
                            break;
                            
                        case "modulo":
                            modulo(programaa,valor);
                            break;
                            
                        case "concatene":
                            concatene(programaa,valor);
                            break;
                            
                        case "elimine":
                            elimine(programaa,valor);
                            break;
                        
                        case "extraiga":
                            extraiga(programaa,valor);
                            break;
                            
                        case "muestre":
                           mostrar(programaa,valor);
                            break;
                            
                        case "imprima":
                            imprimir(programaa,valor);
                            break;
                            
                        case "retorne":
                             modelo.setValueAt(0, 0, 4);
                            
                            break;
                            
           }
           if(JOptionPane.showOptionDialog(this, "¿DESEA SEGUIR LA EJECUCION PASO A PASO?", "Mensaje de Alerta",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{" SI "," NO "},"NO")==0)
        {
            i++;
       }else
        {
          ejecutar();
          String muestra=monitor.getText();
            for (int j = 0; j < resultados.size(); j++) {
                muestra=muestra+resultados.get(j)[1];
            }
          
            monitor.setText(muestra);
            impresora.setText(muestra);
          i=limite;
            }
     }
   }
  
  public void pasoapasofinal(){
          botoncargar.setVisible(false);
        estado.setText("MODO USUARIO");
        String proce=(String)procesos.getSelectedItem();
        JOptionPane.showOptionDialog(this, "CH-MAQUINA EJECUTARA EL PLANIFICADOR DE PROCESOS "+ proce+" PASO A PASO\n"
                        , "PLANIFICADOR DE  CH-MAQUINA.", 
                JOptionPane.INFORMATION_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{" OK "},"OK");
              
        switch (proce){
            case "ROUND-ROBIN":
                // carga en el sistema la prioridad que tendra el programa
                int CUANTUM=0;
                 SpinnerNumberModel sModel = new SpinnerNumberModel(5, 1, 20, 1);
                   JSpinner spinner = new JSpinner(sModel);
                   int pri = JOptionPane.showOptionDialog(null, spinner, "INGRESE VALOR DEL CUANTUM", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                   if (pri == JOptionPane.OK_OPTION)
                   {
                       CUANTUM = (int) spinner.getValue();
                   }
                pasoapaso();
                break;
            case "FCFS":
                pasoapaso();
                break;
            case "SJF/SPN NO EXPROPIATIVO":
                pasoapaso();
                break;
            case "SJF EXPROPIATIVO":
                pasoapaso();
                break;
                
            case "PRIORIDAD":
                pasoapaso();
                break;


        }
    }
  
  public void Quicksort(int p, int r){
        
         int q;
            if(p<r){
               q=partition(p,r); // hace el llamado para obtener el pivote
               Quicksort( p, q-1);// hace el llamado recurcivo con inicio p y fin donde esta el pivote menos 1
               Quicksort( p+1, r);// hace el llamado recurcivo con inicio en donde esta el pivote mas 1 y fin el tamaño del vector
            }
    }
  
  public  int  partition( int p, int r){
     
     float pivote=Float.parseFloat(resultados.get(p)[2]); // asigna al pivote  el valor que este en el inicio dado por p
     int i=p, j=r;
     String[] aux;
     
     while(i<j){
         while(Float.parseFloat(resultados.get(i)[2])<=pivote && i<r){// busca un numero mas grande o igual que el pivote 
             i++;// contabiliza  hasta el lugar donde encontro el nunmero mas grande o igual
         }
         while(Float.parseFloat(resultados.get(j)[2])>pivote && j>p){//busca un numero mas pequeño que el pivote 
             j--; // contabiliza  del fin hasta el inicio hasta el lugar donde encontro el nunmero mas pequeño
         }
         if(i<j){// si la condicion se cumple hace el intercambio d elas posiciones comensando a ordenar el vector pasa el 
             //mas pequeño a la posicion i, y el mas grande a la posicion j
             aux=resultados.get(i);
             resultados.set(i, resultados.get(j));
             resultados.set(j, aux);
         }
         
     }
     //  hace el intercambio de las posiciones comensando a ordenar el vector pasa el 
     //mas pequeño a la posicion p, y el mas grande a la posicion j
     aux=resultados.get(p);
     resultados.set(p, resultados.get(j));
     resultados.set(j, aux);
     
    return j;// retorna  elvalor de j y este sera el nuevo pivote o punto medio 
 }
  
  private void QuicksortDEC(int inicio, int fin){
        
        if (inicio+1 == fin){
            float salario1 = Float.parseFloat(resultados.get(inicio)[2]);
            float salario2 = Float.parseFloat(resultados.get(fin)[2]);
            if(salario1 < salario2){
                resultados.add(inicio, resultados.remove(fin));
            }
            
        }else if(inicio<fin){
            
            float pivote = Float.parseFloat(resultados.get(inicio)[2]);
            
            int posicionpivote = posPivote(inicio, fin, pivote);
            
            if (posicionpivote>0){
                resultados.add(inicio+posicionpivote, resultados.remove(inicio));
            }
            
            int posMayores=inicio+posicionpivote+1;
            boolean encontroMayor = false;
            
            //pasar los mayores a la inicio y los menores al final
            for (int i=inicio; i<inicio+posicionpivote; i++){
                if(Float.parseFloat(resultados.get(i)[2])<pivote){
                    do{
                        if(Float.parseFloat(resultados.get(posMayores)[2])>pivote){
                            resultados.add(i, resultados.remove(posMayores));
                            resultados.add(posMayores, resultados.remove(i+1));
                            
                            encontroMayor= true;
                        }else{
                            
                        }posMayores++;
                    }while(!encontroMayor); //el simboloe ! es para negar si es true se vuelve false y vseversa
                }
            }
            
            QuicksortDEC(inicio, inicio+posicionpivote-1);
            QuicksortDEC(inicio+posicionpivote+1, fin);
            
        }
    }
  
  private int posPivote(int inicio, int fin, float pivote){
        int contador = 0;
        
        //inicio+1 para no contar el pivote
        for (int i= inicio+1; i<=fin; i++){
            if(pivote < Float.parseFloat(resultados.get(i)[2])){
                contador++;
            }
        }
        
        return contador;
    }
  
  
  
  public void ejecutarfinal(){
      botoncargar.setVisible(false);
        estado.setText("MODO USUARIO");
        String proce=(String)procesos.getSelectedItem();
        JOptionPane.showOptionDialog(this, "CH-MAQUINA EJECUTARA EL PLANIFICADOR DE PROCESOS "+ proce+"\n"
                        , "PLANIFICADOR DE  CH-MAQUINA.", 
                JOptionPane.INFORMATION_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{" OK "},"OK");
        String muestra=monitor.getText();
        
        
        switch (proce){
            case "ROUND-ROBIN":
                // carga en el sistema la prioridad que tendra el programa
                int CUANTUM=0;
                 SpinnerNumberModel sModel = new SpinnerNumberModel(5, 1, 20, 1);
                   JSpinner spinner = new JSpinner(sModel);
                   int pri = JOptionPane.showOptionDialog(null, spinner, "INGRESE VALOR DEL CUANTUM", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                   if (pri == JOptionPane.OK_OPTION)
                   {
                       CUANTUM = (int) spinner.getValue();
                   }
                ejecutar();
                for (int i = 0; i < resultados.size(); i++) {
                    int RLP1 = (int)tabla2.getValueAt(i, 5);
                    int RLP2 = (int)tabla2.getValueAt(i+1, 5);
                    float valor= (RLP2-RLP1)/CUANTUM;
                    String []dato=new String[3];
                    dato[0]=resultados.get(i)[0];
                    dato[1]=resultados.get(i)[1];
                    dato[2]=String.valueOf(valor );
                    
                    resultados.set(i, dato);
                    
                    
                }
                
                int inicio = 0;
                int fin = resultados.size()-1;
                Quicksort(inicio, fin);
                
                break;
                
            case "FCFS":
                ejecutar();
                
                break;
            case "SJF/SPN NO EXPROPIATIVO":
                ejecutar();
                for (int i = 0; i < resultados.size(); i++) {
                    int RLP1 = (int)tabla2.getValueAt(i, 5);
                    int RLP2 = (int)tabla2.getValueAt(i+1, 5);
                    float valor= (RLP2-RLP1);
                    String []dato=new String[3];
                    dato[0]=resultados.get(i)[0];
                    dato[1]=resultados.get(i)[1];
                    dato[2]=String.valueOf(valor );
                    
                    resultados.set(i, dato);
                    
                }
                int ini=1,  finn=resultados.size()-1;
                Quicksort(ini, finn);
                
                break;
            case "SJF EXPROPIATIVO":
                ejecutar();
                for (int i = 0; i < resultados.size(); i++) {
                    int RLP1 = (int)tabla2.getValueAt(i, 5);
                    int RLP2 = (int)tabla2.getValueAt(i+1, 5);
                    float tiempo = (float)tabla2.getValueAt(i+1, 7);
                    float rafaga= (RLP2-RLP1);
                    float valor= tiempo+rafaga;
                    String []dato=new String[3];
                    dato[0]=resultados.get(i)[0];
                    dato[1]=resultados.get(i)[1];
                    dato[2]=String.valueOf(valor );
                    
                    resultados.set(i, dato);
                    
                }
                
                int inS=0,  finN=resultados.size()-1;
                Quicksort(inS, finN);
                break;
                
            case "PRIORIDAD":
                ejecutar();
                for (int i = 0; i < resultados.size(); i++) {
                    int PRI = (int)tabla2.getValueAt(i+1, 6);
                    String []dato=new String[3];
                    dato[0]=resultados.get(i)[0];
                    dato[1]=resultados.get(i)[1];
                    dato[2]=String.valueOf(PRI );
                    
                    resultados.set(i, dato);
                    
                }
                int iniS=1,  finnN=resultados.size()-1;
                QuicksortDEC(iniS, finnN);
                break;


        }
        for (int i = 0; i <resultados.size(); i++) {
                    muestra=muestra+resultados.get(i)[1];
                }
        monitor.setText(muestra);
        impresora.setText(muestra);
  }
  
  
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // componentes  de la interfaz del ch-maquina
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        memoria = new javax.swing.JSpinner();
        kernel = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        total_memoria = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        monitor = new javax.swing.JTextPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        impresora = new javax.swing.JTextPane();
        encender = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        apagarmaquina1 = new javax.swing.JToggleButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabla2 = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tablaetiquetas = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        tablavariables = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        botoncargar = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        estado = new javax.swing.JLabel();
        ejecutar = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        macumulador = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        mpos_mem = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        minst = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        mvalor = new javax.swing.JLabel();
        editor = new javax.swing.JButton();
        pasoapaso = new javax.swing.JButton();
        procesos = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        archivo = new javax.swing.JMenu();
        encender2 = new javax.swing.JMenuItem();
        cargarprograma = new javax.swing.JMenuItem();
        apagarmaquina2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        EJEC = new javax.swing.JMenu();
        encender3 = new javax.swing.JMenuItem();
        encender4 = new javax.swing.JMenuItem();
        IMP = new javax.swing.JMenu();
        IMPRI = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        documentacion = new javax.swing.JMenuItem();
        manualt = new javax.swing.JMenuItem();
        manualt1 = new javax.swing.JMenuItem();
        acercade = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("total memoria a utilizar");

        memoria.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        memoria.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                memoriaStateChanged(evt);
            }
        });

        kernel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        kernel.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                kernelStateChanged(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("KERNEL");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("MEMORIA");

        total_memoria.setBackground(new java.awt.Color(255, 255, 255));
        total_memoria.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        total_memoria.setForeground(new java.awt.Color(255, 255, 255));
        total_memoria.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                total_memoriaAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        jScrollPane1.setViewportView(monitor);

        jScrollPane2.setViewportView(impresora);

        encender.setText("ENCENDER MAQUINA");
        encender.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                encenderMouseClicked(evt);
            }
        });
        encender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                encenderActionPerformed(evt);
            }
        });

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Title 1", "Title 2"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabla.setEnabled(false);
        tabla.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tabla);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("MAPA DE MEMORIA");

        apagarmaquina1.setText("APAGAR MAQUINA");
        apagarmaquina1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                apagarmaquina1ActionPerformed(evt);
            }
        });

        tabla2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Título 5", "Título 6"
            }
        ));
        tabla2.setEnabled(false);
        tabla2.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(tabla2);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("tabla de procesos");

        tablaetiquetas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Título 3"
            }
        ));
        tablaetiquetas.setEnabled(false);
        jScrollPane5.setViewportView(tablaetiquetas);

        tablavariables.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "null", "Título 4", "null"
            }
        ));
        tablavariables.setEnabled(false);
        jScrollPane6.setViewportView(tablavariables);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setText("tabla de etiquetas");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setText("tabla de variables");

        botoncargar.setText("CARGAR ACHIVO .CH");
        botoncargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botoncargarActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("LA MAQUINA ESTA:");

        estado.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        estado.setForeground(new java.awt.Color(255, 255, 255));
        estado.setText("APAGADA");

        ejecutar.setText("EJECUTAR");
        ejecutar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ejecutarActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("ACUMULADOR");

        macumulador.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        macumulador.setText("   ");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("POS-MEM");

        mpos_mem.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        mpos_mem.setText("   ");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("INSTRUCCION");

        minst.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        minst.setText("   ");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setText("VALOR");

        mvalor.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        mvalor.setText("   ");

        editor.setText("EDITOR");
        editor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editorActionPerformed(evt);
            }
        });

        pasoapaso.setText("PASO A PASO");
        pasoapaso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasoapasoActionPerformed(evt);
            }
        });

        procesos.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ROUND-ROBIN", "FCFS", "SJF/SPN NO EXPROPIATIVO", "SJF EXPROPIATIVO", "PRIORIDAD" }));
        procesos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                procesosActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText("PROCESO DE PLANIFICACION");

        archivo.setText("ARCHIVO");
        archivo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                archivoMouseClicked(evt);
            }
        });

        encender2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, java.awt.event.InputEvent.CTRL_MASK));
        encender2.setText("ENCENDER MAQUINA");
        encender2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                encender2ActionPerformed(evt);
            }
        });
        archivo.add(encender2);

        cargarprograma.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        cargarprograma.setText("CARGAR PROGRAMA");
        cargarprograma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cargarprogramaActionPerformed(evt);
            }
        });
        archivo.add(cargarprograma);

        apagarmaquina2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        apagarmaquina2.setText("APAGAR MAQUINA");
        apagarmaquina2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                apagarmaquina2ActionPerformed(evt);
            }
        });
        archivo.add(apagarmaquina2);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem3.setText("CERRAR");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        archivo.add(jMenuItem3);

        jMenuBar1.add(archivo);

        EJEC.setText("EJECUTAR");

        encender3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        encender3.setText("RECORRIDO");
        encender3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                encender3ActionPerformed(evt);
            }
        });
        EJEC.add(encender3);

        encender4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        encender4.setText("PASO A PASO");
        encender4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                encender4ActionPerformed(evt);
            }
        });
        EJEC.add(encender4);

        jMenuBar1.add(EJEC);

        IMP.setText("IMPRIMIR");
        IMP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                IMPMouseClicked(evt);
            }
        });

        IMPRI.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        IMPRI.setText("IMPRIMIR ");
        IMPRI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IMPRIActionPerformed(evt);
            }
        });
        IMP.add(IMPRI);

        jMenuBar1.add(IMP);

        jMenu7.setText("AYUDA");
        jMenu7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu7ActionPerformed(evt);
            }
        });

        documentacion.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        documentacion.setText("DOCUMENTACION");
        documentacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                documentacionActionPerformed(evt);
            }
        });
        jMenu7.add(documentacion);

        manualt.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        manualt.setText("MANUAL TECNICO");
        manualt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manualtActionPerformed(evt);
            }
        });
        jMenu7.add(manualt);

        manualt1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_MASK));
        manualt1.setText("MANUAL USUARIO");
        manualt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manualt1ActionPerformed(evt);
            }
        });
        jMenu7.add(manualt1);

        acercade.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        acercade.setText("ACERCA DE");
        acercade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acercadeActionPerformed(evt);
            }
        });
        jMenu7.add(acercade);

        jMenuBar1.add(jMenu7);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(70, 70, 70)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(estado)))
                                .addGap(54, 54, 54)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel1))
                                        .addGap(97, 97, 97)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(memoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(kernel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(total_memoria, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(macumulador, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addGap(11, 11, 11)
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(mpos_mem, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(minst, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(mvalor, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(191, 191, 191)
                                .addComponent(jLabel10)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(248, 248, 248)
                                .addComponent(jLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 622, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 47, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(138, 138, 138)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel7)
                                .addGap(129, 129, 129))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(24, 24, 24)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(encender, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(apagarmaquina1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(botoncargar))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(editor, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(procesos, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(ejecutar)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(pasoapaso)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                                        .addGap(75, 75, 75))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(jLabel6)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(kernel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(memoria, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(total_memoria, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(219, 219, 219)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9)
                                    .addComponent(macumulador))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(mpos_mem))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel13)
                                    .addComponent(minst))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel14)
                                    .addComponent(mvalor))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(estado))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(116, 116, 116)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10)
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(encender)
                                    .addComponent(procesos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(apagarmaquina1)
                                    .addComponent(ejecutar)
                                    .addComponent(pasoapaso))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(botoncargar))
                            .addComponent(editor))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE))
                .addGap(248, 248, 248))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cargarprogramaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cargarprogramaActionPerformed
        // TODO add your handling code here:
        cargararchivo();
    }//GEN-LAST:event_cargarprogramaActionPerformed

    private void total_memoriaAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_total_memoriaAncestorAdded
        // TODO add your handling code here:
      
    }//GEN-LAST:event_total_memoriaAncestorAdded

    
    private void memoriaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_memoriaStateChanged
        // TODO add your handling code here:
        // en caso tal de que el kernel supere  el tamaño de la memoria la memoria se modificara  en 1 mas que el kernel
        int kertemp = (int) kernel.getValue();
        int memtemp = (int) memoria.getValue();
        if (kertemp >= memtemp) {
            memtemp=kertemp+1;
            memoria.setValue(memtemp);
        }
        //muestra la memoria disponible para los programas
        memtotal();
    }//GEN-LAST:event_memoriaStateChanged

    private void kernelStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_kernelStateChanged
        // en caso tal de que el kernel supere  el tamaño de la memoria la memoria se modificara  en 1 mas que el kernel
        int kertemp = (int) kernel.getValue();
        int memtemp = (int) memoria.getValue();
        if (memtemp <= kertemp) {
            kertemp=memtemp-1;
            kernel.setValue(kertemp);
        }
        //muestra la memoria disponible para los programas
        memtotal();
        
    }//GEN-LAST:event_kernelStateChanged

    private void encenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_encenderActionPerformed
       // HACE EL LLAMADO A LA FUNCION PARA QUE REPRODUSCA EL SONIDO DE ENSENDIDO
        encender();
        
        
    }//GEN-LAST:event_encenderActionPerformed

    private void encenderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_encenderMouseClicked
        // TODO add your handling code here:
   
    }//GEN-LAST:event_encenderMouseClicked

    private void archivoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_archivoMouseClicked
        // TODO add your handling code here:
       
       
    }//GEN-LAST:event_archivoMouseClicked

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        
        // codigo encargado de CERRAR EL PROGRAMA
        
        temporales();// borra los temporales
  if(JOptionPane.showOptionDialog(this, "¿ESTA SEGURO QUE DESEA SALIR DEL PROGRAMA?", "Mensaje de Alerta", 
          JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{" SI "," NO "},"NO")==0)
    {
        son("cierre");
        
        // se encarga de  detener un instante el proceso
        try {
            Thread.sleep(3000);// el tiempo es en milisegundos
        } catch (InterruptedException ex) {

        }
        System.exit(0);
    }
  
else
{
       JOptionPane.showMessageDialog(this, "PUEDE CONTINUAR CON LA EJECUCION DEL PROGRAMA");
}
        
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void apagarmaquina2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_apagarmaquina2ActionPerformed
        // TODO add your handling code here:
        
        //llama la funcion encargada de apagar la maquina
        apagar();
        
        
        
    }//GEN-LAST:event_apagarmaquina2ActionPerformed

    private void encender2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_encender2ActionPerformed
       // HACE EL LLAMADO A LA FUNCION PARA QUE REPRODUSCA EL SONIDO DE ENSENDIDO
        encender();
    }//GEN-LAST:event_encender2ActionPerformed

    private void apagarmaquina1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_apagarmaquina1ActionPerformed
        // TODO add your handling code hermie:
         //llama la funcion encargada de apagar la maquina
        apagar();
    }//GEN-LAST:event_apagarmaquina1ActionPerformed

    private void jMenu7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu7ActionPerformed

    private void acercadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acercadeActionPerformed
        // TODO add your handling code here
        JOptionPane.showOptionDialog(this, "Product Version: MI CH-MAQUINA   V.3.0.2\n" +
                "Actualizaciones: en proceso...\n" +
                "Java: 1.7.0_51; Java HotSpot(TM) 64-Bit Server VM 24.51-b03\n" +
                "Runtime: Java(TM) SE Runtime Environment 1.7.0_51-b13\n" +
                "System recomendado: Windows 7 y posterior\n" +
                "creado por :YEISON AGUIRRE OSORIO -- NAUFRAGO\n" +
                "UNIVERSIDAD NACIONAL DE COLOMBIA - SEDE MANIZALES\n"
                + "fecha inicio creacion:febrero 2016\n\n"
                + "simulador de OS encargado de leer instrucciones  de un archivo con \n"
                + "extencion .CH en este estan los pasos y valores iniciales que el \n"
                + "simulador debe interpretar y ejecutar, lo puede hacer de modo recorrido o \n"
                + "paso a paso, durante la ejecucion se ve el mapa de memoria y que hay \n"
                + "almacenado en ella ademas de el cuadro de procesos activos y variables declaradas,\n"
                + "los resultados del proceso pueden visualizarcen en monitor e impresion.\n\n"
                + "© feb2016 - mar2016 CH-MAQUINA All rights reserved.", "ACERCA DE MI CH-MAQUINA."
                       , 
        JOptionPane.INFORMATION_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{" OK "},"OK");
    }//GEN-LAST:event_acercadeActionPerformed

    private void encender3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_encender3ActionPerformed
        // boton ejecutar 
        
        ejecutarfinal();
// TODO add your handling code here:
    }//GEN-LAST:event_encender3ActionPerformed

    private void encender4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_encender4ActionPerformed
        //boton  paso a paso
        
        pasoapasofinal();        
// TODO add your handling code here:
    }//GEN-LAST:event_encender4ActionPerformed

    private void documentacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_documentacionActionPerformed
        // TODO add your handling code here:
        // carga  el documento  que contiene la documentacion 
           
        try{
            //nuevo archivo en esa direccion
            File temp = new File(arc);
            InputStream is = this.getClass().getResourceAsStream("/documentacion/documentacion.pdf");
            FileOutputStream archivoDestino = new FileOutputStream(temp);
            //FileWriter fw = new FileWriter(temp);
            byte[] buffer = new byte[1024*1024];
            //lees el archivo hasta que se acabe...
            int nbLectura;
            while ((nbLectura = is.read(buffer)) != -1)
                archivoDestino.write(buffer, 0, nbLectura);
            //cierras el archivo,el inputS y el FileW
            //fw.close();
            archivoDestino.close();
            is.close();
            //abres el archivo temporal
            Desktop.getDesktop().open(temp);
            
        } catch (IOException ex) {
            
        }   
    }//GEN-LAST:event_documentacionActionPerformed

    private void botoncargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botoncargarActionPerformed
        // TODO add your handling code here:
        ejecutar.setVisible(true);
        IMP.setEnabled(true);
        EJEC.setEnabled(true);
        
        pasoapaso.setVisible(true);
        cargararchivo();
    }//GEN-LAST:event_botoncargarActionPerformed

    private void ejecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ejecutarActionPerformed
        // TODO add your handling code here:
        ejecutarfinal();
        
    }//GEN-LAST:event_ejecutarActionPerformed
    
    
    private void editorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editorActionPerformed
        // TODO add your handling code here:
        // muestra  el boton cargar archivo de nuevo
        botoncargar.setVisible(true);
        // abre el panel del editor
        ambiente des= new ambiente();
        des.setVisible(true);
        
       
    }//GEN-LAST:event_editorActionPerformed

   
    private void pasoapasoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasoapasoActionPerformed
            pasoapasofinal();
        // TODO add your handling code here:
    }//GEN-LAST:event_pasoapasoActionPerformed

    private void IMPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IMPMouseClicked
       //se encarga de imprimir el contenido de la impresora
        
    }//GEN-LAST:event_IMPMouseClicked

    private void IMPRIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IMPRIActionPerformed
        // TODO add your handling code here:
         //se encarga de imprimir el contenido de la impresora
        String print =impresora.getText();
        impresora.setText(print);
        System.out.println("imprimira "+print);
        try {
            
            impresora.print();
        } catch (PrinterException ex) {
            
        }
    }//GEN-LAST:event_IMPRIActionPerformed

    private void manualtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manualtActionPerformed
          // carga  el documento  que contiene la documentacion 
           
        try{
            //nuevo archivo en esa direccion
            File temp = new File(mt);
            InputStream is = this.getClass().getResourceAsStream("/documentacion/manualtecnico.pdf");
            FileOutputStream archivoDestino = new FileOutputStream(temp);
            //FileWriter fw = new FileWriter(temp);
            byte[] buffer = new byte[1024*1024];
            //lees el archivo hasta que se acabe...
            int nbLectura;
            while ((nbLectura = is.read(buffer)) != -1)
                archivoDestino.write(buffer, 0, nbLectura);
            //cierras el archivo,el inputS y el FileW
            //fw.close();
            archivoDestino.close();
            is.close();
            //abres el archivo temporal
            Desktop.getDesktop().open(temp);
            
        } catch (IOException ex) {
            
        }   
    }//GEN-LAST:event_manualtActionPerformed

    private void manualt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manualt1ActionPerformed
          // carga  el documento  que contiene la documentacion 
           
        try{
            //nuevo archivo en esa direccion
            File temp = new File(mu);
            InputStream is = this.getClass().getResourceAsStream("/documentacion/manualusuario.pdf");
            FileOutputStream archivoDestino = new FileOutputStream(temp);
            //FileWriter fw = new FileWriter(temp);
            byte[] buffer = new byte[1024*1024];
            //lees el archivo hasta que se acabe...
            int nbLectura;
            while ((nbLectura = is.read(buffer)) != -1)
                archivoDestino.write(buffer, 0, nbLectura);
            //cierras el archivo,el inputS y el FileW
            //fw.close();
            archivoDestino.close();
            is.close();
            //abres el archivo temporal
            Desktop.getDesktop().open(temp);
            
        } catch (IOException ex) {
            
        }   // TODO add your handling code here:
    }//GEN-LAST:event_manualt1ActionPerformed

    private void procesosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_procesosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_procesosActionPerformed
   
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(entrada.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(entrada.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(entrada.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(entrada.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
         
        java.awt.EventQueue.invokeLater(new Runnable() {
            
            public void run() {
                new entrada().setVisible(true);
               
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu EJEC;
    private javax.swing.JMenu IMP;
    private javax.swing.JMenuItem IMPRI;
    private javax.swing.JMenuItem acercade;
    private javax.swing.JToggleButton apagarmaquina1;
    private javax.swing.JMenuItem apagarmaquina2;
    private javax.swing.JMenu archivo;
    private javax.swing.JButton botoncargar;
    private javax.swing.JMenuItem cargarprograma;
    private javax.swing.JMenuItem documentacion;
    private javax.swing.JButton editor;
    private javax.swing.JButton ejecutar;
    private javax.swing.JButton encender;
    private javax.swing.JMenuItem encender2;
    private javax.swing.JMenuItem encender3;
    private javax.swing.JMenuItem encender4;
    private javax.swing.JLabel estado;
    private javax.swing.JTextPane impresora;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSpinner kernel;
    private javax.swing.JLabel macumulador;
    private javax.swing.JMenuItem manualt;
    private javax.swing.JMenuItem manualt1;
    private javax.swing.JSpinner memoria;
    private javax.swing.JLabel minst;
    private javax.swing.JTextPane monitor;
    private javax.swing.JLabel mpos_mem;
    private javax.swing.JLabel mvalor;
    private javax.swing.JButton pasoapaso;
    private javax.swing.JComboBox procesos;
    private javax.swing.JTable tabla;
    private javax.swing.JTable tabla2;
    private javax.swing.JTable tablaetiquetas;
    private javax.swing.JTable tablavariables;
    private javax.swing.JLabel total_memoria;
    // End of variables declaration//GEN-END:variables
}
