package com.example.pagunoi.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class PdfCreator {
    private Context mContext;

    public PdfCreator(Context mContext) {
        this.mContext = mContext;
    }

    public String buildPdf(Bitmap bmp,
                           String nume,
                           String adresa,
                           String locatieSesizare,
                           String mentiuni) throws DocumentException, FileNotFoundException {
        String mFileName = new SimpleDateFormat("yyyyMMDD_HHmmss", Locale.getDefault())
                .format(System.currentTimeMillis());

        Log.d("REPORT", "Creating Report in PDFCreator");
        String mFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + mFileName + ".pdf";
        Document document = new Document(PageSize.A4,50,50,50,50);
        PdfWriter.getInstance(document,new FileOutputStream(mFilePath));
        document.open();
        Paragraph paragraph1 = new Paragraph();
        paragraph1.setAlignment(Element.ALIGN_LEFT);
        paragraph1.add(MessageCreator.createIllegalGarbageComplaint(nume,adresa,locatieSesizare,mentiuni));
        document.add(paragraph1);
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin()) / image.getWidth()) * 100;
            image.scalePercent(scaler);
            image.setAlignment(Element.ALIGN_LEFT);
            document.add(image);

        } catch (IOException e) {
            e.printStackTrace();
        }
        document.close();
        return mFilePath;
    }
}
