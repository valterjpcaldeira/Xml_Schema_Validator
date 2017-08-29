package xsdValidatoer;

import java.awt.Dialog.ModalityType;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XsdValidator {

	public static void main(String[] args) {

		JDialog dialog = new JDialog();
		dialog.setModal(true);
		dialog.setAlwaysOnTop(true);
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);
		dialog.toFront();
		dialog.setFocusable(true);

		// Custom button text
		Object[] options = { "XML", "GML", "Cancelar" };
		int n = JOptionPane.showOptionDialog(dialog,
				"Deseja executar agora a validação do GML ou XML?",
				"Validação XML/GML", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[2]);

		System.out.println("n =" + n);
		String message = "";
		try{

		if (n == 2) {
			dialog.dispose();
			System.exit(0);
		}

		Schema schema = null;
		Source xmlFile = null;
		Source gmlFile = null;
		message = "Sem erros de Validação do "+options[n];

		File file = new File("xml.xml");
		File fileGml = new File("gml.gml");

		if (n == 0) {

			try {
				xmlFile = new StreamSource(file);
				SchemaFactory schemaFactory = SchemaFactory
						.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

				File f = new File("Schema.xsd");
				schema = schemaFactory.newSchema(f);
			} catch (SAXException e1) {
				e1.printStackTrace();
				message = "Não foi possivel encontrar o ficheiro Schema.xsd";
			}

			Validator validator = schema.newValidator();
			try {
				validator.validate(xmlFile);
			} catch (SAXParseException e) {
				try {
					message = e.getLocalizedMessage() + " (linha:"
							+ e.getLineNumber() + ")";
				} catch (Exception ex) {
					message = e.getMessage();
					ex.printStackTrace();
				}

			} catch (SAXException e) {
				
				message = e.getLocalizedMessage();

			} catch (Exception e) {
				message = "Não foi possivel encontrar o ficheiro xml.xml";
				File fileLog = new File("log.txt");
				FileWriter fw = new FileWriter(fileLog.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(e.getMessage());
				bw.close();

			}

			System.gc();
			Runtime.getRuntime().freeMemory();

		} else {

			try {
				gmlFile = new StreamSource(fileGml);
				SchemaFactory schemaFactory = SchemaFactory
						.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

				File f = new File("SchemaGML.xsd");
				schema = schemaFactory.newSchema(f);

				Validator validatorGML = schema.newValidator();

				validatorGML.validate(gmlFile);

			} catch (SAXParseException e) {
				e.printStackTrace();

				message = e.getLocalizedMessage() + " (linha:"
						+ e.getLineNumber() + ")";

			} catch (SAXException e) {

				e.printStackTrace();

			} catch (Exception ex) {
				ex.printStackTrace();
				message = "Não foi possivel encontrar o ficheiro SchemaGML.xsd ou gml.gml";
			}

		}
		
		}catch(Exception e){
			e.printStackTrace();
			message = "Ocorreu um erro! Verifique que tem os ficheiros na pasta";
		}

		JOptionPane.showMessageDialog(dialog,
				message);

		dialog.dispose();
		System.exit(0);

	}
	

}
