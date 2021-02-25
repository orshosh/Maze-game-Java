package IO;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Deflater;

public class MyCompressorOutputStream extends OutputStream {
    OutputStream file;


    public MyCompressorOutputStream (OutputStream f){
        file = f;
    }

    @Override
    public void write(int b) throws IOException {
        file.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
//        Deflater compressor = new Deflater();
//        compressor.setLevel(Deflater.BEST_COMPRESSION);
//
//        compressor.setInput(b);
//        compressor.finish();
//
//        byte[] result = new byte[b.length];
//        while(!compressor.finished()){
//            int count = compressor.deflate(result);
//            write(result,0,count);
//        }

        byte[]result = new byte[(b.length-12)/8 +13];
        result[0]=b[0];
        result[1]=b[1];
        result[2]=b[2];
        result[3]=b[3];
        int i=4;
        while(i<b.length-8) {
            for (int j = 4; j < result.length; j++) {
                int count = 1;
                String temp = "";
                for (i = i; count < 9 && i < b.length - 8; i++) {
                    temp = temp + (b[i]);
                    count++;
                }
                if (temp != "") {
                    int num = Integer.parseInt(temp, 2);
                    result[j] = (byte) num;
                }
            }
        }

        result[result.length-8]=b[b.length-8];
        result[result.length-7]=b[b.length-7];
        result[result.length-6]=b[b.length-6];
        result[result.length-5]=b[b.length-5];
        result[result.length-4]=b[b.length-4];
        result[result.length-3]=b[b.length-3];
        result[result.length-2]=b[b.length-2];
        result[result.length-1]=b[b.length-1];

        write(result,0,result.length);

        //System.out.println(file.getChannel().size());


    }
}
