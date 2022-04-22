package com.search;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import com.search.*;

public class SearchEngineOne extends JFrame {

	private static final Logger LOGGER = Logger.getLogger("SearchEngineOne");
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					/*SearchEngineOne frame = new SearchEngineOne(new JTextField() ,new JTextField() );
					frame.setVisible(true);*/
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SearchEngineOne(JTextField dirPath,JTextField searchKey) {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JFrame f=new JFrame(Constants.SEARCH_RESULT);
		JLabel lblNewLabel = new JLabel(Constants.SEARCH_RESULT);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(10, 11, 133, 45);
		
		String column[] = { Constants.SECTION, Constants.SECTION_WISE_FINDING };

		LinkedHashMap<String, String> map=this.getDocFile(dirPath.getText(),searchKey.getText());
				
		Object[][] data1 = new Object[map.size()][2];
		Set set = map.entrySet();
		Iterator itr = set.iterator();
		int j = 0;
		while (itr.hasNext()) {
			Map.Entry<String, String> enty = (Map.Entry<String, String>) itr
					.next();

			data1[j][0] = enty.getKey();
			data1[j][1] = enty.getValue();
			j++;
		}
		
		JTable jtbl=new JTable(data1,column);
		jtbl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jtbl.setRowHeight(14, 30);
		jtbl.setAutoscrolls(true);
        jtbl.setShowGrid(false);
        int[] columnsWidth = {400, 1000};
        
        int i = 0;
        for (int width : columnsWidth) {
            TableColumn col = jtbl.getColumnModel().getColumn(i++);
            col.setMinWidth(width);
            col.setMaxWidth(width);
            col.setPreferredWidth(width);
        }
        JScrollPane scrollPane = new JScrollPane(jtbl);
        jtbl.setFillsViewportHeight(false);
        
        scrollPane.setBorder(BorderFactory.createTitledBorder(
        		BorderFactory.createEtchedBorder(), "Search Word", TitledBorder.LEFT,
        		TitledBorder.TOP));
        
        this.setLayout(new BorderLayout());
        this.add(scrollPane);
        this.setPreferredSize(new Dimension(500, 1500));
        
        
		f.add(new JScrollPane(jtbl));
		f.setSize(1500, 1500);
		f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		f.setVisible(true);
		contentPane.add(jtbl);
	}

	public static LinkedHashMap<String, String> searchWordFromDocx(String searchKey,LinkedHashMap<String, String> map,File fileDocx,String fileName,int k) {
		try {
			FileInputStream fis = new FileInputStream(fileDocx);
			XWPFDocument docx = new XWPFDocument(fis);
			
			List<XWPFParagraph> paragraphList = docx.getParagraphs();
			String headingkey = null;
			String findLine = null;
			int i = 0;
			for (XWPFParagraph paragraph : paragraphList) {

				if (Constants.HEADING_STR.equals(paragraph.getStyle())) {
					headingkey = paragraph.getText();
					continue;
				}
				if (getFlag(searchKey, paragraph.getText().toString())) {
					findLine = paragraph.getText();
					
					if (headingkey == null || headingkey == "") {
						i++;
						map.put("(Doc "+k+")", fileName);
						map.put("(Doc "+k+") NonHeading " + i, findLine);
					} else if (findLine == null || findLine == "") {

					} else {
						if (map.containsKey(headingkey)) {
							map.put("(Doc "+k+")", fileName);
							map.put("(Doc "+k+") "+headingkey,getMergedLineValue(map, headingkey,findLine));
						} else {
							map.put("(Doc "+k+")", fileName);
							map.put("(Doc "+k+") "+headingkey, findLine);
						}

					}
				}

			}
			
			List<XWPFTable> tables = docx.getTables();
			int j = 0;
			for (XWPFTable table : tables) {
				for (XWPFTableRow row : table.getRows()) {
					j++;
					for (XWPFTableCell cell : row.getTableCells()) {
						String sFieldValue = cell.getText();
						if (getFlag(searchKey, sFieldValue)) {
							if (cell != null && sFieldValue != null) {
								map.put("(Doc "+k+")", fileName);
								map.put("(Doc "+k+") TableRow " + j, sFieldValue);
							}
						}
					}
				}

			}
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	public static String getMergedLineValue(
			LinkedHashMap<String, String> orgHashMap, String headingkey,
			String findLine) {
		String strVal = null;
		String mergedStr = null;
		StringBuffer strb = new StringBuffer();
		for (Entry entry : orgHashMap.entrySet()) {
			if (entry.getKey().equals(headingkey)) {
				strVal = entry.getValue().toString();
				mergedStr = strb.append(strVal).append("\n").append(findLine)
						.toString();
			}
		}
		return mergedStr;
	}

	public static LinkedHashMap<String, String> getDocFile(String dirPath,String searchKey) {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> mapsecond = null;
		String path = dirPath;
		File dir = new File(path);
		File file = null;
		String[] children = dir.list();
		int j=0;
		if (children != null) {
			for (int i = 0; i < children.length; i++) {
				j++;
				// Get filename of file or directory
				String filename = children[i];
				file = new File(path + File.separator + filename);
				if (!file.isDirectory()) {
					if (file.getName().endsWith(".doc")) {
						System.out.println("File Name " + filename + "(" +
						file.length()+"  bytes)");
						//map=searchWordFromDoc(file);
					} else if (file.getName().endsWith(".docx")
							|| file.getName().endsWith(".docm")) {
						map=searchWordFromDocx(searchKey,map,file,filename,j);
					}
				} else {
					System.out.println(path + File.separator + filename);
				}
			}
		}
		return map;
	}

	public static boolean getFlag(String keyValue, String lineValue) {
		boolean flag = false;
		if (Pattern.matches(".*" + keyValue.toUpperCase() + ".*", lineValue)) {
			flag = true;
		}
		if (Pattern.matches(".*" + keyValue.toLowerCase() + ".*", lineValue)) {
			flag = true;
		}
		if (Pattern.matches(
				".*" + keyValue.trim().substring(0, 1).toUpperCase()
						+ keyValue.substring(1) + ".*", lineValue)) {
			flag = true;
		}
		if (lineValue.contains(keyValue)) {
			flag = true;
		}
		if (lineValue.contains(keyValue)) {
			flag = true;
		}
		return flag;
	}
}
