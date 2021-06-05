package com.example.pagunoi.utils;

import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MessageCreator {
    public static String createIllegalGarbageComplaint(String nume,
                                                       String adresa,
                                                       String locatieSesizare,
                                                       String mentiuni) {
        final String firstPart = "SESIZARE / RECLAMATIE \n Subsemnatul/a ";
        final String secondPart = " domiciliat/a în ";
        final String thirdPart = " în calitate de utilizator al serviciilor de salubrizare, prin prezenta, doresc sa sesizez institutia/autoritatea Dvs. despre faptul ca în localitatea mentionata nu s-au implementat masurile necesare colectarii selective a deseurilor, contrar prevederilor Legii nr. 211/2011 privind regimul deșeurilor, a Legii nr. 249/2015 privind modalitatea de gestionare a ambalajelor și deseurilor de ambalaje, a OUG nr. 196/2005 privind fondul pentru mediu, modificata prin OUG nr. 74/2018, precum si a Legii nr. 101/2006 privind serviciul de salubrizare. \nSituatia concreta la care fac referire consta in: \n";
        final String fourthPart = "\n Locatia la care se face referire in aceasta sesizare : ";
        final String fifthPart = "\n Mai jos este atasata poza ce dovedeste cele spuse : \n";
        StringBuilder builder = new StringBuilder();
        builder.append(firstPart);
        builder.append(nume);
        builder.append(secondPart);
        builder.append(adresa);
        builder.append(thirdPart);
        builder.append(mentiuni);
        builder.append(fourthPart);
        builder.append(locatieSesizare);
        builder.append(fifthPart);
        return builder.toString();
    }
}
