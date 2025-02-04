package Utils;

import com.cloudinary.Cloudinary;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CloudinaryConfig {

    private static final Cloudinary cloudinary;

    static {
        // Create a configuration map
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", ConfigLoader.get("CLOUDINARY_CLOUD_NAME"));
        config.put("api_key", ConfigLoader.get("CLOUDINARY_API_KEY"));
        config.put("api_secret", ConfigLoader.get("CLOUDINARY_API_SECRET"));

        // Initialize Cloudinary instance
        cloudinary = new Cloudinary(config);
    }

    public static Cloudinary getInstance() {
        return cloudinary;
    }

    public static Map<String, Object> getUploadOptions(String originalFilename) {
        // Extract filename without extension
        String fileNameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));

        // Generate a unique identifier (UUID or timestamp)
        String uniqueIdentifier = UUID.randomUUID().toString().substring(0, 8); // Shortened UUID

        // Create a unique filename
        String uniqueFileName = fileNameWithoutExtension + "_" + uniqueIdentifier + extension;

        // Create upload options map
        Map<String, Object> uploadOptions = new HashMap<>();
        uploadOptions.put("folder", "cleaning_service");  // Organize uploads into a folder
        uploadOptions.put("resource_type", "image");
        uploadOptions.put("public_id", uniqueFileName);   // Use unique filename
        uploadOptions.put("overwrite", false);           // Prevent overwriting

        return uploadOptions;
    }
}
