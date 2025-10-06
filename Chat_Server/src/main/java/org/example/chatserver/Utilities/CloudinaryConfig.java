package org.example.chatserver.Utilities;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class CloudinaryConfig {
    private static final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "digs0j48l",
            "api_key", "974685814667245",
            "api_secret", "8LZlDdXJ9LFCgW42T9IJ2-4bh2c"));

    public static Cloudinary getCloudinary() {
        return cloudinary;
    }
}
