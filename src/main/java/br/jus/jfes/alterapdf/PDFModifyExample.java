package br.jus.jfes.alterapdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
	private static final String src1 = "/home/marcos/labs/natjus/";
	
	private static final String dest = "/home/marcos/labs/natjus/alterado3.pdf";
	private static final String dest1 = "/home/marcos/labs/natjus/output/";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*try {
			PdfDocument pdf = new PdfDocument(
				    new PdfReader(src), new PdfWriter(dest));
			
			System.out.println("produtor: "+pdf.getDocumentInfo().getProducer());
			System.out.println("titulo  : "+pdf.getDocumentInfo().getTitle());
			System.out.println("Subject 	  : "+pdf.getDocumentInfo().getSubject());
			
			if (pdf.getDocumentInfo().getTitle()==null) 
				pdf.getDocumentInfo().setTitle("PARECER");
			
			if (pdf.getDocumentInfo().getTitle()!="PARECER" && !pdf.getDocumentInfo().getTitle().isEmpty())
				pdf.getDocumentInfo().setTitle("ALTERADO-JAVA");
			
			pdf.close();
		    System.out.println("PDF lido successfully.");
		} catch (Exception e) {
		    e.printStackTrace();
		} */
		
		//new PDFModifyExample().verificaArquivos();
		try {
			new PDFModifyExample().retiraNome();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
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
		
	    for (File pdffile : filesPdf) {
	        System.out.println(pdffile.getName());
	        System.out.println("");
	        try {
				PdfDocument pdf = new PdfDocument(
					    new PdfReader(pdffile.getAbsoluteFile()), new PdfWriter(dest1+pdffile.getName()));
				
				PdfPage page = pdf.getFirstPage();
		        PdfDictionary dict = page.getPdfObject();

		        PdfObject object = dict.get(PdfName.Contents);
		        
		        if (object instanceof PdfStream) {
		            PdfStream stream = (PdfStream) object;
		            byte[] data0 = stream.getBytes();
		            String dd = new String(data0);
		            System.out.println(dd);
		            System.out.println("----");
		            // transformando em texto
		          /*  PdfDocumentContentParser parser = new PdfDocumentContentParser(pdf);
		            SimpleTextExtractionStrategy strategy = parser.processContent(1, new SimpleTextExtractionStrategy());
		            System.out.println(strategy.getResultantText()); */
		            // modificar
		            
		            String replacedData = data0.toString().replace("Dafne", "Paciente9");
	                //stream.SetData(Encoding.UTF8.GetBytes(replacedData));
		            
		            //String replacedData0 = new String(data0).replaceAll("Dafne Ventura Moura", "PACIENTE0");
		            //String replacedData0 = new String(data0).replaceAll("Dafne", "PACIENTE1");
		            //String replacedData0 = new String(data0).replace("0 0 0 rg\n()Tj", "0 0 0 rg\n(Plan Advanced Payment)");
		            //stream.setData(replacedData0.getBytes(StandardCharsets.UTF_8));
		            //pdf.getWriter().writeBytes(replacedData0.getBytes(StandardCharsets.UTF_8));
		        }
		    	pdf.close();
		    	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

	    } 		
		
	}

	public void retiraNome() throws IOException, InterruptedException {

        CompositeCleanupStrategy strategy = new CompositeCleanupStrategy();
        //strategy.add(new RegexBasedCleanupStrategy("(Dafne)|(DAFNE)"));
        //strategy.add(new RegexBasedCleanupStrategy("(Moura)|(MOURA)"));
        strategy.add(new RegexBasedCleanupStrategy(Pattern.compile("Dafne", Pattern.CASE_INSENSITIVE)));
        strategy.add(new RegexBasedCleanupStrategy(Pattern.compile("Ventura Moura", Pattern.CASE_INSENSITIVE)));
        strategy.add(new RegexBasedCleanupStrategy(Pattern.compile("Dafne Ventura Moura", Pattern.CASE_INSENSITIVE)));
        

        PdfWriter writer = new PdfWriter(dest);
        writer.setCompressionLevel(0);
        PdfDocument pdf = new PdfDocument(new PdfReader(src), writer);

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
