package xsdValidatoer;

import java.awt.Dialog.ModalityType;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
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
		JFrame frame = new JFrame();

		// Custom button text
		Object[] options = { "Escolher .xsd", "Cancelar" };
		int n = JOptionPane.showOptionDialog(dialog, "Escolha o ficheiro de Regras (.xsd):", "Validação XML/GML",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

		System.out.println("n =" + n);
		String message = "";
		File Xsdfile = null;
		Schema schema = null;
		Source xmlFile = null;
		

		try {

			if (n == 1) {
				dialog.dispose();
				System.exit(0);
			}
			
			//Get xsd FIle

			JFileChooser fc = new JFileChooser();

			int returnVal = fc.showOpenDialog(frame);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				Xsdfile = fc.getSelectedFile();
				// This is where a real application would open the file.
				System.out.println("Opening: " + Xsdfile.getName() + ".");
			} else {
				System.out.println("Open command cancelled by user.");
			}

			// Custom button text
			Object[] options2 = { "Escolher ficheiro", "Cancelar" };
			int n2 = JOptionPane.showOptionDialog(dialog, "Escolha o ficheiro para ser validado (.xml ou .gml):",
					"Validação XML/GML", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options2,
					options[1]);

			System.out.println("n =" + n2);
			String message2 = "";

			if (n2 == 1) {
				dialog.dispose();
				System.exit(0);
			}
			
			// GET XM or GML

			JFileChooser fc2 = new JFileChooser();

			File XmlGmlfile = null;

			int returnVal2 = fc2.showOpenDialog(frame);

			if (returnVal2 == JFileChooser.APPROVE_OPTION) {
				XmlGmlfile = fc2.getSelectedFile();
				// This is where a real application would open the file.
				System.out.println("Opening: " + XmlGmlfile.getName() + ".");
				message = "Sem erros de Validação do "+XmlGmlfile.getName();
			} else {
				System.out.println("Open command cancelled by user.");
			}

			try {
				xmlFile = new StreamSource(XmlGmlfile);
				SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

				schema = schemaFactory.newSchema(Xsdfile);
			} catch (SAXException e1) {
				e1.printStackTrace();
				message = "Não foi possivel encontrar o ficheiro Schema.xsd";
			}

			Validator validator = schema.newValidator();
			try {
				validator.validate(xmlFile);
			} catch (SAXParseException e) {
				try {
					message = e.getLocalizedMessage() + " (linha:" + e.getLineNumber() + ")";
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

		} catch (Exception e) {
			e.printStackTrace();
			message = "Ocorreu um erro! Verifique que tem os ficheiros na pasta";
		}

		JOptionPane.showMessageDialog(dialog, message);

		dialog.dispose();
		System.exit(0);

	}

}
