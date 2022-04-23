package net.deechael.dcg.generator;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

final class JJavaFileObject extends SimpleJavaFileObject {

    private final ByteArrayOutputStream byteArrayOutputStream;

    JJavaFileObject(String className, Kind kind) {
        super(URI.create(className + kind.extension), kind);
        this.byteArrayOutputStream = new ByteArrayOutputStream();
    }

    @Override
    public OutputStream openOutputStream() {
        return byteArrayOutputStream;
    }

    public byte[] getBytes() {
        return this.byteArrayOutputStream.toByteArray();
    }

}
