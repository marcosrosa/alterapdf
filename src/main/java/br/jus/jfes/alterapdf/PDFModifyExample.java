package br.jus.jfes.alterapdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.parser.PdfDocumentContentParser;
import com.itextpdf.kernel.pdf.canvas.parser.listener.SimpleTextExtractionStrategy;
import com.itextpdf.pdfcleanup.autosweep.CompositeCleanupStrategy;
import com.itextpdf.pdfcleanup.autosweep.PdfAutoSweep;
import com.itextpdf.pdfcleanup.autosweep.RegexBasedCleanupStrategy;



public class PDFModifyExample {
	private static final String src = "/home/marcos/labs/natjus/DAFNE VENTURA MOURA - VU MANTENOPOLIS- FONOAUDIOLOGIA, TERAPIA, PSICOLOGO, FISIOTERAPIA, PEDAGOGO (AGENESIA DE CORPO CALOSO) - 5000141-07.2021.8.08_.pdf";
	private static final String src0 = "/home/marcos/labs/natjus/CLEUSA DE OLIVEIRA - 2º JUIZADO ESPECIAL CRIMINAL E FAZENDA PÚBLICA DE VITÓRIA – CONSULTA ORTOPÉDICA (GONARTROSE) –  0010319-24.2021.8.08.0024 - PAR.pdf";
	private static final String src1 = "/home/marcos/labs/natjus/";
	
	private static final String dest = "/home/marcos/labs/natjus/alterado3.pdf";
	private static final String dest1 = "/home/marcos/labs/natjus/output/";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//new PDFModifyExample().verificaArquivos();
		try {
			new PDFModifyExample().retiraNome();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch blocko
			e.printStackTrace();
		}
	}	
	
	private File[] listaArquivos() {
		File dir = new File(src1);
	    File [] files = dir.listFiles(new FilenameFilter() {
	        public boolean accept(File dir, String name) {
	            return name.toLowerCase().endsWith(".pdf");
	        }
	    });
	    
	    return files;
	}

	public void verificaArquivos() {
		
		File[] filesPdf = listaArquivos();
		String nome = null;
		String fn = null;
		
	    for (File pdffile : filesPdf) {
	    	fn = pdffile.getName();
	    	int idx = fn.indexOf("-");
	    	if (idx > 0) { 
	    		nome = fn.substring(0, idx-1).trim();
	    		System.out.println(nome);
	    	} else nome = null;
	    	if (nome != null) {
	    		StringTokenizer st = new StringTokenizer(nome);
	    		 while (st.hasMoreElements()) {
	    			String elem = st.nextToken();
	    			System.out.println(elem);
	    		}
	    		System.out.println("");
	    	}
	    	
	    	//abre o arquivo contido na pasta e renomeia a saida para qualquer coisa
			/*PdfDocument pdf = new PdfDocument(
				    new PdfReader(pdffile.getAbsoluteFile()), new PdfWriter(dest1+pdffile.getName()));
	        
	    	pdf.close(); */
		    	

	    } 		
		
	}

	public void retiraNome() throws IOException, InterruptedException {

        CompositeCleanupStrategy strategy = new CompositeCleanupStrategy();
        strategy.add(new RegexBasedCleanupStrategy(Pattern.compile("\\bDafne\\b", Pattern.CASE_INSENSITIVE)));
        //strategy.add(new RegexBasedCleanupStrategy(Pattern.compile("(Processo[\w*0031$])", Pattern.CASE_INSENSITIVE))); 
        //strategy.add(new RegexBasedCleanupStrategy(Pattern.compile("[0-9]{7}-[0-9]{2}.[0-9]{4}.[0-9].[0-9]{2}.[0-9]{4}", Pattern.CASE_INSENSITIVE)));
        //strategy.add(new RegexBasedCleanupStrategy(Pattern.compile("Proc\\w*....[\\s]{0,5}[\\d]{5,7}-[\\d]{2}.[\\d]{4}.[\\d].[\\d]{2}.[\\d]{4}", Pattern.CASE_INSENSITIVE)));
        //como colocar ^ na string 
        strategy.add(new RegexBasedCleanupStrategy(Pattern.compile("processo\\s.*\\s[\\d\\.-]*", Pattern.CASE_INSENSITIVE)));
                
        strategy.add(new RegexBasedCleanupStrategy(Pattern.compile("(Cleusa[\n| ]de[\n| ]Oliveira)", Pattern.CASE_INSENSITIVE)));
        //strategy.add(new RegexBasedCleanupStrategy(Pattern.compile("(Dafne[\n| ]Ventura[\n| ]Moura)", Pattern.CASE_INSENSITIVE)));
        strategy.add(new RegexBasedCleanupStrategy(Pattern.compile("Dafne\\sVentura\\sMoura", Pattern.CASE_INSENSITIVE)));
        strategy.add(new RegexBasedCleanupStrategy(Pattern.compile("EQUIPE TÉCNICA NAT/TJES", Pattern.CASE_INSENSITIVE)));
        
        PdfWriter writer = new PdfWriter(dest);
        writer.setCompressionLevel(0);
        PdfDocument pdf = new PdfDocument(new PdfReader(src0), writer);

        // sweep
        PdfAutoSweep autoSweep = new PdfAutoSweep(strategy);
        autoSweep.cleanUp(pdf);

        // propriedades
		if (pdf.getDocumentInfo().getTitle()==null) 
			pdf.getDocumentInfo().setTitle("PARECER");
		
		if (pdf.getDocumentInfo().getTitle()!="PARECER" && !pdf.getDocumentInfo().getTitle().isEmpty())
			pdf.getDocumentInfo().setTitle("PARECER TÉCNICO");

        
        pdf.close();
        System.out.println("fim procedimento");
        // compare
        //compareResults(cmp, output, outputPath, "4");
    }
	
	
	
}
