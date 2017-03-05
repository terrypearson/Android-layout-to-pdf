package xyz.easyeats.androidlayouttopdf;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

    /**
     * Created by terrypearson on 3/3/17.
     *
     * This is a basic example showing how to create a very basic receipt.
     * Obviously, this gets you the basics. From here, fill out some nice formatting, change pixel densities,
     * etc and you will have a nicely generated pdf!
     */

public class ExamplePdfMaker {

        Context ctx;

        public ExamplePdfMaker(Context ctx){
            this.ctx=ctx;
        }


        public String buildPdf(){
            // create a new document
            PdfDocument document = new PdfDocument();

            int headerTextSize=20;
            int bodyTextSize=10;

            //Calulate total pixels height by finding newlines
            int lastIndex = 0;
            int count = 1;

            int pageWidth=200;

            //Adding title
            TextView textViewTitle = new TextView(this.ctx);
            textViewTitle.layout(0, 0, pageWidth, headerTextSize+4); //text box size heightpx x widthpx (Left, Top,Right,Bottom)
            textViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, headerTextSize);
            textViewTitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textViewTitle.setTextColor(Color.BLACK);
            textViewTitle.setTypeface(Typeface.MONOSPACE);
            textViewTitle.setDrawingCacheEnabled(true);
            textViewTitle.setText("Cactus Bob's");
            //Getting size of text view
            textViewTitle.measure(0, 0);
            int titleWidth=textViewTitle.getMeasuredWidth();
            int titleHeight=textViewTitle.getMeasuredHeight();


            //XXX: Here we create some dummy orders so we can loop through them.
            List<TempOrder> ordersList = new ArrayList<TempOrder>();
            ordersList.add(tempOrderBuilder().setDescription("Mud puppies").setPrice(595));
            ordersList.add(tempOrderBuilder().setDescription("Gator bites\n\t-No tartar\n\t-Extra salt").setPrice(650));
            ordersList.add(tempOrderBuilder().setDescription("Pog chips").setPrice(200));
            ordersList.add(tempOrderBuilder().setDescription("Icecream").setPrice(300));
            ordersList.add(tempOrderBuilder().setDescription("Turkey drumstick").setPrice(500));

            // This tableLayout is what will be printed to the pdf
            TableLayout tableLayout = new TableLayout(this.ctx);

            //Title added to layout
            TableRow titleRow= new TableRow(this.ctx);
            TableRow.LayoutParams lpTitle = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            titleRow.setLayoutParams(lpTitle);
            titleRow.addView(textViewTitle);
            tableLayout.addView(titleRow);

            //We will temporarily store these textviews in a list
            for (TempOrder ord : ordersList) {
                //TableLayout expiriment
                TableRow row= new TableRow(this.ctx);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);


                //Adding body

                int bodyItemHeight=200; //TODO: what is this? Does it matter?
                TextView textView = new TextView(this.ctx);
                textView.layout(0, 0, pageWidth, count*(bodyItemHeight)); //text box size heightpx x widthpx (Left, Top,Right,Bottom)
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, bodyTextSize);
                textView.setTextColor(Color.BLACK);
                textView.setTypeface(Typeface.MONOSPACE);
                //textView.setShadowLayer(5, 2, 2, Color.CYAN); //text shadow
                textView.setText(ord.getDescription()+"\t"+ord.getPrice());

//            bodyTextViews.add(textView);
                row.addView(textView);

                tableLayout.addView(row);

            }

            //Getting size of text view
            tableLayout.measure(0, 0);
            int bodyWidth=tableLayout.getMeasuredWidth();
            int pageHeight=tableLayout.getMeasuredHeight();

            //Create a pdf document
            // int pageHeight=titleHeight+bodyHeight;
            Log.d("ez_Receipt","titleHeight: "+titleHeight);
            Log.d("ez_Receipt","pageHeight: "+pageHeight);
            // crate a page description
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth,pageHeight, 1).create();
            // start a page in the pdf
            PdfDocument.Page page = document.startPage(pageInfo);

            //Drawing to canvas
            Paint paint=new Paint(); paint.setColor(Color.BLACK); //Setting up our paint

            tableLayout.setDrawingCacheEnabled(true);
            tableLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            tableLayout.layout(0, 0, tableLayout.getMeasuredWidth(), tableLayout.getMeasuredHeight());

            tableLayout.buildDrawingCache(true);
            Bitmap cs = Bitmap.createBitmap(tableLayout.getDrawingCache());
            page.getCanvas().drawBitmap(cs, 0, titleHeight, paint); //Body

            // finish the page
            document.finishPage(page);

            String filepath=null;
            try{
                File mypath=new File( this.ctx.getExternalFilesDir(null).toString()+"/"+"example.pdf");
                filepath=mypath.toString();
                document.writeTo(new FileOutputStream(mypath));
                Log.d("ez_Receipt","The external pdf file should be stored at: "+filepath);
            }
            catch (FileNotFoundException e){
                Log.d("ez_Receipt","File not found exception. Make sure WRITE_EXTERNAL_STORAGE permissions exist in your AndroidManifest.xml");
            }
            catch (IOException e){
                Log.d("ez_Receipt","IOException");
            }
            // close the document
            document.close();

            return filepath;
        }


        static TempOrder tempOrderBuilder(){
            return new TempOrder();
        }

    }


class TempOrder {
    private int price;
    private String description;

    public int getPrice() {
        return price;
    }

    public TempOrder setPrice(int price) {
        this.price = price;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TempOrder setDescription(String description) {
        this.description = description;
        return this;
    }
}


