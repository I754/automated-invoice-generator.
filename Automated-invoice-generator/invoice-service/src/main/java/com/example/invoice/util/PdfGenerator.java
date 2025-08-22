package com.example.invoice.util;

import com.example.invoice.model.Invoice;
import com.example.invoice.model.LineItem;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

public class PdfGenerator {
    public static byte[] generate(Invoice inv) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(doc, baos);
            doc.open();

            Font h1 = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font h2 = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font p = new Font(Font.HELVETICA, 10);

            Paragraph title = new Paragraph("INVOICE #" + inv.getId(), h1);
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);
            doc.add(new Paragraph(" "));

            PdfPTable meta = new PdfPTable(2);
            meta.setWidthPercentage(100);
            meta.addCell(cell("Client", h2));
            meta.addCell(cell(inv.getClient().getName() + "\n" + safe(inv.getClient().getAddress()), p));
            meta.addCell(cell("Issue Date", h2));
            meta.addCell(cell(inv.getIssueDate().format(DateTimeFormatter.ISO_DATE), p));
            meta.addCell(cell("Due Date", h2));
            meta.addCell(cell(inv.getDueDate() != null ? inv.getDueDate().format(DateTimeFormatter.ISO_DATE) : "-", p));
            meta.addCell(cell("Status", h2));
            meta.addCell(cell(inv.getStatus().name(), p));
            doc.add(meta);
            doc.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{6, 2, 2, 2});
            table.addCell(cell("Description", h2));
            table.addCell(cell("Qty", h2));
            table.addCell(cell("Unit Price", h2));
            table.addCell(cell("Line Total", h2));

            for (LineItem li : inv.getItems()) {
                table.addCell(cell(li.getDescription(), p));
                table.addCell(cell(String.valueOf(li.getQuantity()), p));
                table.addCell(cell(li.getUnitPrice().toPlainString(), p));
                table.addCell(cell(li.getLineTotal().toPlainString(), p));
            }
            doc.add(table);
            doc.add(new Paragraph(" "));

            PdfPTable totals = new PdfPTable(2);
            totals.setWidthPercentage(40);
            totals.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totals.addCell(cell("Subtotal", h2));
            totals.addCell(cell(inv.getSubTotal().toPlainString(), p));
            totals.addCell(cell("Tax (" + inv.getTaxPercent() + "%)", h2));
            totals.addCell(cell(inv.getTaxAmount().toPlainString(), p));
            totals.addCell(cell("Total", h2));
            totals.addCell(cell(inv.getTotalAmount().toPlainString(), p));
            doc.add(totals);

            doc.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    private static PdfPCell cell(String text, Font font) {
        PdfPCell c = new PdfPCell(new Phrase(text, font));
        c.setPadding(6f);
        return c;
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}
