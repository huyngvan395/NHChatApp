package org.example.chatserver.Utilities;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;

public class CloudinaryConfig {
    private static Dotenv dotenv = Dotenv.load();
    private static String api_key_cloudinary = dotenv.get("API_KEY_CLOUDINARY");
    private static String api_key_secret = dotenv.get("API_KEY_SECRET");
    private static final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "digs0j48l",
            "api_key", api_key_cloudinary,
            "api_secret", api_key_secret));

    public static Cloudinary getCloudinary() {
        return cloudinary;
    }
}
