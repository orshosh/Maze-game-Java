package IO;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class MyDecompressorInputStream extends InputStream {
    InputStream file;

    public MyDecompressorInputStream(InputStream file) {
        this.file = file;
    }

    @Override
    public int read() throws IOException {
        return file.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        byte [] result = new byte [(b.length-12)/8 +13];
        read(result,0,result.length);

        int size1 = (b.length-12)/8;
        int size2 = (b.length-12)%8;

        b[0]=result[0];
        b[1]=result[1];
        b[2]=result[2];
        b[3]=result[3];

        int i=4;
        int resultLength = 4;
            for( int j=0; j<size1;j++){
                int num = result[resultLength];
                resultLength++;

                if(num<0){
                    num = 256+num;
                }
                String temp = Integer.toBinaryString(num);
                while(temp.length()<8) {
                    temp = '0' + temp;
                }
                for( int l=0; l<temp.length();l++){
                    int t = temp.charAt(l) - '0';
                    b[i]=(byte)t;
                    i++;
                }
            }
            if(size2 != 0){
                int num = result[resultLength];
                if(num<0){
                    num = 256+num;
                }
                String temp = Integer.toBinaryString(num);
                while(temp.length()<size2) {
                    temp = '0' + temp;
                }
                    for( int l=0; l<temp.length();l++){
                        int t = temp.charAt(l) - '0';
                        b[i]=(byte)t;
                        i++;
                    }
            }

        b[b.length-8]=result[result.length-8];
        b[b.length-7]=result[result.length-7];
        b[b.length-6]=result[result.length-6];
        b[b.length-5]=result[result.length-5];
        b[b.length-4]=result[result.length-4];
        b[b.length-3]=result[result.length-3];
        b[b.length-2]=result[result.length-2];
        b[b.length-1]=result[result.length-1];



        return -1;
    }


}




