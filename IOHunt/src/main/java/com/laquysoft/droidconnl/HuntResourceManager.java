/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.laquysoft.droidconnl;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.ParcelFileDescriptor;

import java.io.*;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This class unzips the hunt data and stores a list of drawables based on
 * the images inside the zip.
 *
 * @author wolff
 */
public class HuntResourceManager {

    HashMap<String, Drawable> drawables;
    String huntJSON;


    public Boolean unzipFile(Resources res) {
        try {
            drawables = new HashMap<String, Drawable>();
            InputStream is = res.openRawResource(R.raw.hunt);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;

            while ((ze = zis.getNextEntry()) != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int count;

                String filename = ze.getName();
                if (filename.endsWith("/")) {
                    continue;
                }

                filename = filename.substring(filename.lastIndexOf('/') + 1);

                // reading and writing
                while ((count = zis.read(buffer)) != -1) {
                    baos.write(buffer, 0, count);
                }

                byte[] b = baos.toByteArray();

                if (filename.endsWith(".json")) {
                    huntJSON = new String(b);
                } else if (filename.endsWith(".jpg")
                        || filename.endsWith(".png")) {
                    ByteArrayInputStream bais = new ByteArrayInputStream(b);
                    Drawable drw = Drawable.createFromStream(bais, filename);

                    drawables.put(filename, drw);
                }

                zis.closeEntry();
            }

            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    public void unzipDownloadedFile(ParcelFileDescriptor file) {

        try {
            FileInputStream fileInputStream
                    = new ParcelFileDescriptor.AutoCloseInputStream(file);
            drawables = new HashMap<String, Drawable>();
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fileInputStream));
            ZipEntry ze;

            while ((ze = zis.getNextEntry()) != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int count;

                String filename = ze.getName();
                if (filename.endsWith("/")) {
                    continue;
                }

                filename = filename.substring(filename.lastIndexOf('/') + 1);

                // reading and writing
                while ((count = zis.read(buffer)) != -1) {
                    baos.write(buffer, 0, count);
                }

                byte[] b = baos.toByteArray();

                if (filename.endsWith(".json")) {
                    huntJSON = new String(b);
                } else if (filename.endsWith(".jpg")
                        || filename.endsWith(".png")) {
                    ByteArrayInputStream bais = new ByteArrayInputStream(b);
                    Drawable drw = Drawable.createFromStream(bais, filename);

                    drawables.put(filename, drw);
                }

                zis.closeEntry();
            }

            zis.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
