package net.baumarkt.servermanager.utils.config;

/*
 * Created on 13.08.2020 at 20:04
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class BufferedFileWriter extends OutputStreamWriter {

    public BufferedFileWriter( String fileName ) throws IOException
    {
        super( new FileOutputStream( fileName ), StandardCharsets.UTF_8);
    }

    public BufferedFileWriter( String fileName, boolean append ) throws IOException
    {
        super( new FileOutputStream( fileName, append ), StandardCharsets.UTF_8);
    }

    public BufferedFileWriter( String fileName, String charsetName, boolean append ) throws IOException
    {
        super( new FileOutputStream( fileName, append ), Charset.forName( charsetName ) );
    }

    public BufferedFileWriter( File file ) throws IOException
    {
        super( new FileOutputStream( file ), StandardCharsets.UTF_8);
    }

    public BufferedFileWriter( File file, boolean append ) throws IOException
    {
        super( new FileOutputStream( file, append ), StandardCharsets.UTF_8);
    }

    public BufferedFileWriter( File file, String charsetName, boolean append ) throws IOException
    {
        super( new FileOutputStream( file, append ), Charset.forName( charsetName ) );
    }

}
