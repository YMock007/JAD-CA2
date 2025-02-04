package Utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Map;

public class FileUploadHelper {

    private static final Cloudinary cloudinary = CloudinaryConfig.getInstance();

    public static String uploadToCloudinary(Part filePart) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            // Return a default image URL if no file is uploaded
            return "https://res.cloudinary.com/dr7rxzsgz/image/upload/v1738572872/cleaning_service/DefaultPicture.png";
        }

        // Extract the original filename
        String originalFilename = filePart.getSubmittedFileName();

        // Save file to a temporary location
        File tempFile = File.createTempFile("upload_", "_" + originalFilename);
        tempFile.deleteOnExit(); // Ensure the file is deleted after the program exits
        filePart.write(tempFile.getAbsolutePath());

        try {
            // Pass the file to Cloudinary's uploader
            Map<?, ?> uploadResult = CloudinaryConfig.getInstance().uploader().upload(
                tempFile, // Pass the File directly
                CloudinaryConfig.getUploadOptions(originalFilename)
            );

            // Return the secure URL of the uploaded image
            return (String) uploadResult.get("secure_url");
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Image upload failed: " + e.getMessage());
        } finally {
            // Delete the temporary file after uploading
            Files.deleteIfExists(tempFile.toPath());
        }
    }


    public static boolean deleteFromCloudinary(String imageUrl) {
        try {
            if (imageUrl == null || imageUrl.contains("DefaultPicture.png")) {
                return false;  // Don't delete default images
            }

            // Extract public_id from Cloudinary URL
            String publicId = imageUrl.substring(imageUrl.lastIndexOf("/") + 1).split("\\.")[0];

            cloudinary.uploader().destroy("cleaning_service/" + publicId, ObjectUtils.emptyMap());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
