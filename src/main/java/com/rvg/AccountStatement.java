package com.rvg;
import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class AccountStatement {
    private static final BaseColor NUB_BLUE = new BaseColor(70, 138, 169);
    private static final BaseColor TABLE_CELLS = new BaseColor(245, 249, 251); 
    

	public static void main(String[] args) {
        // Create a new document
        Document document = new Document();
//        String hexColor = "4B93B3";
//        BaseColor baseColor = new BaseColor(
//            Integer.valueOf(hexColor.substring(0, 2), 16),
//            Integer.valueOf(hexColor.substring(2, 4), 16),
//            Integer.valueOf(hexColor.substring(4, 6), 16)
//        );
        
     
        try {
            // Create a PDF writer
            PdfWriter.getInstance(document, new FileOutputStream("AccountStatement.pdf"));

            // Open the document
            document.open();

            // Set the header
            addHeader(document);

            // Set the footer
            addFooter(document);

            // Add the bank details block
            addBankDetails(document);

            // Add the transaction table
            addTransactionTable(document);

            // Close the document
            document.close();

            System.out.println("Account statement generated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public static PdfPCell createCombinedCell(String englishText, String arabicText, Font font, BaseColor backgroundColor) throws DocumentException, IOException {
	    PdfPCell cell = createCell(englishText + "\n" + arabicText, font, backgroundColor);

	    // Set the direction of the text to right-to-left for Arabic
	    cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

	    return cell;
	}

    private static void addHeader(Document document) throws DocumentException, MalformedURLException, IOException {
       Image img = Image.getInstance("src/main/resources/nub_header.jpg");
//       img.scaleAbsolute(90f, 90f);

       img.setAlignment(Image.ALIGN_MIDDLE | Image.ALIGN_CENTER);
      

        document.add(img);
    }

    private static void addFooter(Document document) throws DocumentException {
        Paragraph footer = new Paragraph("Generated on " + LocalDate.now(), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.ITALIC));
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
    }

    private static void addBankDetails(Document document) throws DocumentException {
        Paragraph bankDetails = new Paragraph("Bank Name: XYZ Bank\nAddress: City, State, Country\nPhone: +1234567890", FontFactory.getFont(FontFactory.HELVETICA, 12));
        bankDetails.setSpacingAfter(10);
        document.add(bankDetails);
    }
    
    private static void addAccountDetails(Document document) throws DocumentException {
    	
    	 Paragraph accountDetails = new Paragraph("اسم \nName: Rajath V\nحساب \nAccount: 19839323232\nفرع \nBranch: Libya", FontFactory.getFont(FontFactory.HELVETICA, 12));
    	 accountDetails.setSpacingAfter(10);
         document.add(accountDetails);
    	
    }
    
//    protected void drawBlock(Screening screening, PdfContentByte under, PdfContentByte over) {
//        under.saveState();
//        BaseColor color = WebColors.getRGBColor(
//            "#" + screening.getMovie().getEntry().getCategory().getColor());
//        under.setColorFill(color);
//        Rectangle rect = getPosition(screening);
//        under.rectangle(
//                rect.getLeft(), rect.getBottom(), rect.getWidth(), rect.getHeight());
//        under.fill();
//        over.rectangle(
//            rect.getLeft(), rect.getBottom(), rect.getWidth(), rect.getHeight());
//        over.stroke();
//        under.restoreState();
//    }
    
    private static void addTransactionTable(Document document) throws DocumentException, IOException {
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(5);
        table.setWidths(new float[]{1, 2, 1,1,1,1});

        
        String englishDate = "Date";
        String arabicDate = "تاريخ";
        String englishDescription = "Description";
        String arabicDescription = "وصف";
        String englishTransactionId = "Transaction ID";
        String arabicTransactionId = "رقم المعاملة";
        String englishDebit= "Debit";
        String arabicDebit = "دَين";
        String englishCredit = "Credit";
        String arabicCredit = "ائتمان";
        String englishBalance = "Balance";
        String arabicBalance = "توازن";
        table.addCell(createCombinedCell(englishDate, arabicDate, FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, BaseColor.WHITE), NUB_BLUE));
        table.addCell(createCombinedCell(englishDescription, arabicDescription, FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, BaseColor.WHITE), NUB_BLUE));
        table.addCell(createCombinedCell(englishTransactionId, arabicTransactionId, FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, BaseColor.WHITE), NUB_BLUE));
        table.addCell(createCombinedCell(englishDebit, arabicDebit, FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, BaseColor.WHITE), NUB_BLUE));
        table.addCell(createCombinedCell(englishCredit, arabicCredit, FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, BaseColor.WHITE), NUB_BLUE));
        table.addCell(createCombinedCell(englishBalance, arabicBalance, FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, BaseColor.WHITE), NUB_BLUE));


        // Add table data
        List<Transaction> transactions = getTransactions(); // Replace this with your own logic to fetch transactions
        for (Transaction transaction : transactions) {
            table.addCell(createCell(transaction.getDate().toString(), FontFactory.getFont(FontFactory.HELVETICA, 12), TABLE_CELLS));
            table.addCell(createCell(transaction.getDescription(), FontFactory.getFont(FontFactory.HELVETICA, 12), TABLE_CELLS));
            table.addCell(createCell(String.valueOf(transaction.getUid()), FontFactory.getFont(FontFactory.HELVETICA, 12), TABLE_CELLS));
            table.addCell(createCell(String.valueOf(transaction.getCredit()), FontFactory.getFont(FontFactory.HELVETICA, 12), TABLE_CELLS));
            table.addCell(createCell(String.valueOf(transaction.getDebit()), FontFactory.getFont(FontFactory.HELVETICA, 12), TABLE_CELLS));
            table.addCell(createCell(String.valueOf(transaction.getBalance()), FontFactory.getFont(FontFactory.HELVETICA, 12), TABLE_CELLS));
        }

        document.add(table);
    }
    
    

    private static PdfPCell createCell(String content) {
        return createCell(content);
    }

private static PdfPCell createCell(String content, Font font, BaseColor backgroundColor) {
    PdfPCell cell = new PdfPCell(new Phrase(content, font));
//    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    cell.setPadding(5);
    cell.setBackgroundColor(backgroundColor);
    return cell;
}
    private static List<Transaction> getTransactions() {
        // Replace this method with your own logic to fetch transactions from your data source (e.g., database)
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(LocalDate.of(2022, 1, 1), "Transaction 1",UUID.randomUUID(), 100.0,100,100));
        transactions.add(new Transaction(LocalDate.of(2022, 1, 2), "Transaction 2",UUID.randomUUID(), -50.0,-50.0,-50.0));
        transactions.add(new Transaction(LocalDate.of(2022, 1, 3), "Transaction 3",UUID.randomUUID(), 200.0,200.0,200.0));
        return transactions;
    }

    private static class Transaction {
        private LocalDate date;
        private String description;
        private double credit;
        private double debit;
        private double balance;
		private UUID uid;

        public Transaction(LocalDate date, String description, UUID uuid, double credit,double debit,double balance) {
            this.date = date;
            this.description = description;
            this.credit = credit;
            this.debit = debit;
            this.uid =uuid;
            this.balance = balance;

        }

      
		public LocalDate getDate() {
            return date;
        }

        public String getDescription() {
            return description;
        }

        public double getCredit() {
            return credit;
        }
        public double getDebit() {
            return debit;
        }
        public double getBalance() {
            return balance;
        }
        public UUID getUid() {
            return uid;
        }
    }
}